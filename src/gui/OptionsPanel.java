package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class OptionsPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;
	private MusicPlayer musicPlayer;

	public OptionsPanel(MusicPlayer musicPlayer) {
		
		this.musicPlayer = musicPlayer;

		// Set Layout Manager
		setLayout(new BorderLayout());

		// Create & Add Swing Components
		JLabel title = new JLabel("Options");
		title.setFont(new Font("Dialog", Font.BOLD, 50));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(title, BorderLayout.NORTH);
		
		JButton back = new JButton("Back");
		back.addActionListener(this);
		//back.setBorder(new EmptyBorder(30, 30, 30, 30));
		back.setBorder(new EmptyBorder(10,10,10,10));
		//back.setPreferredSize(new Dimension(50, 50));
		add(back, BorderLayout.SOUTH);

		JPanel options = new JPanel();
		options.setBackground(Color.LIGHT_GRAY);
		add(options, BorderLayout.CENTER);
		
		// Set Layout Manager
		GridBagLayout layout = new GridBagLayout();
		options.setLayout(layout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		//gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 0, 0, 0);

		// Add the labels
		JLabel musicLabel = new JLabel("Music");
		gbc.gridx = 0;
		gbc.gridy = 0;
		options.add(musicLabel, gbc);
		
		JLabel filler = new JLabel("");
		gbc.gridx = 1;
		gbc.gridy = 0;
		options.add(filler, gbc);

		// Add the buttons
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		JButton musicStop = new JButton("Stop");
		musicStop.setFont(new Font("Dialog", Font.BOLD, 30));
		gbc.gridx = 2;
		gbc.gridy = 0;
		options.add(musicStop, gbc);
		musicStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.stop();
			}
		});
		
		JButton musicMinus = new JButton("-");
		musicMinus.setFont(new Font("Dialog", Font.BOLD, 30));
		gbc.gridx = 3;
		gbc.gridy = 0;
		options.add(musicMinus, gbc);
		musicMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(-2.0f);
			}
		});
		
		JButton musicPlus = new JButton("+");
		musicPlus.setFont(new Font("Dialog", Font.BOLD, 30));
		gbc.gridx = 4;
		gbc.gridy = 0;
		options.add(musicPlus, gbc);
		musicPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(2.0f);
			}
		});
		
		JLabel label2 = new JLabel("Option 2");
		gbc.gridx = 0;
		gbc.gridy = 1;
		options.add(label2, gbc);

		JLabel label3 = new JLabel("Option 3");
		gbc.gridx = 0;
		gbc.gridy = 2;
		options.add(label3, gbc);
		
//		JLabel leftKey = new JLabel("");
//		Image imgLeftKey = new ImageIcon(this.getClass().getResource("/img/arrow-left.png")).getImage();
//		leftKey.setIcon(new ImageIcon(imgLeftKey));
//		gbc.gridx = 2;
//		gbc.gridy = 1;
//		add(leftKey, gbc);

//		JLabel rightKey = new JLabel("");
//		Image imgRightKey = new ImageIcon(this.getClass().getResource("/img/arrow-right.png")).getImage();
//		rightKey.setIcon(new ImageIcon(imgRightKey));
//		gbc.gridx = 2;
//		gbc.gridy = 2;
//		add(rightKey, gbc);
//
//		JLabel upKey = new JLabel("");
//		Image imgUpKey = new ImageIcon(this.getClass().getResource("/img/arrow-up.png")).getImage();
//		upKey.setIcon(new ImageIcon(imgUpKey));
//		gbc.gridx = 2;
//		gbc.gridy = 3;
//		add(upKey, gbc);

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
