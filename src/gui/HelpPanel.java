package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class HelpPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;

	public HelpPanel() {

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(30, 30, 30, 30)));
		setBackground(Color.LIGHT_GRAY);
		
		// Create & Add Swing Components
		JLabel title = new JLabel("Help");
		title.setFont(new Font("Dialog", Font.BOLD, 50));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);
		
		// Filler
		add(Box.createRigidArea(new Dimension(0, 100)));
		
		JPanel body = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		body.setLayout(layout);
		body.setBackground(Color.LIGHT_GRAY);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		gbc.anchor = GridBagConstraints.EAST;
		gbc.ipadx = 30;
		
		JLabel leftKey = new JLabel("");
		Image imgLeftKey = new ImageIcon(this.getClass().getResource("/img/arrow-left.png")).getImage();
		leftKey.setIcon(new ImageIcon(imgLeftKey));
		gbc.gridx = 0;
		gbc.gridy = 0;
		body.add(leftKey, gbc);

		JLabel rightKey = new JLabel("");
		Image imgRightKey = new ImageIcon(this.getClass().getResource("/img/arrow-right.png")).getImage();
		rightKey.setIcon(new ImageIcon(imgRightKey));
		gbc.gridx = 0;
		gbc.gridy = 1;
		body.add(rightKey, gbc);
		
		JLabel downKey = new JLabel("");
		Image imgDownKey = new ImageIcon(this.getClass().getResource("/img/arrow-down.png")).getImage();
		downKey.setIcon(new ImageIcon(imgDownKey));
		gbc.gridx = 0;
		gbc.gridy = 2;
		body.add(downKey, gbc);
		
		JLabel upKey = new JLabel("");
		Image imgUpKey = new ImageIcon(this.getClass().getResource("/img/arrow-up.png")).getImage();
		upKey.setIcon(new ImageIcon(imgUpKey));
		gbc.gridx = 0;
		gbc.gridy = 3;
		body.add(upKey, gbc);

		JLabel spaceKey = new JLabel("");
		Image imgSpaceKey = new ImageIcon(this.getClass().getResource("/img/space.png")).getImage();
		spaceKey.setIcon(new ImageIcon(imgSpaceKey));
		gbc.gridx = 0;
		gbc.gridy = 4;
		body.add(spaceKey, gbc);
		
		gbc.anchor = GridBagConstraints.WEST;

		JLabel moveLeft = new JLabel("Move Left");
		moveLeft.setFont(new Font("Dialog", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 0;
		body.add(moveLeft, gbc);

		JLabel moveRight = new JLabel("Move Right");
		moveRight.setFont(new Font("Dialog", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 1;
		body.add(moveRight, gbc);
		
		JLabel moveDown = new JLabel("Move Down");
		moveDown.setFont(new Font("Dialog", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 2;
		body.add(moveDown, gbc);

		JLabel rotate = new JLabel("Rotate");
		rotate.setFont(new Font("Dialog", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 3;
		body.add(rotate, gbc);

		JLabel drop = new JLabel("Drop");
		drop.setFont(new Font("Dialog", Font.PLAIN, 20));
		gbc.gridx = 1;
		gbc.gridy = 4;
		body.add(drop, gbc);

		
		//	Column 2
		
		gbc.anchor = GridBagConstraints.EAST;
		
		JLabel mKey = new JLabel("");
		Image imgMKey = new ImageIcon(this.getClass().getResource("/img/mkey.png")).getImage();
		mKey.setIcon(new ImageIcon(imgMKey));
		gbc.gridx = 2;
		gbc.gridy = 0;
		body.add(mKey, gbc);

//		JLabel rightKey2 = new JLabel("");
//		//Image imgRightKey = new ImageIcon(this.getClass().getResource("/img/arrow-right.png")).getImage();
//		rightKey2.setIcon(new ImageIcon(imgRightKey));
//		gbc.gridx = 2;
//		gbc.gridy = 1;
//		body.add(rightKey2, gbc);
		
		gbc.anchor = GridBagConstraints.WEST;
		
		JLabel mute = new JLabel("Toggle Music");
		mute.setFont(new Font("Dialog", Font.PLAIN, 20));
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		body.add(mute, gbc);

//		JLabel label = new JLabel("label");
//		label.setFont(new Font("Dialog", Font.PLAIN, 20));
//		gbc.gridx = 3;
//		gbc.gridy = 1;
//		body.add(label, gbc);

		//////////////////////////////////
		
		add(body);
		
		// Filler
		add(Box.createRigidArea(new Dimension(0, 100)));
		
		JButton back = new JButton("Back");
		back.setFont(new Font("Dialog", Font.PLAIN, 40));
		back.setHorizontalAlignment(SwingConstants.CENTER);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		back.addActionListener(this);
		add(back);

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
