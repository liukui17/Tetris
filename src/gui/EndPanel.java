package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;
	
	public EndPanel() {

		// Set Layout Manager
		setLayout(new BorderLayout());

		// Create & Add Swing Components
		JLabel gameOver = new JLabel("gameOver");
		gameOver.setFont(new Font("Dialog", Font.PLAIN, 40));
		gameOver.setHorizontalAlignment(SwingConstants.CENTER);
		add(gameOver, BorderLayout.CENTER);
		
		JButton back = new JButton("Back to Menu");
		back.setFont(new Font("Dialog", Font.PLAIN, 20));
		back.setHorizontalAlignment(SwingConstants.CENTER);
		back.addActionListener(this);
		add(back, BorderLayout.SOUTH);
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
}
