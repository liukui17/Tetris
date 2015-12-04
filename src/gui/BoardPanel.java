package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Comparator;
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
		TreeSet<Point> p1Corners = findCorners(p1Spaces); 
		TreeSet<Point> p2Corners = findCorners(p2Spaces);

		drawPieceOutLine(g, p1Corners);
		drawPieceOutLine(g, p2Corners);
	}

	public void updateGrid(Color[][] grid, Set<BytePair> p1Spaces, Set<BytePair> p2Spaces) {
		this.grid = grid;
		this.p1Spaces = p1Spaces;
		this.p2Spaces = p2Spaces;
	}

	private void drawPieceOutLine(Graphics g, TreeSet<Point> corners) {
		/*
		 * Has to be TreeSet to ensure the arrays are correctly aligned with each
		 * other since it's sorted and thus maintains a consistent ordering
		 */
		int nPoints = corners.size();
		int[] xCoordinates = new int[nPoints];
		int[] yCoordinates = new int[nPoints];

		int i = 0;
		for (Point c : corners) {
			xCoordinates[i] = (int) c.getX();
			yCoordinates[i] = (int) c.getY();
			i++;
		}

		g.setColor(Color.PINK);
		g.drawPolygon(xCoordinates, yCoordinates, nPoints);
	}

	private static void addOnlyOddAmount(Point p, Set<Point> s) {
		/*
		 * s returns false if p is already in s, so when we add p, we *would*
		 * have an even amount of p in the set if duplicates were allowed, so
		 * we remove p to make it so the set abstractly contains only odd
		 * amounts.
		 */
		if (!s.add(p)) {
			s.remove(p);
		}
	}

	private static TreeSet<Point> findCorners(Set<BytePair> spaces) {
		TreeSet<Point> corners = new TreeSet<Point>(new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				if (a.x > b.x) {
					return 1;
				} if (a.x < b.x) {
					return -1;
				} else {
					if (a.y > b.y) {
						return 1;
					} else if (a.y < b.y) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		});

		for (BytePair p : spaces) {
			int x = p.getX() * CELL_LENGTH;
			int y = p.getY() * CELL_LENGTH;
			
			Point upperLeft = new Point(x, y);
			addOnlyOddAmount(upperLeft, corners);

			Point upperRight = new Point(x + CELL_LENGTH, y);
			addOnlyOddAmount(upperRight, corners);

			Point lowerLeft = new Point(x, y + CELL_LENGTH);
			addOnlyOddAmount(lowerLeft, corners);

			Point lowerRight = new Point(x + CELL_LENGTH, y + CELL_LENGTH);
			addOnlyOddAmount(lowerRight, corners);
		}

		return corners;
	}
}
