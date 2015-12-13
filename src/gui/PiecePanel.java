package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PiecePanel extends JPanel {
	private static final int CELL_LENGTH = 20;

	public PiecePanel() {
		setPreferredSize(new Dimension(4 * CELL_LENGTH, 4 * CELL_LENGTH));
		setMaximumSize(new Dimension(4 * CELL_LENGTH, 4 * CELL_LENGTH));
		setBackground(Color.WHITE);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				g.setColor(Color.WHITE);
				g.fill3DRect(j * CELL_LENGTH, i * CELL_LENGTH, CELL_LENGTH, CELL_LENGTH, true);
			}
		}
	}
}
