package infrastructure;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ServerConnectionsManager implements Runnable {
	
	OneClientConnectionHandler[] handlers;
	BlockingQueue<Byte> commands;
	BlockingQueue<GameState> outStates;
	
	public ServerConnectionsManager(BlockingQueue<Byte> commands,
																	BlockingQueue<GameState> outStates,
																	Socket[] clientSockets) {
		handlers = new OneClientConnectionHandler[GameUtil.NUM_PLAYERS];
		for (int i = 0; i < GameUtil.NUM_PLAYERS; i++) {
			handlers[i] = new OneClientConnectionHandler(clientSockets[i], commands, handlers, i);
		}
		this.commands = commands;
		this.outStates = outStates;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < handlers.length; i++) {
			new Thread(handlers[i]).start();
		}
		GameState nextState = null;
		while (true) {
			try {
				nextState = outStates.take();
				for (int i = 0; i < handlers.length; i++) {
				//	System.out.println(nextState);
					if (handlers[i] != null) {
						handlers[i].giveNewOutState(nextState);
					}
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace(System.err);
			}
		}
	}
}
