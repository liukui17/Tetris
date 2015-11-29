package infrastructure;

import java.net.Socket;

public class ServerInputThread extends InputThread {
	
	public ServerInputThread(Socket socket) {
		super(socket);
	}
	
	public void read() {
		
	}
}
