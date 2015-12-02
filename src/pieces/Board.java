package pieces;

import infrastructure.*;

import java.util.Map;
import java.util.Set;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Board {
	static final int DEBUG = 0;
	
	/* 
	 * 0 indicates no collision
	 * 		handle: nothing; its as valid move
	 * 1 indicates collision with a fixed square
	 * 		handle: if it is moving down, move back up and set this piece down
	 * 						and generate a new piece for that player; for other operations,
	 * 						just undo it to get back to previous (valid) state
	 * 2 indicates collision with another player's piece
	 * 		handle: undo the previous operation to get back to
	 * 						previous (valid) state
	 * 3 indicates collision with both a fixed square another player's piece
	 * 		handle: if moving down, move the piece back up and then set it
	 * 						down since it has gone as far down as it can (even though
	 * 						it has hit another player's piece)
	 * 						
	 */
	static final int[] COLLISION_CODES = {0, 1, 2, 3};
	
	/*
	 * General comment about move mechanics:
	 * The way I have it is we move the piece regardless, check if
	 * it leads to a valid state, then do the reverse/inverse operation
	 * if it isn't to move it back to where it was. It will save us
	 * from having to create new Pieces for each move. The extra move
	 * back (from inverse operation) is cheap and we have to check
	 * anyways.
	 */
	
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
	
	/**
	 * Drops a player's piece to the bottom of the board.
	 * A bit inefficient since all it does is keep trying
	 * to fall (one row at a time) until it can't anymore.
	 * If too slow, can try optimizing this.
	 */
	public synchronized void drop(int player) {
		while (tryMoveDown(player)) {}
	}
	
	public synchronized boolean tryMoveLeft(int player) {
		playerPieces[player].moveLeft();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].moveRight();
			return false;
		}
		return true;
	}
	
	public synchronized boolean tryMoveRight(int player) {
		playerPieces[player].moveRight();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].moveLeft();
			return false;
		}
		return true;
	}
	
	public synchronized boolean tryMoveDown(int player) {
		playerPieces[player].moveDown();
		int collisions = checkNoCollisionsMovedPiece(player);
		if (collisions == 1 || collisions == 3) {
			playerPieces[player].moveUp();
			addToSetSquares(player);
			return false;
		} else if (collisions == 2) {
			playerPieces[player].moveUp();
			return false;
		}
		return true;
	}
	
	public synchronized boolean tryRotateLeft(int player) {
		playerPieces[player].rotateLeft();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].rotateRight();
			return false;
		}
		return true;
	}
	
	public synchronized boolean tryRotateRight(int player) {
		playerPieces[player].rotateRight();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].rotateLeft();
			return false;
		}
		return true;
	}
	
	/**
	 * Checks for collisions in the entire board. Don't need to call
	 * this every time something changes since players' moves are
	 * processed sequentially so we can just check if that particular
	 * player's move is valid. Perhaps every now and then. Good to just
	 * have this method.
	 */
	public synchronized boolean checkNoCollisionsEntireBoard() {
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
	
	/**
	 * Check that the given player's piece isn't colliding with
	 * anything else, i.e. its new position is valid. Call this
	 * immediately after that particular player's piece has moved
	 * (no need to verify the validity of all of the other players'
	 * pieces). Returns appropriate collision code to indicate to
	 * caller how a collision should be handled.
	 */
	public synchronized int checkNoCollisionsMovedPiece(int player) {
		boolean setSquares = checkNoCollisionsWithSetSquares(player);
		boolean otherPlayers = checkNoCollisionsWithOtherPlayers(player);
		if (setSquares && otherPlayers) {
			return COLLISION_CODES[0];
		} else if (setSquares) {
			return COLLISION_CODES[1];
		} else if (otherPlayers) {
			return COLLISION_CODES[2];
		} else {
			return COLLISION_CODES[3];
		}
	}
	
	/**
	 * Given a player, check that his/her current falling piece isn't
	 * colliding with any of the squares already fixed or are out of
	 * the bounds of the board.
	 */
	private synchronized boolean checkNoCollisionsWithSetSquares(int player) {
		for (Square square : playerPieces[player].squares) {
			Square[] row = boardRows.get(square.y);
			if (row != null) {
			//	System.out.println("This row is null");
				// check there isn't already another square occupying that space
				if (row[square.x] != null) {
					return false;
				}
			}
			// check its within board bounds
			if (square.y >= GameUtil.BOARD_HEIGHT || square.y < 0) {
				return false;
			}
		}
		if (playerPieces[player].hasHitBottom()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Check that there are no collisions between the given player's piece
	 * and the pieces of the other players.
	 */
	private synchronized boolean checkNoCollisionsWithOtherPlayers(int player) {
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
	
	/**
	 * Gets all of the colors of the given row.
	 */
	public synchronized Color[] getRowColors(int rowNum) {
		Square[] row = boardRows.get(rowNum);
		if (row != null) {
			Color[] rowColors = new Color[row.length];
			for (int i = 0; i < rowColors.length; i++) {
				// null array element means that square is not occupied
				// so set the corresponding color to GRAY (PIECE_COLORS[0])
				if (row[i] == null) {
					rowColors[i] = GameUtil.PIECE_COLORS[0];
				} else {
					rowColors[i] = row[i].color;
				}
			}
			return rowColors;
		} else {
			return null;
		}
	}
	
	/**
	 * Removes all full rows on the board
	 * and returns the number of rows removed.
	 */
	public synchronized int removeFullRows() {
		List<Integer> fullRows = getFullRows();
		for (int i = 0; i < fullRows.size(); i++) {
			clearRow(fullRows.get(i));
		}
		return fullRows.size();
	}
	
	/**
	 * Adds the given player's current falling piece to the
	 * squares that are already fixed at the bottom of the board
	 * (private utility function; call when appropriate).
	 */
	private synchronized void addToSetSquares(int player) {
		// Adds the given player's current piece into the pieces
	  // that are no longer moving (hit the bottom).
		for (Square square : playerPieces[player].squares) {
			Square[] row = boardRows.get(square.y);
			if (row == null) {
				boardRows.put(square.y, new Square[GameUtil.BOARD_WIDTH]);
			}
			boardRows.get(square.y)[square.x] = square;
		}
		// generate new piece for the player
		playerPieces[player] = PieceFactory.generateNewPiece(player);
	}
	
	/**
	 * Returns all of the full rows in the board.
	 */
	public synchronized List<Integer> getFullRows() {
		List<Integer> fullRows = new LinkedList<Integer>();
		Set<Integer> rowNumbers = boardRows.keySet();
		for (Integer i : rowNumbers) {
			Square[] nextRow = boardRows.get(i);
			if (isFullRow(nextRow)) {
				fullRows.add(i);
			}
		}
		return fullRows;
	}
	
	/**
	 * Simply helper function that checks if a given row
	 * is full or not. If this isn't needed by anything else
	 * other than for removing full rows, it would probably
	 * be more efficient to just combine this with clearRow
	 * to get a 'clearFullRows' or something so we can avoid
	 * having to allocate a new list for the full row numbers.
	 */
	private synchronized boolean isFullRow(Square[] row) {
		for (int i = 0; i < row.length; i++) {
			if (row[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Clears the specified row unconditionally if it is present
	 * (private utility function).
	 */
	private synchronized void clearRow(int rowToClear) {
		Square[] row = boardRows.get(rowToClear);
		if (row != null) {
			if (isFullRow(boardRows.get(rowToClear))) {
				boardRows.remove(rowToClear);
			}
		}
	}
	
	/**
	 * Returns if the game is over. A game is defined to be
	 * over if the top row is nonempty.
	 */
	public synchronized boolean isGameOver() {
		Square[] top = boardRows.get(0);
		if (top != null) {
			for (int i = 0; i < top.length; i++) {
				if (top[i] != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	private synchronized void checkRep() {
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
