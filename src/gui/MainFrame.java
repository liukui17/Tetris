package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import infrastructure.Encoder;
import infrastructure.GameUtil;

public class MainFrame extends JFrame {
	private static final int WIDTH = BoardPanel.CELL_LENGTH * GameUtil.BOARD_WIDTH + (GameUtil.BOARD_WIDTH + 1) + 60;
	private static final int HEIGHT = BoardPanel.CELL_LENGTH * GameUtil.BOARD_HEIGHT + (GameUtil.BOARD_HEIGHT + 1) + 60;
	private static final long EASY_INTERVAL = 1000;
	private static final long MEDIUM_INTERVAL = 500;
	private static final long HARD_INTERVAL = 250;

	private MenuPanel menuPanel;
	private HelpPanel helpPanel;
	private GamePanel gamePanel;
	private OptionsPanel optionsPanel;
	private WaitingPanel waitingPanel;
	private EndPanel endPanel;
	
	private Socket socket;
	
	private boolean drawGhosts;
	private long dropInterval;
	
	private MusicPlayer musicPlayer;

	public MainFrame(String title, String hostName, int portNum) {		
		super(title);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);
		
		drawGhosts = true;
		dropInterval = EASY_INTERVAL;
		musicPlayer = new MusicPlayer();

		// Set layout manager
		setLayout(new BorderLayout());

		// Create Swing component
		menuPanel = new MenuPanel();
		helpPanel = new HelpPanel();
		optionsPanel = new OptionsPanel(musicPlayer);
		waitingPanel = new WaitingPanel();
		endPanel = new EndPanel();

		// Add Swing components to content pane
		Container c = getContentPane();
		c.add(menuPanel, BorderLayout.CENTER);

		// Set Listeners
		menuPanel.setButtonListener(new ButtonListener() {
			public void buttonClicked(String s) {
				c.remove(menuPanel);
				revalidate();
				repaint();

				switch (s) {
				case "Options":
					c.add(optionsPanel, BorderLayout.CENTER);
					break;
				case "Help":
					c.add(helpPanel, BorderLayout.CENTER);
					break;
				case "Start":
					c.add(waitingPanel, BorderLayout.CENTER);
					musicPlayer.playCrickets();
					revalidate();
					repaint();

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							socket = null;
							boolean connected = false;
							while (!connected) {
								try {
									socket = new Socket(hostName, portNum);
								} catch (Exception e) {
									System.out.println("Server not accepting connections");
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
									continue;
								}
								connected = true;
							}

							try {
								DataInputStream in = new DataInputStream(socket.getInputStream());
								DataOutputStream out = new DataOutputStream(socket.getOutputStream());

								// Should block here until server sends boolean
								int playerNumber = in.readInt();
								
								out.writeLong(dropInterval);

								c.remove(waitingPanel);

								gamePanel = new GamePanel(in, out, playerNumber, musicPlayer, endPanel, drawGhosts);
								Thread gameThread = new Thread(gamePanel);
								c.add(gamePanel, BorderLayout.CENTER);
								gameThread.start();
								revalidate();
								repaint();
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
					revalidate();
					repaint();
					
					break;
				}

				revalidate();
				repaint();
			}
		});

		helpPanel.setButtonListener(new ButtonListener() {
			public void buttonClicked(String s) {
				if (s.equals("Back")) {
					c.add(menuPanel, BorderLayout.CENTER);
					c.remove(helpPanel);
					revalidate();
					repaint();
				}

			}
		});

		optionsPanel.setButtonListener(new ButtonListener() {
			public void buttonClicked(String s) {
				switch (s) {
				case "Back":
					c.add(menuPanel, BorderLayout.CENTER);
					c.remove(optionsPanel);
					revalidate();
					repaint();
					break;
				case "No":
					drawGhosts = false;
					break;
				case "Yes":
					drawGhosts = true;
					break;
				case "Easy":
					dropInterval = EASY_INTERVAL;
					break;
				case "Medium":
					dropInterval = MEDIUM_INTERVAL;
					break;
				case "Hard":
					dropInterval = HARD_INTERVAL;
					break;
				}
			}
		});
		
		endPanel.setButtonListener(new ButtonListener() {
			public void buttonClicked(String s) {
				if (s.equals("Back to Menu")) {
					musicPlayer.stop();
					musicPlayer.close();
					c.add(menuPanel, BorderLayout.CENTER);
					c.remove(gamePanel);
					revalidate();
					repaint();
				}

			}
		});

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				if (socket != null) {
					DataOutputStream out;
					try {
						out = new DataOutputStream(socket.getOutputStream());
						out.writeByte(Encoder.QUIT_MASK);
						if (gamePanel != null) {
							gamePanel.manager.toggleHasQuit();
						}
						out.close();
						socket.close();
					} catch (IOException e1) {
						System.out.println("Failed to notify server...");
					//	e1.printStackTrace();
					}
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
			
		});
	}

}
