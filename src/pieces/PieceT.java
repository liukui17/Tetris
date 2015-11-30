package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceT extends Piece {
	
	public PieceT(List<Square> squares) {
		super(squares);
	}
	
	//       []
	//     [][][]
	public PieceT(int startRow, int startCol) {
		super();
		squares.add(new Square(startRow + 1, startCol, GameUtil.PIECE_COLORS[6]));
		squares.add(new Square(startRow + 1, startCol + 1, GameUtil.PIECE_COLORS[6]));
		squares.add(new Square(startRow, startCol + 1, GameUtil.PIECE_COLORS[6]));
		squares.add(new Square(startRow + 1, startCol + 2, GameUtil.PIECE_COLORS[6]));
	}
	
	public void rotateLeft() {
		
	}
	
	public void rotateRight() {
		
	}
}
