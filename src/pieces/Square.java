package pieces;
import java.awt.Color;

public class Square {
	
	// probably easier to just leave these without access modifiers
	// and just allow objects in this package to modify them directly
	// and see them as well (also allows us to not have to push and pop
	// a new stack frame just to get their values)
	int x;
	int y;
	// not going to ever change the color of a square
	final Color color;

	public Square(int x, int y, Color color) {
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
}
