package infrastructure;

import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameTimer extends Thread {	
	// The default drop interval in milliseconds
	private static final long DEFAULT_DROP_INTERVAL = 1000;

	// The change in time which the drop interval will be decremented by
	private static final long SPEED_UP_CHANGE = 500;

	private BlockingQueue<GameState> out;

	// The timer backbone of this class
	private ScheduledExecutorService timer;

	// The module to perform the dropping
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
		timer = Executors.newSingleThreadScheduledExecutor();
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
		timer.scheduleAtFixedRate(dropper, 0, dropInterval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Ends this GameTimer.
	 */
	public void end() {
		timer.shutdownNow();
		System.out.println("Ended Timer");
	}

	/**
	 * Speeds up the game timer by the default speed up change
	 */
	public void speedUp() {
		dropInterval -= SPEED_UP_CHANGE;
		dropper.cancel();
		dropper = new Dropper();
		timer.scheduleAtFixedRate(dropper, 0, dropInterval, TimeUnit.MILLISECONDS);
		System.out.println("Sped up");
	}

	/**
	 * The TimerTask that will drop the blocks for both players
	 */
	private class Dropper extends TimerTask {
		@Override
		public void run() {
			gameState.tryMoveDown(0);
			gameState.tryMoveDown(1);

			GameState currentState = gameState.getCurrentState();

			out.add(currentState);
		}
	}
}
