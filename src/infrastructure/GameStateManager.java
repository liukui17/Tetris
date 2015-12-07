package infrastructure;

import java.awt.Color;
import java.util.Set;

import pieces.Board;
import pieces.Piece;

public class GameStateManager {
	private static final int SCORE_INCREASE_RATE = 10;
	
	private Board board;
	private int p1Score;
	private int p2Score;
	
	public GameStateManager() {
		board = new Board();
		p1Score = 0;
		p2Score = 0;
	}
	
	public synchronized int getScore(int player) {
		if (player == 0) {
			return p1Score;
		} else {
			return p2Score;
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
		
		// get player 1/2 pieces
		Set<BytePair> p1Piece = board.getPiece(0).getBytePairs();
		Set<BytePair> p2Piece = board.getPiece(1).getBytePairs();
		
		return new GameState(currentBoard, p1Piece, p2Piece, p1Score, p2Score, board.isGameOver());
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
	
	private synchronized void updateScore(int player) {
		int change = SCORE_INCREASE_RATE * board.removeFullRows();
		
		if (player == 0) {
			p1Score += change;
		} else {
			p2Score += change;
		}
	}
}
