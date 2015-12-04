package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import infrastructure.GameUtil;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Color[][] grid;
	private int cellWidth;
	private int cellHeight;

	public BoardPanel() {
		grid = new Color[GameUtil.BOARD_HEIGHT][GameUtil.BOARD_WIDTH];
		cellWidth = 30;
		cellHeight = 30;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		// Can either recompute cell dimensions everytime try to repaint
		// So that it scales or hardcode to avoid computation
//		cellWidth = getWidth() / GameUtil.BOARD_WIDTH;
//		cellHeight = getHeight() / GameUtil.BOARD_HEIGHT;

		if (grid != null) {
			for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
				for (int j = 0; j < GameUtil.BOARD_WIDTH; j++) {
					Color c = grid[i][j];
					if (c != null) {
						g.setColor(c);
						g.fill3DRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight, true);
					} else {
						// fill with default color
						g.setColor(Color.gray);
						g.fill3DRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight, true);
					}
				}
			}
		}
	}

	public void updateGrid(Color[][] grid) {
		this.grid = grid;
	}
}
