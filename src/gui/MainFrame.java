import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Controller
// All communication to/from components goes through here
public class MainFrame extends JFrame {
	private static final int WIDTH = 600;
	private static final int HEIGHT = 840;

	private MenuPanel menuPanel;
	private HelpPanel helpPanel;
	private GamePanel gamePanel;
	private OptionsPanel optionsPanel;

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
		gamePanel = new GamePanel();
		optionsPanel = new OptionsPanel();

		Thread gameThread = new Thread(gamePanel);
		gameThread.start();

		// Add Swing components to content pane
		Container c = getContentPane();
		c.add(menuPanel, BorderLayout.CENTER);

		// Set Listeners
		menuPanel.setButtonListener(new ButtonListener() {
			public void buttonClicked(String s) {
				switch (s) {
					case "Options":
						c.add(optionsPanel, BorderLayout.CENTER);
						break;
					case "Help":
						c.add(helpPanel, BorderLayout.CENTER);
						break;
					case "Start":
						// TO DO
						// Should load intermediate loading screen until player 2 has connected
						// SwingWorker?
						// low priority
						c.add(gamePanel, BorderLayout.CENTER);
						break;
				}

				c.remove(menuPanel);
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

		// Keyboard Dispatcher
		// Instead of printing, need to send input to network
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					int key = e.getKeyCode();

					switch (key) {
						case KeyEvent.VK_LEFT:
							System.out.println("Left");
							break;
						case KeyEvent.VK_RIGHT:
							System.out.println("Right");
							break;
						case KeyEvent.VK_UP:
							System.out.println("Up");
							break;
						case KeyEvent.VK_DOWN:
							System.out.println("Down");
							break;
						case KeyEvent.VK_SPACE:
							System.out.println("Space");
							break;
					}

				}
				return false;
			}
		});

		// Remove this stuff later
		Color[][] grid = new Color[10][20];
		int x1, y1, x2, y2, x3, y3;
		grid[x1 = 0][0] = Color.CYAN;
		grid[0][1] = Color.CYAN;
		grid[0][2] = Color.CYAN;
		grid[1][2] = Color.CYAN;

		grid[0][18] = Color.GREEN;
		grid[0][17] = Color.GREEN;
		grid[1][18] = Color.GREEN;
		grid[1][17] = Color.GREEN;

		grid[2][15] = Color.MAGENTA;
		grid[2][16] = Color.MAGENTA;
		grid[1][16] = Color.MAGENTA;
		grid[0][16] = Color.MAGENTA;
		grid[9][19] = Color.RED;
		grid[8][19] = Color.RED;
		grid[7][19] = Color.BLUE;
		grid[6][19] = Color.BLUE;
		grid[2][19] = Color.YELLOW;
		grid[1][19] = Color.YELLOW;
		gamePanel.updateGrid(grid);
	}

	// Updates grid
	public void updateGrid(Color[][] grid) {
		gamePanel.updateGrid(grid);
	}


}
