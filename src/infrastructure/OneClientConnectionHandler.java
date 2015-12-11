package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class OneClientConnectionHandler implements Runnable {
	private static final long DISPLAY_DELAY = 100;
	
	Socket clientSocket;
	DataInputStream in;
	DataOutputStream out;
	BlockingQueue<Byte> commands;
	GameState currOutState;
	OneClientConnectionHandler[] otherHandlers;
	int handleIndex;
	boolean stopWriting;
	boolean hasChanged;
	
	public OneClientConnectionHandler(Socket clientSocket, BlockingQueue<Byte> commands,
																		OneClientConnectionHandler[] otherHandlers, int handleIndex) {
		try {
			this.clientSocket = clientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.commands = commands;
			this.otherHandlers = otherHandlers;
			this.handleIndex = handleIndex;
			currOutState = null;
			stopWriting = false;
			hasChanged = false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	void giveNewOutState(GameState outState) {
		currOutState = outState;
		hasChanged = true;
	}
	
	@Override
	public void run() {
		Thread reader = new Thread(new ReadThread(in));
		Thread writer = new Thread(new WriteThread(out, System.currentTimeMillis() + DISPLAY_DELAY));
		reader.start();
		writer.start();
		try {
			reader.join();
			stopWriting = true;
			writer.join();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		try {
			otherHandlers[handleIndex] = null;
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException ioe) {
			System.out.println("Failed to close connection to client...");
		//	ioe.printStackTrace(System.err);
		}
	}

	public class ReadThread implements Runnable {
		DataInputStream player;
		
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
					if ((msg & Encoder.QUIT_MASK) == 0) {
						commands.add(msg);
					} else {
					//	System.out.println("got here");
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	public class WriteThread implements Runnable {
		private DataOutputStream out;
		private long displayDelay;

		public WriteThread(DataOutputStream out, long displayDelay) {
			this.out = out;
			this.displayDelay = displayDelay;
		}

		@Override
		public void run() {
			while (!stopWriting) {
				if (OneClientConnectionHandler.this.currOutState != null && hasChanged) {
					writeSingleState();
					hasChanged = false;
				}
			}
		}
		
		private void writeSingleState() {
			try {
				// send out the Color[][] first
				for (Color[] row : OneClientConnectionHandler.this.currOutState.getBoard()) {
					long msgLong = Encoder.gridRowToNetworkMessage(row);
					out.writeLong(msgLong);
				}

				// send out the score and isGameOver
				int[] playerScores = new int[GameUtil.NUM_PLAYERS];
				for (int i = 0; i < GameUtil.NUM_PLAYERS; i++) {
					playerScores[i] = OneClientConnectionHandler.this.currOutState.getScore(i);
				}

				for (int i = 0; i < GameUtil.NUM_PLAYERS; i++) {
					out.writeInt(playerScores[i]);
				}

				boolean isGameOver = OneClientConnectionHandler.this.currOutState.getIsGameOver();
				out.writeBoolean(isGameOver);
				
				// send out player 1's then player 2's falling piece spaces
				long[] pSpaces = new long[GameUtil.NUM_PLAYERS];
				for (int i = 0; i < pSpaces.length; i++) {
					pSpaces[i] = Encoder.encodeSpacesOfPiece(OneClientConnectionHandler.this.currOutState.getSpaces(i));
				}
				
				for (int i = 0; i < pSpaces.length; i++) {
					out.writeLong(pSpaces[i]);
				}
				
				// send the delay for the gui to display the game state
				out.writeLong(displayDelay);
			} catch (IOException ioe) {
				System.out.println("Client has closed.");
			//	ioe.printStackTrace();
			}
		}
	}
}
