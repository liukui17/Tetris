package infrastructure;

import java.awt.Color;
import java.util.List;
import java.util.Set;

/**
 * A GameState is a "struct" to store information about the game state.
 * It stores the board itself, the current scores for each player, and whether
 * the game is over.
 */
public class GameState {
	private final Color[][] board;
	private List<Set<BytePair>> playerSpaces;
	private final int[] playerScores;
	private final boolean isGameOver;
	
	public GameState(Color[][] board, List<Set<BytePair>> playerSpaces,
					 int[] playerScores, boolean isGameOver) {
		this.board = board;
		this.playerSpaces = playerSpaces;
		this.isGameOver = isGameOver;
		this.playerScores = playerScores;
	}
	
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
