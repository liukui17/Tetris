package pieces;

import java.util.Random;

import infrastructure.GameUtil;

public class PieceFactory {
	static final Random RANDOM = new Random();
	
	/**
	 * Generates a random new piece for the given player.
	 */
	public static Piece generateNewPiece(int player, int sectionWidth) {
		int piece = RANDOM.nextInt(7);
		switch (piece) {
			case 0:
				return new PieceI(-1, player * sectionWidth +
						sectionWidth / Piece.NUM_SQUARES_PER_PIECE);
			case 1:
				return new PieceJ(-2, player * sectionWidth +
						sectionWidth / Piece.NUM_SQUARES_PER_PIECE);
			case 2:
				return new PieceL(-2, player * sectionWidth +
						sectionWidth / Piece.NUM_SQUARES_PER_PIECE);
			case 3:
				return new PieceO(-2, player * sectionWidth +
						sectionWidth / Piece.NUM_SQUARES_PER_PIECE);
			case 4:
				return new PieceS(-2, player * sectionWidth +
						sectionWidth / Piece.NUM_SQUARES_PER_PIECE);
			case 5:
				return new PieceT(-2, player * sectionWidth +
						sectionWidth / Piece.NUM_SQUARES_PER_PIECE);
			case 6:
				return new PieceZ(-2, player * sectionWidth +
						sectionWidth / Piece.NUM_SQUARES_PER_PIECE);
			default:
				return null;
		}
	}
}
