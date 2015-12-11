package infrastructure;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A GameState is a "struct" to store information about the game state.
 * It stores the Color[][] representing the board itself, the current p1Score,
 * and a boolean of whether the game is over.
 */
public class GameState {
	private final Color[][] board;
	private List<Set<BytePair>> playerSpaces;
//	private Set<BytePair> p1Spaces;
//	private Set<BytePair> p2Spaces;
	private final int[] playerScores;
//	private final int p1Score;
//	private final int p2Score;
	private final boolean isGameOver;
	
	public GameState(Color[][] board, List<Set<BytePair>> playerSpaces,
					 int[] playerScores, boolean isGameOver) {
		this.board = board;
		this.playerSpaces = playerSpaces;
		this.isGameOver = isGameOver;
		this.playerScores = playerScores;
	}

/*	public GameState(Color[][] board, Set<BytePair> p1Spaces, Set<BytePair> p2Spaces,
					 int p1Score, int p2Score, boolean isGameOver) {
		this.board = board;
		this.p1Spaces = p1Spaces;
		this.p2Spaces = p2Spaces;
		this.p1Score = p1Score;
		this.p2Score = p2Score;
		this.isGameOver = isGameOver;
	} */
	
	public Color[][] getBoard() {
		return board;
	}
	
	public Set<BytePair> getSpaces(int player) {
		if (player < 0 || player >= playerSpaces.size()) {
			return null;
		}
		return playerSpaces.get(player);
	}
	
	public int getScore(int player) {
		if (player < 0 || player >= playerScores.length) {
			return -1;
		} else {
			return playerScores[player];
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
