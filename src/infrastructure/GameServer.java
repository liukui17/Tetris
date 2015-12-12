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
					String gameName = in.readUTF();
					int numPlayers = in.readInt();

					/*
					 * 0 = can't create game; it already exists
					 * 1 = illegal number of players
					 * 2 = game successfully created
					 */
					if (makingGame) {
						if (games.containsKey(gameName)) {
							out.writeByte(GAME_ALREADY_EXISTS);
						} else {
							if (numPlayers <= 0) {
								out.writeByte(ILLEGAL_NUM_PLAYERS);
							} else {
								Game game = new Game(numPlayers);
								
								game.numConnectedPlayers++;
								game.playerSockets[0] = player;
								
								games.put(gameName, game);

								out.writeByte(SUCCESS_CREATION);
								if (numPlayers == 1) {
									createGame(game);
								}
								doneSetup = true;
							}
						}
					} else {
						if (games.containsKey(gameName)) {
							out.writeByte(SUCCESS_JOIN);
							Game game = games.get(gameName);
							game.numConnectedPlayers++;
							game.playerSockets[game.numConnectedPlayers] = player;
							if (game.isReady()) {
								createGame(game);
								doneSetup = true;
							}
						} else {
							out.writeByte(GAME_DOES_NOT_EXIST);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createGame(Game game) throws IOException {
		Socket[] clientSockets = game.playerSockets;
		
		notifyPlayerNumbers(clientSockets);

		long dropInterval = getInitialDropInterval(clientSockets);

		new Thread(new GameThread(clientSockets, dropInterval)).start();
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
	private static void notifyPlayerNumbers(Socket[] playerSockets) throws IOException {
		DataOutputStream[] notifiers = new DataOutputStream[playerSockets.length];
		for (int i = 0; i < notifiers.length; i++) {
			notifiers[i] = new DataOutputStream(playerSockets[i].getOutputStream());
		}

		for (int i = 0; i < notifiers.length; i++) {
			notifiers[i].writeInt(i);
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
	private static long getInitialDropInterval(Socket[] playerSockets) throws IOException {
		DataInputStream[] streams = new DataInputStream[playerSockets.length];
		for (int i = 0; i < streams.length; i++) {
			streams[i] = new DataInputStream(playerSockets[i].getInputStream());
		}

		long interval = 0;
		for (int i = 0; i < streams.length; i++) {
			interval += streams[i].readLong();
		}

		return interval / streams.length;
	}
	
	static class Game {
		int capacity;
		int numConnectedPlayers;
		Socket[] playerSockets;
		
		public Game(int numPlayers) {
			capacity = numPlayers;
			numConnectedPlayers = 0;
			playerSockets = new Socket[capacity];
		}
		
		public boolean isReady() {
			return capacity == numConnectedPlayers;
		}
	}
}