package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ClientConnectionManager implements Runnable {
	private BlockingQueue<GameState> inputStates;
	private BlockingQueue<Byte> outputCommandBytes;

	private DataInputStream inFromServer;
	private DataOutputStream outToServer;

	public ClientConnectionManager(BlockingQueue<Byte> commands,
			BlockingQueue<GameState> inputStates,
			DataInputStream inFromServer,
			DataOutputStream outToServer) {
		this.outputCommandBytes = commands;
		this.inputStates = inputStates;

		this.inFromServer = inFromServer;
		this.outToServer = outToServer;
	}

	@Override
	public void run() {
		new Thread(new ReadThread()).start();
		new Thread(new WriteThread()).start();
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
						long rowLong = inFromServer.readLong();
						Encoder.networkMessageToGridRow(rowLong, board[i]);
					}

					// read the score and isGameOver
					int p1Score = inFromServer.readInt();
					int p2Score = inFromServer.readInt();
					boolean isGameOver = inFromServer.readBoolean();
					
					// read the pieces of each player
					Set<BytePair> p1Spaces = Encoder.decodeSpaces(inFromServer.readLong());
					Set<BytePair> p2Spaces = Encoder.decodeSpaces(inFromServer.readLong());
					
					// put the board state, score and isGameOver in a GameState struct
					GameState state = new GameState(board, p1Spaces, p2Spaces, p1Score, p2Score, isGameOver);
					
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
