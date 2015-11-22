package pieces;
import java.awt.Color;

public class Square {
/*	private int x;
	private int y;
	private Color color; */
	
	// probably easier to just leave these without access modifiers
	// and just allow objects in this package to modify them directly
	// and see them as well
	int x;
	int y;
	// not going to ever change the color of a square
	final Color color;

	public Square(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

/*	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	} */
}
