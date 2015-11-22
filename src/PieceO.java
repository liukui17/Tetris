import java.awt.Color;
import java.util.ArrayList;

public class PieceO extends Piece {
	
	public PieceO(Square[] squares) {
		super(squares);
	}
	
	public PieceO(int i, int j, Color color) {
		super(i, j, color);
		squares = new ArrayList<Square>();
		squares.add(new Square(i, j, color));
		squares.add(new Square(i + 1, j, color));
		squares.add(new Square(i, j + 1, color));
		squares.add(new Square(i + 1, j + 1, color));
	}
	
	public void rotateLeft(Board board) {}
	
	public void rotateRight(Board board) {}
}
