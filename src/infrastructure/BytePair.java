package infrastructure;

/**
 * An ordered pairing of two bytes. Fundamentally a Point, except that it stores
 * bytes for its x and y values instead of ints.
 */
public class BytePair {
	private byte x;
	private byte y;
	
	public BytePair(byte x, byte y) {
		this.x= x;
		this.y = y;
	}
	
	public byte getX() {
		return x;
	}
	
	public byte getY() {
		return y;
	}
	
	@Override
	public int hashCode() {
		return 31 * x + 19 * y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BytePair)) {
			return false;
		}
		BytePair b = (BytePair) o;
		return x == b.x && y == b.y;
	}
	
	/**
	 * Returns a String representation of this BytePair. The format is the same
	 * as how a point is expressed in mathematics: (x, y)
	 * 
	 * @return a String representation of this BytePair
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
