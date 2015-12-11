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
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(PORT_NUM);
			System.out.println("Started server");
			
			while (true) {
				Socket[] clientSockets = new Socket[GameUtil.NUM_PLAYERS];
				for (int i = 0; i < GameUtil.NUM_PLAYERS; i++) {
					clientSockets[i] = server.accept();
					System.out.println("Player " + i + " connected.");
				}
				
				notifyPlayerNumbers(clientSockets);
				
				new Thread(new GameThread(clientSockets)).start();
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
	private static void notifyPlayerNumbers(Socket[] playerSockets) throws IOException {
		DataOutputStream[] notifiers = new DataOutputStream[playerSockets.length];
		for (int i = 0; i < notifiers.length; i++) {
			notifiers[i] = new DataOutputStream(playerSockets[i].getOutputStream());
		}
		
		for (int i = 0; i < notifiers.length; i++) {
			notifiers[i].writeInt(i);
		}
	}
}

/*package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket; */

/**
 * The server application that will connect players together to
 * play a game of collaborative Tetris.
 */
/*public class GameServer {
	public static final int PORT_NUM = 3333;
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(PORT_NUM);
			System.out.println("Started server");
			
			while (true) {
				Socket player1 = server.accept();
				System.out.println("Player 1 connected");
				
				Socket player2 = server.accept();
				System.out.println("Player 2 connected");
				
				notifyPlayerNumbers(player1, player2);
				
				long dropInterval = getInitialDropInterval(player1, player2);
				
				new Thread(new GameThread(player1, player2, dropInterval)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	} */
	
	/**
	 * Notifies the players connected to sockets p1 and p2 which player number
	 * they are
	 * 
	 * @param p1 player 1's socket
	 * @param p2 player 2's socket
	 * 
	 * @throws IOException on IO errors
	 */
/*	private static void notifyPlayerNumbers(Socket p1, Socket p2) throws IOException {
		DataOutputStream outToP1 = new DataOutputStream(p1.getOutputStream());
		DataOutputStream outToP2 = new DataOutputStream(p2.getOutputStream());
		
		// arbitrary decision
		outToP1.writeBoolean(true);
		outToP2.writeBoolean(false);
	}
	
	private static long getInitialDropInterval(Socket p1, Socket p2) throws IOException {
		DataInputStream inFromP1 = new DataInputStream(p1.getInputStream());
		DataInputStream inFromP2 = new DataInputStream(p2.getInputStream());
		
		return (inFromP1.readLong() + inFromP2.readLong()) / 2;
	}
} */