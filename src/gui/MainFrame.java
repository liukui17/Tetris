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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import infrastructure.Encoder;
import infrastructure.GameServer;
import infrastructure.GameUtil;

public class MainFrame extends JFrame {
	private static final int WIDTH = 500;/*BoardPanel.CELL_LENGTH * GameUtil.BOARD_WIDTH + 200;*/
	private static final int HEIGHT = BoardPanel.CELL_LENGTH * GameUtil.BOARD_HEIGHT + (GameUtil.BOARD_HEIGHT + 1) + 100;
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
	
	private boolean getCreateOrJoin() {
		Object[] options = {"Create a new game", "Join a current game"};
		int response = JOptionPane.showOptionDialog(waitingPanel, "Would you like to create or join a game",
				"Create or join a game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		return response == JOptionPane.YES_OPTION;
	}
	
	private String getGameName() {
		return getTypedInput("Enter a game name");
	}
	
	private String getTypedInput(String prompt) {
		String input = "";
		while (input.isEmpty()) {
			input = (String) JOptionPane.showInputDialog(waitingPanel,
          																						prompt);
		}
		return input;
	}
	
	private int getNumPlayers() {
		int numPlayers = -1;
		while (true) {
			try {
				numPlayers = Integer.parseInt((String) JOptionPane.showInputDialog(
            waitingPanel,
            "Enter number of players"));
				break;
			} catch (NumberFormatException e) {
				displayError("Not a number. Try again");
			}
		}
		return numPlayers;
	}
	
	private void displayError(String message) {
		JOptionPane.showMessageDialog(waitingPanel,
		    message, "error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void displayMessage(String message) {
		JOptionPane.showMessageDialog(waitingPanel, message);
	}
	
	private boolean canStartGame(byte response) {
		switch(response) {
			case GameServer.GAME_ALREADY_EXISTS : displayError("Game already exists"); return false;
			case GameServer.GAME_DOES_NOT_EXIST : displayError("Game does not exist"); return false;
			case GameServer.ILLEGAL_NUM_PLAYERS : displayError("Illegal number of players"); return false;
			case GameServer.SUCCESS_CREATION : displayMessage("Successfully created game"); return true;
			case GameServer.SUCCESS_JOIN : displayMessage("Successfully joined game"); return true;
			default : return false;
		}
	}

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
							boolean createOrJoin = getCreateOrJoin();
							
//							String gameName = getGameName();
//							int numPlayers = getNumPlayers();

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
				
								out.writeBoolean(createOrJoin);
								
								String gameName;
								int numPlayers;
								
								while (true) {
									gameName = getGameName();
									numPlayers = getNumPlayers();
									out.writeUTF(gameName);
									out.writeInt(numPlayers);
									if (canStartGame(in.readByte())) {
										break;
									}
								}

								// Should block here until server sends boolean
								int playerNumber = in.readInt();
								
								out.writeLong(dropInterval);

								c.remove(waitingPanel);
								
								System.out.println("from MainFrame " + numPlayers);

								gamePanel = new GamePanel(in, out, playerNumber, musicPlayer, endPanel, drawGhosts, numPlayers);
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
