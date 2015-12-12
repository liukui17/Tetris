package tests;
import pieces.*;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
	Board board;
	
	@Before
	public void setUp() {
		board = new Board(4);
	}
	
	@Test
	public void testGeneralMoveDown() {
		Piece p1 = board.getPiece(0);
		List<Square> p1Squares = p1.getSpaces();
		int initX = p1Squares.get(0).getX();
		int initY = p1Squares.get(0).getY();
		board.tryMoveDown(0);
		int newX = p1Squares.get(0).getX();
		int newY = p1Squares.get(0).getY();
		assert(initX == newX);
		assert(initY == newY - 1);
	}
	
	@Test
	public void testDropDown() {
		Piece p1 = board.getPiece(0);
		Piece p2 = board.getPiece(1);
		board.drop(0);
		board.drop(1);
		Piece p1n = board.getPiece(0);
		Piece p2n = board.getPiece(1);
		
		System.out.println(p1.toString());
		System.out.println(p2.toString());
		System.out.println(p1n.toString());
		System.out.println(p2n.toString());
		
		// check their addresses are different, i.e. we
		// actually generated new pieces
		assert(p1 != p1n);
		assert(p2 != p2n);
		
	}
}
