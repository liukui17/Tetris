package pieces;
import java.awt.Color;
import java.util.List;

public class PieceO extends Piece {
	private static final Color COLOR = Color.YELLOW;
	
	public PieceO(List<Square> squares) {
		super(squares);
	}
	
	//     [][]
	//     [][]
	// (this tetromino has no rotations since symmetric, i.e. everything
	// is fixed under rotations)
	public PieceO(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow, COLOR));
		squares.add(new Square(startCol, startRow + 1, COLOR));
		squares.add(new Square(startCol + 1, startRow, COLOR));
		squares.add(new Square(startCol + 1, startRow + 1, COLOR));
	}
	
	public byte getByte() {
		return 4;
	}
	
	public void rotateLeft() {}
	
	public void rotateRight() {}
}
