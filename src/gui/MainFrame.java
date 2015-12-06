package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
	private static final int WIDTH = 680;
	private static final int HEIGHT = 780;

	private MenuPanel menuPanel;
	private HelpPanel helpPanel;
	private GamePanel gamePanel;
	private OptionsPanel optionsPanel;
	private WaitingPanel waitingPanel;
	
	private MusicPlayer musicPlayer;

	public MainFrame(String title, String hostName, int portNum) {		
		super(title);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);
		
		musicPlayer = new MusicPlayer();

		// Set layout manager
		setLayout(new BorderLayout());

		// Create Swing component
		menuPanel = new MenuPanel();
		helpPanel = new HelpPanel();
		//gamePanel = new GamePanel();
		optionsPanel = new OptionsPanel(musicPlayer);
		waitingPanel = new WaitingPanel();


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
					revalidate();
					repaint();

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							Socket socket = null;
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
								boolean isPlayerOne = in.readBoolean();

								c.remove(waitingPanel);

								gamePanel = new GamePanel(in, out, isPlayerOne, musicPlayer);
								Thread gameThread = new Thread(gamePanel);
//								gameThread.start();
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
				}

				c.remove(helpPanel);
				revalidate();
				repaint();
			}
		});

		optionsPanel.setButtonListener(new ButtonListener() {
			public void buttonClicked(String s) {
				if (s.equals("Back")) {
					c.add(menuPanel, BorderLayout.CENTER);
				}

				c.remove(optionsPanel);
				revalidate();
				repaint();
			}
		});

	}

}
