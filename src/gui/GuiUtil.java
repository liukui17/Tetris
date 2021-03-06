package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GuiUtil {
	
	public static JLabel addLabel(JPanel panel, String name, int fontSize) {
		JLabel label = new JLabel(name);
		label.setFont(new Font("Dialog", Font.PLAIN, fontSize));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(label);
		return label;
	}
	
	public static JButton addButton(JPanel panel, String name, int fontSize, List<JButton> list) {
		JButton but = new JButton(name);
		but.setFont(new Font("Dialog", Font.PLAIN, fontSize));
		but.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(but);
		list.add(but);
		return but;
	}
	
	public static void formatButtons(List<JButton> list) {
		int maxWidth = 0;
		int maxHeight = 0;
		
		for (JButton but : list) {
			maxWidth = Math.max((int)but.getPreferredSize().getWidth(), maxWidth);
			maxHeight = Math.max((int)but.getPreferredSize().getHeight(), maxHeight);
		}
		
		Dimension dim = new Dimension(maxWidth, maxHeight);
		
		for (JButton but : list) {
			but.setMaximumSize(dim);
			but.setMinimumSize(dim);
			but.setPreferredSize(dim);
		}
	}
}
