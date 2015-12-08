package gui;

import javax.swing.Box;
import javax.swing.JLabel;

public class WaitingPanel extends TemplatePanel {
	
	public WaitingPanel() {
		super("Waiting");

		body.add(Box.createVerticalGlue());
		GuiUtil.addLabel(body, "Waiting for other player", 40);
		body.add(Box.createVerticalGlue());
	}
}
