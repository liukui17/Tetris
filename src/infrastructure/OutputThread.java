package infrastructure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class OutputThread implements Runnable {
	DataOutputStream out;
	
	public OutputThread(Socket socket) {
		try {
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			write();
		}
	}
	
	public abstract void write();
	
}
