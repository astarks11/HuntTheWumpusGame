package view;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.Move;
import model.HuntTheWumpusGame;

public class GraphicView extends JPanel implements Observer, KeyListener {
	
	private HuntTheWumpusGame game; // hunt the wumpus game
	private int width; // width of game board
	private int height; // height of game board
	private Image img_ground; // image of the ground
	private Image img_blood; // image of blood
	private Image img_slime; // image of slime
	private Image img_goop; // image of goop
	private Image img_slimePit; // image of slimePit
	private Image img_hunter; // image of hunter
	private Image img_wumpus; // image of wumpus
	private ImageObserver imgObserv; // image observer
	private final int imgX = 50; // width of images
	private final int imgY = 50; // width of images
	private boolean GODMODE; // turned on and will not loose

	
	private boolean arrowFired = false; // flag for arrowfired
    private final Set<Integer> pressed = new HashSet<Integer>(); // hashset to allow multiple key listeners
	private boolean gameOver = false; // true if game is over
	private boolean lossByArrow = false; // flag for loss by arrow
	private final String GAMEOVERMESSAGE1 = "WUMPUS GOT YOU!"; // loss by wumpus 
	private final String GAMEOVERMESSAGE2 = "You fell into a SLIME PIT!"; // loss by slime pit
	private final String GAMEOVERMESSAGE3 = "You got the WUMPUS!"; // game win
	private final String GAMEOVERMESSAGE4 = "Your arrow bouned off a rock and hit you!"; // arrowcide

	
	// constructor
	public GraphicView(HuntTheWumpusGame game, int width, int height)
	{
		this.game = game;
		this.width = width;
		this.height = height;
		this.setSize(imgX*game.getSize(),imgY*game.getSize());
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(this);
		initializeImages();
		//initilializeView();
		GODMODE = game.isGodMode();

	}
	

	private void initilializeView()
	{
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		// ------------------- paint background -----------------
		for (int i = 0; i < game.getSize(); i++){
			for (int j = 0; j < game.getSize(); j++){
				if (game.isVisible(i,j))
					g2.drawImage(img_ground,j*imgX,i*imgY,imgObserv);
			}
		}
		// ------------------- paint background -----------------
		
		
		
		for (int i = 0; i < game.getSize(); i++){
			for (int j = 0; j < game.getSize(); j++){
				if (game.isBlood(i,j) && game.isVisible(i,j))
				{
					g2.drawImage(img_blood,j*imgX,i*imgY,imgObserv);
				}
				if (game.isSlime(i,j) && game.isVisible(i,j))
				{
					g2.drawImage(img_slime,j*imgX,i*imgY,imgObserv);
				}
				if (game.isSlimePit(i,j) && game.isVisible(i,j))
				{
					g2.drawImage(img_slimePit,j*imgX,i*imgY,imgObserv);
				}
				if (game.isGoop(i,j) && game.isVisible(i,j))
				{
					g2.drawImage(img_goop,j*imgX,i*imgY,imgObserv);
				}
				if (game.isWumpus(i,j) && game.isVisible(i,j))
				{
					g2.drawImage(img_wumpus,j*imgX,i*imgY,imgObserv);
				}
				if (i == game.getHuntersRow() && j == game.getHuntersCol())
				{
					g2.drawImage(img_hunter,j*imgX,i*imgY,imgObserv);
				}
			}
		}
		
	}
	
	/*-------------------------------------------------------------
	 * Method: initializeImages
	 * Purpose: This method sets the image variables with their images.
	 * If the images cannot be located an IOException will be thrown
	 * -----------------------------------------------------------*/
	private void initializeImages()
	{
		try {
			img_ground = ImageIO.read(new File("images/Ground.png"));
			img_blood = ImageIO.read(new File("images/Blood.png"));
			img_goop = ImageIO.read(new File("images/Goop.png"));
			img_slime = ImageIO.read(new File("images/Slime.png"));
			img_slimePit = ImageIO.read(new File("images/slimePit.png"));
			img_hunter = ImageIO.read(new File("images/TheHunter.png"));
			img_wumpus = ImageIO.read(new File("images/Wumpus.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // initializeImages
	




	@Override
	public void keyPressed(KeyEvent e) {
	
	}


	@Override
	public void keyReleased(KeyEvent e) {

	}

@Override
public void keyTyped(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void update(Observable o, Object arg) {

	repaint();

	if (!GODMODE)
	{
	if (game.lossByWumpus()) {
		game.setBoardVisible();
		JOptionPane.showMessageDialog(null,GAMEOVERMESSAGE1);
	}
	if (game.lossBySlimePit()) {
			game.setBoardVisible();
			JOptionPane.showMessageDialog(null,GAMEOVERMESSAGE2);
	}
	if (game.isArrowShot() && !game.isWumpusDead()){
		game.setBoardVisible();
		JOptionPane.showMessageDialog(null,GAMEOVERMESSAGE4);
	}
	}
	
	if (game.isGameOver()){
		game.setBoardVisible();
		JOptionPane.showMessageDialog(null,GAMEOVERMESSAGE3);
		this.setFocusable(true);
	}	
	
}


}