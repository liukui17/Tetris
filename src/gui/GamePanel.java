package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import infrastructure.ClientConnectionManager;
import infrastructure.Encoder;
import infrastructure.GameState;

public class GamePanel extends JPanel implements Runnable {
	private BoardPanel boardPanel;
	private ButtonListener buttonListener;

	private BlockingQueue<Byte> commands;
	private BlockingQueue<GameState> inputBoards; 

	private boolean isPlayerOne;

	public GamePanel(DataInputStream in, DataOutputStream out, boolean isPlayerOne) {
		this.isPlayerOne = isPlayerOne;
		commands = new LinkedBlockingQueue<Byte>();
		inputBoards = new LinkedBlockingQueue<GameState>();

		Thread managerThread = new Thread(new ClientConnectionManager(commands, inputBoards, in, out));
		managerThread.start();

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// Create & Add Swing Components
		Dimension minSize = new Dimension(100, 20);
		Dimension prefSize = new Dimension(100, 20);
		Dimension maxSize = new Dimension(100, 20);

		// Filler
		add(new Box.Filler(minSize, prefSize, maxSize));

		boardPanel = new BoardPanel();
		add(boardPanel);

		// Filler
		add(new Box.Filler(minSize, prefSize, maxSize));

		// Keyboard Dispatcher
		// Instead of printing, need to send input to network
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					int key = e.getKeyCode();

					byte msg = Encoder.encodeKeyPress(key);
					commands.add(msg);
				}
				return false;
			}
		});

		setBackground(Color.LIGHT_GRAY);
	}

	public void updateGrid(Color[][] grid) {
		boardPanel.updateGrid(grid);
		boardPanel.revalidate();
		boardPanel.repaint();
	}

	public void run() {
		while (true) {
			// check inputBoards
			// if something repaint
			//System.out.println(isPlayerOne);
		}
	}
}
