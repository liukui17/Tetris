package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HelpPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;

	public HelpPanel() {

		// Set Layout Manager
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;

		// Create & Add Swing Components
		JLabel title = new JLabel("Help");
		title.setFont(new Font("Dialog", Font.BOLD, 50));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(title, gbc);

		JLabel moveLeft = new JLabel("Move Left");
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(moveLeft, gbc);

		JLabel moveRight = new JLabel("Move Right");
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(moveRight, gbc);

		JLabel rotate = new JLabel("Rotate");
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(rotate, gbc);

		JLabel drop = new JLabel("Drop");
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(drop, gbc);

		JLabel leftKey = new JLabel("");
		Image imgLeftKey = new ImageIcon(this.getClass().getResource("/img/arrow-left.png")).getImage();
		leftKey.setIcon(new ImageIcon(imgLeftKey));
		gbc.gridx = 2;
		gbc.gridy = 1;
		add(leftKey, gbc);

		JLabel rightKey = new JLabel("");
		Image imgRightKey = new ImageIcon(this.getClass().getResource("/img/arrow-right.png")).getImage();
		rightKey.setIcon(new ImageIcon(imgRightKey));
		gbc.gridx = 2;
		gbc.gridy = 2;
		add(rightKey, gbc);

		JLabel upKey = new JLabel("");
		Image imgUpKey = new ImageIcon(this.getClass().getResource("/img/arrow-up.png")).getImage();
		upKey.setIcon(new ImageIcon(imgUpKey));
		gbc.gridx = 2;
		gbc.gridy = 3;
		add(upKey, gbc);

		JLabel spaceKey = new JLabel("");
		Image imgSpaceKey = new ImageIcon(this.getClass().getResource("/img/space.png")).getImage();
		spaceKey.setIcon(new ImageIcon(imgSpaceKey));
		gbc.gridx = 2;
		gbc.gridy = 4;
		add(spaceKey, gbc);

		JButton test = new JButton("Back");
		test.addActionListener(this);
		gbc.gridx = 1;
		gbc.gridy = 5;
		add(test, gbc);

		setBackground(Color.LIGHT_GRAY);
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
