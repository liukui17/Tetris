package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Manages the connection, and the data sent/received through the connection
 * between the server and the players.
 */
public class ServerConnectionManager implements Runnable {
	/*
	 * The delay to send to all players simultaneously (in ms)
	 */
	private static final long DISPLAY_DELAY = 100;

	private BlockingQueue<Byte> commands;
	private BlockingQueue<GameState> outStates;
	
	private Socket[] playerSockets;
	private DataInputStream[] playerInputStreams;
	private DataOutputStream[] playerOutputStreams;
	
	private int numPlayers;

	public ServerConnectionManager(BlockingQueue<Byte> commands,
			BlockingQueue<GameState> outStates,
			Socket[] playerSockets) {
		this.commands = commands;
		this.outStates = outStates;
		this.numPlayers = playerSockets.length;
		System.out.println("from serverconnmanager " + numPlayers);

		this.playerSockets = playerSockets;
		playerInputStreams = new DataInputStream[this.playerSockets.length];
		playerOutputStreams = new DataOutputStream[this.playerSockets.length];
		try {
			for (int i = 0; i < this.playerSockets.length; i++) {
				playerInputStreams[i] = new DataInputStream(playerSockets[i].getInputStream());
				playerOutputStreams[i] = new DataOutputStream(playerSockets[i].getOutputStream());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace(System.err);
		}
	}

	@Override
	public void run() {
		for (int i = 0; i < numPlayers; i++) {
			new Thread(new ReadThread(playerInputStreams[i], i)).start();
		}
		new Thread(new WriteManager()).start();
	}

	/**
	 * The thread responsible for reading command bytes from
	 * a player.
	 */
	public class ReadThread implements Runnable {
		// the DataInputStream that this thread will read from
		private DataInputStream player;
		boolean isFinished;
		int playerNumber;

		/**
		 * Constructs a new ReadThread from the specified DataInputStream
		 * 
		 * @param player the DataInputStream that commands will be read from
		 * 
		 * @requires player != null
		 */
		public ReadThread(DataInputStream player, int playerNumber) {
			this.player = player;
			isFinished = false;
			this.playerNumber = playerNumber;
		}

		@Override
		public void run() {
			while (!isFinished) {
				try {
					byte msg = player.readByte();
					if ((msg & Encoder.QUIT_MASK) == 0) {
						commands.add(msg);
					} else {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			try {
				player.close();
				playerInputStreams[playerNumber] = null;
				playerOutputStreams[playerNumber].close();
				playerOutputStreams[playerNumber] = null;
				playerSockets[playerNumber].close();
				playerSockets[playerNumber] = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * The thread that is responsible for writing out the board
	 * state to all players to both players simultaneously AND fairly.
	 */
	public class WriteManager implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					GameState state = outStates.take();

					long displayDelay = System.currentTimeMillis() + DISPLAY_DELAY;

					Thread[] writeThreads = new Thread[numPlayers];
					for (int i = 0; i < writeThreads.length; i++) {
						if (playerOutputStreams[i] != null) {
							writeThreads[i] = new Thread(new WriteThread(playerOutputStreams[i], state, displayDelay));
						}
					}
					
					for (int i = 0; i < writeThreads.length; i++) {
						if (writeThreads[i] != null) {
							writeThreads[i].start();
						}
					}
					
					for (int i = 0; i < writeThreads.length; i++) {
						if (writeThreads[i] != null) {
							writeThreads[i].join();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public class WriteThread implements Runnable {
		private DataOutputStream out;
		private GameState state;
		private long displayDelay;

		public WriteThread(DataOutputStream out, GameState state, long displayDelay) {
			this.out = out;
			this.state = state;
			this.displayDelay = displayDelay;
		}

		@Override
		public void run() {
			try {
				// send out the Color[][] first
				for (Color[] row : state.getBoard()) {
					long msgLong = Encoder.gridRowToNetworkMessage(row, numPlayers * GameUtil.PLAYER_START_SECTION_WIDTH);
					out.writeLong(msgLong);
				}

				// send out the score and isGameOver
				int[] playerScores = new int[numPlayers];
				for (int i = 0; i < numPlayers; i++) {
					playerScores[i] = state.getScore(i);
				}

				for (int i = 0; i < numPlayers; i++) {
					out.writeInt(playerScores[i]);
				}

				boolean isGameOver = state.getIsGameOver();
				out.writeBoolean(isGameOver);
				
				// send out the player's pieces
				long[] pSpaces = new long[numPlayers];
				for (int i = 0; i < pSpaces.length; i++) {
					pSpaces[i] = Encoder.encodeSpacesOfPiece(state.getSpaces(i), numPlayers * GameUtil.PLAYER_START_SECTION_WIDTH);
				}
				
				for (int i = 0; i < pSpaces.length; i++) {
					out.writeLong(pSpaces[i]);
				}
				
				// send the delay for the gui to display the game state
				out.writeLong(displayDelay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
