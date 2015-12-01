package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WaitingPanel extends JPanel {
	public WaitingPanel() {

		// Set Layout Manager
		setLayout(new BorderLayout());

		// Create & Add Swing Components
		JLabel waiting = new JLabel("Waiting for other player");
		waiting.setFont(new Font("Dialog", Font.PLAIN, 40));
		waiting.setHorizontalAlignment(SwingConstants.CENTER);
		add(waiting, BorderLayout.CENTER);
	}
}
