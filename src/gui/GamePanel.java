package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import infrastructure.ClientConnectionManager;
import infrastructure.Encoder;
import infrastructure.GameState;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private static final Dimension LABEL_SIZE = new Dimension(100, 20);

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
		setBackground(Color.LIGHT_GRAY);

		// Create & Add Swing Components
		// Left Panel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(Color.LIGHT_GRAY);
		
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel leftTitle = GuiUtil.addLabel(leftPanel, "Player 1", 20);
		leftTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		leftTitle.setPreferredSize(LABEL_SIZE);
		
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		GuiUtil.addLabel(leftPanel, "Score", 20);
		
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		leftScore = GuiUtil.addLabel(leftPanel, "0", 20);
		
//		// Filler
//		leftPanel.add(Box.createRigidArea(new Dimension(0, 40)));
//		
//		JLabel leftNextLabel = new JLabel("Next");
//		leftNextLabel.setFont(LABEL_FONT);
//		leftNextLabel.setPreferredSize(LABEL_SIZE);
//		leftNextLabel.setAlignmentX(CENTER_ALIGNMENT);
//		leftPanel.add(leftNextLabel);
		
		leftPanel.add(Box.createVerticalGlue());
		
		add(leftPanel);
		
		add(Box.createHorizontalGlue());

		boardPanel = new BoardPanel();
		boardPanel.setPreferredSize(new Dimension(480, 720));
		boardPanel.setAlignmentX(CENTER_ALIGNMENT);
		boardPanel.setAlignmentY(CENTER_ALIGNMENT);
		add(boardPanel);
		
		add(Box.createHorizontalGlue());

		// Right Panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(Color.LIGHT_GRAY);
		
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel rightTitle = GuiUtil.addLabel(rightPanel, "Player 2", 20);
		rightTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		rightTitle.setPreferredSize(LABEL_SIZE);
		
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		GuiUtil.addLabel(rightPanel, "Score", 20);
		
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		rightScore = GuiUtil.addLabel(rightPanel, "0", 20);
		
//		// Filler
//		rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));
//		
//		JLabel leftNextLabel = new JLabel("Next");
//		leftNextLabel.setFont(LABEL_FONT);
//		leftNextLabel.setPreferredSize(LABEL_SIZE);
//		leftNextLabel.setAlignmentX(CENTER_ALIGNMENT);
//		rightPanel.add(leftNextLabel);
		
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

				endPanel.setScore(0, state.getScore(0));
				endPanel.setScore(1, state.getScore(1));
				
				add(endPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
				
				System.out.println("GAME OVER");
				break;
			}

			// Update score
			leftScore.setText(Integer.toString(state.getScore(0)));
			rightScore.setText(Integer.toString(state.getScore(1)));

			// Update grid
			boardPanel.updateGrid(state.getBoard(), state.getSpaces(0), state.getSpaces(1));
			boardPanel.revalidate();
			boardPanel.repaint();
			
			revalidate();
			repaint();
		}
	}
}
