package pieces;
import java.util.List;
import java.util.Set;

import infrastructure.BytePair;
import infrastructure.GameUtil;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class Piece {
	static final int DEBUG = 0;
	public static final int NUM_SQUARES_PER_PIECE = 4;
	
	// Collection is just a collection of static methods that
	// operate on or return collections; best use something like
	// ArrayList for constant time access and amortized O(1) add
	// since it resizes; leave without access modifier so subclasses
	// have access
	List<Square> squares;
	int orientation;
	
	public Piece(List<Square> squares) {
		this.squares = new ArrayList<Square>(squares);
		orientation = 0;
		checkRep();
	}
	
	// essentially a copy constructor
	public Piece(Piece other) {
		this.squares = new ArrayList<Square>();
		for (Square square : other.squares) {
			squares.add(new Square(square));
		}
		orientation = 0;
	}
	
	public Piece() {
		squares = new ArrayList<Square>(NUM_SQUARES_PER_PIECE);
		orientation = 0;
	}
	
	public synchronized List<Square> getSpaces() {
		return squares;
	}
	
	/**
	 * Returns a Set of BytePairs of the coordinates of this piece's spaces.
	 * 
	 * @return a Set of BytePairs of the coordinates of this piece's spaces.
	 */
	public synchronized Set<BytePair> getBytePairs() {
		Set<BytePair> set = new HashSet<BytePair>(); 
		for (Square s : squares) {
			set.add(s.getBytePair());
		}
		return set;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public synchronized boolean containsSquare(Square other, int width) {
		for (Square square : squares) {
			if (GameUtil.modulo(other.x, width) == GameUtil.modulo(square.x, width)
					&& other.y == square.y) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Define "hitting" the bottom to mean having a square
	 * that is at row 24 (which doesn't exist); signals
	 * caller to put the piece back in its previous, valid
	 * position.
	 */
	public synchronized boolean hasHitBottom() {
		for (Square square : squares) {
			if (square.y >= GameUtil.BOARD_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	protected synchronized void moveLeft() {
		for (Square square : squares) {
			square.x--;
		}
	}

	protected synchronized void moveRight() {
		for (Square square : squares) {
			square.x++;
		}
	}

	protected synchronized void moveDown() {
		for (Square square : squares) {
			square.y = square.y + 1;
		}
	}
	
	protected synchronized void moveUp() {
		for (Square square : squares) {
			square.y = square.y - 1;
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("[");
		buf.append(squares.get(0).toString());
		for (int i = 1; i < squares.size(); i++) {
			buf.append(", " + squares.get(i).toString());
		}
		buf.append("]");
		return buf.toString();
	}
	
	public abstract byte getByte();

	protected abstract void rotateLeft();

	protected abstract void rotateRight();
	
	protected void rotateLeftFixedSquareOrigin(int pivotIndex) {
		Square pivot = squares.get(pivotIndex);
		for (Square square : squares) {
			int oldX = square.x - pivot.x;
			int oldY = square.y - pivot.y;
		//	int newX = GameUtil.modulo(-oldY, numPlayers) + pivot.x;
			int newX = -oldY + pivot.x;
			int newY = oldX + pivot.y;
		//	square.x = GameUtil.modulo(newX, numPlayers);
			square.x = newX;
			square.y = newY;
		}
	}
	
	protected void rotateRightFixedSquareOrigin(int pivotIndex) {
		Square pivot = squares.get(pivotIndex);
		for (Square square : squares) {
			int oldX = square.x - pivot.x;
			int oldY = square.y - pivot.y;
		//	int newX = GameUtil.modulo(oldY, numPlayers) + pivot.x;
			int newX = oldY + pivot.x;
			int newY = -oldX + pivot.y;
		//	square.x = GameUtil.modulo(newX, numPlayers);
			square.x = newX;
			square.y = newY;
		}
	}
	
	protected void checkRep() {
		assert(squares != null);
		if (DEBUG > 0) {
			/*
			 * make sure we don't have null squares
			 */
			for (Square square : squares) {
				assert(square != null);
			}
			if (DEBUG > 1) {
				/*
				 * make sure the entire piece is one color
				 */
				Color color = squares.get(0).color;
				for (int i = 1; i < squares.size(); i++) {
					assert(squares.get(i).color.equals(color));
				}
			}
		}
	}
}
