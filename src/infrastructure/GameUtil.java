package infrastructure;

import java.awt.Color;
import java.util.Random;

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
	public static Random rng = new Random();
}
