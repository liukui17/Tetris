package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceL extends Piece {
	
	public PieceL(List<Square> squares) {
		super(squares);
	}
	
	//           [ ]
	//     [ ][x][ ]	(x indicates fixed point in rotate)
	public PieceL(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow + 1, GameUtil.PIECE_COLORS[3]));
		squares.add(new Square(startCol + 1, startRow + 1, GameUtil.PIECE_COLORS[3])); // pivot (index 1)
		squares.add(new Square(startCol + 2, startRow + 1, GameUtil.PIECE_COLORS[3]));
		squares.add(new Square(startCol + 2, startRow, GameUtil.PIECE_COLORS[3]));
	}
	
	public void rotateLeft() {
		rotateLeftFixedSquareOrigin(1);
	}
	
	public void rotateRight() {
		rotateRightFixedSquareOrigin(1);
	}
}
