package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/*
 * Justification for only one ConnectionManager for both players:
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
public class ConnectionManager implements Runnable {
	private BlockingQueue<Byte> commands;
	private BlockingQueue<Color[]> out;
	
	private DataInputStream inFromP1;
	private DataOutputStream outToP1;
	private DataInputStream inFromP2;
	private DataOutputStream outToP2;
	
	public ConnectionManager(BlockingQueue<Byte> commands, BlockingQueue<Color[]> out,
													 DataInputStream inFromP1, DataOutputStream outToP1,
													 DataInputStream inFromP2, DataOutputStream outToP2) {
		this.commands = commands;
		this.out = out;
		
		this.inFromP1 = inFromP1;
		this.outToP1 = outToP1;
		this.inFromP2 = inFromP2;
		this.outToP2 = outToP2;
	}

	@Override
	public void run() {
		new Thread(new ReadThread(inFromP1)).start();
		new Thread(new ReadThread(inFromP2)).start();
		new Thread(new WriteThread()).start();
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
	 * state to both players.
	 */
	public class WriteThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					Color[] row;
					row = out.take();
					long msgLong = Encoder.gridRowToNetworkMessage(row);
					
					/*
					 * Have to sequentially send out update, so make it
					 * random to make it fair
					 */
					if ((Math.random() * 10) % 2 == 0) {
						outToP1.writeLong(msgLong);
						outToP2.writeLong(msgLong);
					} else {
						outToP2.writeLong(msgLong);
						outToP1.writeLong(msgLong);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}