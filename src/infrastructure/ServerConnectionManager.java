package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/*
 * Justification for only one ServerConnectionManager for both players:
 * We can read commands issued by either player concurrently (which
 * we definitely want to do for efficiency), however, at the end of
 * the day, we HAVE to make changing the board state sequential with
 * locks to prevent data races.
 * 
 * So by using the blocking queue to hold commands issued by BOTH
 * players, we can keep track of the commands and have the game engine
 * sequentially dequeue commands from it, while also allowing the game
 * to read from both players concurrently by having the blocking queue
 * take care of the concurrency management/locking for us.
 */

/**
 * Manages the connection, and the data sent/received through the connection
 * between the server and the players.
 */
public class ServerConnectionManager implements Runnable {
	/*
	 * The delay to send to both players simultaneously (in ms)
	 */
	private static final long SEND_DELAY = 100;

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
					GameState state1 = outStates.take();
					GameState state2 = new GameState(state1);
					// state.printBoard();

					long sendTime = System.currentTimeMillis() + SEND_DELAY;

					Thread p1 = new Thread(new WriteThread(outToP1, state1, sendTime));
					Thread p2 = new Thread(new WriteThread(outToP2, state2, sendTime));
					
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
		private long sendTime;

		public WriteThread(DataOutputStream out, GameState state, long sendTime) {
			this.out = out;
			this.state = state;
			this.sendTime = sendTime;
		}

		@Override
		public void run() {
			try {
				while (System.currentTimeMillis() < sendTime) { System.out.println("waiting"); }
				
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

				// System.out.println("sent board to client");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
