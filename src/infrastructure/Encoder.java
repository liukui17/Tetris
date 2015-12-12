package infrastructure;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Encoder {
	public static final int BITS_PER_COLOR = 3; // encode each color with 3 bits
	public static final int COLOR_MASK = (1 << BITS_PER_COLOR) - 1;  // 00000111
	public static final int BYTE = 8;
	public static final long BYTE_MASK = 255;  // 00..11111111
	
	public static final byte COMMAND_MASK = (1 << (BYTE / 2)) - 1; // 00001111
	
	public static final int PLAYER_BITS = 3;
	public static final byte PLAYER_MASK = ((1 << PLAYER_BITS) - 1) << (BYTE / 2); // 01110000
	
	public static final byte QUIT_MASK = (byte) (1 << (BYTE - 1)); // 1000000

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

		// empty row
		if (row == null) {
			for (int i = 0; i < GameUtil.BOARD_WIDTH; i++) {
				networkFormat += colorToInt(Color.GRAY);
				networkFormat <<= BITS_PER_COLOR;
			}
		} else {
			/*
			 * Left squares end up on the left bits of the long
			 */
			for (int i = 0; i < row.length - 1; i++) {
				networkFormat += colorToInt(row[i]);
				networkFormat <<= BITS_PER_COLOR;
			}
			networkFormat += colorToInt(row[row.length - 1]);
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
		/*
		 * Decode the long from right to left to allow use of a simple mask
		 */
		for (int i = row.length - 1; i >= 0; i--) {
			int nextColor = (int) (bits & COLOR_MASK);
			row[i] = GameUtil.PIECE_COLORS[nextColor];
			bits >>= BITS_PER_COLOR;
		}
	}

	/**
	 * Decodes the specified byte into a Board command and executes it.
	 * 
	 * @param bits the byte containing the command
	 * @param player an int denoting the player who issued the command
	 * @param gameState the GameStateManager the command will take action on
	 * 
	 * @requires bits must follow the established encoding && player must be
	 * either 0 or 1 && board != null
	 */
	public static void decodeCommand(byte bits, GameStateManager gameState) {
		// get command using from bottom four bits
		byte command = (byte) (bits & COMMAND_MASK);
		
		// get player number from next top three bits
		int player = (bits & PLAYER_MASK) >> (BYTE / 2);
				
		switch (command) {
			case 0: gameState.tryMoveLeft(player); break;
			case 1: gameState.tryMoveRight(player); break;
			case 2: gameState.tryRotateLeft(player); break;
			case 4: gameState.tryMoveDown(player); break;
			case 8: gameState.drop(player); break;
			default:	// just ignore anything else that is given so the user can
						// accidentally hit some other key without us having to throw
						// an illegal argument exception
		}
	}

	/**
	 * Encodes the specified player's key press identified by the specified int
	 * to a byte and returns it
	 * 
	 * @param key an int representing the key that the specified player pressed
	 * @param player a boolean identifying which player pressed the key (true if
	 * player 1 pressed, false if player 2 pressed)
	 * 
	 * @return an encoded byte of the specified player's key press
	 */
	public static byte encodeKeyPress(int key, int player) {
		byte encoding = 0;
		encoding += player << (BYTE / 2);
		switch (key) {
			case KeyEvent.VK_LEFT: return (byte) (encoding | 0);
			case KeyEvent.VK_RIGHT: return (byte) (encoding | 1);
			case KeyEvent.VK_UP: return (byte) (encoding | 2);
			case KeyEvent.VK_DOWN: return (byte) (encoding | 4);
			case KeyEvent.VK_SPACE: return (byte) (encoding | 8);
			default: return COMMAND_MASK; // use 00001111 as default for anything unrecognized
		}
	}
	
	/**
	 * Encodes the coordinates of the spaces of the piece described by the
	 * specified Set of BytePairs.
	 * 
	 * @param piece the Set of BytePairs that contain the coordinates of its spaces
	 * 
	 * @requires piece != null && piece does not contain null
	 * 
	 * @return an encoded long of the spaces
	 */
	public static long encodeSpacesOfPiece(Set<BytePair> piece) {
		/*
		 * This will only work because
		 *   - all pieces have 4 spaces
		 *   - each space is described by 2 indices
		 *   - each index can be encoded into 1 byte due to our board size
		 *   
		 *   4 spaces/piece * 2 indices/space * 1 byte/index = 8 bytes = 1 long
		 */
		
		long encoding = 0;
		
		Iterator<BytePair> itr = piece.iterator();
		
		BytePair s = itr.next();
		encoding |= GameUtil.modulo(s.getX(), GameUtil.BOARD_WIDTH);
		encoding <<= BYTE;  // shift it over a byte
		encoding |= (s.getY() & BYTE_MASK);
		
		for (int i = 0; i < piece.size() - 1; i++) {
			BytePair space = itr.next();
			
			encoding <<= BYTE;
			encoding |= GameUtil.modulo(space.getX(), GameUtil.BOARD_WIDTH);
			encoding <<= BYTE;  // shift it over a byte
			encoding |= (space.getY() & BYTE_MASK);
		}
		return encoding;
	}

	/**
	 * Decodes the specified encoding into a Set of BytePairs representing the
	 * spaces of a piece and returns it.
	 * 
	 * @param encoding the long that is to be decoded
	 * 
	 * @return a Set of BytePairs containing the decoded spaces
	 */
	public static Set<BytePair> decodeSpaces(long encoding) {
		Set<BytePair> spaces = new HashSet<BytePair>();

		for (int i = 0; i < 4; i++) {
			long y = encoding & BYTE_MASK;
			encoding >>= BYTE;
			long x = encoding & BYTE_MASK;
			encoding >>= BYTE;
			spaces.add(new BytePair((byte) x, (byte) y));
		}
		
		return spaces;
	}

}
