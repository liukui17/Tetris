package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionsPanel extends TemplatePanel implements ActionListener {
	
	private ButtonListener buttonListener;

	public OptionsPanel(MusicPlayer musicPlayer) {
		super("Options");

		// Option 1 Panel
		JPanel option1 = new JPanel();
		option1.setBackground(Color.LIGHT_GRAY);
		option1.setLayout(new BoxLayout(option1, BoxLayout.X_AXIS));
		body.add(option1);

		// Add the labels
		GuiUtil.addLabel(option1, "Music: ", 30);

		option1.add(Box.createHorizontalGlue());

		// Add the buttons
		GuiUtil.addButton(option1, "Stop", 30, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.stop();
			}
		});

		option1.add(Box.createRigidArea(new Dimension(10, 0)));

		GuiUtil.addButton(option1, "-", 30, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(-4.0f);
			}
		});

		option1.add(Box.createRigidArea(new Dimension(10, 0)));

		GuiUtil.addButton(option1, "+", 30, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(4.0f);
			}
		});
		
		// Filler
		body.add(Box.createRigidArea(new Dimension(0, 50)));

		// Option 2 Panel
		JPanel option2 = new JPanel();
		option2.setBackground(Color.LIGHT_GRAY);
		option2.setLayout(new BoxLayout(option2, BoxLayout.X_AXIS));
		body.add(option2);

		// Add the labels
		GuiUtil.addLabel(option2, "Ghost Pieces: ", 30);

		option2.add(Box.createHorizontalGlue());

		// Add the buttons
		JButton ghostNoBtn = GuiUtil.addButton(option2, "No", 30, buttonList);
		ghostNoBtn.addActionListener(this);
		
		option2.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton ghostYesBtn = GuiUtil.addButton(option2, "Yes", 30, buttonList);
		ghostYesBtn.addActionListener(this);
		
		// Border the last clicked button
		ghostNoBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				ghostYesBtn.setBorder(null);
				ghostNoBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			}
		});
		
		ghostYesBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				ghostNoBtn.setBorder(null);
				ghostYesBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			}
		});
		
		// Filler
		body.add(Box.createRigidArea(new Dimension(0, 50)));

		// Option 3 Panel
		JPanel option3 = new JPanel();
		option3.setBackground(Color.LIGHT_GRAY);
		option3.setLayout(new BoxLayout(option3, BoxLayout.X_AXIS));
		body.add(option3);

		// Add the labels
		GuiUtil.addLabel(option3, "Label 3: ", 30);

		option3.add(Box.createHorizontalGlue());

		// Add the buttons
		GuiUtil.addButton(option3, "But 1", 30, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do something
			}
		});
		
		option3.add(Box.createRigidArea(new Dimension(10, 0)));
		
		GuiUtil.addButton(option3, "But 2", 30, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do something
			}
		});
		
		body.add(Box.createVerticalGlue());
		
		JButton backButton = GuiUtil.addButton(body, "Back", 30, buttonList);
		backButton.addActionListener(this);
		
		GuiUtil.formatButtons(buttonList);

	}
	
	//private Dimension find

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
