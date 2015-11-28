package infrastructure;
import java.awt.Color;

public class Encoder {
	public static final int BITS_PER_COLOR = 3; // encode each color with 3 bits
	public static final int MASK = (1 << BITS_PER_COLOR) - 1; // 111
	
	public static int colorToInt(Color color) {
		for (int i = 0; i < GameUtil.PIECE_COLORS.length; i++) {
			if (GameUtil.PIECE_COLORS[i].equals(color)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Converts a row of colors to the bit encoding that will be sent
	 * over the network.
	 * 
	 * @param row
	 * 						the array of colors to encode
	 * @return a long containing the bits encoding the colors that can
	 * 				 sent over the network
	 */
	public static long gridRowToNetworkMessage(Color[] row) {
		long networkFormat = 0;
		for (int i = 0; i < row.length; i++) {
			networkFormat += colorToInt(row[i]);
			networkFormat <<= BITS_PER_COLOR;
		}
		return networkFormat;
	}
	
	/**
	 * Modifies a row of colors to have the colors specified by the bits
	 * in the given long (sent over the network)
	 * 
	 * @param bits
	 * 							the bit encoding of the colors for each square
	 * 							in a given row (received through network)
	 * @param row 
	 * 						the row of colors to update in the board
	 * @requires row != null && row.length == BOARD_WIDTH
	 * @effects updates the array of colors 'row'
	 * @modifies the array of colors 'row'
	 */
	public static void networkMessageToGridRow(long bits, Color[] row) {
		for (int i = 0; i < row.length; i++) {
			int nextColor = (int) (bits & MASK);
			row[i] = GameUtil.PIECE_COLORS[nextColor];
			bits >>= BITS_PER_COLOR;
		}
	}

	/**
	 * Decodes the specified byte into a String and returns it.
	 * 
	 * @param bits the byte containing the command
	 * 
	 * @return a String of the human readable command encoded in the byte
	 */
	public static String decodeCommand(byte bits) {
		/*
		 * 01111111 = 127
		 * 
		 * Ignore the MSB because it indicates the player who made
		 * the command, which does not provide any information about
		 * the actual action to be taken.
		 */
		byte command = (byte) (bits & 127);
		
		switch (command) {
			case 0: return "left";
			case 1: return "right";
			case 2: return "rotate";
			case 4: return "drop";
			default: return "???";
		}
	}
}
