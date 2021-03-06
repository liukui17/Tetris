package pieces;

import infrastructure.*;

import java.util.Map;
import java.util.Queue;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Board {
	static final int DEBUG = 0;
	
	// size of the queue of upcoming pieces for each player
	static final int PIECE_QUEUE_SIZE = 1;
	
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
	
	/*
	 * array holding each player's currently falling piece
	 */
	Piece[] playerPieces;
	
	List<Queue<Piece>> playerUpcomingPieces;
	
	int sectionWidth;
	
	/*
	 * boardRows stores the FIXED rows of the Tetris game. It only stores the
	 * spaces that have hit the bottom and can no longer move; it does NOT store
	 * the falling pieces.
	 */
	Map<Integer, Square[]> boardRows;
	private int numPlayers;
	
	public Board(int numPlayers) {
		this.numPlayers = numPlayers;
		this.sectionWidth = GameUtil.computeSectionWidth(this.numPlayers);
		
		boardRows = new HashMap<Integer, Square[]>();
		playerPieces = new Piece[numPlayers];
		for (int i = 0; i < playerPieces.length; i++) {
			playerPieces[i] = PieceFactory.generateNewPiece(i, sectionWidth);
		}
		playerUpcomingPieces = new ArrayList<Queue<Piece>>(numPlayers);
		for (int i = 0; i < numPlayers; i++) {
			Queue<Piece> nextQueue = new LinkedList<Piece>();
			for (int j = 0; j < PIECE_QUEUE_SIZE; j++) {
				nextQueue.add(PieceFactory.generateNewPiece(i, sectionWidth));
			}
			playerUpcomingPieces.add(nextQueue);
		}
		checkRep();
	}
	
	public synchronized Piece[] getPlayerUpcomingPieces() {
		Piece[] upcoming = new Piece[numPlayers];
		for (int i = 0; i < upcoming.length; i++) {
			Queue<Piece> nextQueue = playerUpcomingPieces.get(i);
			if (nextQueue != null) {
				upcoming[i] = nextQueue.peek();
			} else {
				upcoming[i] = null;
			}
		}
		return upcoming;
	}
	
/*	public synchronized byte[] getPlayerUpcomingPieces() {
		byte[] upcoming = new byte[numPlayers];
		for (int i = 0; i < upcoming.length; i++) {
			Queue<Piece> nextQueue = playerUpcomingPieces.get(i);
			if (nextQueue != null) {
				upcoming[i] = nextQueue.peek().getByte();
			} else {
				upcoming[i] = 0;
			}
		}
		return upcoming;
	} */
	
	/**
	 * Disables a given player's piece (do this for players
	 * that disconnect mid-game)
	 */
	public void disable(int player) {
		if (player >= 0 && player < numPlayers) {
			playerPieces[player] = null;
			playerUpcomingPieces.set(player, null);
		}
	}
	
	/**
	 * Gets the current falling piece for the given player.
	 * The caller should NOT modify the piece they get.
	 */
	public Piece getPiece(int player) {
		if (player >= playerPieces.length || player < 0) {
			return null;
		}
		return playerPieces[player];
	}
	
	/**
	 * Gets the queue of upcoming pieces for the given player.
	 * The caller should NOT modify the queue they get.
	 */
	public Queue<Piece> getPlayerPieceQueue(int player) {
		if (player >= playerPieces.length || player < 0) {
			return null;
		}
		return playerUpcomingPieces.get(player);
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
		if (playerPieces[player] == null) {
			return false;
		}
		playerPieces[player].moveLeft();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].moveRight();
			return false;
		}
		return true;
	}
	
	public synchronized boolean tryMoveRight(int player) {
		if (playerPieces[player] == null) {
			return false;
		}
		playerPieces[player].moveRight();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].moveLeft();
			return false;
		}
		return true;
	}
	
	public synchronized boolean tryMoveDown(int player) {
		if (playerPieces[player] == null) {
			return false;
		}
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
		if (playerPieces[player] == null) {
			return false;
		}
		playerPieces[player].rotateLeft();
		if (checkNoCollisionsMovedPiece(player) != 0) {
			playerPieces[player].rotateRight();
			return false;
		}
		return true;
	}
	
	public synchronized boolean tryRotateRight(int player) {
		if (playerPieces[player] == null) {
			return false;
		}
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
			return COLLISION_CODES[2];
		} else if (otherPlayers) {
			return COLLISION_CODES[1];
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
		if (playerPieces[player] == null) {
			return true;
		}
		for (Square square : playerPieces[player].squares) {
			Square[] row = boardRows.get(square.y);
			if (row != null) {
				// check there isn't already another square occupying that space
				if (row[GameUtil.modulo(square.x, GameUtil.BOARD_WIDTH)] != null) {
					return false;
				}
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
			if (i != player && playerPieces[i] != null) {
				for (Square square : playerPieces[player].squares) {
					if (playerPieces[i].containsSquare(square, GameUtil.BOARD_WIDTH)) {
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
		Color[] rowColors = new Color[GameUtil.BOARD_WIDTH];
		Square[] row = boardRows.get(rowNum);
		if (row != null) {
			assert rowColors.length == row.length;
			
			for (int i = 0; i < rowColors.length; i++) {
				// null array element means that square is not occupied
				// so set the corresponding color to GRAY (PIECE_COLORS[0])
				if (row[i] == null) {
					rowColors[i] = GameUtil.EMPTY;
				} else {
					rowColors[i] = row[i].color;
				}
			}
		}
		// fill in falling piece squares
		for (int i = 0; i < playerPieces.length; i++) {
			if (playerPieces[i] != null) {
				for (Square square : playerPieces[i].squares) {
					if (square.y == rowNum) {
						rowColors[GameUtil.modulo(square.x, GameUtil.BOARD_WIDTH)] = square.color;
					}
				}
			}
		}
		// fill gaps with gray
		for (int i = 0; i < rowColors.length; i++) {
			if (rowColors[i] == null) {
				rowColors[i] = GameUtil.EMPTY;
			}
		}
		return rowColors;
	}
	
	/**
	 * Removes all full rows on the board
	 * and returns the number of rows removed.
	 */
	public synchronized int removeFullRows() {
		int numRemoved = 0;
		for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
			if (isFullRow(i)) {
				clearRow(i);
				numRemoved++;
			}
		}
		return numRemoved;
	}
	
	/**
	 * Adds the given player's current falling piece to the
	 * squares that are already fixed at the bottom of the board
	 * (private utility function; call when appropriate).
	 */
	private synchronized void addToSetSquares(int player) {
		if (playerPieces[player] != null) {
			// Adds the given player's current piece into the pieces
			// that are no longer moving (hit the bottom).
			for (Square square : playerPieces[player].squares) {
				Square[] row = boardRows.get(square.y);
				if (row == null) {
					boardRows.put(square.y, new Square[GameUtil.BOARD_WIDTH]);
				}
				boardRows.get(square.y)[GameUtil.modulo(square.x, GameUtil.BOARD_WIDTH)] = square;
			}
			/*
			 * Update the current player's falling piece by getting it from the
			 * next piece in that player's queue. Then, generate a new piece and
			 * add that to the queue. 
			 */
			playerPieces[player] = playerUpcomingPieces.get(player).remove();
			playerUpcomingPieces.get(player).add(PieceFactory.generateNewPiece(player, sectionWidth));
		}
	}
	
	/**
	 * Simply helper function that checks if a given row
	 * is full or not. If this isn't needed by anything else
	 * other than for removing full rows, it would probably
	 * be more efficient to just combine this with clearRow
	 * to get a 'clearFullRows' or something so we can avoid
	 * having to allocate a new list for the full row numbers.
	 */
	private synchronized boolean isFullRow(int rowNum) {
		Square[] row = boardRows.get(rowNum);
		if (row == null) {
			return false;
		}
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
		for (int i = rowToClear; i > 0; i--) {
			Square[] above = boardRows.get(i - 1);
			if (above != null) {
				boardRows.put(i, above);
			} else {
				boardRows.remove(i);
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
				assert(boardRows.get(i).length == numPlayers);
			}
		}
	}
}
