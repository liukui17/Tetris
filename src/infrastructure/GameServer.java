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
	public static final byte VALID_NUM_PLAYERS = 4;
	public static final byte GAME_FULL = 6;
	public static final byte ACCEPTED = 7;

	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(PORT_NUM);
			System.out.println("Started server");

			while (true) {
				Socket player = server.accept();
				new Thread(new SetupThread(player)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class SetupThread implements Runnable {
		private Socket player;
		private DataInputStream in;
		private DataOutputStream out;

		public SetupThread(Socket player) {
			try{ 
				this.player = player;
				this.in = new DataInputStream(player.getInputStream());
				this.out = new DataOutputStream(player.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					boolean makingGame = in.readBoolean();

					if (makingGame) {
						String gameName = getGameName(true, in, out);

						if (gameName.isEmpty()) {
							continue;
						}

						int numPlayers = getNumPlayers(in, out);
						if (numPlayers == -1) {
							continue;
						}

						Game game = new Game(numPlayers);

						game.numConnectedPlayers++;
						game.playerSockets[0] = player;
						game.playersIn[0] = in;
						game.playersOut[0] = out;

						games.put(gameName, game);

						out.writeByte(ACCEPTED);

						if (numPlayers == 1) {
							new Thread(new GameThreadWrapper(gameName)).start();
						}
						
						break;
					} else {  // JOINING GAME
						String gameName = getGameName(false, in, out);

						if (gameName.isEmpty()) {
							continue;
						}

						Game game = games.get(gameName);

						if (game.numConnectedPlayers >= game.capacity) {
							out.writeByte(GAME_FULL);
							continue;
						} else {
							game.numConnectedPlayers++;

							int index = game.numConnectedPlayers - 1;
							game.playerSockets[index] = player;
							game.playersIn[index] = in;
							game.playersOut[index] = out;

							out.writeByte(ACCEPTED);

							if (game.isReady()) {
								new Thread(new GameThreadWrapper(gameName)).start();
							}
							
							break;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private static String getGameName(boolean creating, DataInputStream in, DataOutputStream out) throws IOException {
		String gameName = "";
		while (true) {
			gameName = in.readUTF();

			if (gameName.isEmpty()) {
				// Client hit cancel
				return gameName;
			}

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
		/*
		 * Client ensures the user either entered a positive number or hit cancel,
		 * so no need to infinite loop and verify
		 */
		if (numPlayers <= 0) {
			// Client hit cancel
			return -1;
		} else {
			out.writeByte(VALID_NUM_PLAYERS);
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

				notifyNumberOfPlayers(game.playersOut);

				notifyPlayerNumbers(game.playersOut);

				long dropInterval = getInitialDropInterval(game.playersIn);
				
				boolean assistance = getMajorityAssistancePreference(game.playersIn);

				notifyMajorityAssistancePreference(game.playersOut, assistance);
				
				Thread thread = new Thread(new GameThread(clientSockets, dropInterval, assistance));

				thread.start();

				thread.join();

				games.remove(name);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void notifyNumberOfPlayers(DataOutputStream[] out) throws IOException {
		for (int i = 0; i < out.length; i++) {
			out[i].writeInt(out.length);
		}
	}

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
	
	private static void notifyMajorityAssistancePreference(DataOutputStream[] out, boolean preference) throws IOException {
		for (int i = 0; i < out.length; i++) {
			out[i].writeBoolean(preference);
		}
	}
	
	/**
	 * Obtains the majority assistance preference of the pool of players.
	 * 
	 * @param in streams reading from players
	 * @return whether or not to turn on upcoming piece assistance based on majority vote
	 * @throws IOException iff error on reading the dropping intervals from each player
	 */
	private static boolean getMajorityAssistancePreference(DataInputStream[] in) throws IOException {
		int numWantAssistance = 0;
		for (int i = 0; i < in.length; i++) {
			if (in[i].readBoolean()) {
				numWantAssistance++;
			}
		}
		if (numWantAssistance > in.length / 2) {
			return true;
		} else {
			return false;
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
			System.out.println("read interval from player " + i);
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