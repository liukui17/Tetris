package pieces;
import java.awt.Color;
import java.util.List;

public class PieceI extends Piece {
	private static final Color COLOR = Color.CYAN;
	
	public PieceI(List<Square> squares) {
		super(squares);
	}
	
	//     [][][][]
	public PieceI(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow, COLOR));
		squares.add(new Square(startCol + 1, startRow, COLOR));
		squares.add(new Square(startCol + 2, startRow, COLOR));
		squares.add(new Square(startCol + 3, startRow, COLOR));
	}
	
	public void rotateLeft() {
		// temporary
		rotateLeftFixedSquareOrigin(2);
	}
	
	public void rotateRight() {
		// temporary
		rotateRightFixedSquareOrigin(2);
	}
}
