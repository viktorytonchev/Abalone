package ss.project.abalone;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Abstract class for keeping a player in the Abalone game. Module 2 
 * project
 * 
 * 
 * @author Dimitar Popov and Viktor Tonchev
 * @version $Revision: 1.0 $
 */
public abstract class Player {

    // -- Instance variables -----------------------------------------

    private String name;
    private Color color;
    private Player teammate;
    private int pushedOutBalls;
    
    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Player object.
     * @requires name is not null
     * @requires color is either WHITE, BLACK, BLUE or RED
     * @ensures the Name of this player will be name
     * @ensures the Mark of this player will be mark
     */
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.pushedOutBalls = 0;
        
        teammate=null;
    }

    // -- Queries ----------------------------------------------------

    /**
     * Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the color of the player.
     */
    public Color getColor() {
        return color;
    }

	public Player getTeammate() {
		return teammate;
	}

	public void setTeammate(Player teamMate) {
		this.teammate = teamMate;
	}

	public void createTeam(Player teamMate) {
		this.teammate = teamMate;
		teamMate.setTeammate(this);
	}

	public boolean isInTeam() {
		return teammate != null;
	}
    // -- Commands ---------------------------------------------------

	public void setName(String name) {
		this.name=name;
	}

	public int getPushedOutBalls() {
		return pushedOutBalls;
	}

	public void increasePushedOutBalls() {
		this.pushedOutBalls ++;
	};

    /**
     * Makes a move on the board. <br>
     * @requires board is not null and not full
     * @param board the current board
     */
//    public void makeMove(Board board) {
//        int choice = determineMove(board);
//        board.setField(choice, getMark());
//    }

    
}
