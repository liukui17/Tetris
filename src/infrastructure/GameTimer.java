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
	private static final long SPEED_UP_CHANGE = 50;

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
		timer.scheduleAtFixedRate(dropper, 0, dropInterval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Ends this GameTimer.
	 */
	public void end() {
		timer.shutdownNow();
	}

	/**
	 * Speeds up the game timer by the default speed up change
	 */
	public void speedUp() {
		// check that we can actually still speed up
		if (dropInterval > 50) {
			dropInterval -= SPEED_UP_CHANGE;
		}
		
		dropper.cancel();
		dropper = new Dropper();
		timer.scheduleAtFixedRate(dropper, 0, dropInterval, TimeUnit.MILLISECONDS);
		System.out.println("Sped up");
	}

	/**
	 * The TimerTask that will drop the blocks for all players
	 */
	private class Dropper extends TimerTask {
		@Override
		public void run() {
			for (int i = 0; i < GameUtil.NUM_PLAYERS; i++) {
				gameState.tryMoveDown(i);
			}

			GameState currentState = gameState.getCurrentState();

			out.add(currentState);
		}
	}
}
