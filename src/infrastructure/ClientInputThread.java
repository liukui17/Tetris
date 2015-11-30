package infrastructure;

import java.net.Socket;

public class ClientInputThread extends InputThread {

		public ClientInputThread(Socket socket) {
			super(socket);
		}
		
		public void read() {
			
		}
}
