package pieces;
import java.awt.Color;
import java.util.List;

public class PieceZ extends Piece {
	private static final Color COLOR = Color.RED;
	
	public PieceZ(List<Square> squares) {
		super(squares);
	}
	
	//     [ ][ ]
	//        [x][ ]	(x indicates fixed point in rotate)
	public PieceZ(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow, COLOR));
		squares.add(new Square(startCol + 1, startRow, COLOR));
		squares.add(new Square(startCol + 1, startRow + 1,COLOR)); // pivot (index 2)
		squares.add(new Square(startCol + 2, startRow + 1, COLOR));
	}
	
	public byte getByte() {
		return 7;
	}
	
	public void rotateLeft() {
		rotateLeftFixedSquareOrigin(2);
	}
	
	public void rotateRight() {
		rotateRightFixedSquareOrigin(2);
	}
}
