package infrastructure;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pieces.Board;
import pieces.Piece;

public class GameStateManager {
	private static final int SCORE_INCREASE_RATE = 10;
	
	private Board board;
	private int[] playerScores;
	private int numPlayers;
	private boolean upcomingAssist;
	
	public GameStateManager(int numPlayers, boolean upcomingAssist) {
		this.numPlayers = numPlayers;
		this.upcomingAssist = upcomingAssist;
		
		board = new Board(numPlayers);
		playerScores = new int[numPlayers];
		for (int i = 0; i < playerScores.length; i++) {
			playerScores[i] = 0;
		}
		
	}
	
	public synchronized int getScore(int player) {
		if (player < 0 || player >= playerScores.length) {
			return -1;
		} else {
			return playerScores[player];
		}
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
		
		// get player pieces
		List<Set<BytePair>> playerPieces = new ArrayList<Set<BytePair>>(numPlayers);
		for (int i = 0; i < numPlayers; i++) {
			Piece nextPlayerPiece = board.getPiece(i);
			if (nextPlayerPiece != null) {
				playerPieces.add(nextPlayerPiece.getBytePairs());
			}
		}
		
		if (upcomingAssist) {
			byte[] upcoming = board.getPlayerUpcomingPieces();
			return new GameState(currentBoard, playerPieces, playerScores, board.isGameOver(), upcoming);
		} else {
			return new GameState(currentBoard, playerPieces, playerScores, board.isGameOver());
		}
	}
	
	public synchronized boolean tryMoveLeft(int player) {
		boolean moved = board.tryMoveLeft(player);
		updateScore(player);
		return moved;
	}
	
	public synchronized boolean tryMoveRight(int player) {
		boolean moved = board.tryMoveRight(player);
		updateScore(player);
		return moved;
	}
	
	public synchronized boolean tryMoveDown(int player) {
		boolean moved = board.tryMoveDown(player);
		updateScore(player);
		return moved;
	}
	
	public synchronized boolean tryRotateLeft(int player) {
		boolean moved = board.tryRotateLeft(player);
		updateScore(player);
		return moved;
	}
	
	public synchronized boolean tryRotateRight(int player) {
		boolean moved = board.tryRotateRight(player);
		updateScore(player);
		return moved;
	}
	
	public synchronized void drop(int player) {
		board.drop(player);
		updateScore(player);
	}
	
	public synchronized boolean isGameOver() {
		return board.isGameOver();
	}
	
	public synchronized Piece getPiece(int player) {
		return board.getPiece(player);
	}
	
	public synchronized void disable(int player) {
		board.disable(player);
	}
	
	private synchronized void updateScore(int player) {
		if (player >= 0 && player < playerScores.length) {
			int change = SCORE_INCREASE_RATE * board.removeFullRows();
			playerScores[player] += change;
		}
	}
}
