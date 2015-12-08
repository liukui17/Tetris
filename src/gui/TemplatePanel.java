package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TemplatePanel extends JPanel {
	
	protected JPanel body;
	protected List<JButton> buttonList;
	
	public TemplatePanel(String title) {
		setLayout(new BorderLayout());
		setBackground(Color.LIGHT_GRAY);

		addTitle(this, title);
		body = GuiUtil.addBody(this);
		buttonList = new ArrayList<JButton>();
	}
	
	private void addTitle(JPanel panel, String title) {
		JLabel label = new JLabel(title);
		label.setFont(new Font("Dialog", Font.BOLD, 50));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
		panel.add(label, BorderLayout.NORTH);
	}

}
