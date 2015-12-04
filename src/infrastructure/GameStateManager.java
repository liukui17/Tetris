package infrastructure;

import java.awt.Color;

import pieces.Board;

public class GameStateManager {
	private static final int SCORE_INCREASE_RATE = 10;
	
	private Board board;
	private int score;
	
	public GameStateManager() {
		board = new Board();
		score = 0;
	}
	
	public int getScore() {
		return score;
	}
	
	/**
	 * Returns a new GameState of the current game state.
	 * 
	 * @return a new GameState of the current game state
	 */
	public synchronized GameState getCurrentState() {
		// convert board into a Color[][]
		Color[][] currentBoard = new Color[GameUtil.BOARD_HEIGHT][GameUtil.BOARD_WIDTH];
		for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
			currentBoard[i] = board.getRowColors(i);
		}
		return new GameState(currentBoard, score, board.isGameOver());
	}
	
	public synchronized boolean tryMoveLeft(int player) {
		boolean moved = board.tryMoveLeft(player);
		updateScore();
		System.out.println("LEFT");
		return moved;
	}
	
	public synchronized boolean tryMoveRight(int player) {
		boolean moved = board.tryMoveRight(player);
		updateScore();
		System.out.println("RIGHT");
		return moved;
	}
	
	public synchronized boolean tryMoveDown(int player) {
		boolean moved = board.tryMoveDown(player);
		updateScore();
		return moved;
	}
	
	public synchronized boolean tryRotateLeft(int player) {
		boolean moved = board.tryRotateLeft(player);
		updateScore();
		return moved;
	}
	
	public synchronized boolean tryRotateRight(int player) {
		boolean moved = board.tryRotateRight(player);
		updateScore();
		return moved;
	}
	
	public synchronized void drop(int player) {
		board.drop(player);
		updateScore();
	}
	
	private void updateScore() {
		score += SCORE_INCREASE_RATE * board.removeFullRows();
	}
}
