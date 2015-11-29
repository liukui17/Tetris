package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceZ extends Piece {
	
	public PieceZ(List<Square> squares) {
		super(squares);
	}
	
	//     [][]
	//       [][]
	public PieceZ(int startRow, int startCol) {
		super();
		squares.add(new Square(startRow, startCol, GameUtil.PIECE_COLORS[7]));
		squares.add(new Square(startRow, startCol + 1, GameUtil.PIECE_COLORS[7]));
		squares.add(new Square(startRow + 1, startCol + 1, GameUtil.PIECE_COLORS[7]));
		squares.add(new Square(startRow + 1, startCol + 2, GameUtil.PIECE_COLORS[7]));
	}
	
	public void rotateLeft() {
		
	}
	
	public void rotateRight() {
		
	}
}
