package pieces;
//import java.util.Collection;
import java.util.List;

import infrastructure.GameUtil;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Piece {
	static final int DEBUG = 0;
	public static final int NUM_SQUARES_PER_PIECE = 4;

	// private Collection<Square> squares;
	
	// Collection is just a collection of static methods that
	// operate on or return collections; best use something like
	// arraylist for constant time access and amortized O(1) add
	// since it resizes; leave without access modifier so subclasses
	// have access
	List<Square> squares;
	
	public Piece(List<Square> squares) {
		this.squares = new ArrayList<Square>(squares);
		checkRep();
	}
	
	// essentially a copy constructor
	public Piece(Piece other) {
		this.squares = new ArrayList<Square>();
		for (Square square : other.squares) {
			squares.add(new Square(square));
		}
	}
	
	public Piece() {
		squares = new ArrayList<Square>();
	}
	
	public List<Square> getSpaces() {
		return squares;
	}

/*	public Piece(Collection<Square> squares) {
		assert(squares.size() == NUM_SQUARES_PER_PIECE);
		this.squares = squares;
	}

	public Collection<Square> getSpaces() {
		return squares;
	} */
	
	public boolean containsSquare(Square other) {
		for (Square square : squares) {
			if (other.x == square.x && other.y == square.y) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasHitBottom() {
		for (Square square : squares) {
			if (square.y >= GameUtil.BOARD_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	protected void moveLeft() {
		for (Square square : squares) {
			square.x = (square.x - 1) % GameUtil.BOARD_WIDTH;
		}
	}

	protected void moveRight() {
		for (Square square : squares) {
			square.x = (square.x + 1) % GameUtil.BOARD_WIDTH;
		}
	}

	protected void moveDown() {
		for (Square square : squares) {
			square.y = square.y + 1;
		}
	}
	
	protected void moveUp() {
		for (Square square : squares) {
			square.y = square.y - 1;
		}
	}

	protected abstract void rotateLeft();

	protected abstract void rotateRight();
	
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
