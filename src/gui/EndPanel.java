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
	
	JLabel scoreLabel;
	
	public EndPanel() {

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(30, 30, 30, 30)));
		setBackground(Color.LIGHT_GRAY);

		// Create & Add Swing Components
		JLabel gameOver = new JLabel("Game Over");
		gameOver.setFont(new Font("Dialog", Font.BOLD, 40));
		gameOver.setHorizontalAlignment(SwingConstants.CENTER);
		gameOver.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(gameOver);
		
		add(Box.createVerticalGlue());
		
		scoreLabel = new JLabel("Score: 0");
		scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(scoreLabel);
		
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
	public void setScore(int score) {
		scoreLabel.setText("score: " + score);
	}
}
