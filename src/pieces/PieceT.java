package pieces;
import java.awt.Color;
import java.util.List;

public class PieceT extends Piece {
	private static final Color COLOR = Color.MAGENTA;
	
	public PieceT(List<Square> squares) {
		super(squares);
	}
	
	//        [ ]
	//     [ ][x][ ]	(x indicates fixed point in rotate)
	public PieceT(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow + 1, COLOR));
		squares.add(new Square(startCol + 1, startRow + 1, COLOR)); // pivot (index 1)
		squares.add(new Square(startCol + 1, startRow, COLOR));
		squares.add(new Square(startCol + 2, startRow + 1, COLOR));
	}
	
	public char getCharacterRepresentation() {
		return 'T';
	}
	
	public void rotateLeft() {
		rotateLeftFixedSquareOrigin(1);
	}
	
	public void rotateRight() {
		rotateRightFixedSquareOrigin(1);
	}
}
