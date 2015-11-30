package infrastructure;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class InputThread implements Runnable {
	DataInputStream in;
	
	public InputThread(Socket socket) {
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			read();
		}
	}
	
	public abstract void read();
	
}
