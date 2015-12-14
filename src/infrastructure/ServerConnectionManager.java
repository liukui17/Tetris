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
	private final boolean upcomingAssist;
	
	int numQuit;

	public ServerConnectionManager(BlockingQueue<Byte> commands,
			BlockingQueue<GameState> outStates,
			Socket[] playerSockets, boolean upcomingAssist) {
		this.commands = commands;
		this.outStates = outStates;
		this.numPlayers = playerSockets.length;
		this.upcomingAssist = upcomingAssist;
		numQuit = 0;

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
		Thread[] readers = new Thread[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			readers[i] = new Thread(new ReadThread(playerInputStreams[i], i));
		}
		Thread writer = new Thread(new WriteManager());
		for (int i = 0; i < numPlayers; i++) {
			readers[i].start();
		}
		writer.start();
		try {
			for (int i = 0; i < numPlayers; i++) {
				readers[i].join();
			}
		//	writer.join();
		} catch (InterruptedException ie) {
		//	System.out.println("done");
		}
		System.out.println("terminated normally");
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
					commands.add(msg);
					if ((msg & Encoder.COMMAND_MASK) == 0) {
					//	System.out.println("broke here");
						numQuit++;
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
					
					boolean hasWritten = false;

					Thread[] writeThreads = new Thread[numPlayers];
					for (int i = 0; i < writeThreads.length; i++) {
						if (playerOutputStreams[i] != null) {
							writeThreads[i] = new Thread(new WriteThread(playerOutputStreams[i], state, displayDelay));
							hasWritten = true;
						}
					}
					
					if (!hasWritten) {
					//	System.out.println("write manager terminated");
						break;
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
					long msgLong = Encoder.gridRowToNetworkMessage(row, GameUtil.BOARD_WIDTH);
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
					pSpaces[i] = Encoder.encodeSpacesOfPiece(state.getSpaces(i), GameUtil.BOARD_WIDTH);
				}
				
				for (int i = 0; i < pSpaces.length; i++) {
					out.writeLong(pSpaces[i]);
				}
				
				if (upcomingAssist) {
					byte[] upcoming = state.getUpcomingPieces();
					for (int i = 0; i < upcoming.length; i++) {
						out.writeByte(Encoder.encodeUpcomingPiece(upcoming[i], i));
					}
				}
				
				// send the delay for the gui to display the game state
				out.writeLong(displayDelay);
			} catch (Exception e) {
			//	e.printStackTrace();
			}
		}
	}
}
