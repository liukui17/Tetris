package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import infrastructure.ClientConnectionManager;
import infrastructure.Encoder;
import infrastructure.GameState;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

	private BoardPanel boardPanel;
	private ScorePanel scorePanel;
	private EndPanel endPanel;

	private BlockingQueue<Byte> commands;
	private BlockingQueue<GameState> gameState;
	
	private MusicPlayer musicPlayer;

	public GamePanel(DataInputStream in, DataOutputStream out, boolean isPlayerOne, MusicPlayer musicPlayer, EndPanel endPanel) {

		commands = new LinkedBlockingQueue<Byte>();
		gameState = new LinkedBlockingQueue<GameState>();
		
		this.musicPlayer = musicPlayer;
		musicPlayer.playBGM();
		
		this.endPanel = endPanel;

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


		// Keyboard Dispatcher
		// Instead of printing, need to send input to network
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					int key = e.getKeyCode();
					
					/*
					 * Toggles playing music
					 */
					if (key == KeyEvent.VK_M) {
						if (musicPlayer.isPlaying()) {
							musicPlayer.stop();
						} else {
							musicPlayer.start();
						}
						return false;
					}

					byte msg = Encoder.encodeKeyPress(key, isPlayerOne);
					commands.add(msg);
				}
				return false;
			}
		});

		setBackground(Color.LIGHT_GRAY);
	}

	public void run() {
		while (true) {
			GameState state = null;
			try {
				state = gameState.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Check if game over
			if (state.getIsGameOver()) {
				removeAll();
				add(endPanel);
				revalidate();
				repaint();
				System.out.println("GAME OVER");
				break;
			}

			// Update score
			scorePanel.updateScore(state.getScore());
			scorePanel.revalidate();
			scorePanel.repaint(1);

			// Update grid
			boardPanel.updateGrid(state.getBoard(), state.getSpaces(0), state.getSpaces(1));
			boardPanel.revalidate();
			boardPanel.repaint(1);
		}
	}
}
