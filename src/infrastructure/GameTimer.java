package infrastructure;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import pieces.Board;

public class GameTimer extends Thread {	
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
	
	private Board board;
	
	private Score score;
	
	/**
	 * Constructs a new GameTimer with the specified initialDropInterval.
	 * 
	 * @param initialDropInterval the drop interval at which the game will begin
	 * with
	 */
	public GameTimer(long initialDropInterval, Board board, Score score) {
		timer = new Timer();
		dropper = new Dropper();
		dropInterval = initialDropInterval;
		this.board = board;
		this.score = score;
	}
	
	/**
	 * Constructs a new GameTimer with the default drop interval.
	 */
	public GameTimer(Board board, Score score) {
		this(DEFAULT_DROP_INTERVAL, board, score);
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
			System.out.println("inside dropper");
			if ((Math.random() * 10) % 2 == 0) {
				board.tryMoveDown(0);
				board.tryMoveDown(1);
			} else {
				board.tryMoveDown(1);
				board.tryMoveDown(0);
			}
			

		}
	}
}
