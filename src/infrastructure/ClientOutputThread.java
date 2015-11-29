package infrastructure;

import java.net.Socket;

public class ClientOutputThread extends OutputThread {
	
	public ClientOutputThread(Socket socket) {
		super(socket);
	}

	public void write() {
		
	}
}
