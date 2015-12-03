package infrastructure;

import java.awt.Color;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public class GameTimer extends Thread {	
	// The default drop interval in milliseconds
	private static final long DEFAULT_DROP_INTERVAL = 1000;
	
	// The change in time which the drop interval will be decremented by
	private static final long SPEED_UP_CHANGE = 500;
	
	private BlockingQueue<GameState> out;
	
	// The Timer backbone of this class
	private Timer timer;
	
	// The TimerTask to perform the dropping
	TimerTask dropper;

	// The current interval between drops in milliseconds
	private long dropInterval;
	
	private GameStateManager gameState;
	
	/**
	 * Constructs a new GameTimer with the specified initialDropInterval.
	 * 
	 * @param initialDropInterval the drop interval at which the game will begin
	 * with
	 */
	public GameTimer(long initialDropInterval, GameStateManager gameState,
									 BlockingQueue<GameState> out) {
		timer = new Timer();
		dropper = new Dropper();
		dropInterval = initialDropInterval;
		this.gameState = gameState;
		this.out = out;
	}
	
	/**
	 * Constructs a new GameTimer with the default drop interval.
	 */
	public GameTimer(GameStateManager gameState, BlockingQueue<GameState> out) {
		this(DEFAULT_DROP_INTERVAL, gameState, out);
	}
	
	@Override
	public void run() {
		System.out.println("Started timer");
		timer.scheduleAtFixedRate(dropper, 0, dropInterval);
	}
	
	/**
	 * Ends this GameTimer.
	 */
	public void end() {
		timer.cancel();
		System.out.println("Ended Timer");
	}
	
	/**
	 * Speeds up the game timer by the default speed up change
	 */
	public void speedUp() {
		dropInterval -= SPEED_UP_CHANGE;
		dropper.cancel();
		dropper = new Dropper();
		timer.scheduleAtFixedRate(dropper, 0, dropInterval);
		System.out.println("Sped up");
	}
	
	/**
	 * The TimerTask that will drop the blocks for both players
	 */
	private class Dropper extends TimerTask {
		@Override
		public void run() {
			// randomize for fairness
			if (GameUtil.rng.nextInt(2) == 0) {
				if (!gameState.tryMoveDown(0)) {
					System.out.println("Failed to drop 1");
				}
				if (!gameState.tryMoveDown(1)) {
					System.out.println("Failed to drop 2");
				}
			} else {
				if (!gameState.tryMoveDown(1)) {
					System.out.println("Failed to drop 3");
				}
				if (!gameState.tryMoveDown(0)) {
					System.out.println("Failed to drop 4");
				}
			}
			
			GameState currentState = gameState.getCurrentState();
			Color[][] board = currentState.getBoard();
			
			for (int i = 0; i < board.length; i++) {
				System.out.println(Arrays.deepToString(board[i]));
			}
			
			out.add(currentState);
		}
	}
}
