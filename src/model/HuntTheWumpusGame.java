package model;
import java.util.Observable;
import java.util.Random;

import controller.Move;

public class HuntTheWumpusGame extends Observable {
	
	// ------------- game items -----------------------
	private char[][] board; // game board
	private boolean[][] visibleBoard; // board visible to user
	private int size; 		// game board size
	
	private final int GAMESIZE = 10; // gameboard size
	private final int BLOODBUFFER = 2; // buffer of blood spots around wumpus
	private final int SLIMEBUFFER = 1; // number of slime spots around slime pit
	private final char WUMPUS = 'W'; // wumpus character
	private final char HUNTER = 'O'; // hunter character
	private final char SLIMEPIT = 'P'; // pit char
	private final char BLOOD = 'B'; // blood char
	private final char SLIME = 'S'; // slime char
	private final char GOOP = 'G'; // good char (blood and slime in same location)
	private final char EMPTY = 'X'; // empty location
	private final char VISITED = ' '; // visited char
	private boolean godMode = false; // god mode for player
	private boolean arrowShot = false; // flag for arrow shot
	private boolean wumpusHit = false; // flag for wumpus hit
	private boolean gameOver = false; // flag if wumpus is hit by arrow
	private boolean coverageTest = false; // flag for testing coverage
	private int coverageTestNum = 0; // number of coverage test
	
	private int hunterLocationRow; // hunters location at row
	private int hunterLocationColumn; // hunters location at column
	
	// ------------- game items -----------------------
	
	
	/*-----------------------------------------------
	 * Constructor: HuntTheWumpusGame()
	 * Purpose: When called the constructor
	 * should set board size, and initialize the game
	 * board with the piece locations
	 * --------------------------------------------*/
	public HuntTheWumpusGame()
	{
		size = GAMESIZE;
		initializeBoard();
	} // constructor
	
	/*-----------------------------------------------
	 * Constructor: HuntTheWumpusGame(String "static")
	 * Purpose: When called the constructor
	 * should set board size, and initialize the game
	 * board with the piece locations
	 * --------------------------------------------*/
	public HuntTheWumpusGame(String str)
	{
		size = GAMESIZE;
		if (str.equals("static"))
		{
			initializeStaticBoard();
		}
	} // constructor
	
	
	/*---------------------------------------------
	 * Method: startNewGame()
	 * Purpose: This method calls the initializeBoard method
	 * and basically resets the board.
	 */
	public void startNewGame()
	{
		initializeBoard();
		notifyObservers();
		arrowShot = false;
		wumpusHit = false;
		gameOver = false;
	}
	
	private void initializeStaticBoard()
	{
		// set board size
		// set every spot to empty
		board = new char[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				board[i][j] = EMPTY;
		
		// set visible board size
		// set every board spot to empty
		visibleBoard = new boolean[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				visibleBoard[i][j] = false;
		
		hunterLocationRow = 0;
		hunterLocationColumn = 0;
		visibleBoard[hunterLocationRow][hunterLocationColumn] = true;
		board[0][1] = WUMPUS;
		board[0][2] = BLOOD;
		board[0][3] = SLIME;
		board[0][4] = SLIMEPIT;
		board[0][5] = GOOP;
	}
	
	/*-----------------------------------------------
	 * Method: initializeBoard()
	 * Purpose: This method randomizes the locations of the 
	 * Wumpus, Hunter, slime pits, and blood/slime rooms.
	 * There should be 3-5 slime pits. The hunter position must be
	 * in an empty room.
	 * --------------------------------------------*/
	private void initializeBoard()
	{
		int numberOfPits; // number of pits
		int row; // row location
		int col; // column location
		Random rand; // random objcet
		
		// set board size
		// set every spot to empty
		board = new char[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				board[i][j] = EMPTY;
		
		// set visible board size
		// set every board spot to empty
		visibleBoard = new boolean[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				visibleBoard[i][j] = false;
		
		// create random object
		rand = new Random();
		
		// set wumpus location
		row = rand.nextInt(size);
		col = rand.nextInt(size);
		// for coverage purposes set wumpus if coverage = true
		if (coverageTest)
		{
			row = setWumpusForCoverageRow(coverageTestNum);
			col = setWumpusForCoverageCol(coverageTestNum);
		}
		// set wumpus
		board[row][col] = WUMPUS;
		// surround wumpus with blood
		for (int i = 1; i < BLOODBUFFER+1; i++)
		{
			
			board[(row+i)%size][col] = BLOOD;
				
			if (row-i < 0)
				board[size-(-1*(row-i))][col] = BLOOD;
			else
				board[row-i][col] = BLOOD;

			board[row][(col+i)%size] = BLOOD;
				
			if (col-i < 0)
				board[row][size-(-1*(col-i))] = BLOOD;
			else
				board[row][col-i] = BLOOD;
			
			if (i < ((BLOODBUFFER+1)/2 + (BLOODBUFFER+1)%2))
			{
				
					board[(row+i)%size][(col+i)%size] = BLOOD;
				if (col-i < 0)
					board[(row+i)%size][size-(-1*(col-i))] = BLOOD;
				else
					board[(row+i)%size][col-i] = BLOOD;
				
				if (row-i < 0)
					board[size-(-1*(row-i))][(col+i)%size] = BLOOD;
				else
					board[row-i][(col+i)%size] = BLOOD;
				
				if (row-i >= 0 && col-i >= 0)
					board[row-i][col-i] = BLOOD;
				else if (row-i < 0 && col-i >= 0)
					board[size-(-1*(row-i))][col-i] = BLOOD;
				else if (col-i < 0 && row-i >= 0)
					board[row-i][size-(-1*(col-i))] = BLOOD;
				else 
					board[size-(-1*(row-i))][size-(-1*(col-i))] = BLOOD;


			}
		}
		
		// number of slime pits (3-5)
		numberOfPits = rand.nextInt(3)+3;
		
		// set slime pits
		// pits cannot go on wumpus, blood, or pits
		// slime cannot go on wumpus or pits 
		// (blood + slime) = goop
		row = rand.nextInt(size);
		col = rand.nextInt(size);
		for (int i = 0; i < numberOfPits; i++)
		{
			while (board[row][col] == WUMPUS || board[row][col] == SLIMEPIT || board[row][col] == BLOOD)
			{
				row = rand.nextInt(size);
				col = rand.nextInt(size);
			}
			board[row][col] = SLIMEPIT;
			
			// surround slime pit with slime
			for (int j = 1; j < SLIMEBUFFER+1; j++)
			{
				if (board[(row+j)%size][col] != WUMPUS && board[(row+j)%size][col] != SLIMEPIT)
				{
					if (board[(row+j)%size][col] == BLOOD)
						board[(row+j)%size][col] = GOOP;
					else
						board[(row+j)%size][col] = SLIME;
				}
				
				if (row - j >= 0 && board[row-j][col] != WUMPUS && board[row-j][col] != SLIMEPIT)
				{
					if (board[row-j][col] == BLOOD)
						board[row-j][col] = GOOP;
					else
						board[row-j][col] = SLIME;
				} else if (row - j < 0 && board[size-(-1*(row-j))][col] != WUMPUS && board[size-(-1*(row-j))][col] != SLIMEPIT)
				{
					if (board[size-(-1*(row-j))][col] == BLOOD)
						board[size-(-1*(row-j))][col] = GOOP;
					else
						board[size-(-1*(row-j))][col] = SLIME;
				}
				
				if (col - j >= 0 && board[row][col-j] != WUMPUS && board[row][col-j] != SLIMEPIT)
				{
					if (board[row][col-j] == BLOOD)
						board[row][col-j] = GOOP;
					else
						board[row][col-j] = SLIME;
				} else if (col-j < 0 && board[row][size-(-1*(col-j))] != WUMPUS && board[row][size-(-1*(col-j))] != SLIMEPIT)
				{
					if (board[row][size-(-1*(col-j))] == BLOOD)
						board[row][size-(-1*(col-j))] = GOOP;
					else
						board[row][size-(-1*(col-j))] = SLIME;
				}
				if (board[row][(col+j)%size] != WUMPUS && board[row][(col+j)%size] != SLIMEPIT)
				{
					if (board[row][(col+j)%size] == BLOOD)
						board[row][(col+j)%size] = GOOP;
					else
						board[row][(col+j)%size] = SLIME;
				}
			}
		}
		
		// set players locations
		// players location must be empty
		row = rand.nextInt(size);
		col = rand.nextInt(size);
		while (board[row][col] != EMPTY)
		{
			row = rand.nextInt(size);
			col = rand.nextInt(size);
		}
		// set hunter's char on board
		// set instance hunters location variables
		// set visible board to true at that location
		//board[row][col] = HUNTER;		// might not want to set board to have hunter char(might just be visible) -------------------------------------
		hunterLocationRow = row;
		hunterLocationColumn = col;
		visibleBoard[hunterLocationRow][hunterLocationColumn] = true;
	} // initializeBoard
	
	/*----------------------------------
	 * method: setWumpusForCoverageRow
	 * Purpose: used by testing for edge cases on wumpus
	 * -------------------------------*/
	private int setWumpusForCoverageRow(int num) {
		switch (num){
		case 1:
			return 9;
		case 2:
			return 0;
		case 3:
			return 0;
		case 4:
			return 9;
		case 8:
			return 0;
		case 9: 
			return 6;
		default:
			return 9;			
		}
	}
	/*----------------------------------
	 * method: setWumpusForCoverageCol
	 * Purpose: used by testing for edge cases on wumpus
	 * -------------------------------*/
	private int setWumpusForCoverageCol(int num) {
		switch (num){
		case 1:
			return 9;
		case 2:
			return 9;
		case 3:
			return 0;
		case 4:
			return 0;
		case 8:
			return 7;
		case 9: 
			return 0;
		default:
			return 9;
		}
	}

	/*-------------------------------------
	 * Method: move()
	 * Purpose: To change the location of the hunter based on
	 * the direction from the argument. Also sets the new location
	 * to true in the visible board 2d array.
	 * Parameter: enum Move & has four directions
	 * 
	 * NOTES: This method assumes that the move is valid
	 * ----------------------------------*/
	public char move(Move direction)
	{
		if (direction == Move.LEFT)
		{
			hunterLocationColumn --;
			hunterLocationColumn = hunterLocationColumn % size;
			// if user wraps left passed 0, send to location size - 1
			if (hunterLocationColumn < 0)
				hunterLocationColumn = size-1;
			visibleBoard[hunterLocationRow][hunterLocationColumn] = true;
			
		}
		else if (direction == Move.RIGHT)
		{
			hunterLocationColumn++;
			hunterLocationColumn = hunterLocationColumn % size;
			visibleBoard[hunterLocationRow][hunterLocationColumn] = true;
		}
		else if (direction == Move.UP)
		{
			hunterLocationRow--;
			// if user wraps upwards passed 1, set to size-1 row
			if (hunterLocationRow < 0)
				hunterLocationRow = size-1;
			hunterLocationRow = hunterLocationRow % size;
			visibleBoard[hunterLocationRow][hunterLocationColumn] = true;
		}
		else
		{
			hunterLocationRow++;
			hunterLocationRow = hunterLocationRow % size;
			visibleBoard[hunterLocationRow][hunterLocationColumn] = true;
		}
		
		// update gameBoard by notifying observers
		notifyObservers();
		return board[hunterLocationRow][hunterLocationColumn];
	} // move
	
	
	/*---------------------------------------
	 * Method: lossByWumpus()
	 * Purpose: This method returns true if the hunters 
	 * location on the board is equal to the wumpus
	 * ---------------------------------------*/
	public boolean lossByWumpus()
	{
		if (board[hunterLocationRow][hunterLocationColumn] == WUMPUS)
			return true;
		return false;
	} // lossByWumpus
	
	/*---------------------------------------
	 * Method: lossBySlimePit()
	 * Purpose: This method returns true if the hunters
	 * location on the board is equivalent to the slime pit
	 * ----------------------------------------*/
	public boolean lossBySlimePit()
	{
		if (board[hunterLocationRow][hunterLocationColumn] == SLIMEPIT)
			return true;
		return false;
	}
	
	/*---------------------------------------------
	 * Method: shootArrow()
	 * Purpose: This method determines if the shot
	 * hits the wumpus if so returns true else
	 * returns false. This method causes the arrow
	 * to wrap around the board using modulus.
	 * Parameters: enum Move & has 4 directions
	 * --------------------------------------------*/
	public void shootArrow(Move direction)
	{
		int row = hunterLocationRow;
		int col = hunterLocationColumn;
		arrowShot(true);
		
		if (direction == Move.LEFT)
		{
			int i = col-1;
			while (i != hunterLocationColumn)
			{
				if (i < 0)
					i = size-1;
				if (board[row][i] == WUMPUS)
				{
					wumpusHit = true;
					gameOver = true;
				}
				i--;
			}
		}
		if (direction == Move.RIGHT)
		{
			for (int i = col; i < (size+col); i++)
			{
				if (board[row][i%size] == WUMPUS)
				{
					wumpusHit = true;
					gameOver = true;
				}
			}
		} 
		if (direction == Move.UP)
		{
			int i = row-1;
			while (i != row)
			{
				if (i < 0)
					i = size-1;
				if (board[i][col] == WUMPUS)
				{
					wumpusHit = true;
					gameOver = true;
				}
				i--;
			}
		}
		if (direction == Move.DOWN)
		{
			for (int i = row; i < (size+row); i++)
			{
				if (board[i%size][col] == WUMPUS)
				{
					wumpusHit = true;
					gameOver = true;
				}
			}
		}  	
	} // shootArrow
	
	
	/*--------------------------------------------
	 * Method: isHunter
	 * Purpose: This method returns true if
	 * the coordinates passed through the parameter
	 * is the location of the hunter
	 *---------------------------------------------*/
	public boolean isHunter(int row, int col)
	{
		if (row == hunterLocationRow && col == hunterLocationColumn)
			return true;
		return false;
	} // isHunter
	
	
	public boolean isVisible(int row, int col)
	{
		if (visibleBoard[row][col] == true)
			return true;
		else
			return false;
	} // isVisible
	
	
	/*----------------------------------------
	 * Method: getBoardChar
	 * 
	 * Purpose: This method returns the char
	 * at the position passed through the parameter
	 * ------------------------------------*/
	public char getBoardChar(int row, int col)
	{
		return board[row][col];
	}
	
	/*----------------------------------------
	 * Method: isEmpty
	 * Purpose: This method returns true if the 
	 * position is empty (or equivalent to empty char)
	 *----------------------------------------*/
	public boolean isEmpty(int row, int col)
	{
		if (board[row][col] == EMPTY)
			return true;
		return false;
	}
	
	
	/*------------------------
	 * Method: printBaord()
	 * Purpose: prints basic grid board
	 * ----------------------*/
	public void printBoard()
	{
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println("");
		}
	}
	
	/*--------------------------
	 * Method: setBoardVisible
	 * Purpose: This method sets all of
	 * the booleans in visibleBoard[][] boolean array
	 * to true
	 * -----------------------*/
	
	public void setBoardVisible()
	{
		for (int i = 0; i < size; i ++){
			for (int j = 0; j < size; j++){
				visibleBoard[i][j] = true;
			}
		}
	}
	
	// returns true if player is on blood
	public boolean isPlayerOnBlood(){
		if (board[hunterLocationRow][hunterLocationColumn] == BLOOD)
			return true;
		else
			return false;	
	}
	// returns true if player is on slime
	public boolean isPlayerOnSlime(){
		if (board[hunterLocationRow][hunterLocationColumn] == SLIME)
			return true;
		else
			return false;	
	}
	// returns true if player is on goop
	public boolean isPlayerOnGoop(){
		if (board[hunterLocationRow][hunterLocationColumn] == GOOP)
			return true;
		else
			return false;	
	}
	
	// returns true if board location is blood
	public boolean isBlood(int row, int col) { 
		if (getBoardChar(row,col) == BLOOD) 
			return true; 
		else 
			return false; 
		}
	// returns true if board location is slime
	public boolean isSlime(int row, int col) { 
		if (getBoardChar(row,col) == SLIME) 
			return true; 
		else 
			return false; 
		}
	// returns true if board location is slime pit
	public boolean isSlimePit(int row, int col) { 
		if (getBoardChar(row,col) == SLIMEPIT) 
			return true; 
		else 
			return false; 
		}
	// returns true if board location is goop
	public boolean isGoop(int row, int col) { 
		if (getBoardChar(row,col) == GOOP) 
			return true; 
		else 
			return false; 
		}
	// returns true if board location is wumpus
	public boolean isWumpus(int row, int col) { 
		if (getBoardChar(row,col) == WUMPUS) 
			return true; 
		else 
			return false; 
		}
	
	private void arrowShot(boolean bool)
	{
		this.arrowShot = bool;
	}
	
	// getters
	public boolean isGameOver() { return gameOver; }
	public boolean isWumpusDead() { return wumpusHit; }
	public boolean isArrowShot() { return arrowShot; }
	public boolean isGodMode() { return godMode; }
	public int getSize() { return size; }
	public int getHuntersRow() { return hunterLocationRow; }
	public int getHuntersCol() { return hunterLocationColumn; }
	public char getHunterChar() { return HUNTER; }
	public boolean getCoverageTest() { return coverageTest; }
	// setter
	public void setCoverageTest() {coverageTest = true;}
	public void setCoverageTestNum(int n) {coverageTestNum = n;}
} // HuntTheWumpusGame
