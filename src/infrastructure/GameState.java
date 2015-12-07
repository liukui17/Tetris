package infrastructure;

import java.awt.Color;
import java.util.Set;

/**
 * A GameState is a "struct" to store information about the game state.
 * It stores the Color[][] representing the board itself, the current p1Score,
 * and a boolean of whether the game is over.
 */
public class GameState {
	private final Color[][] board;
	private Set<BytePair> p1Spaces;
	private Set<BytePair> p2Spaces;
	private final int p1Score;
	private final int p2Score;
	private final boolean isGameOver;
	
	public GameState(Color[][] board, Set<BytePair> p1Spaces, Set<BytePair> p2Spaces,
					 int p1Score, int p2Score, boolean isGameOver) {
		this.board = board;
		this.p1Spaces = p1Spaces;
		this.p2Spaces = p2Spaces;
		this.p1Score = p1Score;
		this.p2Score = p2Score;
		this.isGameOver = isGameOver;
	}
	
	public Color[][] getBoard() {
		return board;
	}
	
	public Set<BytePair> getSpaces(int player) {
		if (player == 0) {
			return p1Spaces;
		} else {
			return p2Spaces;
		}
	}
	
	public int getScore(int player) {
		if (player == 0) {
			return p1Score;
		} else {
			return p2Score;
		}
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
