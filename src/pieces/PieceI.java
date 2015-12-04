package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceI extends Piece {
	
	public PieceI(List<Square> squares) {
		super(squares);
	}
	
	//     [][][][]
	public PieceI(int startRow, int startCol) {
		super();
		squares.add(new Square(startRow, startCol, GameUtil.PIECE_COLORS[1]));
		squares.add(new Square(startRow, startCol + 1, GameUtil.PIECE_COLORS[1]));
		squares.add(new Square(startRow, startCol + 2, GameUtil.PIECE_COLORS[1]));
		squares.add(new Square(startRow, startCol + 3, GameUtil.PIECE_COLORS[1]));
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
