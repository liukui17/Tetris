package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pieces.Board;

/**
 * A GameThread manages one single game of collaborative Tetris
 * between two players p1 and p2.
 */
public class GameThread implements Runnable {
	// sockets for each player
	private Socket p1Socket;
	private Socket p2Socket;
	
	// BlockingQueues allows data transfer between threads
	private BlockingQueue<Byte> commands;
	private BlockingQueue<Color[]> out;
	
	private int score;
	private Board board;
	private GameTimer timer;
	
	public GameThread(Socket p1Socket, Socket p2Socket) throws IOException {	
		this.p1Socket = p1Socket;
		this.p2Socket = p2Socket;
		
		commands = new LinkedBlockingQueue<Byte>();
		out = new LinkedBlockingQueue<Color[]>();
		
		score = 0;
		
		board = new Board();
		timer = new GameTimer(board);
	}

	@Override
	public void run() {
		/*
		 * Set-up and start the thread that reads commands from players and
		 * sends out responses.
		 * 
		 * Commands issued by either player will be enqueued into the shared
		 * commands BlockingQueue.
		 * 
		 * Board data to be sent to players will be dequeued from the shared
		 * out BlockingQueue.
		 */
		try {
			new Thread(new ServerConnectionManager(commands, out,
					new DataInputStream(p1Socket.getInputStream()),
					new DataOutputStream(p1Socket.getOutputStream()),
					new DataInputStream(p2Socket.getInputStream()),
					new DataOutputStream(p1Socket.getOutputStream()))).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Start the timer
		 */
		timer.start();
		
		/*
		 * Dequeues command bytes from the command BlockingQueue, parses it,
		 * updates the board, and enqueues the new board state to the out
		 * BlockingQueue.
		 * 
		 * Currently only prints out messages for testing until the rest of the
		 * game is implemented
		 */
		while (true) {
			byte commandByte;
			try {
				// get the next command
				commandByte = commands.take();
				
				// find out whose piece to move
				String pieceToMove;
				if (commandByte >= 0) {
					pieceToMove = "p1";
				} else {
					pieceToMove = "p2";
				}
				
				// find out the command to perform
				String command = Encoder.decodeCommand(commandByte);
				System.out.println(pieceToMove + " " + command);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
