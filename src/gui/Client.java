package gui;

import javax.swing.*;

public class Client {
	private static final String HOST_NAME = "localhost";
	private static final int PORT_NUM = 3333;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame("Tetris", HOST_NAME, PORT_NUM);
			}
		});

	}
}
