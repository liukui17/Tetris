package pieces;
import java.awt.Color;

import infrastructure.BytePair;
import infrastructure.GameUtil;

public class Square {
	
	// probably easier to just leave these without access modifiers
	// and just allow objects in this package to modify them directly
	// and see them as well (also allows us to not have to push and pop
	// a new stack frame just to get their values)
	int x;
	int y;
	// not going to ever change the color of a square
	final Color color;

	public Square(int y, int x, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	// essentially a copy constructor
	public Square(Square other) {
		this.x = other.x;
		this.y = other.y;
		this.color = other.color;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
	}
	
	/**
	 * Returns a BytePair containing this Square's coordinates.
	 * 
	 * @return a BytePair containing this Square's coordinates
	 */
	public BytePair getBytePair() {
		return new BytePair((byte) getX(), (byte) getY());
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + GameUtil.colorToString(color) + ")";
	}
}
