package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A GameThread manages one single game of collaborative Tetris
 * between two players p1 and p2.
 */
public class GameThread implements Runnable {
	private static final int INITIAL_THRESHOLD = 100;
	private static final int THRESHOLD_INCREASE_RATE = 100;

	// sockets for each player
	Socket[] playerSockets;

	// BlockingQueues allows data transfer between threads
	private BlockingQueue<Byte> commandsFromClient;
	private BlockingQueue<GameState> outStates;

	// the next threshold to reach before increasing the drop rate
	private int threshold;

	private GameStateManager gameState;
	private GameTimer timer;

	public GameThread(Socket[] playerSockets, long initialDropInterval) throws IOException {	
		this.playerSockets = playerSockets;

		commandsFromClient = new LinkedBlockingQueue<Byte>();
		outStates = new LinkedBlockingQueue<GameState>();

		threshold = INITIAL_THRESHOLD;

		gameState = new GameStateManager();
		timer = new GameTimer(initialDropInterval, gameState, outStates);
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
	/*	try {
			new Thread(new ServerConnectionManager(commandsFromClient, outStates,
					new DataInputStream(playerSockets[0].getInputStream()),
					new DataOutputStream(playerSockets[0].getOutputStream()),
					new DataInputStream(playerSockets[1].getInputStream()),
					new DataOutputStream(playerSockets[1].getOutputStream()))).start(); */
			new Thread(new ServerConnectionsManager(commandsFromClient, outStates, playerSockets)).start();
	/*	} catch (IOException e) {
			e.printStackTrace();
		} */

		// send out the initial state to the client
		GameState initialState = gameState.getCurrentState();
		outStates.add(initialState);

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

				// decode the command and perform it
				Encoder.decodeCommand(commandByte, gameState);

				// get the new game state
				GameState updatedGameState = gameState.getCurrentState();

				int sumScore = 0;
				for (int i = 0; i < GameUtil.NUM_PLAYERS; i++) {
					sumScore += gameState.getScore(i);

				}
				/*
				 * speed up the timer if we've reached the current threshold and update
				 * the threshold
				 */
				if (sumScore >= threshold) {
					timer.speedUp();
					threshold += THRESHOLD_INCREASE_RATE;
				}

				// determine if it's game over
				boolean isGameOver = updatedGameState.getIsGameOver();

				outStates.add(updatedGameState);

				// break out of the while(true) loop if it's game over
				if (isGameOver) {
					timer.end();
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
