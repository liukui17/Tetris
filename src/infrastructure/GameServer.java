package infrastructure;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server application that will connect players together to
 * play a game of competitive Tetris.
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
				
				long dropInterval = getInitialDropInterval(clientSockets);
				
				new Thread(new GameThread(clientSockets, dropInterval)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Notifies the players connected to sockets in the specified Socket[] which
	 * player number they are.
	 * 
	 * @param playerSockets the Socket[] that contains the Sockets for each player
	 * 
	 * @requires playerSockets != null
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
	
	/**
	 * Obtains the initial dropping interval from the player's difficult settings.
	 * The initial dropping interval is defined to be the average of all of the
	 * players' settings.
	 * 
	 * @param playerSockets
	 * 						socket connections to the players
	 * @return the initial dropping interval
	 * @throws IOException iff error on reading the dropping intervals from each player
	 */
	private static long getInitialDropInterval(Socket[] playerSockets) throws IOException {
		DataInputStream[] streams = new DataInputStream[playerSockets.length];
		for (int i = 0; i < streams.length; i++) {
			streams[i] = new DataInputStream(playerSockets[i].getInputStream());
		}
		
		long interval = 0;
		for (int i = 0; i < streams.length; i++) {
			interval += streams[i].readLong();
		}
		
		return interval / streams.length;
	}
}