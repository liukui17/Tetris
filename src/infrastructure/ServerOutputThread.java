package infrastructure;

import java.net.Socket;

public class ServerOutputThread extends OutputThread {
	
	public ServerOutputThread(Socket socket) {
		super(socket);
	}

	public void write() {
		
	}
}
