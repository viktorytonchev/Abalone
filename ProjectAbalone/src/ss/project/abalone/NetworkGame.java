package ss.project.abalone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

import ss.project.abalone.exception.InvalidMoveException;
import ss.project.abalone.exception.InvalidPasswordException;
import ss.project.abalone.exception.InvalidSelectionException;
import ss.project.abalone.exception.JoinGameException;
import ss.project.abalone.server.AbaloneClientHandler;
import ss.utils.TextIO;

/**
 * Class for maintaining the Abalone game.
 * Lab assignment Module 2
 * @author Dimitar Popov and Viktor Tonchev
 */
public class NetworkGame {
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

    /**
     * Number of players joined.
     * @invariant the index is always between 0 and NUMBER_PLAYERS
     */
    private int joinedPlayers;

    /**
     * Number of players that are ready.
     * @invariant the index is always between 0 and NUMBER_PLAYERS
     */
    private int readyPlayers;

    /**
     * Game password.
     * @invariant the password is not hashed
     */
    private String password;
    
    /**
     * Game password.
     * @invariant the password is not hashed
     */
    private String name;

    private HashMap<String, AbaloneClientHandler> playerHandlers;
    
    // -- Constructors -----------------------------------------------

    public NetworkGame(String gameName, String password, int capacity){
        board = new Board();
        players = new Player[capacity];
        numberOfPlayers = capacity;
        name = gameName;
        this.password = password;
        current = 0;
        joinedPlayers = 0;
        readyPlayers = 0;
        playerHandlers = new HashMap<String, AbaloneClientHandler>();
    }    
    
    public synchronized void join(String playerName, AbaloneClientHandler handler)  throws JoinGameException {

    	if (  joinedPlayers == numberOfPlayers) {
    		throw new JoinGameException("The game is full. Please join another game.");
    	}
    	
    	for ( int i = 0;i<players.length;i++) {
    		if ( players[i] == null) {
    	    	players[i] = new HumanPlayer(playerName, Color.getbyIndex(joinedPlayers));
    	    	// Put the handler
    	    	playerHandlers.put(playerName, handler);
    	    	handler.joinActiveGame(this);
    	    	joinedPlayers++;
    	    	// Exit if the player was joined
    	    	break;
    		}
    	}

    	if (  joinedPlayers == numberOfPlayers) {
    		if ( numberOfPlayers == 2 ) {
    			board.initForTwo(players[0], players[1]);
    		} else if ( numberOfPlayers == 3 )  {
    			board.initForThree(players[0], players[1], players[2]);
    		} else if ( numberOfPlayers == 4 ) {
    			board.initForFour(players[0], players[1], players[2], players[3]);
    		}    		
    	}
    }


    public synchronized void quit(String playerName)  throws JoinGameException {

    	for ( int i = 0;i<players.length;i++) {
    		if ( players[i] != null) {
    			if ( players[i].getName().equals(playerName)) {
        	    	players[i] = null;
        	    	AbaloneClientHandler handler = playerHandlers.remove(playerName);
        	    	handler.quitActiveGame();
        	    	joinedPlayers--;
        	    	break;
    			}
    		}
    	}
    }
    
    // -- Commands ---------------------------------------------------


    public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public int getJoinedPlayers() {
		return joinedPlayers;
	}

	public void setJoinedPlayers(int joinedPlayers) {
		this.joinedPlayers = joinedPlayers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasPassword() {
		return this.password != "";
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

	public boolean checkPassword(String password2) {
		return this.password.equals(password2);
	}
	
	/**
	 * Returns a Collection with all client handlers of an active game
	 * 
	 * @return Collection with all client handlers
	 */
    public Collection<AbaloneClientHandler> getClientHandlers(){
    	return this.playerHandlers.values();
    }
    
    public AbaloneClientHandler getNextTurnClientHandler() {
    	// Get next turn player handler from the hashmap by current player name
    	return this.playerHandlers.get(players[current].getName());
    }

    
	public void move(String[] move) throws InvalidSelectionException,  InvalidMoveException{
		try {
			Player currentPlayer = players[current];
			ArrayList<Field> selection = new ArrayList<Field>();
			
			Direction direction = Direction.stringToDirection(move[move.length -1]);
			
			// Remove direction first
			for (int i=0;i<move.length-1;i++) {
				// BOARDINDEX
				selection.add(board.getField(Integer.parseInt(move[i])-1));
			}
			
			ArrayList<Field> expandedSelection  = board.validateAndExpandPlayerSelection2(currentPlayer, selection, direction);
			board.move(currentPlayer, expandedSelection, direction);
			
			if (current == numberOfPlayers -1 ) {
				current = 0;
			} else {
				current++;
			}
		} catch (InvalidSelectionException | InvalidMoveException  e) {
			throw e;
		}

		
	}

	public synchronized void ready(String username) {
		// TODO Auto-generated method stub
		if ( readyPlayers < numberOfPlayers ) {
			readyPlayers++;
			
			if ( readyPlayers < numberOfPlayers ) {
			
			}
		}
		
	}

	public int getReadyPlayers() {
		return readyPlayers;
	}

	public boolean isOver() {
		if ( isTeamGame ) {
			return ( players[0].getPushedOutBalls() + players[2].getPushedOutBalls() ) == 6 ||
					( players[1].getPushedOutBalls() + players[1].getPushedOutBalls() == 6 );	
		} else {
			for (int i=0;i<players.length;i++) {
				if (players[i].getPushedOutBalls()==6) {
					return true;
				}
			}
			
			return false;
			
		}
			
	}

}
