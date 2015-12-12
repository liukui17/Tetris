package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * The server application that will connect players together to
 * play a game of competitive Tetris.
 */
public class GameServer {
	public static final int PORT_NUM = 3333;

	private static Map<String, Game> games = new HashMap<String, Game>();
	
	public static final byte GAME_ALREADY_EXISTS = 0;
	public static final byte ILLEGAL_NUM_PLAYERS = 1;
	public static final byte SUCCESS_CREATION = 2;
	public static final byte GAME_DOES_NOT_EXIST = 3;
	public static final byte SUCCESS_JOIN = 4;
	public static final byte VALID_NUM_PLAYERS = 5;
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(PORT_NUM);
			System.out.println("Started server");

			while (true) {
				boolean doneSetup = false;

				while (!doneSetup) {
					Socket player = server.accept();
					DataInputStream in = new DataInputStream(player.getInputStream());
					DataOutputStream out = new DataOutputStream(player.getOutputStream());
					
					boolean makingGame = in.readBoolean();
					
					if (makingGame) {
						String gameName = getGameName(true, in, out);
						int numPlayers = getNumPlayers(in, out);

						Game game = new Game(numPlayers);
						
						game.numConnectedPlayers++;
						game.playerSockets[0] = player;
						game.playersIn[0] = in;
						game.playersOut[0] = out;
						
						games.put(gameName, game);

						if (numPlayers == 1) {
							// createGame(gameName);
							new Thread(new GameThreadWrapper(gameName)).start();
						}
						
						doneSetup = true;
					} else {
						String gameName = getGameName(false, in, out);
						
						Game game = games.get(gameName);
						
						game.numConnectedPlayers++;
						
						int index = game.numConnectedPlayers - 1;
						game.playerSockets[index] = player;
						game.playersIn[index] = in;
						game.playersOut[index] = out;
						
						if (game.isReady()) {
							// createGame(gameName);
							new Thread(new GameThreadWrapper(gameName)).start();
							doneSetup = true;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getGameName(boolean creating, DataInputStream in, DataOutputStream out) throws IOException {
		String gameName = "";
		while (true) {
			gameName = in.readUTF();
			if (creating) {
				if (games.containsKey(gameName)) {
					out.writeByte(GAME_ALREADY_EXISTS);
				} else {
					out.writeByte(GAME_DOES_NOT_EXIST);
					break;
				}
			} else {
				if (games.containsKey(gameName)) {
					out.writeByte(GAME_ALREADY_EXISTS);
					break;
				} else {
					out.writeByte(GAME_DOES_NOT_EXIST);
				}
			}
		}	
		return gameName;
	}
	
	private static int getNumPlayers(DataInputStream in, DataOutputStream out) throws IOException {
		int numPlayers = in.readInt();
		while (true) {
			if (numPlayers <= 0) {
				out.writeByte(ILLEGAL_NUM_PLAYERS);
			} else {
				out.writeByte(VALID_NUM_PLAYERS);
				break;
			}
			numPlayers = in.readInt();
		}
		return numPlayers;
	}

	private static class GameThreadWrapper implements Runnable {
		private String name;
		
		public GameThreadWrapper(String name) {
			this.name = name;
		}
		
		@Override
		public void run() {
			try {
				Game game = games.get(name);
				Socket[] clientSockets = game.playerSockets;
				
				notifyPlayerNumbers(game.playersOut);

				long dropInterval = getInitialDropInterval(game.playersIn);

				Thread thread = new Thread(new GameThread(clientSockets, dropInterval));
				
				thread.start();
				thread.join();

				games.remove(game);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	private static void createGame(String name) throws IOException {
//		Game game = games.get(name);
//		Socket[] clientSockets = game.playerSockets;
//		
//		notifyPlayerNumbers(game.playersOut);
//
//		long dropInterval = getInitialDropInterval(game.playersIn);
//
//		Thread thread = new Thread(new GameThread(clientSockets, dropInterval));
//		
//		thread.start();
//
//		games.remove(game);
//	}
	
	/**
	 * Notifies the players connected to sockets in the specified Socket[] which
	 * player number they are.
	 * 
	 * @param playerSockets the Socket[] that contains the Sockets for each player
	 * 
	 * @requires playerSockets != null
	 * 
	 * @throws IOException on IO errors
	 */
	private static void notifyPlayerNumbers(DataOutputStream[] out) throws IOException {
		for (int i = 0; i < out.length; i++) {
			out[i].writeInt(i);
		}
	}

	/**
	 * Obtains the initial dropping interval from the player's difficult settings.
	 * The initial dropping interval is defined to be the average of all of the
	 * players' settings.
	 * 
	 * @param playerSockets
	 * 						socket connections to the players
	 * @return the initial dropping interval
	 * @throws IOException iff error on reading the dropping intervals from each player
	 */
	private static long getInitialDropInterval(DataInputStream[] in) throws IOException {
		long interval = 0;
		for (int i = 0; i < in.length; i++) {
			interval += in[i].readLong();
		}

		return interval / in.length;
	}
	
	static class Game {
		int capacity;
		int numConnectedPlayers;
		Socket[] playerSockets;
		DataInputStream[] playersIn;
		DataOutputStream[] playersOut;
		
		public Game(int numPlayers) {
			capacity = numPlayers;
			numConnectedPlayers = 0;
			playerSockets = new Socket[capacity];
			playersIn = new DataInputStream[capacity];
			playersOut = new DataOutputStream[capacity];
		}
		
		public boolean isReady() {
			return capacity == numConnectedPlayers;
		}
	}
}