import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GamePanel extends JPanel {
	private BoardPanel boardPanel;
	private ButtonListener buttonListener;

	public GamePanel() {

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		// Create & Add Swing Components
		Dimension minSize = new Dimension(100, 20);
		Dimension prefSize = new Dimension(100, 20);
		Dimension maxSize = new Dimension(100, 20);

		// Placeholder
		add(new Box.Filler(minSize, prefSize, maxSize));

		boardPanel = new BoardPanel();
		add(boardPanel);

		// Placeholder
		add(new Box.Filler(minSize, prefSize, maxSize));

		setBackground(Color.LIGHT_GRAY);
	}

	public void updateGrid(Color[][] grid) {
		boardPanel.updateGrid(grid);
		boardPanel.revalidate();
		boardPanel.repaint();
	}
}
