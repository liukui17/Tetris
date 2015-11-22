package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PieceJ extends Piece {
	
	public PieceJ(List<Square> squares) {
		super(squares);
	}
	
	//       []
	//       [][][]
	public PieceJ(int startRow, int startCol, Color color) {
		squares.add(new Square(startRow, startCol, color));
		squares.add(new Square(startRow + 1, startCol, color));
		squares.add(new Square(startRow + 1, startCol + 1, color));
		squares.add(new Square(startRow + 1, startCol + 2, color));
	}
	
	public void rotateLeft(Board board) {
		
	}
	
	public void rotateRight(Board board) {
		
	}
}
