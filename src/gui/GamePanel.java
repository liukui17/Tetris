package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import infrastructure.ClientConnectionManager;
import infrastructure.Encoder;
import infrastructure.GameState;

public class GamePanel extends JPanel implements Runnable {
	private BoardPanel boardPanel;
	private ScorePanel scorePanel;
	private EndPanel endPanel;

	private BlockingQueue<Byte> commands;
	private BlockingQueue<GameState> gameState; 

	private boolean isPlayerOne;

	public GamePanel(DataInputStream in, DataOutputStream out, boolean isPlayerOne) {
		this.isPlayerOne = isPlayerOne;
		commands = new LinkedBlockingQueue<Byte>();
		gameState = new LinkedBlockingQueue<GameState>();

		Thread managerThread = new Thread(new ClientConnectionManager(commands, gameState, in, out));
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

		// Score Panel
		scorePanel = new ScorePanel();
		add(scorePanel);
		
		endPanel = new EndPanel();

		// Keyboard Dispatcher
		// Instead of printing, need to send input to network
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					int key = e.getKeyCode();

					byte msg = Encoder.encodeKeyPress(key, isPlayerOne);
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
			System.out.println("started gp thread");
			GameState state = null;
			try {
				state = gameState.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Arrays.deepToString(state.getBoard()[0]));
			
			// Check if game over
			if (state.getIsGameOver()) {
				removeAll();
				add(endPanel);
				revalidate();
				repaint();
				break;
			}

			// Update score
			scorePanel.updateScore(state.getScore());
			scorePanel.revalidate();
			scorePanel.repaint();

			// Update grid
			boardPanel.updateGrid(state.getBoard());
			boardPanel.revalidate();
			boardPanel.repaint();
		}
	}
}
