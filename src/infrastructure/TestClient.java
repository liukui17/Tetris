package infrastructure;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.io.*;

/**
 * A simple test client for GameServer.java. Two of these must
 * be connected to the same GameServer for the game to proceed.
 * 
 * How to use:
 * 1. Run GameServer
 * 2. Run one instance of TestClient (after this the server is still waiting
 * 		for another player to connect)
 * 3. Run another separate instance of TestClient (now the game will start)
 * 4. Clients type in a decimal representation of a 2's complement byte and
 * 		press enter to submit move to server
 * 5. There is no graceful way to end the game yet, so use Ctrl-C on all parties
 * 		of the game to "end" it
 */
public class TestClient {
	// the port number of the server
  public static final int PORT_NUM = 3333;

	public static void main(String[] args) {
		try {
			Socket connection = new Socket("localhost", PORT_NUM);
			System.out.println("Connected to server");

      Scanner in = new Scanner(System.in);
      DataOutputStream out = new DataOutputStream(connection.getOutputStream());

      System.out.println("Type in a decimal representation of a 2's complement byte");
      while (in.hasNextByte()) {
        byte b = in.nextByte();
        out.writeByte(b);
        System.out.println("Type in a decimal representation of a 2's complement byte");
      }
      
      connection.close();
      in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
