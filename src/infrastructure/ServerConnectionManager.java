package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Manages the connection, and the data sent/received through the connection
 * between the server and the players.
 */
public class ServerConnectionManager implements Runnable {
	/*
	 * The delay to send to both players simultaneously (in ms)
	 */
	private static final long DISPLAY_DELAY = 100;

	private BlockingQueue<Byte> commands;
	private BlockingQueue<GameState> outStates;

	private DataInputStream inFromP1;
	private DataOutputStream outToP1;
	private DataInputStream inFromP2;
	private DataOutputStream outToP2;

	public ServerConnectionManager(BlockingQueue<Byte> commands,
			BlockingQueue<GameState> outStates,
			DataInputStream inFromP1, DataOutputStream outToP1,
			DataInputStream inFromP2, DataOutputStream outToP2) {
		this.commands = commands;
		this.outStates = outStates;

		this.inFromP1 = inFromP1;
		this.outToP1 = outToP1;
		this.inFromP2 = inFromP2;
		this.outToP2 = outToP2;
	}

	@Override
	public void run() {
		new Thread(new ReadThread(inFromP1)).start();
		new Thread(new ReadThread(inFromP2)).start();
		new Thread(new WriteManager()).start();
	}

	/**
	 * The thread responsible for reading command bytes from
	 * a player.
	 */
	public class ReadThread implements Runnable {
		// the DataInputStream that this thread will read from
		private DataInputStream player;

		/**
		 * Constructs a new ReadThread from the specified DataInputStream
		 * 
		 * @param player the DataInputStream that commands will be read from
		 * 
		 * @requires player != null
		 */
		public ReadThread(DataInputStream player) {
			this.player = player;
		}

		@Override
		public void run() {
			while (true) {
				try {
					byte msg = player.readByte();
					commands.add(msg);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/** 
	 * The thread that is responsible for writing out the board
	 * state to both players to both players simultaneously AND fairly.
	 */
	public class WriteManager implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					GameState state = outStates.take();

					long displayDelay = System.currentTimeMillis() + DISPLAY_DELAY;

					Thread p1 = new Thread(new WriteThread(outToP1, state, displayDelay));
					Thread p2 = new Thread(new WriteThread(outToP2, state, displayDelay));
					
					p1.start();
					p2.start();
					p1.join();
					p2.join();
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
					long msgLong = Encoder.gridRowToNetworkMessage(row);
					out.writeLong(msgLong);
				}

				// send out the score and isGameOver
				int score = state.getScore();
				boolean isGameOver = state.getIsGameOver();
				out.writeInt(score);
				out.writeBoolean(isGameOver);
				
				// send out player 1's then player 2's falling piece spaces
				long p1Spaces = Encoder.encodeSpacesOfPiece(state.getSpaces(0));
				long p2Spaces = Encoder.encodeSpacesOfPiece(state.getSpaces(1));
				
				out.writeLong(p1Spaces);
				out.writeLong(p2Spaces);
				
				// send the delay for the gui to display the game state
				out.writeLong(displayDelay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
