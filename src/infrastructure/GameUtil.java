package infrastructure;

import java.awt.Color;

public class GameUtil {
	public static final int LONG_SIZE = 64;
	public static final int BOARD_WIDTH = LONG_SIZE / Encoder.BITS_PER_COLOR;
	public static final int BOARD_HEIGHT = 24;

	public static final Color[] OUTLINE_COLORS = {
		Color.BLACK,
		Color.PINK
	};
	
	public static final Color EMPTY = Color.WHITE;
	public static final Color GHOST = Color.LIGHT_GRAY;
	
	public static final boolean[][] I_SHAPE = {{true, true, true, true}, 
												{false, false, false, false}, 
												{false, false, false, false}, 
												{false, false, false, false}};
	
	public static final boolean[][] J_SHAPE = {{true, false, false, false}, 
												{true, true, true, false}, 
												{false, false, false, false}, 
												{false, false, false, false}};
	
	public static final boolean[][] L_SHAPE = {{false, false, true, false}, 
												{true, true, true, false}, 
												{false, false, false, false}, 
												{false, false, false, false}};
	
	public static final boolean[][] O_SHAPE = {{false, false, false, false}, 
												{false, true, true, false}, 
												{false, true, true, false}, 
												{false, false, false, false}};
	
	public static final boolean[][] S_SHAPE = {{false, true, true, false}, 
												{true, true, false, false}, 
												{false, false, false, false}, 
												{false, false, false, false}};
	
	public static final boolean[][] Z_SHAPE = {{true, true, false, false}, 
												{false, true, true, false}, 
												{false, false, false, false}, 
												{false, false, false, false}};
	
	public static final boolean[][] T_SHAPE = {{false, true, false, false}, 
												{true, true, true, false}, 
												{false, false, false, false}, 
												{false, false, false, false}};

	public static final Color I_COLOR = Color.CYAN;
	public static final Color J_COLOR = Color.BLUE;
	public static final Color L_COLOR = Color.ORANGE;
	public static final Color O_COLOR = Color.YELLOW;
	public static final Color S_COLOR = Color.GREEN;
	public static final Color T_COLOR = Color.MAGENTA;
	public static final Color Z_COLOR = Color.RED;
	
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
