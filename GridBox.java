import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/*
* The GridBox class involves the grids/boxes which are the base of the whole game together with their interactive behaviors 
* 
* @author  Mahek Bharat Parmar
* @version 1.0
* @since   20-Jan-2022
*/

public class GridBox extends JButton {
	
	int status;		//status of a particular grid (0 if available / unoccupied, 1 if occupied by player 1, and 2 if occupied by player 2)
	
	public GridBox() {
		super();
		reset();
	}
	
	/* This method updates/changes the colors of the grid, depending on the player
	 * @param status, the player
	 * 
	 */
	public void updateColor(int status) {
		if (status == 1) {							//if grid selected by player 1, change the color to red
			this.setBackground(Color.red);
		}
		if (status ==2) {							//if grid selected by player 2, change color to blue
			this.setBackground(Color.blue);
		}
	}
	
	/* This method updates the status of a particular GridBox object
	 * @param currentPlayer, the currentPlayer who selected the grid
	 * @return true, if an unoccupied grid box was clicked, else returns false
	 */
	
	public boolean updateStatus(int currentPlayer) {
		if (status == 0) {							//if clicked on an unoccupied grid, update status and color of the grid and return true 
			status = currentPlayer;
			updateColor(currentPlayer);
			return true;
		}
		else {
			return false;				//if an already selected grid is re-selected, dont change anything return false 
		}
	}
	
	/* Accessor method for the status of the GridBox object
	 * @return status, status of the GridBox object
	 * 
	 */
	public int getStatus() {
		return status;
	}
	
	
	/* This function resets/clears the data of the individual grid boxes 
	 * This occurs whenever New Game is clicked which could be when the game is completed or incomplete
	 *  
	 */
	public void reset() {
		this.setBackground(Color.white);				//changing the color back to white
		Border border = new LineBorder(Color.lightGray, 2);
		//this.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		this.setOpaque(true);			
		this.setBorderPainted(true);
		this.setBorder(border);
		status = 0;										//sets the status to 0, implying that the grid box is available and not occupied by either player
	}
	
	
}
