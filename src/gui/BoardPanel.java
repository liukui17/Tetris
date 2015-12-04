package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import infrastructure.GameUtil;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Color[][] grid;
	
	/*
	 * Length of a side of a square cell
	 */
	private int cellLength;

	public BoardPanel() {
		grid = new Color[GameUtil.BOARD_HEIGHT][GameUtil.BOARD_WIDTH];
		cellLength = 30;
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
					g.setColor(c);
					g.fill3DRect(j * cellLength, i * cellLength, cellLength, cellLength, true);
				}
			}
		}
	}

	public void updateGrid(Color[][] grid) {
		this.grid = grid;
	}
}
