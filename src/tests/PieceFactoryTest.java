package tests;

import pieces.Piece;
import pieces.PieceFactory;

public class PieceFactoryTest {
	public static void main(String[] args) {
		Piece[] arr = new Piece[10];
		for (int i = 0; i < 10; i++) {
			arr[i] = PieceFactory.generateNewPiece(0);
			System.out.println(arr[i].toString());
		}
	}
}
