package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class EndPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;
	
	JLabel p1ScoreLabel;
	JLabel p2ScoreLabel;
	
	public EndPanel() {

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(30, 30, 30, 30)));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setBackground(Color.LIGHT_GRAY);

		// Create & Add Swing Components
		JLabel gameOver = new JLabel("Game Over");
		gameOver.setFont(new Font("Dialog", Font.BOLD, 40));
		gameOver.setHorizontalAlignment(SwingConstants.CENTER);
		gameOver.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(gameOver);
		
		add(Box.createVerticalGlue());
		
		p1ScoreLabel = new JLabel("P1 Score: 0");
		p1ScoreLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		p1ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		p1ScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(p1ScoreLabel);
		
		p2ScoreLabel = new JLabel("P2 Score: 0");
		p2ScoreLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		p2ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		p2ScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(p2ScoreLabel);
		
		add(Box.createVerticalGlue());
		
		JButton back = new JButton("Back to Menu");
		back.setFont(new Font("Dialog", Font.PLAIN, 20));
		back.setHorizontalAlignment(SwingConstants.CENTER);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		back.addActionListener(this);
		add(back);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (buttonListener != null) {
			buttonListener.buttonClicked(e.getActionCommand());
		}
	}

	public void setButtonListener(ButtonListener listener) {
		this.buttonListener = listener;
	}

	/*
	 * Sets the score
	 */
	public void setScore(int player, int score) {
		if (player == 0) {
			p1ScoreLabel.setText("P1 Score: " + score);
		} else {
			p2ScoreLabel.setText("P2 Score: " + score);
		}
	}
	
	
}
