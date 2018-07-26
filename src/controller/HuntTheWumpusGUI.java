package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.HuntTheWumpusGame;
import view.Controls;
import view.GraphicView;
import view.TextView;

public class HuntTheWumpusGUI extends JFrame implements KeyListener {
	
	public static void main(String[] args)
	{
		HuntTheWumpusGUI gui = new HuntTheWumpusGUI();
		gui.setVisible(true);
	}
	
	
	private HuntTheWumpusGame game; // game object
	private TextView textView; // textView object
	private GraphicView graphicView; // graphic view
	private JPanel currentView; // current gameView
	private Controls controls; // control view
	private static final int WIDTH = 516; // game board width
	private static final int HEIGHT = 560; // game board height
	private static final int GAMEWIDTH = 0; // game board location width
	private static final int GAMEHEIGHT = 0; // game board location height
    private final Set<Integer> pressed = new HashSet<Integer>(); // hashset to allow multiple key listeners

	// constructor
	public HuntTheWumpusGUI()
	{
		// set close, height, location, title
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(0, 0);
		this.setTitle("Hunt The Wumpus");
		
		// set menu
		setMenu();
		// initialize
		game = new HuntTheWumpusGame();
		// initialize view
		textView = new TextView(game,WIDTH,HEIGHT);
		graphicView = new GraphicView(game,WIDTH,HEIGHT);
		controls = new Controls(game,WIDTH,HEIGHT);
		// add observer from observable
		game.addObserver(textView);
		game.addObserver(graphicView);
		// set view
		this.setViewTo(graphicView);
		this.addKeyListener(this);
		this.setFocusable(true);
	} // constructor
	
	private void setMenu()
	{		
		// add menu to newGame
		JMenuItem menu = new JMenu("Options");
		JMenuItem newGame = new JMenuItem("New Game");
		menu.add(newGame);
		
		// add menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(menu);
		
		// add view menu to menu
		JMenuItem view = new JMenu("Views");
		menu.add(view);
		// add controls
		JMenuItem control = new JMenuItem("Controls");
		menu.add(control);
		// add textView options to view
		JMenuItem textView = new JMenuItem("Text View");
		view.add(textView);
		// add graphic view options to view
		JMenuItem graphicView = new JMenuItem("Graphic View");
		view.add(graphicView);

		// add the same listener to all menu items requiring actions
		MenuItemListener menuListener = new MenuItemListener();
		newGame.addActionListener(menuListener);
		// add action listener for graphic view
		textView.addActionListener(menuListener);
		// add action listener for text view
		graphicView.addActionListener(menuListener);
		// add action listener for controls
		control.addActionListener(menuListener);

		
	} // setMenu
	
	
	/* --------------------------------------------------
	 * Class: MenuItemListener
	 * Author: Alex Starks
	 * 
	 * Purpose: This class implements action listener
	 * to override the actionPerformed method that will
	 * respond to JMenuItem clicks.
	 * -------------------------------------------------*/
	public class MenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String text = ((JMenuItem) e.getSource()).getText();
			
			if (text.equals("New Game")) {
				game.startNewGame();
				graphicView.update(game, null);		
				textView.update(game,null);
			}
			if (text.equals("Text View")) {
				setViewTo(textView);
			}
			if (text.equals("Graphic View")){
				setViewTo(graphicView);
			}
			if (text.equals("Controls")) {
				setViewTo(controls);
			}
		}
	} // MenuItemListener

	/*----------------------------------------
	 * Method: setViewTo
	 * Purpose: This method resets the game board view.
	 * --------------------------------------*/
	public void setViewTo(JPanel view)
	{
		if (currentView != null)
			this.remove(currentView);
		currentView = view;
		this.add(currentView);
		currentView.repaint();
		this.validate();	
	} // setViewTo
	

	
	@Override
	public void keyPressed(KeyEvent e) {
		
		// set up flags for shooting event
		boolean spaceFlag = false; // flag for space button
		boolean leftFlag = false; // flag for left button
		boolean rightFlag = false; // flag for right button
		boolean upFlag = false; // flag for up button
		boolean downFlag = false; // flag for down button
		// add to hash set to check for simultaneous key actions
		 pressed.add(e.getKeyCode());
	        if (pressed.size() > 1) {
	    			for (Integer in : pressed)
	    			{
	    				if (in == 32)
	    					spaceFlag = true;
	    				if (in == 37)
	    					leftFlag = true;
	    				if (in == 39)
	    					rightFlag = true;
	    				if (in == 38)
	    					upFlag = true;
	    				if (in == 40)
	    					downFlag = true;
	    			}
	    			if (spaceFlag && leftFlag) {
	    				game.shootArrow(Move.LEFT);
	    			} else if (spaceFlag && rightFlag)
	    			{
	    				game.shootArrow(Move.RIGHT);
	    			} else if (spaceFlag && upFlag)
	    			{
	    				game.shootArrow(Move.UP);
	    			} else if (spaceFlag && downFlag)
	    			{
	    				game.shootArrow(Move.DOWN);
	    			} else {
						JOptionPane.showMessageDialog(null, "Invalid move");
	    			}	    				
	        }
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// set movements and consequences
		// if arrow is not fired
		if (!game.isArrowShot()){
				if (e.getKeyCode() == 37)
				{
						game.move(Move.LEFT);
				}
				if (e.getKeyCode() == 39)
				{
						game.move(Move.RIGHT);
				}
				if (e.getKeyCode() == 38)
				{
						game.move(Move.UP);
				}
				if (e.getKeyCode() == 40)
				{
						game.move(Move.DOWN);
				}
		}
				graphicView.update(game, null);		
				textView.update(game,null);
		pressed.removeAll(pressed);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
