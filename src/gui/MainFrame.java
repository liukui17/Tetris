package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JFrame;

// Controller
// All communication to/from components goes through here
public class MainFrame extends JFrame {
	private static final int WIDTH = 600;
	private static final int HEIGHT = 840;

	private MenuPanel menuPanel;
	private HelpPanel helpPanel;
	private GamePanel gamePanel;
	private OptionsPanel optionsPanel;
	private WaitingPanel waitingPanel;

	public MainFrame(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setVisible(true);

		// Set layout manager
		setLayout(new BorderLayout());

		// Create Swing component
		menuPanel = new MenuPanel();
		helpPanel = new HelpPanel();
		//gamePanel = new GamePanel();
		optionsPanel = new OptionsPanel();
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
				doLayout();
				System.out.println("after remove menu");

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
						
						try {
							Socket socket = new Socket("localhost", 3333);
							DataInputStream in = new DataInputStream(socket.getInputStream());
							DataOutputStream out = new DataOutputStream(socket.getOutputStream());

							// Should block here until server sends boolean
							boolean isPlayerOne = in.readBoolean();
							c.remove(waitingPanel);

							gamePanel = new GamePanel(in, out, isPlayerOne);
							Thread gameThread = new Thread(gamePanel);
							gameThread.start();
							c.add(gamePanel, BorderLayout.CENTER);
						} catch (Exception e) {
							e.printStackTrace();
						}

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
