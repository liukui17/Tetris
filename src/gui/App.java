import javax.swing.JFrame;
import javax.swing.*;

public class App {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				MainFrame mainFrame = new MainFrame("Tetris");
			}

		});

	}
}
