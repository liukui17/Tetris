package infrastructure;

import java.awt.Color;

/**
 * A GameState is a "struct" to store information about the game state.
 * It stores the Color[][] representing the board itself, the current score,
 * and a boolean of whether the game is over.
 */
public class GameState {
	private final Color[][] board;
	private final int score;
	private final boolean isGameOver;
	
	public GameState(Color[][] board, int score, boolean isGameOver) {
		this.board = board;
		this.score = score;
		this.isGameOver = isGameOver;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param other
	 */
	public GameState(GameState other) {
		Color[][] otherBoard = other.getBoard();
		board = new Color[otherBoard.length][otherBoard[0].length];
		for (int i = 0; i < otherBoard.length; i++) {
			for (int j = 0; j < otherBoard[0].length; j++) {
				board[i][j] = otherBoard[i][j];
			}
		}
		
		score = other.getScore();
		isGameOver = other.getIsGameOver();
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
	
	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(GameUtil.colorToString(board[i][j]) + " ");
			}
			System.out.println();
		}
	}
}
