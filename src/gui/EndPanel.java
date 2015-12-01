package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndPanel extends JPanel {
	public EndPanel() {

		// Set Layout Manager
		setLayout(new BorderLayout());

		// Create & Add Swing Components
		JLabel gameOver = new JLabel("gameOver");
		gameOver.setFont(new Font("Dialog", Font.PLAIN, 40));
		gameOver.setHorizontalAlignment(SwingConstants.CENTER);
		add(gameOver, BorderLayout.CENTER);
	}
}
