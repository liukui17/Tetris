package pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import infrastructure.GameUtil;

public class PieceO extends Piece {
	
	public PieceO(List<Square> squares) {
		super(squares);
	}
	
	//     [][]
	//     [][]
	// (this tetromino has no rotations since symmetric, i.e. everything
	// is fixed under rotations)
	public PieceO(int startRow, int startCol) {
		super();
		squares.add(new Square(startCol, startRow, GameUtil.PIECE_COLORS[4]));
		squares.add(new Square(startCol, startRow + 1, GameUtil.PIECE_COLORS[4]));
		squares.add(new Square(startCol + 1, startRow, GameUtil.PIECE_COLORS[4]));
		squares.add(new Square(startCol + 1, startRow + 1, GameUtil.PIECE_COLORS[4]));
	}
	
	public void rotateLeft() {}
	
	public void rotateRight() {}
}
