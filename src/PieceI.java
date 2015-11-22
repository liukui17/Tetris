import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PieceI extends Piece {
	
	public PieceI(List<Square> squares) {
		super(squares);
	}
	
	//     [][][][]
	public PieceI(int startRow, int startCol, Color color) {
		super(startRow, startCol, color);
		squares = new ArrayList<Square>();
		squares.add(new Square(startRow, startCol, color));
		squares.add(new Square(startRow, startCol + 1, color));
		squares.add(new Square(startRow, startCol + 1, color));
		squares.add(new Square(startRow, startCol + 1, color));
	}
	
	public void rotateLeft(Board board) {
		
	}
	
	public void rotateRight(Board board) {
		
	}
}
