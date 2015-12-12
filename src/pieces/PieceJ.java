package pieces;
import java.awt.Color;
import java.util.List;

public class PieceJ extends Piece {
	private static final Color COLOR = Color.BLUE;
	
	public PieceJ(List<Square> squares) {
		super(squares);
	}
	
	//       [ ]
	//       [ ][x][ ]	(x indicates fixed point in rotate)
	public PieceJ(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow, COLOR));
		squares.add(new Square(startCol, startRow + 1, COLOR));
		squares.add(new Square(startCol + 1, startRow + 1, COLOR)); // pivot (index 2)
		squares.add(new Square(startCol + 2, startRow + 1, COLOR));
	}
	
	public void rotateLeft() {
		rotateLeftFixedSquareOrigin(2);
	}
	
	public void rotateRight() {
		rotateRightFixedSquareOrigin(2);
	}
}
