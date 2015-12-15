package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

public class EndPanel extends TemplatePanel implements ActionListener {
	private ButtonListener buttonListener;
	JButton backButton;

	JLabel[] scoreLabels;
	
	public EndPanel(int numPlayers) {
		super("Game Over");
		
		body.add(Box.createVerticalGlue());
		
		scoreLabels = new JLabel[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			scoreLabels[i] = GuiUtil.addLabel(body, "", 30);
			body.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		
		body.add(Box.createVerticalGlue());
		
		backButton = GuiUtil.addButton(body, "Back to Menu", 30, buttonList);
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
	
	public void setScores(JLabel[] scoreLabels) {
		for (int i = 0; i < scoreLabels.length; i++) {
			this.scoreLabels[i].setText("P" + i + " Score: " + scoreLabels[i].getText());
		}
	}
}
