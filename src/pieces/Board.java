package pieces;

import infrastructure.*;

import java.util.Map;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Board {
	static final int DEBUG = 0;
	
	// 0 indicates no collision
	// 1 indicates collision with a set square
	// 2 indicates collision with another player's piece
	// 3 indicates collision with both a set square another player's piece
	static final int[] COLLISION_CODES = {0, 1, 2, 3};
	
	Piece[] playerPieces;
	Map<Integer, Square[]> boardRows;
	
	public Board() {
		boardRows = new HashMap<Integer, Square[]>();
		playerPieces = new Piece[GameUtil.NUM_PLAYERS];
		for (int i = 0; i < playerPieces.length; i++) {
			playerPieces[i] = PieceFactory.generateNewPiece(i);
		}
		checkRep();
	}
	
	public void drop(int player) {
		while (tryMoveDown(player)) {}
	}
	
	public boolean tryMoveLeft(int player) {
		playerPieces[player].moveLeft();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].moveRight();
			return false;
		}
		return true;
	}
	
	public boolean tryMoveRight(int player) {
		playerPieces[player].moveRight();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].moveLeft();
			return false;
		}
		return true;
	}
	
	public boolean tryMoveDown(int player) {
		playerPieces[player].moveDown();
		int collisions = checkNoCollisionsMovedPiece(player);
		if (collisions == 1) {
			addToSetSquares(player);
			return false;
		} else if (collisions == 2 && collisions == 3) {
			playerPieces[player].moveUp();
			return false;
		}
		return true;
	}
	
	public boolean tryRotateLeft(int player) {
		playerPieces[player].rotateLeft();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].rotateRight();
			return false;
		}
		return true;
	}
	
	public boolean tryRotateRight(int player) {
		playerPieces[player].rotateRight();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].rotateLeft();
			return false;
		}
		return true;
	}
	
	public boolean checkNoCollisionsEntireBoard() {
		for (int i = 0; i < playerPieces.length; i++) {
			if (!checkNoCollisionsWithSetSquares(i)) {
				return false;
			}
		}
		for (int i = 0; i < playerPieces.length; i++) {
			if (!checkNoCollisionsWithOtherPlayers(i)) {
				return false;
			}
		}
		return true;
	}
	
	public int checkNoCollisionsMovedPiece(int player) {
		boolean setSquares = checkNoCollisionsWithSetSquares(player);
		boolean otherPlayers = checkNoCollisionsWithOtherPlayers(player);
		if (setSquares && otherPlayers) {
			return COLLISION_CODES[3];
		} else if (setSquares) {
			return COLLISION_CODES[1];
		} else if (otherPlayers) {
			return COLLISION_CODES[2];
		} else {
			return COLLISION_CODES[0];
		}
	}
	
	private boolean checkNoCollisionsWithSetSquares(int player) {
		for (Square square : playerPieces[player].squares) {
			if (boardRows.get(square.y)[square.x] != null) {
				return false;
			}
		}
		if (playerPieces[player].hasHitBottom()) {
			return false;
		}
		return true;
	}
	
	private boolean checkNoCollisionsWithOtherPlayers(int player) {
		for (int i = 0; i < playerPieces.length; i++) {
			if (i != player) {
				for (Square square : playerPieces[player].squares) {
					if (playerPieces[i].containsSquare(square)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public Color[] getRowColors(int rowNum) {
		Square[] row = boardRows.get(rowNum);
		if (row != null) {
			Color[] rowColors = new Color[row.length];
			for (int i = 0; i < rowColors.length; i++) {
				rowColors[i] = row[i].color;
			}
			return rowColors;
		} else {
			return null;
		}
	}
	
	public void addToSetSquares(int player) {
		for (Square square : playerPieces[player].squares) {
			Square[] row = boardRows.get(square.y);
			if (row == null) {
				boardRows.put(square.y, new Square[GameUtil.BOARD_WIDTH]);
			}
			boardRows.get(square.y)[square.x] = square;
		}
		playerPieces[player] = PieceFactory.generateNewPiece(player);
	}
	
	public List<Integer> getFullRows() {
		List<Integer> fullRows = new LinkedList<Integer>();
		for (int i = 0; i < boardRows.keySet().size(); i++) {
			if (isFullRow(boardRows.get(i))) {
				fullRows.add(i);
			}
		}
		return fullRows;
	}
	
	public boolean isFullRow(Square[] row) {
		for (int i = 0; i < row.length; i++) {
			if (row[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	public void clearRow(int rowToClear) {
		Square[] row = boardRows.get(rowToClear);
		if (row != null) {
			if (isFullRow(boardRows.get(rowToClear))) {
				boardRows.remove(rowToClear);
			}
		}
	}
	
	private void checkRep() {
		for (int i = 0; i < playerPieces.length; i++) {
			assert(playerPieces[i] != null);
		}
		assert(boardRows != null);
		assert(boardRows.keySet().size() >= 0);
		assert(boardRows.keySet().size() < GameUtil.BOARD_HEIGHT);
		if (DEBUG > 0) {
			for (int i = 0; i < boardRows.keySet().size(); i++) {
				assert(boardRows.get(i).length == GameUtil.BOARD_WIDTH);
			}
		}
	}
}
