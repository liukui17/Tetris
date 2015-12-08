package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

public class EndPanel extends TemplatePanel implements ActionListener {
	private ButtonListener buttonListener;
	
	private JLabel p1ScoreLabel;
	private JLabel p2ScoreLabel;
	
	public EndPanel() {
		super("Game Over");
		
		body.add(Box.createVerticalGlue());

		p1ScoreLabel = GuiUtil.addLabel(body, "", 30);
		
		// Filler
		body.add(Box.createRigidArea(new Dimension(0, 10)));
		
		p2ScoreLabel = GuiUtil.addLabel(body, "", 30);
		
		body.add(Box.createVerticalGlue());
		
		JButton backButton = GuiUtil.addButton(body, "Back to Menu", 30);
		backButton.addActionListener(this);
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
