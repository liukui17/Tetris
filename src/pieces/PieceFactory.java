package pieces;

import java.util.Random;

import infrastructure.GameUtil;

public class PieceFactory {
	static final Random RANDOM = new Random();
	
	/**
	 * Generates a random new piece for the given player.
	 */
	public static Piece generateNewPiece(int player) {
		int piece = RANDOM.nextInt(7);
		switch (piece) {
			case 0:
				return new PieceI(0, player * GameUtil.PLAYER_START_SECTION_WIDTH +
						GameUtil.PLAYER_START_SECTION_WIDTH / Piece.NUM_SQUARES_PER_PIECE);
			case 1:
				return new PieceJ(0, player * GameUtil.PLAYER_START_SECTION_WIDTH +
						GameUtil.PLAYER_START_SECTION_WIDTH / Piece.NUM_SQUARES_PER_PIECE);
			case 2:
				return new PieceL(0, player * GameUtil.PLAYER_START_SECTION_WIDTH +
						GameUtil.PLAYER_START_SECTION_WIDTH / Piece.NUM_SQUARES_PER_PIECE);
			case 3:
				return new PieceO(0, player * GameUtil.PLAYER_START_SECTION_WIDTH +
						GameUtil.PLAYER_START_SECTION_WIDTH / Piece.NUM_SQUARES_PER_PIECE);
			case 4:
				return new PieceS(0, player * GameUtil.PLAYER_START_SECTION_WIDTH +
						GameUtil.PLAYER_START_SECTION_WIDTH / Piece.NUM_SQUARES_PER_PIECE);
			case 5:
				return new PieceT(0, player * GameUtil.PLAYER_START_SECTION_WIDTH +
						GameUtil.PLAYER_START_SECTION_WIDTH / Piece.NUM_SQUARES_PER_PIECE);
			case 6:
				return new PieceZ(0, player * GameUtil.PLAYER_START_SECTION_WIDTH +
						GameUtil.PLAYER_START_SECTION_WIDTH / Piece.NUM_SQUARES_PER_PIECE);
			default:
				return null;
		}
	}
}
