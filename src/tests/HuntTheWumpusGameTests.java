package tests;
import static org.junit.Assert.*;
import org.junit.Test;

import controller.Move;
import model.HuntTheWumpusGame;


public class HuntTheWumpusGameTests {

	@Test
	public void testGame()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame();
		game.startNewGame();
	}
	
	@Test
	public void testMove() {
		HuntTheWumpusGame game = new HuntTheWumpusGame();
		int size = game.getSize();
		for (int i = 0; i < size; i++)
			game.move(Move.LEFT);
		for (int i = 0; i < size; i++)
			game.move(Move.RIGHT);
		for (int i = 0; i < size; i++)
			game.move(Move.UP);
		for (int i = 0; i < size; i++)
			game.move(Move.DOWN);
		
		HuntTheWumpusGame game1 = new HuntTheWumpusGame("static");
		assertTrue(game1.move(Move.DOWN) == 'X');
		assertTrue(game1.move(Move.RIGHT) == 'X');
		assertTrue(game1.move(Move.LEFT) == 'X');
		assertTrue(game1.move(Move.UP) == 'X');
		assertTrue(game1.move(Move.RIGHT) == 'W');
	}
	
	@Test
	public void testLosses()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.lossByWumpus());
		game.move(Move.RIGHT);
		assertTrue(game.lossByWumpus());
		HuntTheWumpusGame game1 = new HuntTheWumpusGame("static");
		assertFalse(game1.lossBySlimePit());
		game1.move(Move.DOWN);
		game1.move(Move.RIGHT);
		game1.move(Move.RIGHT);
		game1.move(Move.RIGHT);
		game1.move(Move.RIGHT);
		game1.move(Move.UP);
		assertTrue(game1.lossBySlimePit());
	}
	
	@Test
	public void testArrow()
	{		
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		game.shootArrow(Move.LEFT);
		HuntTheWumpusGame game1 = new HuntTheWumpusGame("static");
		game1.shootArrow(Move.RIGHT);
		HuntTheWumpusGame game2 = new HuntTheWumpusGame("static");
		game2.move(Move.DOWN);
		game2.move(Move.RIGHT);
		game2.shootArrow(Move.UP);
		HuntTheWumpusGame game3 = new HuntTheWumpusGame("static");
		game3.move(Move.DOWN);
		game3.move(Move.RIGHT);
		game3.shootArrow(Move.DOWN);
	}
	
	@Test 
	public void testIsHunter()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isHunter(0, 0));
		assertFalse(game.isHunter(0, 1));
	}
	
	@Test
	public void testIsVisible() {
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isVisible(0, 0));
		assertFalse(game.isVisible(0, 1));
		
	}
	@Test
	public void testGetBoardChar() {
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.getBoardChar(0, 1) == 'W');
		assertTrue(game.getBoardChar(0, 2) == 'B');
		assertTrue(game.getBoardChar(0, 3) == 'S');
		assertTrue(game.getBoardChar(0, 4) == 'P');
		assertTrue(game.getBoardChar(0, 5) == 'G');
	}
	
	@Test 
	public void isEmpty() {
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isEmpty(0,0));
		assertFalse(game.isEmpty(0,1));
	}

	@Test
	public void setVisible()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isVisible(1, 0));
		game.setBoardVisible();
		assertTrue(game.isVisible(1, 0));
	}
	
	@Test
	public void testIsPlayerOnBlood(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isPlayerOnBlood());
		game.move(Move.DOWN);
		game.move(Move.RIGHT);
		game.move(Move.RIGHT);
		game.move(Move.UP);
		assertTrue(game.isPlayerOnBlood());
	}
	
	@Test
	public void testIsPlayerOnSlime(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isPlayerOnSlime());
		game.move(Move.DOWN);
		game.move(Move.RIGHT);
		game.move(Move.RIGHT);
		game.move(Move.RIGHT);
		game.move(Move.UP);
		assertTrue(game.isPlayerOnSlime());
	}
	@Test
	public void testIsPlayerOnGoop(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isPlayerOnGoop());
		game.move(Move.DOWN);
		game.move(Move.RIGHT);
		game.move(Move.RIGHT);
		game.move(Move.RIGHT);
		game.move(Move.RIGHT);
		game.move(Move.RIGHT);
		game.move(Move.UP);
		assertTrue(game.isPlayerOnGoop());
	}
	@Test
	public void testIsBlood()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isBlood(0,2));
		assertFalse(game.isBlood(0, 1));
	}
	@Test
	public void testIsSlime()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isSlime(0,3));
		assertFalse(game.isSlime(0, 1));
	}
	@Test
	public void testIsGoop()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isGoop(0,5));
		assertFalse(game.isGoop(0, 1));
	}
	
	@Test
	public void testIsSlimePit()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isSlimePit(0,4));
		assertFalse(game.isSlimePit(0, 1));
	}
	@Test
	public void testIsWumpus()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertTrue(game.isWumpus(0,1));
		assertFalse(game.isWumpus(0, 0));
	}
	
	// game over variable is only set when arrow is shot
	@Test
	public void testIsGameOver(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isGameOver());
		game.shootArrow(Move.LEFT);
		assertTrue(game.isGameOver());
	}
	@Test
	public void testIsWumpusDead(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isWumpusDead());
		game.shootArrow(Move.LEFT);
		assertTrue(game.isWumpusDead());
	}
	@Test
	public void testIsArrowShot(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isArrowShot());
		game.shootArrow(Move.LEFT);
		assertTrue(game.isArrowShot());
	}
	@Test
	public void testIsGodMode(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.isGodMode());
	}
	@Test
	public void testgetHuntersRow(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.getHuntersRow() == 1);
		assertTrue(game.getHuntersRow() == 0);
	}
	@Test
	public void testgetHuntersColumn(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.getHuntersCol() == 1);
		assertTrue(game.getHuntersCol() == 0);
	}
	
	@Test
	public void testGetHunterChar(){
		HuntTheWumpusGame game = new HuntTheWumpusGame("static");
		assertFalse(game.getHunterChar() == 'X');
		assertTrue(game.getHunterChar() == 'O');
	}
	
	@Test
	public void testWumpusSlimePitPlacementCoverage()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame();
		assertFalse(game.getCoverageTest());
		game.setCoverageTest();
		game.startNewGame();
		assertTrue(game.getCoverageTest());
		game.setCoverageTestNum(1);
		game.startNewGame();
		assertTrue(game.isWumpus(9, 9));
		assertTrue(game.isBlood(0, 0) || game.isGoop(0, 0));
		
		game.setCoverageTestNum(2);
		game.startNewGame();
		assertTrue(game.isWumpus(0, 9));
		assertTrue(game.isBlood(0, 0) || game.isGoop(0, 0));

		game.setCoverageTestNum(3);
		game.startNewGame();
		assertTrue(game.isWumpus(0, 0));
		assertTrue(game.isBlood(9, 0) || game.isGoop(9, 0));

		game.setCoverageTestNum(4);
		game.startNewGame();
		assertTrue(game.isWumpus(9, 0));
		assertTrue(game.isBlood(0, 0) || game.isGoop(0, 0));


		game.setCoverageTestNum(8);
		game.startNewGame();
		assertTrue(game.isWumpus(0, 7));
		assertTrue(game.isBlood(9, 7) || game.isGoop(9, 7));
		
		game.setCoverageTestNum(9);
		game.startNewGame();
		assertTrue(game.isWumpus(6, 0));
		assertTrue(game.isBlood(6,9) || game.isGoop(6, 9));


	}
	
	@Test
	public void printBoard()
	{
		HuntTheWumpusGame game = new HuntTheWumpusGame();
		game.printBoard();
	}
	
	
}
