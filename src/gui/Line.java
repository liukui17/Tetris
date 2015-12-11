package gui;

import java.awt.Point;

/**
 * A Line connecting two points.
 */
public class Line {
	private Point start;
	private Point end;
	
	public Line(Point start, Point end) {
		this.start = start;
		this.end   = end;
	}
	
	public Point getStart() {
		return start;
	}
	
	public Point getEnd() {
		return end;
	}
	
	@Override
	public int hashCode() {
		return start.hashCode() + 37 * end.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Line)) {
			return false;
		}
		Line l = (Line) o;
		return (start.equals(l.start) && end.equals(l.end)) ||
					 (start.equals(l.end)&& end.equals(l.start)) ;
	}
}
