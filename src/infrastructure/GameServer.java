package infrastructure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server application that will connect players together to
 * play a game of collaborative Tetris.
 */
public class GameServer {
	public static final int PORT_NUM = 3333;
	
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(PORT_NUM);
			System.out.println("Started server");
			
			while (true) {
				Socket player1 = server.accept();
				System.out.println("Player 1 connected");
				Socket player2 = server.accept();
				System.out.println("Player 2 connected");
				
				new Thread(new GameThread(player1, player2)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}