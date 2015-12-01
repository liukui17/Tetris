package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	private Color[][] grid;
	private int cellWidth;
	private int cellHeight;

	public 

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);

		int cellWidth = getWidth() / GameUtil.BOARD_WIDTH;
		int cellHeight = getHeight() / GameUtil.BOARD_HEIGHT;

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
