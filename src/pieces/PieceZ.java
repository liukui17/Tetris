package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceZ extends Piece {
	
	public PieceZ(List<Square> squares) {
		super(squares);
	}
	
	//     [ ][ ]
	//        [x][ ]	(x indicates fixed point in rotate)
	public PieceZ(int startRow, int startCol) {
		super();
		squares.add(new Square(startRow, startCol, GameUtil.PIECE_COLORS[7]));
		squares.add(new Square(startRow, startCol + 1, GameUtil.PIECE_COLORS[7]));
		squares.add(new Square(startRow + 1, startCol + 1, GameUtil.PIECE_COLORS[7])); // pivot (index 2)
		squares.add(new Square(startRow + 1, startCol + 2, GameUtil.PIECE_COLORS[7]));
	}
	
	public void rotateLeft() {
		rotateLeftFixedSquareOrigin(2);
	}
	
	public void rotateRight() {
		rotateRightFixedSquareOrigin(2);
	}
}
