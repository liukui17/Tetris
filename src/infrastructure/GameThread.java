package infrastructure;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pieces.Board;

/**
 * A GameThread manages one single game of collaborative Tetris
 * between two players p1 and p2.
 */
public class GameThread implements Runnable {
	private static final int SCORE_INCREASE_RATE = 10;
	private static final int INITIAL_THRESHOLD = 100;
	private static final int THRESHOLD_INCREASE_RATE = 100;
	
	// sockets for each player
	private Socket p1Socket;
	private Socket p2Socket;
	
	// BlockingQueues allows data transfer between threads
	private BlockingQueue<Byte> commandsFromClient;
	private BlockingQueue<GameState> outStates;
	
	// the current score
	private int score;
	
	// the next threshold to reach before increasing the drop rate
	private int threshold;
	
	private Board board;
	private GameTimer timer;
	
	public GameThread(Socket p1Socket, Socket p2Socket) throws IOException {	
		this.p1Socket = p1Socket;
		this.p2Socket = p2Socket;
		
		commandsFromClient = new LinkedBlockingQueue<Byte>();
		outStates = new LinkedBlockingQueue<GameState>();
		
		score = 0;
		threshold = INITIAL_THRESHOLD;
		
		board = new Board();
		timer = new GameTimer(board);
	}

	@Override
	public void run() {
		/*
		 * Set-up and start the thread that reads commandsFromClient from players and
		 * sends out GameState responses.
		 * 
		 * Commands issued by either player will be enqueued into the shared
		 * commandsFromClient BlockingQueue.
		 * 
		 * GameState data to be sent to players will be dequeued from the shared
		 * outStates BlockingQueue.
		 */
		try {
			new Thread(new ServerConnectionManager(commandsFromClient, outStates,
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
		 * updates the board, and enqueues the new game state to the outStates
		 * BlockingQueue.
		 */
		while (true) {
			byte commandByte;
			try {
				// get the next command
				commandByte = commandsFromClient.take();
				
				// find out whose piece to move
				int player;
				if (commandByte >= 0) {
					player = 0;  // player 1's move
				} else {
					player = 1;  // player 2's move
				}
				
				// decode the command and performs it
				Encoder.decodeCommand(commandByte, player, board);
				
				// find full rows, remove them, and update score
				List<Integer> fullRows = board.getFullRows();
				
				for (Integer r : fullRows) {
					score += SCORE_INCREASE_RATE;
					board.clearRow(r);
				}
				
				/*
				 * speed up the timer if we've reached the current threshold and update
				 * the threshold
				 */
				if (score >= threshold) {
					timer.speedUp();
					threshold += THRESHOLD_INCREASE_RATE;
				}
				
				// build a Color[][] of the updated board
				Color[][] updatedBoard = new Color[GameUtil.BOARD_HEIGHT][GameUtil.BOARD_WIDTH];
				for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
					updatedBoard[i] = board.getRowColors(i);;
				}
				
				// determine if it's game over
				boolean isGameOver = false; // board.isGameOver();
				
				// put the updated board, score and isGameOver in a GameState and send it
				GameState state = new GameState(updatedBoard, score, isGameOver);
				
				outStates.add(state);
				
				// break out of the while(true) loop if it's game over
				if (isGameOver) {
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
