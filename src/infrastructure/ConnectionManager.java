package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class ConnectionManager implements Runnable {
	private BlockingQueue<Color[]> inFromClientQueue;
	private BlockingQueue<Color[]> outToClientQueue;
	private DataInputStream inFromClient;
	private DataOutputStream outToClient;
	
	public ConnectionManager(BlockingQueue<Color[]> inFromClientQueue,
												BlockingQueue<Color[]> outToClientQueue,
												DataInputStream inFromClient,
												DataOutputStream outToClient) {
		this.inFromClientQueue = inFromClientQueue;
		this.outToClientQueue  = outToClientQueue;
		this.inFromClient = inFromClient;
		this.outToClient  = outToClient;
	}

	@Override
	public void run() {
		new Thread(new ReadThread()).start();
		new Thread(new WriteThread()).start();
	}
	
	public class ReadThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					long msgLong = inFromClient.readLong();
					Color[] row = new Color[Encoder.BOARD_WIDTH];
					Encoder.networkMessageToGridRow(msgLong, row);
					inFromClientQueue.add(row);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public class WriteThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				Color[] row = outToClientQueue.remove();
				long msgLong = Encoder.gridRowToNetworkMessage(row);
				try {
					outToClient.writeLong(msgLong);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
