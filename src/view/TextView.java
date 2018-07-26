package view;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.Move;
import model.HuntTheWumpusGame;
/*------------------------------------------------------
 * Class: TextView
 * Author: Alex Starks
 * 
 * Purpose: This class is used by the HuntTheWumpusGame.java and
 * contains the code needed to create the JPanel text view of the game.
 * The class must extend JPanel and also fit the height and width
 * passed through the parameters. 
 * -----------------------------------------------------*/
public class TextView extends JPanel implements Observer, KeyListener {
	
	private HuntTheWumpusGame game; // the game object
	private int width, height; // the board width and height
	private int size; // size of game board
	private final int HEIGHTBUFFER = 50; // buffer for height around frame
	private final int WIDTHBUFFER = 50; // bugger for width around frame
	private JTextField[][] spots; // game move locations
	private Font myFont = new Font("Arial", Font.TRUETYPE_FONT, 19); // own font
	private Font notifyFont = new Font("Arial", Font.TRUETYPE_FONT, 20); // own font
	private final String EMPTY = "[    ]"; // empty string
	private JTextArea notification; // notification box
	private final String GAMEOVERMESSAGE1 = "WUMPUS GOT YOU!"; // loss by wumpus 
	private final String GAMEOVERMESSAGE2 = "You fell into a SLIME PIT!"; // loss by slime pit
	private final String GAMEOVERMESSAGE3 = "You got the WUMPUS!"; // game win
	private final String GAMEOVERMESSAGE4 = "Your arrow bouned off a rock and hit you!"; // arrowcide
	private final String PLAYERONBLOOD = "You notice BLOOD on your shoe"; // player on blood message
	private final String PLAYERONSLIME = "You notice SLIME on your shoe"; // player on slime message
	private final String PLAYERONGOOP = "You notice GOOP on your shoe"; // player on goop message
	private boolean gameOver = false; // true if game is over
	private boolean lossByArrow = false; // flag for loss by arrow
	private boolean GODMODE; // turned on and will not loose
	private boolean arrowFired = false; // flag for arrowfired
    private final Set<Integer> pressed = new HashSet<Integer>(); // hashset to allow multiple key listeners

	public TextView(HuntTheWumpusGame game, int width, int height)
	{
		this.game = game;
		this.width = width;
		this.height = height;
		this.size = game.getSize();
		initializeView();
		GODMODE = game.isGodMode();
	}
	
	
	/*--------------------------------------
	 * Method: initializeView()
	 * Purpose: This class is used to initialize
	 * the view of the game board when the game is 
	 * first started. This method uses a JPanl and series
	 * of JTextFields 
	 * ------------------------------------*/
	private void initializeView() {

		// set new JPanel
		JPanel textView = new JPanel();
		// set panel location
		textView.setLocation(0,0);
		// set game panel size
		textView.setSize(width,height-(HEIGHTBUFFER*3));
		// set layout or no show
		this.setLayout(null);
		// set layout of textView
		textView.setLayout(new GridLayout(size, size, 0, 0));
		// add JPanel view
		this.add(textView);
		
		// set spots
		spots = new JTextField[size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				
				if (game.isVisible(i, j))
					spots[i][j] = new JTextField("[ O ]");
				else
					spots[i][j] = new JTextField("[ X ]");
				
				spots[i][j].setFont(myFont);
				spots[i][j].setEditable(false);
				spots[i][j].setBorder(null);
				textView.add(spots[i][j]);
				
				
			} // for
		} // for
		
		// add text area
		JPanel notifyBox = new JPanel();
		notifyBox.setLocation(0,height-150);
		notifyBox.setSize(width, 50 );
		this.add(notifyBox);

		
		notification = new JTextArea("");
		notification.setBackground(null);
		notification.setLocation((int)width/2,height-100);
		notification.setFont(notifyFont);
		notification.setVisible(true);
		notifyBox.add(notification);
		
	}


	@Override
	public void update(Observable o, Object arg) {
		
		updateBoard();
		playerLocationNotification();
		if (!GODMODE)
		{
		if (game.lossByWumpus()) {
			game.setBoardVisible();
			notification.setText(GAMEOVERMESSAGE1); // loss by wumpus
		}
		if (game.lossBySlimePit()) {
				game.setBoardVisible();
				notification.setText(GAMEOVERMESSAGE2); // loss by slime pit
		}
		if (game.isArrowShot() && !game.isWumpusDead()){
			game.setBoardVisible();
			notification.setText(GAMEOVERMESSAGE4);
		}
		}
		
		if (game.isWumpusDead()){
			game.setBoardVisible();
			notification.setText(GAMEOVERMESSAGE3);
		}
	
	}
	
	/*-------------------------------------------
	 * Method: playerLocationNotification()
	 * Purpose: This method sets depending on the
	 * return value of the game methods
	 * -----------------------------------------*/
	private void playerLocationNotification()
	{
		if (game.isPlayerOnBlood())
			notification.setText(PLAYERONBLOOD);
		else if (game.isPlayerOnSlime())
			notification.setText(PLAYERONSLIME);
		else if (game.isPlayerOnGoop())
			notification.setText(PLAYERONGOOP);
		else
			notification.setText(null);
	}
	
	/*----------------------------------------------
	 * Method: updateBoard()
	 * Purpose: This method is called by the update method 
	 * to create update the game board. This is called every
	 * time a user input is accepted. The game board output is
	 * based off of the chars from the game board getChar method
	 * and is displayed depending on if its true from the
	 * game board visible method.
	 * ---------------------------------------------*/
	private void updateBoard()
	{
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				
				// check for character position
				if (i == game.getHuntersRow() && j == game.getHuntersCol()) {
					spots[i][j].setText("[ "+game.getHunterChar()+" ]");
				}
				else if (game.isVisible(i,j)) {
					if (game.isEmpty(i,j)) {
						spots[i][j].setText(EMPTY);
					} else {
						spots[i][j].setText("[ "+game.getBoardChar(i,j)+" ]");
					}
				} else {
					spots[i][j].setText("[ X ]");
				}
			}
		}
	} // updateBoard


	@Override
	public void keyPressed(KeyEvent e) {
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
	
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
