public class Piece {
	private Collection<Square> squares;

	public Piece (Collection<Square> squares) {
		this.squares = squares;
	}

	public Collection<Square> getSpaces() {
		return squares;
	}

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

	public void rotateLeft(Board board) {

	}

	public void rotateRight(Board board) {

	}
}
