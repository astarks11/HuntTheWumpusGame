package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.HuntTheWumpusGame;

public class Controls extends JPanel {

	private HuntTheWumpusGame game; // hunt the wumpus game


	// constructor
	public Controls(HuntTheWumpusGame game, int width, int height)
	{
		this.game = game;
		this.setSize(width,height);
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		
		JTextArea field = new JTextArea("Controls:\n\nMovement: \tarrow keys\nShoot Arrow: \tspace + arrow keys");
		field.setSize(width,height);
		field.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		field.setLocation(100, 100);
		this.add(field);
		field.setVisible(true);

	}
	
}
