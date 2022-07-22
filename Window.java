import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


import java.util.ArrayList;
import java.util.Random;

/*
* The Window class displays the game and controls all interactions with it
*
* @author  Mahek Bharat Parmar
* @version 1.0
* @since   20-Jan-2022
*/


public class Window implements ActionListener {
	int row;
	int col;
	JFrame frame;
	Random randgen = new Random();
	final int NUM_PLAYERS=2;
	JLabel playerInfo;
	GridBox [][] gridsToAdd;
	int currentPlayer;
	boolean play = false;
	
	public Window(int row, int col) {
		this.row = row;
		this.col = col;
		
		
		
		//the frame where the game is played, and setting its preferred size
		frame = new JFrame("Mind The Gap");
		frame.setPreferredSize(new Dimension(600,600));
		
		//extracting the content pane from the frame and setting its layout to a border layout
		Container contentPane = frame.getContentPane();
		
		//the top panel, displays the player info and the new game button
		JPanel topPanel = new JPanel();
		
		//a button for new game
		JButton newGame = new JButton("New Game");
		newGame.addActionListener(e -> play()); 		//corresponding action listener for this new game button
		JPanel newGamePanel = new JPanel();
		newGamePanel.add(newGame);				//adding the new game button to a JPanel so that the button remains central 
		
		//playerInfo, the label which shows the game related info, such as the current player and the winner
		playerInfo = new JLabel(String.format("%-70s", "Don't select neighbour squares! Click to begin>>>"));			
		JPanel playerInfoPanel = new JPanel();
		playerInfoPanel.add(playerInfo);			//adding the playerInfo label to another label, so that it player info remains central
		
		topPanel.add(playerInfoPanel);				//adding to the corresponding top panel
		topPanel.add(newGamePanel);
		
		JPanel bottomPanel = new JPanel();							//creating a bottom panel
		bottomPanel.setLayout(new GridLayout(row, col));	//setting its layout to a grid layout so all the grids are equal
		
		gridsToAdd = new GridBox [row][col];				//a 2-d array that stores GridBox objects
		
		for (int i=0 ; i<row ; i++) {
			for (int j=0 ; j<col ; j ++) {
				gridsToAdd[i][j] = new GridBox();				//adding the grids
				bottomPanel.add(gridsToAdd[i][j]);
				gridsToAdd[i][j].addActionListener(this);		//adding the corresponding action listeners for each grid
			}
		}
		
		contentPane.add(topPanel, BorderLayout.NORTH);				//adding the corresponding top and bottom panels to the content pane of the frame
		contentPane.add(bottomPanel, BorderLayout.CENTER);
		
		//some housekeeping code
		frame.pack();	
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);	
	}
	
			
	
	/* This method is invoked whenever the newGame button is clicked
	 * It resets all the grids and sets play to true so the game can be played
	 *  
	 */
	public void play() {
		//ever grid in the bottom panel is going to be reseted by invoking the reset function on every GridBox object stored in the 2-d array gridsToAdd
		for (int i=0 ; i<row ; i++) {
			for (int j=0 ; j<col ; j ++) {
				gridsToAdd[i][j].reset();
			}
		}
		
		currentPlayer = randgen.nextInt(NUM_PLAYERS) +1 ;					//generates a random player, +1 because the range starts from 0 
		playerInfo.setText(String.format("%-70s", "Player " + (currentPlayer) + "'s turn"));		//display which player starts the game
		
		play = true;
	}
	
	
	/* The event handler method 
	 * @param aevt, the action event which has happened
	 * 
	 */
	public void actionPerformed(ActionEvent aevt) {
		
		Object source = aevt.getSource();										//identifying the source of the event
		if (source instanceof GridBox && play) {								// if the grid box was selected, and play was true/on
			if (((GridBox) source).updateStatus(currentPlayer)) {				//only enter the loop if the current grid box selected is available
				if (checker((GridBox)source)) {
					currentPlayer = (currentPlayer%2) +1 ;
					playerInfo.setText(String.format("%-70s" ,"Player " + (currentPlayer) + "'s turn"));
					}
				else {
					playerInfo.setText(String.format("%-70s" , "Player " + ((currentPlayer%2)+1) + " wins! Click >>> to start"));
					play = false;	
				}
			}
		}
	}
	
	/* This method checks where the grid box clicked was a valid one or not
	 * @param currentBox, the current grid box which was clicked by the player
	 * @return true if selection was valid, else return false
	 */
	public boolean checker(GridBox currentBox) {
		boolean result = true;													//initializing to true
		for (int q = 0 ; q<row ; q++) {											//to find out the row and column where the current grid box is found in the 2-d array
			for (int w = 0 ; w<col ; w++) { 
				if (gridsToAdd[q][w] == currentBox) {
					
					ArrayList <Integer> neighbourStatus = new ArrayList<>();	//the array list which contains the status of all the valid neighbors of the current grid box
					neighbourStats(neighbourStatus, currentBox, q,w);			//invoking the neighbourStats to update the above neighbourStatus array list with the status of all valid neighbors
					int s=0;													//initialize to 0
					while (result && s<neighbourStatus.size()) {				//compares the status of current grid box with the status of all of its valid neighbors
						if (neighbourStatus.get(s) == currentPlayer ){			//if status of current neighbor == status of any valid neighbor, its an invalid move and we set result = false and break the loop
							result = false;
						}
						s++;						
					}
				}
			}
		}
		return result;	
	}
	
	/* This method uses exception handling to find out the valid neighbors for the given grid
	 * Since this is a 2-d grid, not all the grids have 8 neighbors and hence the use of exception handling
	 * @param neighbourStatus an array list which contains the status of all the neighbors of a given grid, initially empty, but this arraylist gets update in this function
	 * @param currentGrid, the current grid box whose corresponding neighbors were finding 
	 * @param currentRow, the row in which this current grid box is, in the 2-d array
	 * @param currentCol, the column in which this current grid box is, in the 2-d array
	 *  
	 */
	public void neighbourStats(ArrayList<Integer> neighbourStatus, GridBox currentGrid, int currentRow, int currentCol) {
		
		try { neighbourStatus.add(gridsToAdd[currentRow-1][currentCol-1].getStatus());}
		catch (Exception e) {}		//if error, ignore and do nothing i.e. dont add to the arraylist as not a valid neighbor

		try { neighbourStatus.add(gridsToAdd[currentRow-1][currentCol].getStatus());}
		catch (Exception e) {}

		try { neighbourStatus.add(gridsToAdd[currentRow-1][currentCol+1].getStatus());}
		catch (Exception e) {}

		try { neighbourStatus.add(gridsToAdd[currentRow][currentCol-1].getStatus());}
		catch (Exception e) {}

		try { neighbourStatus.add(gridsToAdd[currentRow][currentCol+1].getStatus());}
		catch (Exception e) {}

		try { neighbourStatus.add(gridsToAdd[currentRow+1][currentCol-1].getStatus());}
		catch (Exception e) {}

		try { neighbourStatus.add(gridsToAdd[currentRow+1][currentCol].getStatus());}
		catch (Exception e) {}

		try { neighbourStatus.add(gridsToAdd[currentRow+1][currentCol+1].getStatus());}
		catch (Exception e) {}
			
			
		}
	
	
	/* This is an alternative method to the above which uses conditional statements to find the valid neighbors of a given cell/grid 
	 * Since this is a 2-d grid, not all the grids have 8 neighbors and hence the use of exception handling
	 * @param neighbourStatus an array list which contains the status of all the neighbors of a given grid, initially empty but it gets modified by the function
	 * @param currentGrid, the current grid box whose corresponding neighbors were finding 
	 * @param currentRow, the row in which this current grid box is, in the 2-d array
	 * @param currentCol, the column in which this current grid box is, in the 2-d array
	 * 
	 * 
	 * public void neighbourStats(ArrayList<Integer> neighbourStatus, GridBox
	 * currentGrid, int currentRow, int currentCol) { if ( (currentRow-1 >=0) &&
	 * (currentRow-1<= row-1) && (currentCol-1 >=0) && (currentCol-1 <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow-1][currentCol-1].getStatus()); } if
	 * ( (currentRow-1 >=0) && (currentRow-1<= row-1) && (currentCol >=0) &&
	 * (currentCol <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow-1][currentCol].getStatus()); } if (
	 * (currentRow-1 >=0) && (currentRow-1<= row-1) && (currentCol+1 >=0) &&
	 * (currentCol+1 <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow-1][currentCol+1].getStatus()); } if
	 * ( (currentRow >=0) && (currentRow<= row-1) && (currentCol-1 >=0) &&
	 * (currentCol-1 <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow][currentCol-1].getStatus()); } if (
	 * (currentRow >=0) && (currentRow<= row-1) && (currentCol+1 >=0) &&
	 * (currentCol+1 <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow][currentCol+1].getStatus()); } if (
	 * (currentRow+1 >=0) && (currentRow+1<= row-1) && (currentCol-1 >=0) &&
	 * (currentCol-1 <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow+1][currentCol-1].getStatus()); } if
	 * ( (currentRow >=0) && (currentRow<= row-1) && (currentCol+1 >=0) &&
	 * (currentCol+1 <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow][currentCol+1].getStatus()); } if (
	 * (currentRow+1 >=0) && (currentRow+1<= row-1) && (currentCol+1 >=0) &&
	 * (currentCol+1 <= col-1) ){
	 * neighbourStatus.add(gridsToAdd[currentRow+1][currentCol+1].getStatus()); }
	 * 
	 * }
	 */
	

}

	



