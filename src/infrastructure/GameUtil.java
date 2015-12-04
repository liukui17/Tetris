package infrastructure;

import java.awt.Color;

public class GameUtil {
	public static final int NUM_PLAYERS = 2;
	public static final int PLAYER_START_SECTION_WIDTH = 8;
	public static final int BOARD_WIDTH = NUM_PLAYERS * PLAYER_START_SECTION_WIDTH;
	public static final int BOARD_HEIGHT = 24;
	public static final Color[] PIECE_COLORS = {
		Color.GRAY, // empty
		Color.CYAN, // I
		Color.BLUE, // J
		Color.ORANGE, // L
		Color.YELLOW, // O
		Color.GREEN, // S (there is no lime so set this for now)
		Color.MAGENTA, // T (there is no purple so set this for now)
		Color.RED // Z
	};
	
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
		if (color.equals(Color.GRAY)) {
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
