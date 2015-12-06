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

public class OptionsPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;
	private MusicPlayer musicPlayer;

	private static final Dimension BUTTON_DIM = new Dimension(150, 0);

	public OptionsPanel(MusicPlayer musicPlayer) {

		this.musicPlayer = musicPlayer;

		// Set Layout Manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(30, 30, 30, 30)));

		// Create & Add Swing Components
		JLabel title = new JLabel("Options");
		title.setFont(new Font("Dialog", Font.BOLD, 50));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title);

		// Filler
		add(Box.createRigidArea(new Dimension(0, 100)));

		// Option 1
		JPanel option1 = new JPanel();
		option1.setBackground(Color.LIGHT_GRAY);
		option1.setLayout(new BoxLayout(option1, BoxLayout.X_AXIS));
		option1.setPreferredSize(new Dimension(0, 100));
		add(option1);

		// Add the labels
		JLabel label1 = new JLabel("Music: ");
		label1.setFont(new Font("Dialog", Font.PLAIN, 40));
		option1.add(label1);

		option1.add(Box.createHorizontalGlue());

		// Add the buttons
		JButton musicStop = new JButton("Stop");
		musicStop.setFont(new Font("Dialog", Font.BOLD, 30));
		musicStop.setPreferredSize(BUTTON_DIM);
		musicStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.stop();
			}
		});
		option1.add(musicStop);

		option1.add(Box.createRigidArea(new Dimension(10, 0)));

		JButton musicMinus = new JButton("-");
		musicMinus.setFont(new Font("Dialog", Font.BOLD, 30));
		musicMinus.setPreferredSize(BUTTON_DIM);
		musicMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(-2.0f);
			}
		});
		option1.add(musicMinus);

		option1.add(Box.createRigidArea(new Dimension(10, 0)));

		JButton musicPlus = new JButton("+");
		musicPlus.setFont(new Font("Dialog", Font.BOLD, 30));
		musicPlus.setPreferredSize(BUTTON_DIM);
		musicPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(2.0f);
			}
		});
		option1.add(musicPlus);
		
		// Filler
		add(Box.createVerticalGlue());

		// Option 2
		JPanel option2 = new JPanel();
		option2.setBackground(Color.LIGHT_GRAY);
		option2.setLayout(new BoxLayout(option2, BoxLayout.X_AXIS));
		option2.setPreferredSize(new Dimension(0, 100));
		add(option2);

		// Add the labels
		JLabel label2 = new JLabel("Label 2: ");
		label2.setFont(new Font("Dialog", Font.PLAIN, 40));
		option2.add(label2);

		option2.add(Box.createHorizontalGlue());

		// Add the buttons
		JButton opt2but1 = new JButton("but 1");
		opt2but1.setFont(new Font("Dialog", Font.BOLD, 30));
		opt2but1.setPreferredSize(BUTTON_DIM);
		opt2but1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do something
			}
		});
		option2.add(opt2but1);
		
		option2.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton opt2but2 = new JButton("but 2");
		opt2but2.setFont(new Font("Dialog", Font.BOLD, 30));
		opt2but2.setPreferredSize(BUTTON_DIM);
		opt2but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do something
			}
		});
		option2.add(opt2but2);
		
		// Filler
		add(Box.createVerticalGlue());

		// Option 3
		JPanel option3 = new JPanel();
		option3.setBackground(Color.LIGHT_GRAY);
		option3.setLayout(new BoxLayout(option3, BoxLayout.X_AXIS));
		option3.setPreferredSize(new Dimension(0, 100));
		add(option3);

		// Add the labels
		JLabel label3 = new JLabel("Label 3: ");
		label3.setFont(new Font("Dialog", Font.PLAIN, 40));
		option3.add(label3);

		option3.add(Box.createHorizontalGlue());

		// Add the buttons
		JButton opt3but1 = new JButton("but 1");
		opt3but1.setFont(new Font("Dialog", Font.BOLD, 30));
		opt3but1.setPreferredSize(BUTTON_DIM);
		opt3but1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do something
			}
		});
		option3.add(opt3but1);
		
		option3.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton opt3but2 = new JButton("but 2");
		opt3but2.setFont(new Font("Dialog", Font.BOLD, 30));
		opt3but2.setPreferredSize(BUTTON_DIM);
		opt3but2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do something
			}
		});
		option3.add(opt3but2);
		
		// Filler
		add(Box.createRigidArea(new Dimension(0, 100)));
		
		// Add the back button
		JButton back = new JButton("Back");
		back.setFont(new Font("Dialog", Font.PLAIN, 40));
		back.addActionListener(this);
		back.setHorizontalAlignment(SwingConstants.CENTER);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(back);

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
