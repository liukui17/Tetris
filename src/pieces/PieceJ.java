package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceJ extends Piece {
	
	public PieceJ(List<Square> squares) {
		super(squares);
	}
	
	//       [ ]
	//       [ ][x][ ]	(x indicates fixed point in rotate)
	public PieceJ(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow, GameUtil.PIECE_COLORS[2]));
		squares.add(new Square(startCol, startRow + 1, GameUtil.PIECE_COLORS[2]));
		squares.add(new Square(startCol + 1, startRow + 1, GameUtil.PIECE_COLORS[2])); // pivot (index 2)
		squares.add(new Square(startCol + 2, startRow + 1, GameUtil.PIECE_COLORS[2]));
	}
	
	public void rotateLeft() {
		rotateLeftFixedSquareOrigin(2);
	}
	
	public void rotateRight() {
		rotateRightFixedSquareOrigin(2);
	}
}
