package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceL extends Piece {
	
	public PieceL(List<Square> squares) {
		super(squares);
	}
	
	//         []
	//     [][][]
	public PieceL(int startRow, int startCol) {
		super();
		squares.add(new Square(startRow + 1, startCol, GameUtil.PIECE_COLORS[3]));
		squares.add(new Square(startRow + 1, startCol + 1, GameUtil.PIECE_COLORS[3]));
		squares.add(new Square(startRow + 1, startCol + 2, GameUtil.PIECE_COLORS[3]));
		squares.add(new Square(startRow, startCol + 2, GameUtil.PIECE_COLORS[3]));
	}
	
	public void rotateLeft() {
		
	}
	
	public void rotateRight() {
		
	}
}
