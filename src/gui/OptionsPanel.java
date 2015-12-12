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
		GuiUtil.addLabel(option1, "Volume: ", 30);

		option1.add(Box.createHorizontalGlue());

		// Add the buttons
		GuiUtil.addButton(option1, "Stop", 25, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.stop();
			}
		});

		option1.add(Box.createRigidArea(new Dimension(10, 0)));

		GuiUtil.addButton(option1, "-", 25, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(-4.0f);
			}
		});

		option1.add(Box.createRigidArea(new Dimension(10, 0)));

		GuiUtil.addButton(option1, "+", 25, buttonList).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicPlayer.start();
				musicPlayer.adjustVolume(4.0f);
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
		GuiUtil.addLabel(option3, "Difficulty:", 30);

		option3.add(Box.createHorizontalGlue());

		// Add the buttons
		JButton easyButton = GuiUtil.addButton(option3, "Easy", 25, buttonList);
		easyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		easyButton.addActionListener(this);
		
		option3.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton mediumButton = GuiUtil.addButton(option3, "Medium", 25, buttonList);
		mediumButton.addActionListener(this);
		
		option3.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton hardButton = GuiUtil.addButton(option3, "Hard", 25, buttonList);
		hardButton.addActionListener(this);
		
		easyButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mediumButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				hardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				easyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			}
		});
		
		mediumButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				easyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				hardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				mediumButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			}
		});
		
		hardButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				easyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				mediumButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				hardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
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
		GuiUtil.addLabel(option2, "Ghost Pieces:", 30);

		option2.add(Box.createHorizontalGlue());

		// Add the buttons
		JButton ghostYesBtn = GuiUtil.addButton(option2, "Yes", 25, buttonList);
		ghostYesBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		ghostYesBtn.addActionListener(this);

		option2.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton ghostNoBtn = GuiUtil.addButton(option2, "No", 25, buttonList);
		ghostNoBtn.addActionListener(this);
		
		// Border the last clicked button
		ghostNoBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				ghostYesBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				ghostNoBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			}
		});
		
		ghostYesBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				ghostNoBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				ghostYesBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			}
		});
		
		// filler
		body.add(Box.createRigidArea(new Dimension(0, 50)));
		
		JPanel option4 = new JPanel();
		option4.setBackground(Color.LIGHT_GRAY);
		option4.setLayout(new BoxLayout(option4, BoxLayout.X_AXIS));
		body.add(option4);
		
		GuiUtil.addLabel(option4, "Show Upcoming:", 30);
		option4.add(Box.createHorizontalGlue());
		JButton upcomingYesButton = GuiUtil.addButton(option4, "Yes", 25, buttonList);
		upcomingYesButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
		upcomingYesButton.addActionListener(this);
		
		option4.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton upcomingNoButton = GuiUtil.addButton(option4, "No", 25, buttonList);
		upcomingNoButton.addActionListener(this);
		
		upcomingYesButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				upcomingNoButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				upcomingYesButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
				
			}
		});
		
		upcomingNoButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				upcomingYesButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				upcomingNoButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
			}
		});
		
		//---------------------------------
		
		body.add(Box.createVerticalGlue());
		
		JButton backButton = GuiUtil.addButton(body, "Back", 25, buttonList);
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
