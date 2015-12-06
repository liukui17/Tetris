package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		p1Spaces = new HashSet<BytePair>();
		p2Spaces = new HashSet<BytePair>();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		Set<BytePair> ghostLocations = findGhostLocation(p1Spaces, grid);
		ghostLocations.addAll(findGhostLocation(p2Spaces, grid));
		
		// draw the spaces themselves
		if (grid != null) {			
			for (int i = 0; i < GameUtil.BOARD_HEIGHT; i++) {
				for (int j = 0; j < GameUtil.BOARD_WIDTH; j++) {
					Color c = grid[i][j];
					
					g.setColor(c);
					
					if (isGhostSpace(j, i, ghostLocations)) {
						if (c.equals(GameUtil.PIECE_COLORS[0])) {
							g.setColor(GameUtil.GHOST);
						} 
					}
						
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
	
	private static boolean occupied(Set<BytePair> bottom, Color[][] board) {
		for (BytePair space : bottom) {
			int x = GameUtil.modulo((int) space.getX(), GameUtil.BOARD_WIDTH);
			int y = (int) space.getY();
			
			if (!board[y][x].equals(GameUtil.PIECE_COLORS[0])) {
				return true;
			}
		}
		return false;
	}

	private static Set<BytePair> findGhostLocation(Set<BytePair> spaces, Color[][] board) {
		Set<BytePair> rest = new HashSet<BytePair>();
		Set<BytePair> bottom = groupSpaces(spaces, rest);

		List<BytePair> newBottom = new LinkedList<BytePair>(bottom);
		List<BytePair> newRest = new LinkedList<BytePair>(rest);
		
		movePieceDown(newBottom, newRest);

		if (occupied(bottom, board)) {
			movePieceUp(newBottom, newRest);
		}
		
		while (canMoveDown(newBottom, board)) {
			movePieceDown(newBottom, newRest);
		}

		Set<BytePair> ghostLocation = new HashSet<BytePair>();

		for (BytePair space : newBottom) {
			ghostLocation.add(space);
		}

		for (BytePair space : newRest) {
			ghostLocation.add(space);
		}

		return ghostLocation;
	}
	
	private boolean isGhostSpace(int x, int y, Set<BytePair> ghostLocations) {
		return ghostLocations.contains(new BytePair((byte) x, (byte) y));
	}
	
	/**
	 * Categorizes the specified spaces into "bottom" spaces (the spaces closest
	 * to the bottom of the board for each column) and non-bottom pieces. Returns
	 * the non-bottom spaces through the specified output parameter rest and
	 * returns the "bottom" spaces through the regular return value.
	 * 
	 * @param spaces the spaces to categorize
	 * @param rest the OUTPUT PARAMETER of the non-bottom spaces
	 * 
	 * @requires spaces != null && rest != null
	 * 
	 * @return the Set of bottom spaces
	 */
	private static Set<BytePair> groupSpaces(Set<BytePair> spaces, Set<BytePair> rest) {
		Map<Byte, BytePair> tempBottom = new HashMap<Byte, BytePair>();

		for (BytePair s : spaces) {
			byte x = s.getX();
			byte y = s.getY();

			if (tempBottom.containsKey(x)) {
				BytePair spaceInMap = tempBottom.get(x);

				/*
				 * The new space is lower than the current one, so replace it and put
				 * the current bottom space into rest
				 */
				if (spaceInMap.getY() < y) {
					BytePair oldSpace = tempBottom.put(x, s);
					assert oldSpace == spaceInMap;
					rest.add(spaceInMap);
				} else {
					rest.add(s);
				}
			} else {
				tempBottom.put(x, s);
			}
		}

		Set<BytePair> bottom = new HashSet<BytePair>();
		for (Byte key : tempBottom.keySet()) {
			bottom.add(tempBottom.get(key));
		}

		return bottom;
	}
	
	private static void movePieceDown(List<BytePair> bottom, List<BytePair> rest) {
		for (int i = 0; i < bottom.size(); i++) {
			BytePair space = bottom.get(i);
			bottom.set(i, new BytePair(space.getX(), (byte) (space.getY() + 1)));
		}

		for (int i = 0; i < rest.size(); i++) {
			BytePair space = rest.get(i);
			rest.set(i, new BytePair(space.getX(), (byte) (space.getY() + 1)));
		}
	}
	
	private static void movePieceUp(List<BytePair> bottom, List<BytePair> rest) {
		for (int i = 0; i < bottom.size(); i++) {
			BytePair space = bottom.get(i);
			bottom.set(i, new BytePair(space.getX(), (byte) (space.getY() - 1)));
		}

		for (int i = 0; i < rest.size(); i++) {
			BytePair space = rest.get(i);
			rest.set(i, new BytePair(space.getX(), (byte) (space.getY() - 1)));
		}
	}

	private static boolean canMoveDown(Collection<BytePair> bottom, Color[][] board) {
		if (bottom.isEmpty()) {
			return false;
		}
		
		for (BytePair space : bottom) {
			byte x = space.getX();
			byte y = space.getY();

			// hit the bottom
			if (y + 1 > GameUtil.BOARD_HEIGHT - 1) {
				return false;
			}

			// collide with another piece
			int newX = GameUtil.modulo(x, GameUtil.BOARD_WIDTH);
			if (!board[y + 1][newX].equals(GameUtil.PIECE_COLORS[0])) {
				return false;
			}
		}
		return true;
	}

	private static void drawPieceOutLine(Graphics g, Color color, Set<Line> sides) {
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
