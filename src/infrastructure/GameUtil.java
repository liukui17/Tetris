package infrastructure;

import java.awt.Color;

public class GameUtil {
	//public static final int NUM_PLAYERS = 2;
	//public static final int PLAYER_START_SECTION_WIDTH = 7;
	public static final int LONG_SIZE = 64;
	public static final int BOARD_WIDTH = LONG_SIZE / Encoder.BITS_PER_COLOR;
	public static final int BOARD_HEIGHT = 24;

	public static final Color[] OUTLINE_COLORS = {
		Color.BLACK,
		Color.PINK
	};
	
	public static final Color EMPTY = Color.WHITE;
	public static final Color GHOST = Color.LIGHT_GRAY;
	
	public static int computeSectionWidth(int numPlayers) {
		return BOARD_WIDTH / numPlayers;
	}
	
	/**
	 * Computes positive modulus, i.e. the remainder of doing dividend / divisor
	 */
	public static int modulo(int dividend, int divisor) {
		int res = dividend % divisor;
		if (res < 0) {
			res += divisor;
		}
		return res;
	}

	/**
	 * Returns a human-readable String representation of the specified Color.
	 * 
	 * @param color the Color to obtain a human-readable String representation for
	 * 
	 * @requires color != null
	 * 
	 * @return a human-readable String representation of the specified Color.
	 * Can return ??? if the specified color is not known to the game
	 */
	public static String colorToString(Color color) {
		if (color.equals(EMPTY)) {
			return "_";
		} else if (color.equals(Color.CYAN)) {
			return "C";
		} else if (color.equals(Color.BLUE)) {
			return "B";
		} else if (color.equals(Color.ORANGE)) {
			return "O";
		} else if (color.equals(Color.YELLOW)) {
			return "Y";
		} else if (color.equals(Color.GREEN)) {
			return "G";
		} else if (color.equals(Color.MAGENTA)) {
			return "M";
		} else if (color.equals(Color.RED)){
			return "R";
		} else {
			return "?";
		}
	}
}
