package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ClientConnectionManager implements Runnable {
	private BlockingQueue<GameState> inputStates;
	private BlockingQueue<Byte> outputCommandBytes;

	private DataInputStream inFromServer;
	private DataOutputStream outToServer;

	boolean hasQuit;

	public ClientConnectionManager(BlockingQueue<Byte> commands,
			BlockingQueue<GameState> inputStates,
			DataInputStream inFromServer,
			DataOutputStream outToServer) {
		this.outputCommandBytes = commands;
		this.inputStates = inputStates;

		this.inFromServer = inFromServer;
		this.outToServer = outToServer;

		hasQuit = false;
	}

	public void toggleHasQuit() {
		hasQuit = !hasQuit;
	}

	@Override
	public void run() {
		Thread reader = new Thread(new ReadThread());
		Thread writer = new Thread(new WriteThread());
		reader.start();
		writer.start();
		try {
			reader.join();
			writer.join();
		} catch (InterruptedException ie) {
			ie.printStackTrace(System.err);
		}
		try {
			inFromServer.close();
			outToServer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace(System.err);
		}
	}

	/**
	 * The thread responsible for reading GameStates from the server
	 */
	public class ReadThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					// read the board state
					Color[][] board = new Color[GameUtil.BOARD_HEIGHT][GameUtil.BOARD_WIDTH];
					for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
						if (hasQuit) {
							return;
						}
					//	System.out.println(i);
						long rowLong = inFromServer.readLong();
						Encoder.networkMessageToGridRow(rowLong, board[i]);
					}

					// read the score and isGameOver
					int[] playerScores = new int[GameUtil.NUM_PLAYERS];
					for (int i = 0; i < playerScores.length; i++) {
						playerScores[i] = inFromServer.readInt();
					}
					boolean isGameOver = inFromServer.readBoolean();
					
					// read the pieces of each player
					List<Set<BytePair>> playerSpaces = new ArrayList<Set<BytePair>>(GameUtil.NUM_PLAYERS);
					for (int i = 0; i < GameUtil.NUM_PLAYERS; i++) {
						playerSpaces.add(Encoder.decodeSpaces(inFromServer.readLong()));
					}
					
					// put the board state, score and isGameOver in a GameState struct
					GameState state = new GameState(board, playerSpaces, playerScores, isGameOver);
					
					// wait for synchronization
					long delay = inFromServer.readLong();
					while (System.currentTimeMillis() < delay) { }

					// send it to the GUI
					inputStates.add(state);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/**
	 * The thread responsible for writing out encoded command bytes to the server
	 */
	public class WriteThread implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					byte command = outputCommandBytes.take();
					outToServer.writeByte(command);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
