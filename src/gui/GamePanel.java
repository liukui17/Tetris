package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import infrastructure.ClientConnectionManager;
import infrastructure.Encoder;
import infrastructure.GameState;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private static final Dimension LABEL_SIZE = new Dimension(100, 20);
	private static final Font LABEL_FONT = new Font("Dialog", Font.PLAIN, 20);

	private BoardPanel boardPanel;
	private EndPanel endPanel;
	private JLabel leftScore;
	private JLabel rightScore;

	private BlockingQueue<Byte> commands;
	private BlockingQueue<GameState> gameState;
	
	private MusicPlayer musicPlayer;

	public GamePanel(DataInputStream in, DataOutputStream out, boolean isPlayerOne, MusicPlayer musicPlayer, EndPanel endPanel) {

		commands = new LinkedBlockingQueue<Byte>();
		gameState = new LinkedBlockingQueue<GameState>();
		
		this.endPanel = endPanel;
		this.musicPlayer = musicPlayer;
		musicPlayer.playBGM();

		Thread managerThread = new Thread(new ClientConnectionManager(commands, gameState, in, out));
		managerThread.start();

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// Create & Add Swing Components
		// Left Panel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(Color.LIGHT_GRAY);
		
		// Filler
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JLabel leftScoreLabel = new JLabel("Score");
		leftScoreLabel.setFont(LABEL_FONT);
		leftScoreLabel.setPreferredSize(LABEL_SIZE);
		leftScoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		leftPanel.add(leftScoreLabel);
		
		// Filler
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		leftScore = new JLabel("0");
		leftScore.setFont(LABEL_FONT);
		leftScore.setPreferredSize(LABEL_SIZE);
		leftScore.setAlignmentX(CENTER_ALIGNMENT);
		leftPanel.add(leftScore);
		
		// Filler
		leftPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		
		JLabel leftNextLabel = new JLabel("Next");
		leftNextLabel.setFont(LABEL_FONT);
		leftNextLabel.setPreferredSize(LABEL_SIZE);
		leftNextLabel.setAlignmentX(CENTER_ALIGNMENT);
		leftPanel.add(leftNextLabel);
		
		leftPanel.add(Box.createVerticalGlue());
		
		add(leftPanel);

		boardPanel = new BoardPanel();
		add(boardPanel);

		// Right Panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(Color.LIGHT_GRAY);
		
		// Filler
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JLabel rightScoreLabel = new JLabel("Score");
		rightScoreLabel.setFont(LABEL_FONT);
		rightScoreLabel.setPreferredSize(LABEL_SIZE);
		rightScoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		rightPanel.add(rightScoreLabel);
		
		// Filler
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		rightScore = new JLabel("0");
		rightScore.setFont(LABEL_FONT);
		rightScore.setPreferredSize(LABEL_SIZE);
		rightScore.setAlignmentX(CENTER_ALIGNMENT);
		rightPanel.add(rightScore);
		
		// Filler
		rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		
		JLabel rightNextLabel = new JLabel("Next");
		rightNextLabel.setFont(LABEL_FONT);
		rightNextLabel.setPreferredSize(LABEL_SIZE);
		rightNextLabel.setAlignmentX(CENTER_ALIGNMENT);
		rightPanel.add(rightNextLabel);
		
		rightPanel.add(Box.createVerticalGlue());
		
		add(rightPanel);
		


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
				setLayout(new BorderLayout());

				endPanel.setScore(state.getScore());
				add(endPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
				
				System.out.println("GAME OVER");
				break;
			}

			// Update score
			leftScore.setText(Integer.toString(state.getScore()));
			rightScore.setText(Integer.toString(state.getScore()));

			// Update grid
			boardPanel.updateGrid(state.getBoard(), state.getSpaces(0), state.getSpaces(1));
			boardPanel.revalidate();
			boardPanel.repaint();
			
			revalidate();
			repaint();
		}
	}
}
