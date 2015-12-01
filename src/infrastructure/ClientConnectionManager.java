package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ClientConnectionManager implements Runnable {
	private BlockingQueue<Color[][]> inputBoards;
	private BlockingQueue<Byte> outputCommandBytes;
	
	private DataInputStream inFromServer;
	private DataOutputStream outToServer;

	public ClientConnectionManager(BlockingQueue<Byte> commands,
																 BlockingQueue<Color[][]> inputBoards,
																 DataInputStream inFromServer,
																 DataOutputStream outToServer) {
		this.outputCommandBytes = commands;
		this.inputBoards = inputBoards;
		
		this.inFromServer = inFromServer;
		this.outToServer = outToServer;
	}

	@Override
	public void run() {
		new Thread(new ReadThread()).start();
		new Thread(new WriteThread()).start();
	}
	
	/**
	 * The thread responsible for reading longs from the server and converting
	 * them to Color[][]'s
	 */
	public class ReadThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Color[][] board = new Color[GameUtil.BOARD_HEIGHT][GameUtil.BOARD_WIDTH];
					for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
						long rowLong = inFromServer.readLong();
						Encoder.networkMessageToGridRow(rowLong, board[i]);
					}
					inputBoards.add(board);
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
