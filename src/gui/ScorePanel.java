package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ScorePanel extends JPanel {
	private JLabel scoreLabel;
	private JLabel score;
	public ScorePanel() {

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(100, 100));
		setMaximumSize(new Dimension(100, 840));

		// Create & Add Swing Components
		scoreLabel = new JLabel("Score\n");
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(scoreLabel);
		
		score = new JLabel("0");
		add(score);
		

		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(100, 600));
		filler.setBackground(Color.LIGHT_GRAY);
		add(filler);

		// Filler
//		Dimension minSize = new Dimension(100, 500);
//		Dimension prefSize = new Dimension(100, 500);
//		Dimension maxSize = new Dimension(100, 500);
//		add(new Box.Filler(minSize, prefSize, maxSize));
	}
	
	public void updateScore(int score) {
		this.score.setText(Integer.toString(score));
	}
}
