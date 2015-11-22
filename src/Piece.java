//import java.util.Collection;
import java.util.List;
import java.awt.Color;
import java.util.ArrayList;

public abstract class Piece {
	public static final int NUM_SQUARES_PER_PIECE = 4;

	// private Collection<Square> squares;
	
	// Collection is just a collection of static methods that
	// operate on or return collections; best use something like
	// arraylist for constant time access and amortized O(1) add
	// since it resizes; leave without access modifier so subclasses
	// have access
	List<Square> squares;
	
	public Piece(List<Square> squares) {
		this.squares = squares;
	}
	
	public Piece(int startRow, int startCol, Color color) {}
	
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

	public void moveLeft(Board board) {

	}

	public void moveRight(Board board) {

	}

	public void moveDown(Board board) {

	}

	// Drops the piece down until it cannot go down.
	public void drop(Board board) {

	}

	// Move piece down over time
	public void fall(Board board) {

	}

	public abstract void rotateLeft(Board board);

	public abstract void rotateRight(Board board);
}
