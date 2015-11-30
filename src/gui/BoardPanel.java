import java.awt.*;
import javax.swing.*;

public class BoardPanel extends JPanel {
	private Color[][] grid;
	private int width;
	private int height;
	private int cellSize;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);

		width = getWidth();
		height = getHeight();
		// 10 cells wide
		cellSize = width / 10;

		if (grid != null) {
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					Color c = grid[i][j];
					if (c != null) {
						g.setColor(c);
						g.fill3DRect(i * cellSize, j * cellSize, cellSize, cellSize, true);
					}
				}
			}
		}
	}

	public void updateGrid(Color[][] grid) {
		this.grid = grid;
	}
}
