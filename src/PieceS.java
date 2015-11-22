import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PieceS extends Piece {
	
	public PieceS(List<Square> squares) {
		super(squares);
	}
	
	//       [][]
	//     [][]
	public PieceS(int startRow, int startCol, Color color) {
		super(startRow, startCol, color);
		squares = new ArrayList<Square>();
		squares.add(new Square(startRow + 1, startCol, color));
		squares.add(new Square(startRow + 1, startCol + 1, color));
		squares.add(new Square(startRow, startCol + 1, color));
		squares.add(new Square(startRow, startCol + 2, color));
	}
	
	public void rotateLeft(Board board) {
		
	}
	
	public void rotateRight(Board board) {
		
	}
}
