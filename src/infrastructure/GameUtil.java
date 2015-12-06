package infrastructure;

import java.awt.Color;

public class GameUtil {
	public static final int NUM_PLAYERS = 2;
	public static final int PLAYER_START_SECTION_WIDTH = 8;
	public static final int BOARD_WIDTH = NUM_PLAYERS * PLAYER_START_SECTION_WIDTH;
	public static final int BOARD_HEIGHT = 24;
	public static final Color[] PIECE_COLORS = {
		Color.WHITE, // empty
		Color.CYAN, // I
		Color.BLUE, // J
		Color.ORANGE, // L
		Color.YELLOW, // O
		Color.GREEN, // S (there is no lime so set this for now)
		Color.MAGENTA, // T (there is no purple so set this for now)
		Color.RED // Z
	};
	public static final Color GHOST = Color.LIGHT_GRAY;
	
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
		if (color.equals(PIECE_COLORS[0])) {
			return "_";
		} else if (color.equals(PIECE_COLORS[1])) {
			return "C";
		} else if (color.equals(PIECE_COLORS[2])) {
			return "B";
		} else if (color.equals(PIECE_COLORS[3])) {
			return "O";
		} else if (color.equals(PIECE_COLORS[4])) {
			return "Y";
		} else if (color.equals(PIECE_COLORS[5])) {
			return "G";
		} else if (color.equals(PIECE_COLORS[6])) {
			return "M";
		} else if (color.equals(PIECE_COLORS[7])){
			return "R";
		} else {
			return "?";
		}
	}
}
