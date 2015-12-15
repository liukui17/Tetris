package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import infrastructure.BytePair;
import infrastructure.ClientConnectionManager;
import infrastructure.Encoder;
import infrastructure.GameState;
import infrastructure.GameUtil;

public class GamePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private static final Dimension LABEL_SIZE = new Dimension(100, 20);

	private BoardPanel boardPanel;
	private EndPanel endPanel;

	private JLabel[] scoreLabels;
	private PiecePanel[] upcomingPieces;

	private KeyEventDispatcher keyDispatcher;

	private BlockingQueue<Byte> commands;
	private BlockingQueue<GameState> gameState;
	
	private MusicPlayer musicPlayer;
	private int numPlayers;
	
	ClientConnectionManager manager;

	public GamePanel(DataInputStream in, DataOutputStream out, int playerNumber, MusicPlayer musicPlayer,
					 EndPanel endPanel, boolean drawGhosts, boolean upcomingAssistance, int numPlayers) {

		commands = new LinkedBlockingQueue<Byte>();
		gameState = new LinkedBlockingQueue<GameState>();
		
		this.endPanel = endPanel;
		this.musicPlayer = musicPlayer;
		this.numPlayers = numPlayers;
		musicPlayer.playBGM();
		

		System.out.println("from gamepanel " + numPlayers);

		manager = new ClientConnectionManager(commands, gameState, in, out, numPlayers, upcomingAssistance);
		Thread managerThread = new Thread(manager);
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
		GuiUtil.addLabel(leftPanel, "Score", 30);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		scoreLabels = new JLabel[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			JLabel nextTitleLabel = GuiUtil.addLabel(leftPanel, "Player " + i, 20);
			nextTitleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
			nextTitleLabel.setPreferredSize(LABEL_SIZE);
			
			leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			scoreLabels[i] =  GuiUtil.addLabel(leftPanel, "0", 20);
			leftPanel.add(Box.createVerticalGlue());
		}
		
		leftPanel.add(Box.createVerticalGlue());
		
		add(leftPanel);
		
		add(Box.createHorizontalGlue());

		boardPanel = new BoardPanel(drawGhosts, numPlayers);
		boardPanel.setAlignmentX(CENTER_ALIGNMENT);
		boardPanel.setAlignmentY(CENTER_ALIGNMENT);
		add(boardPanel);
		
		add(Box.createHorizontalGlue());

		// Right Panel
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(Color.LIGHT_GRAY);
		
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		JLabel rightTitle = GuiUtil.addLabel(rightPanel, "Next", 30);
		rightTitle.setPreferredSize(LABEL_SIZE);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		if (upcomingAssistance) {
			System.out.println(upcomingAssistance);
			upcomingPieces = new PiecePanel[numPlayers];
			for (int i = 0; i < numPlayers; i++) {
				rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
				JLabel nextTitleLabel = GuiUtil.addLabel(rightPanel, "Player " + i, 20);
				nextTitleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
				nextTitleLabel.setPreferredSize(LABEL_SIZE);
				
				rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
				PiecePanel piece = new PiecePanel();
				rightPanel.add(piece);
				upcomingPieces[i] = piece;
				rightPanel.add(Box.createVerticalGlue());
			}
		} else {
			upcomingPieces = null;
		}
		
		rightPanel.add(Box.createVerticalGlue());
		
		add(rightPanel);
		
		add(Box.createRigidArea(new Dimension(10, 0)));
		
		// Keyboard Dispatcher
		keyDispatcher = new KeyEventDispatcher() {
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

					byte msg = Encoder.encodeKeyPress(key, playerNumber);
					commands.add(msg);
				}
				return false;
			}
		};
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyDispatcher);

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

				endPanel.setScores(scoreLabels);
				
				add(endPanel, BorderLayout.CENTER);
				revalidate();
				repaint();
				
				System.out.println("GAME OVER");
				KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyDispatcher);
				break;
			}

			// update scores
			for (int i = 0; i < numPlayers; i++) {
				scoreLabels[i].setText(Integer.toString(state.getScore(i)));
			}

			// update next piece
			if (upcomingPieces != null) {
				byte[] upcoming = state.getUpcomingPieces();
				for (int i = 0; i < numPlayers; i++) {
					byte pieceByte = upcoming[i];
					// update piece based upon pieceByte
					switch (pieceByte) {
					case 1:
						upcomingPieces[i].updatePiece(GameUtil.I_SHAPE, GameUtil.I_COLOR); break;
					case 2:
						upcomingPieces[i].updatePiece(GameUtil.J_SHAPE, GameUtil.J_COLOR); break;
					case 3:
						upcomingPieces[i].updatePiece(GameUtil.L_SHAPE, GameUtil.L_COLOR); break;
					case 4:
						upcomingPieces[i].updatePiece(GameUtil.O_SHAPE, GameUtil.O_COLOR); break;
					case 5:
						upcomingPieces[i].updatePiece(GameUtil.S_SHAPE, GameUtil.S_COLOR); break;
					case 6:
						upcomingPieces[i].updatePiece(GameUtil.T_SHAPE, GameUtil.T_COLOR); break;
					case 7:
						upcomingPieces[i].updatePiece(GameUtil.Z_SHAPE, GameUtil.Z_COLOR); break;
					default:
						upcomingPieces[i].updatePiece(null, GameUtil.EMPTY);
					}
				}
			}

			// Update grid
			List<Set<BytePair>> spaces = new ArrayList<Set<BytePair>>(numPlayers);
			for (int i = 0; i < numPlayers; i++) {
				spaces.add(state.getSpaces(i));
			}
			boardPanel.updateGrid(state.getBoard(), spaces);
			boardPanel.revalidate();
			boardPanel.repaint();
			
			revalidate();
			repaint();
		}
	}
}
