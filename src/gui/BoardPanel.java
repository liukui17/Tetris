package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import infrastructure.GameUtil;

public class BoardPanel extends JPanel {
	private Color[][] grid;
	private int cellWidth;
	private int cellHeight;

	public BoardPanel() {
		cellWidth = getWidth() / GameUtil.BOARD_WIDTH;
		cellHeight = getHeight() / GameUtil.BOARD_HEIGHT;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);

		if (grid != null) {
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[i].length; j++) {
					Color c = grid[i][j];
					if (c != null) {
						g.setColor(c);
						g.fill3DRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight, true);
					}
				}
			}
		}
	}

	public void updateGrid(Color[][] grid) {
		this.grid = grid;
	}
}
