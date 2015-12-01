package infrastructure;
import java.awt.Color;

import pieces.Board;

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
	 * Decodes the specified byte into a Board command and executes it.
	 * 
	 * @param bits the byte containing the command
	 * @param player an int denoting the player who issued the command
	 * @param board the board the command will take action on
	 * 
	 * @requires bits must follow the established encoding && player must be
	 * either 0 or 1 && board != null
	 */
	public static void decodeCommand(byte bits, int player, Board board) {
		/*
		 * 01111111 = 127
		 * 
		 * Ignore the MSB because it indicates the player who made
		 * the command, which does not provide any information about
		 * the actual action to be taken.
		 */
		byte command = (byte) (bits & 127);
		
		switch (command) {
			case 0: board.tryMoveLeft(player); break;
			case 1: board.tryMoveRight(player); break;
			case 2: board.tryRotateLeft(player); break;
			case 4: board.tryRotateRight(player); break;
			case 8: board.drop(player); break;
			default: throw new IllegalArgumentException();
		}
	}
}
