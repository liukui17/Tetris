package gui;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(3333);
			System.out.println("Started server");

				Socket player1 = s.accept();
				System.out.println("Player 1 connected");
				Socket player2 = s.accept();
				System.out.println("Player 2 connected");
				
				DataOutputStream p1_out = new DataOutputStream(player1.getOutputStream());
				DataOutputStream p2_out = new DataOutputStream(player2.getOutputStream());
				
				p1_out.writeBoolean(true);
				p2_out.writeBoolean(false);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
