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
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MenuPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;

	public MenuPanel() {

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(30, 30, 30, 30)));
		setBackground(Color.LIGHT_GRAY);

		// Create & Add Swing Components
		JLabel title = new JLabel("Tetris");
		title.setFont(new Font("Dialog", Font.BOLD, 50));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);
		
		// Filler
		add(Box.createRigidArea(new Dimension(0, 200)));
		add(Box.createVerticalGlue());

		JButton options = new JButton("Options");
		options.setFont(new Font("Dialog", Font.PLAIN, 40));
		options.setAlignmentX(Component.CENTER_ALIGNMENT);
		options.addActionListener(this);
		options.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		
		add(options);
		
		// Filler
		add(Box.createRigidArea(new Dimension(0, 50)));

		JButton help = new JButton("Help");
		help.setFont(new Font("Dialog", Font.PLAIN, 40));
		help.setAlignmentX(Component.CENTER_ALIGNMENT);
		help.addActionListener(this);
		add(help);
		
		// Filler
		add(Box.createRigidArea(new Dimension(0, 50)));

		JButton start = new JButton("Start");
		start.setFont(new Font("Dialog", Font.PLAIN, 40));
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.addActionListener(this);
		add(start);

		int maxWidth = Math.max(Math.max((int)options.getPreferredSize().getWidth(), (int)help.getPreferredSize().getWidth()), (int)start.getPreferredSize().getWidth());
		Dimension buttonDim = new Dimension(maxWidth, 0);
		options.setMaximumSize(buttonDim);
		help.setMaximumSize(buttonDim);
		start.setMaximumSize(buttonDim);
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
