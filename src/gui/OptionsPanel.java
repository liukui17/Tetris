import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsPanel extends JPanel implements ActionListener {
	private ButtonListener buttonListener;

	public OptionsPanel() {

		// Set Layout Manager
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;

		// Create & Add Swing Components
		JLabel title = new JLabel("Options");
		title.setFont(new Font("Dialog", Font.BOLD, 50));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(title, gbc);

		JLabel label1 = new JLabel("Option 1");
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(label1, gbc);

		JLabel label2 = new JLabel("Option 2");
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(label2, gbc);

		JLabel label3 = new JLabel("Option 3");
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(label3, gbc);

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

		JButton back = new JButton("Back");
		back.addActionListener(this);
		gbc.gridx = 1;
		gbc.gridy = 5;
		add(back, gbc);

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
