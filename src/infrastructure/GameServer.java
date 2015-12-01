package infrastructure;

import java.io.DataOutputStream;
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
				
				notifyPlayerNumbers(player1, player2);
				
				new Thread(new GameThread(player1, player2)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Notifies the players connected to sockets p1 and p2 which player number
	 * they are
	 * 
	 * @param p1 player 1's socket
	 * @param p2 player 2's socket
	 * 
	 * @throws IOException on IO errors
	 */
	private static void notifyPlayerNumbers(Socket p1, Socket p2) throws IOException {
		DataOutputStream outToP1 = new DataOutputStream(p1.getOutputStream());
		DataOutputStream outToP2 = new DataOutputStream(p2.getOutputStream());
		
		// arbitrary decision
		outToP1.writeBoolean(true);
		outToP2.writeBoolean(false);
	}
}