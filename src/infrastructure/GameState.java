package infrastructure;

import java.awt.Color;

/**
 * A GameState is a "struct" to store information about the game state.
 * It stores the Color[][] representing the board itself, the current score,
 * and a boolean of whether the game is over.
 */
public class GameState {
	final Color[][] board;
	final int score;
	private final boolean isGameOver;
	
	public GameState(Color[][] board, int score, boolean isGameOver) {
		this.board = board;
		this.score = score;
		this.isGameOver = isGameOver;
	}
	
	public Color[][] getBoard() {
		return board;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean getIsGameOver() {
		return isGameOver;
	}
}
