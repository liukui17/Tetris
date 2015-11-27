package infrastructure;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
	/**
	 * A "test" main. Will be removed later.
	 */
	public static void main(String[] args) throws InterruptedException {
		GameTimer t = new GameTimer();
		t.start();
		Thread.sleep(5000);
		t.speedUp();
		Thread.sleep(5000);
		t.end();
	}
	
	// The default drop interval in milliseconds
	private static final long DEFAULT_DROP_INTERVAL = 1000;
	
	// The change in time which the drop interval will be decremented by
	private static final long SPEED_UP_CHANGE = 500;
	
	// The Timer backbone of this class
	private Timer timer;
	
	// The TimerTask to perform the dropping
	TimerTask dropper;

	// The current interval between drops in milliseconds
	private long dropInterval;
	
	/**
	 * Constructs a new GameTimer with the specified initialDropInterval.
	 * 
	 * @param initialDropInterval the drop interval at which the game will begin
	 * with
	 */
	public GameTimer(long initialDropInterval) {
		timer = new Timer();
		dropper = new Dropper();
		dropInterval = initialDropInterval;
	}
	
	/**
	 * Constructs a new GameTimer with the default drop interval.
	 */
	public GameTimer() {
		this(DEFAULT_DROP_INTERVAL);
	}
	
	/**
	 * Starts this GameTimer.
	 */
	public void start() {
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
	 * The TimerTask that will drop the blocks
	 * 
	 * DISCLAIMER: IN PROGRESS. Currently on prints messages
	 */
	private class Dropper extends TimerTask {
		private int i = 0;
		
		@Override
		public void run() {
			// NEED TO LOCK BOARD WHEN DROPPING
			System.out.println("dropped " + i);
			i++;
		}
	}
}
