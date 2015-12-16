package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import infrastructure.Encoder;
import infrastructure.GameServer;
import infrastructure.GameUtil;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private static final int WIDTH = 500;/*BoardPanel.CELL_LENGTH * GameUtil.BOARD_WIDTH + 200;*/
	private static final int HEIGHT = BoardPanel.CELL_LENGTH * GameUtil.BOARD_HEIGHT + (GameUtil.BOARD_HEIGHT + 1) + 100;
	private static final long EASY_INTERVAL = 1000;
	private static final long MEDIUM_INTERVAL = 500;
	private static final long HARD_INTERVAL = 250;

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 3333;

	private MenuPanel menuPanel;
	private HelpPanel helpPanel;
	private GamePanel gamePanel;
	private OptionsPanel optionsPanel;
	private WaitingPanel waitingPanel;
	private EndPanel endPanel;

	private Socket socket;

	private boolean drawGhosts;
	private boolean upcomingAssistance;
	private long dropInterval;
	private int playerNumber;
	private int numPlayers;
	private String hostName;
	private int portNum;

	private MusicPlayer musicPlayer;

	private byte getChoice() {
		Object[] options = {"Create a new game", "Join a current game", "Back"};
		int response = JOptionPane.showOptionDialog(waitingPanel, "Would you like to create or join a game",
				"Create or join a game", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		switch (response) {
			case JOptionPane.YES_OPTION: return GameServer.CREATING;
			case JOptionPane.NO_OPTION: return GameServer.JOINING;
			case JOptionPane.CANCEL_OPTION: return GameServer.CLIENT_DISCONNECTED;
			default: return GameServer.CLIENT_DISCONNECTED; 
		}
	}

	private String getGameName() {
		return getTypedInput("Enter a game name");
	}

	private String getTypedInput(String prompt) {
		String input = "";
		while (input.isEmpty()) {
			input = (String) JOptionPane.showInputDialog(waitingPanel,
					prompt, "Title", JOptionPane.PLAIN_MESSAGE);
			// null = cancel button
			if (input == null) {
				return null;
			}
		}
		return input;
	}
	
	/*
	 * Ensures user types in a positive number. Will return -1 iff user hit the
	 * cancel button
	 */
	private int getNumPlayers() {
		int numPlayers = -1;
		while (true) {
			try {
				String numPlayersString = (String) JOptionPane.showInputDialog(
						waitingPanel, "Enter number of players", "Players", 
						JOptionPane.PLAIN_MESSAGE);
				
				// null = cancel button
				if (numPlayersString == null) {
					return -1;
				}
				
				numPlayers = Integer.parseInt(numPlayersString);
				
				if (numPlayers > 0) {
					return numPlayers;
				} else {
					displayError("Number must be positive");
				}
			} catch (NumberFormatException e) {
				displayError("Not a number. Try again");
			}
		}
	}

	private void displayError(String message) {
		JOptionPane.showMessageDialog(waitingPanel,
				message, "error", JOptionPane.ERROR_MESSAGE);
	}

	private void displayMessage(String message) {
		JOptionPane.showMessageDialog(waitingPanel, message, "Reponse", JOptionPane.PLAIN_MESSAGE);
	}

	private String sendNameToServer(boolean creating, DataInputStream in, DataOutputStream out) throws IOException {
		byte response;
		
		String gameName = ""; 
		while (true) {
			gameName = getGameName();

			if (gameName == null) {
				out.writeUTF("");
				return null;
			}

			out.writeUTF(gameName);

			response = in.readByte();

			if (creating) {
				if (response == GameServer.GAME_DOES_NOT_EXIST) {
					break;
				} else {
					displayMessage("Game already exists");
				}
			} else {
				if (response == GameServer.GAME_ALREADY_EXISTS) {
					break;
				} else {
					displayMessage("Game does not exist");
				}
			}
		}

		return gameName;
	}

	private int sendNumPlayersToServer(DataInputStream in, DataOutputStream out) throws IOException {
		byte response;
		int numPlayers = -1;
		while (true) {
			numPlayers = getNumPlayers();
			
			if (numPlayers == -1) {
				out.writeInt(numPlayers);
				// Client hit cancel; tell the server to start the protocol over
				return -1;
			}
			
			out.writeInt(numPlayers);
			response = in.readByte();
			if (response != GameServer.ILLEGAL_NUM_PLAYERS) {
				return numPlayers;
			}
		}
	}

	public MainFrame(String title) {		
		super(title);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);

		drawGhosts = true;
		upcomingAssistance = true;
		dropInterval = EASY_INTERVAL;
		musicPlayer = new MusicPlayer();

		// Set layout manager
		setLayout(new BorderLayout());

		// Create Swing component
		menuPanel = new MenuPanel();
		helpPanel = new HelpPanel();
		optionsPanel = new OptionsPanel(musicPlayer);
		waitingPanel = new WaitingPanel();
		//endPanel = new EndPanel();

		hostName = DEFAULT_HOST;
		portNum = DEFAULT_PORT;

		// Add Swing components to content pane
		Container c = getContentPane();
		c.add(menuPanel, BorderLayout.CENTER);

		// Set Listeners
		ButtonListener endListener = new ButtonListener() {
			public void buttonClicked(String s) {
				if (s.equals("Back to Menu")) {
					musicPlayer.stop();
					musicPlayer.close();
					c.add(menuPanel, BorderLayout.CENTER);
					c.remove(gamePanel);
					setSize(WIDTH, HEIGHT);
					revalidate();
					repaint();
				}

			}
		};
		
		menuPanel.setButtonListener(new ButtonListener() {
			public void buttonClicked(String s) {
				c.remove(menuPanel);
				revalidate();
				repaint();

				switch (s) {
				case "Exit": System.exit(0);
					
				case "Options":
					c.add(optionsPanel, BorderLayout.CENTER);
					musicPlayer.playBGM();
					musicPlayer.stop();
					setSize(optionsPanel.getPreferredSize());
					setMinimumSize(optionsPanel.getPreferredSize());
					break;
					
				case "Help":
					c.add(helpPanel, BorderLayout.CENTER);
					setMinimumSize(helpPanel.getPreferredSize());
					break;
					
				case "Start":
					c.add(waitingPanel, BorderLayout.CENTER);
					//musicPlayer.playCrickets();
					revalidate();
					repaint();

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							socket = null;
							boolean connected = false;
							while (!connected) {
								try {
									System.out.println("Trying to connect to " + hostName + " " + portNum);
									socket = new Socket(hostName, portNum);
								} catch (Exception e) {
									JTextField host = new JTextField();
									host.setText(hostName);
									
									JTextField port = new JTextField();
									port.setText(Integer.toString(portNum));
									
									Object[] message = {"Host name:", host, "Port number:", port};

									int option = JOptionPane.showConfirmDialog(waitingPanel, message, "Enter a valid Host and Port", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
									if (option == JOptionPane.OK_OPTION) {
										hostName = host.getText();
										try {
											portNum = Integer.parseInt(port.getText());
										} catch (Exception e1) {
											JOptionPane.showMessageDialog(waitingPanel, "Port number must be an integer", "Error", JOptionPane.PLAIN_MESSAGE);
											continue;
										}
									} else {
										c.remove(waitingPanel);
										c.add(menuPanel);
										musicPlayer.stop();
										revalidate();
										repaint();
										return;
									}
									continue;
								}
								connected = true;
							}

							try {
								DataInputStream in = new DataInputStream(socket.getInputStream());
								DataOutputStream out = new DataOutputStream(socket.getOutputStream());

								while (true) {
									byte choice = getChoice();
									out.writeByte(choice);

									String gameName;

									// create
									if (choice == GameServer.CREATING) {
										gameName = sendNameToServer(true, in, out);
										if (gameName == null) {
											continue;
										}

										int numPlayersSent = sendNumPlayersToServer(in, out);
										if (numPlayersSent == -1) {
											continue;
										}
										
										byte response = in.readByte();
										System.out.println("create");

										displayMessage("Sucessfully created game");
										break;
									} else if (choice == GameServer.JOINING) {  // join
										gameName = sendNameToServer(false, in, out);
										
										if (gameName == null) {
											continue;
										}

										byte serverAccepted = in.readByte();

										if (serverAccepted == GameServer.GAME_FULL) {
											displayError("Game full");
											continue;
										}

										displayMessage("Successfully joined game");
										break;
									} else {
										in.close();
										out.close();
										
										c.remove(waitingPanel);
										c.add(menuPanel);
										musicPlayer.stop();
										revalidate();
										repaint();
										return;
									}
								}
								musicPlayer.playCrickets();

								numPlayers = in.readInt();
								
								endPanel = new EndPanel(numPlayers);
								endPanel.setButtonListener(endListener);
								
								System.out.println(numPlayers);

								playerNumber = in.readInt();
								
								System.out.println(playerNumber);
								
								out.writeLong(dropInterval);
								
								out.writeBoolean(upcomingAssistance);
								
								upcomingAssistance = in.readBoolean();

								c.remove(waitingPanel);

								gamePanel = new GamePanel(in, out, playerNumber, musicPlayer, endPanel, drawGhosts, upcomingAssistance, numPlayers);

								setSize(gamePanel.getPreferredSize());
								setMinimumSize(gamePanel.getPreferredSize());
								
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
		
		optionsPanel.easyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dropInterval = EASY_INTERVAL;
			//	System.out.println("easy");
			}
		});
		
		optionsPanel.mediumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dropInterval = MEDIUM_INTERVAL;
			//	System.out.println("medium");
			}
		});
		
		optionsPanel.hardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dropInterval = HARD_INTERVAL;
			//	System.out.println("hard");
			}
		});
		
		optionsPanel.ghostYesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawGhosts = true;
			//	System.out.println("ghosts: " + drawGhosts);
			}
		});
		
		optionsPanel.ghostNoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawGhosts = false;
			//	System.out.println("ghosts: " + drawGhosts);
			}
		});
		
		optionsPanel.upcomingYesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upcomingAssistance = true;
				//System.out.println("upcoming: " + upcomingAssistance);
			}
		});
		
		optionsPanel.upcomingNoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upcomingAssistance = false;
				//System.out.println("upcoming: " + upcomingAssistance);
			}
		});
		
		optionsPanel.backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.add(menuPanel, BorderLayout.CENTER);
				c.remove(optionsPanel);
				revalidate();
				repaint();
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
						out.writeByte(Encoder.encodeKeyPress(KeyEvent.VK_UNDEFINED, playerNumber));
						if (gamePanel != null) {
							gamePanel.manager.toggleHasQuit();
						}
						out.close();
						socket.close();
					} catch (IOException e1) {
						System.out.println("Failed to notify server...");
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
