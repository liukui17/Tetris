package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

import infrastructure.BytePair;
import infrastructure.GameUtil;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Color[][] grid;
	private Set<BytePair> p1Spaces;
	private Set<BytePair> p2Spaces;

	/*
	 * Length of a side of a square cell
	 */
	private static final int CELL_LENGTH = 30;

	public BoardPanel() {
		grid = new Color[GameUtil.BOARD_HEIGHT][GameUtil.BOARD_WIDTH];
		p1Spaces = new TreeSet<BytePair>();
		p2Spaces = new TreeSet<BytePair>();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		// draw the spaces themselves
		if (grid != null) {
			for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
				for (int j = 0; j < GameUtil.BOARD_WIDTH; j++) {
					Color c = grid[i][j];
					g.setColor(c);
					g.fill3DRect(j * CELL_LENGTH, i * CELL_LENGTH, CELL_LENGTH, CELL_LENGTH, true);
				}
			}
		}

		// draw the outlines of the falling pieces
		Set<Line> p1Sides = findSides(p1Spaces); 
		Set<Line> p2Sides = findSides(p2Spaces);

		drawPieceOutLine(g, Color.BLACK, p1Sides);
		drawPieceOutLine(g, Color.PINK, p2Sides);
	}

	public void updateGrid(Color[][] grid, Set<BytePair> p1Spaces, Set<BytePair> p2Spaces) {
		this.grid = grid;
		this.p1Spaces = p1Spaces;
		this.p2Spaces = p2Spaces;
	}

	private void drawPieceOutLine(Graphics g, Color color, Set<Line> sides) {
		g.setColor(color);
		
		for (Line l : sides) {
			Point start = l.getStart();
			Point end = l.getEnd();
			
			int startX = (int) start.getX();
			int startY = (int) start.getY();
			int endX = (int) end.getX();
			int endY = (int) end.getY();
			
			g.drawLine(startX, startY, endX, endY);
		}
	}

	private static Set<Line> findSides(Set<BytePair> spaces) {
		Map<Line, Integer> sideCounts = new HashMap<Line, Integer>();

		for (BytePair p : spaces) {
			int x = GameUtil.modulo(p.getX(), GameUtil.BOARD_WIDTH) * CELL_LENGTH;
			int y = p.getY() * CELL_LENGTH;
			
			Point upperLeft = new Point(x, y);
			Point upperRight = new Point(x + CELL_LENGTH, y);
			Point lowerLeft = new Point(x, y + CELL_LENGTH);
			Point lowerRight = new Point(x + CELL_LENGTH, y + CELL_LENGTH);
			
			Set<Line> pieceSides = new HashSet<Line>();
			pieceSides.add(new Line(upperLeft, upperRight));
			pieceSides.add(new Line(lowerLeft, lowerRight));
			pieceSides.add(new Line(upperLeft, lowerLeft));
			pieceSides.add(new Line(upperRight, lowerRight));
			
			assert pieceSides.size() == 4;
			
			for (Line l : pieceSides) {
				if (sideCounts.containsKey(l)) {
					sideCounts.put(l, sideCounts.get(l) + 1);
				} else {
					sideCounts.put(l, 1);
				}
			}
		}
		
		Set<Line> sides = new HashSet<Line>();
		for (Line l : sideCounts.keySet()) {
			if (sideCounts.get(l) == 1) {
				sides.add(l);
			}
		}
		
		return sides;
	}
}
