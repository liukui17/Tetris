package infrastructure;

/**
 * An ordered pairing of two bytes.
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
}
