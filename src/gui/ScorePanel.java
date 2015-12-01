package gui;

import java.awt.Dimension;

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

		// Create & Add Swing Components
		scoreLabel = new JLabel("Score");
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(scoreLabel);
		
		score = new JLabel("0");
		add(score);
	}
	
	public void updateScore(int score) {
		this.score.setText(Integer.toString(score));
	}
}
