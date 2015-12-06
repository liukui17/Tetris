package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WaitingPanel extends JPanel implements ComponentListener {
	private MusicPlayer musicPlayer;
	
	public WaitingPanel(MusicPlayer musicPlayer) {
		
		this.musicPlayer = musicPlayer;

		// Set Layout Manager
		setLayout(new BorderLayout());

		// Create & Add Swing Components
		JLabel waiting = new JLabel("Waiting for other player");
		waiting.setFont(new Font("Dialog", Font.PLAIN, 40));
		waiting.setHorizontalAlignment(SwingConstants.CENTER);
		this.addComponentListener(this);
		add(waiting, BorderLayout.CENTER);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		musicPlayer.playCrickets();
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		musicPlayer.playCrickets();
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		musicPlayer.playCrickets();
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		System.out.println("HIDDEN");
		musicPlayer.stop();
	}
}
