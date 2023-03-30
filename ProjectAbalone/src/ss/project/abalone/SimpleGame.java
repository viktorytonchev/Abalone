package ss.project.abalone;

import java.util.ArrayList;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

import ss.utils.TextIO;

/**
 * Class for maintaining the Abalone game.
 * Lab assignment Module 2
 * @author Dimitar Popov and Viktor Tonchev
 */
public class SimpleGame {
   /**
     * The number of players.
     * @invariant Can be 2,3 or 4 
     */
	private int numberOfPlayers;

   /**
     * Shows if the game is team or mellee game.
     * @invariant 2 or 3 player games are not a team games, only 4 player game is
     */
    boolean isTeamGame = false;

   /**
     * The board.
     * @invariant board is never null
     */
    private ClientBoard board;

    /**
     * The 2, 3 or 4 players of the game.
     * @invariant the length of the array equals numberOfPlayers
     * @invariant all array items are never null
     */
    private Player[] players;

    /**
     * Index of the current player.
     * @invariant the index is always between 0 and NUMBER_PLAYERS
     */
    private int currentPlayerIndex;

	private String name;

    // -- Constructors -----------------------------------------------

    public String getName() {
		return name;
	}



	/**
     * Creates a new Game object.
     */
    public SimpleGame(String name, int playerCount){

        this.numberOfPlayers = playerCount;
        this.name = name;
        
    	this.board = new ClientBoard();
    	this.players = new Player[playerCount];
        
        // Init Players
        for (int i = 0; i< playerCount; i++) {
            players[i] = new HumanPlayer( "Player 1", Color.getbyIndex(i) );
        }
        currentPlayerIndex = 0;
        // reset the board
        reset();
    }



    /**
     * Resets the game. <br>
     * The board is emptied and player[0] becomes the current player.
     */
    private void reset() {
    	currentPlayerIndex = 0;
        board.reset();

        switch (numberOfPlayers) {
		case 2:
			board.initForTwo(players[0], players[1]);
			break;
		case 3:
			board.initForThree(players[0], players[1], players[2]);
			break;
		case 4:
		    board.initForFour(players[0], players[1], players[2], players[3]);
			break;
		default:
		    board.initForTwo(players[0], players[1]);
			break;
		}
    }

    // was SimpleBoard
    public ClientBoard getBoard() {
		return board;
	}

	public void setBoard(ClientBoard board) {
		this.board = board;
	}


    public String toString() {
       return "\nCurrent game situation: \n\n" + board.toString()
                + "\n";
    }

	public void doUpdate(String[] move) {
		
		board.move(move);
		
	}

	public void setPlayerName(Color ballColor, String userName) {
		for (int i=0;i<numberOfPlayers;i++) {
			if ( players[i].getColor().equals(ballColor) ) {
				players[i].setName(userName);
			}
		}
		
	}
 
}
