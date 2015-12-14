package pieces;
import java.awt.Color;
import java.util.List;

public class PieceS extends Piece {
	private static final Color COLOR = Color.GREEN;
	
	public PieceS(List<Square> squares) {
		super(squares);
	}
	
	//        [ ][ ]
	//     [ ][x]			(x indicates fixed point in rotate)
	public PieceS(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow + 1, COLOR));
		squares.add(new Square(startCol + 1, startRow + 1, COLOR)); // pivot (index 1)
		squares.add(new Square(startCol + 1, startRow, COLOR));
		squares.add(new Square(startCol + 2, startRow, COLOR));
	}
	
	public char getCharacterRepresentation() {
		return 'S';
	}
	
	public void rotateLeft() {
		rotateLeftFixedSquareOrigin(1);
	}
	
	public void rotateRight() {
		rotateRightFixedSquareOrigin(1);
	}
}
