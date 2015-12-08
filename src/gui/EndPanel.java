package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

public class EndPanel extends TemplatePanel implements ActionListener {
	private ButtonListener buttonListener;
	
	private JLabel p1ScoreLabel;
	private JLabel p2ScoreLabel;
	
	public EndPanel() {
		super("Game Over");
		
		body.add(Box.createVerticalGlue());

		p1ScoreLabel = GuiUtil.addLabel(body, "", 30);
		
		body.add(Box.createRigidArea(new Dimension(0, 10)));
		
		p2ScoreLabel = GuiUtil.addLabel(body, "", 30);
		
		body.add(Box.createVerticalGlue());
		
		JButton backButton = GuiUtil.addButton(body, "Back to Menu", 30, buttonList);
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
