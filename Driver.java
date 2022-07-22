import javax.swing.*;


/*
*The Driver class is the calss through which the game is loaded and played 
* 
* @author  Mahek Bharat Parmar
* @version 1.0
* @since   20-Jan-2022
*/

public class Driver {
	//number of rows and columns what we'd like to have in the games' grid
	final static int ROW  = 4;
	final static int COL = 4;
	
	public static void main(String [] args) {
		Window gameWindow = new Window(ROW, COL);
	}
}
