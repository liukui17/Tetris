package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.KeyStroke;

public class MenuPanel extends TemplatePanel implements ActionListener {
	private ButtonListener buttonListener;

	public MenuPanel() {
		super("Tetris");
		
		body.add(Box.createVerticalGlue());

		JButton optionsButton = GuiUtil.addButton(body, "Options", 30, buttonList);
		optionsButton.addActionListener(this);
		optionsButton.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		
		body.add(Box.createRigidArea(new Dimension(0, 50)));

		JButton helpButton = GuiUtil.addButton(body, "Help", 30, buttonList);
		helpButton.addActionListener(this);
		
		body.add(Box.createRigidArea(new Dimension(0, 50)));

		JButton startButton = GuiUtil.addButton(body, "Start", 30, buttonList);
		startButton.addActionListener(this);

		GuiUtil.formatButtons(buttonList);
	}

	public void actionPerformed(ActionEvent e) {
		if (buttonListener != null) {
			buttonListener.buttonClicked(e.getActionCommand());
		}
	}

	public void setButtonListener(ButtonListener listener) {
		this.buttonListener = listener;
	}
}
