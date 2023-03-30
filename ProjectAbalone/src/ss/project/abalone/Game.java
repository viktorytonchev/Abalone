package ss.project.abalone;

import java.util.ArrayList;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

import ss.utils.TextIO;

/**
 * Class for maintaining the Abalone game.
 * Lab assignment Module 2
 * @author Dimitar Popov and Viktor Tonchev
 */
public class Game {
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
    private Board board;

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
    private int current;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Game object.
     * @requires s0 and s1 to be non-null
     * @param s0 the first player
     * @param s1 the second player
     */
    public Game(Player s0,  Player s1){
        board = new Board();
        players = new Player[2];
        numberOfPlayers = 2;
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }

    public Game(Player s0,  Player s1, Player s2){
        board = new Board();
        players = new Player[3];
        numberOfPlayers = 3;
        players[0] = s0;
        players[1] = s1;
        players[2] = s2;
        current = 0;
    }
    /*
     * Constructs a game with 4 players. The game is a team game now
     */
    public Game(Player s0,  Player s1, Player s2, Player s3 ){
        board = new Board();
        players = new Player[4];
        numberOfPlayers = 4;
        players[0] = s0;
        players[1] = s1;
        players[2] = s2;
        players[3] = s3;
        current = 0;
    }

    /**
     * Resets the game. <br>
     * The board is emptied and player[0] becomes the current player.
     */
    private void reset() {
        current = 0;
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

    public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	/**
     * Prints the game situation.
     */
    private void update() {
        System.out.println("\ncurrent game situation: \n\n" + board.toString()
                + "\n");
    }

    private void printField(Field field) {
    	System.out.print(field.toString2());
    }
    /**
     * Prints the result of the last game. <br>
     * @requires the game to be over
     */
    private void printResult() {
        if (this.hasWinner()) {
        	// TODO
//            Player winner = board.isWinner(players[0].getMark()) ? players[0]
//                    : players[1];
//            System.out.println("Speler " + winner.getName() + " ("
//                    + winner.getMark().toString() + ") has won!");
//        } else {
//            System.out.println("Draw. There is no winner!");
        }
    }


    /**
     * Returns true if the game is over. The game is over when there is a winner.
     * @ensures true if the there is a winner
     * @return true if the game is over
     */
    public boolean gameOver() {
    	return this.hasWinner();
    }
 

    /**
     * Returns true if the game has a winner. 
     * @ensures true when ............ 
     * @return true if the game has a winner.
     */
    public boolean hasWinner() {
        boolean hasWinner  = false;
        
    	if ( !isTeamGame ) {
        	for ( int i = 0; i< numberOfPlayers; i++ ) {
        		hasWinner = board.getPushedOutBalls(players[i].getColor()) == 6;
        		if ( hasWinner ) {
        			break;
        		}
        	}
        } else {
        	hasWinner = ( board.getPushedOutBalls(players[0].getColor()) + 
        			      board.getPushedOutBalls(players[2].getColor()) ) == 6 ||
        			      ( board.getPushedOutBalls(players[1].getColor()) + 
                			      board.getPushedOutBalls(players[3].getColor()) ) == 6;
        }
    	
    	return hasWinner;
    }
    

}
