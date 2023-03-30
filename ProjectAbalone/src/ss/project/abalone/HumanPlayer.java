package ss.project.abalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ss.project.abalone.exception.InvalidSelectionException;


/**
 * Class for maintaining a human player in ABalone Module 2 lab assignment
 * 
 * @authors Dimitar Popov and Viktor Tonchev
 * @version $Revision: 1.0 $
 */
public class HumanPlayer extends Player {

	// -- Constructors -----------------------------------------------

	/**
	 * Creates a new human player object.
	 * @requires name is not null
	 * @requires color is either YELLOW, BLACK, BLUE or RED
	 * @ensures the Name of this player will be name
	 * @ensures the Color of this player will be color
	 */
	public HumanPlayer(String name, Color color) {
		super(name, color); 
	}

}
