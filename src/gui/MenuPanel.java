import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MenuPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;

	public MenuPanel() {

		// Set Layout Manager
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;

		// Create & Add Swing Components
		JLabel title = new JLabel("Tetris");
		title.setFont(new Font("Dialog", Font.BOLD, 50));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(20, 20, 20, 20);
		add(title, gbc);

		JButton options = new JButton("Options");
		options.addActionListener(this);
		options.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(options, gbc);

		JButton help = new JButton("Help");
		help.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(help, gbc);

		JButton start = new JButton("Start");
		start.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(start, gbc);

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
