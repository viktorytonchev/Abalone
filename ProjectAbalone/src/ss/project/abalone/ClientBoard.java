package ss.project.abalone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ss.project.abalone.exception.InvalidMoveException;

/**
 * Board for the Abalone game. Module 2 project.
 *
 * @author Dimitar Popov and Viktor Tonchev
 * @version $Revision: 1.0 $
 */
public class ClientBoard extends SimpleBoard {
    

    // -- Constructors -----------------------------------------------

    /**
     * Creates an empty board. Initializes references of the neighbouring field pointers for 
     * each field on the board
     * @ensures all fields are EMPTY
     * @ensures all fields neighbour pointers are set up
     */
    public ClientBoard() {
    	super();
    }
    
    private ArrayList<Field> expandForSummito( ArrayList<Field> moveList, Direction direction){
    	Collections.sort(moveList);
    	
    	if ( moveList.size() == 1 ) {
    		return moveList;
    	}
    	
    	ArrayList<Field> expandedMove = new ArrayList<Field>(moveList);
    	
    	SelectionOrientation orientation = detemineSelectionOrientation(moveList);
    	
		boolean moveToABiggerFieldIndex = ( orientation == SelectionOrientation.LEFTRIGHT && direction == Direction.RIGHT ) ||
				( orientation == SelectionOrientation.UPLEFTDOWNRIGHT && direction == Direction.DOWNRIGHT ) ||
				( orientation == SelectionOrientation.UPRIGHTDOWNLEFT && direction == Direction.DOWNLEFT );

		Field frontBallField;
		Field endBallField;

		// Determine first field to expand
		if (! moveToABiggerFieldIndex) {
			frontBallField = moveList.get(moveList.size() - 1);
			endBallField = moveList.get(0);
		} else {
			frontBallField = moveList.get(0);
			endBallField = moveList.get(moveList.size() - 1);
		}
    	
		Field neighbour = endBallField.getNeighbour(direction);
		
		while ( neighbour!= null ) {
			if ( neighbour.getBall() == null ) {
				break;
			}
			expandedMove.add(neighbour);
			neighbour = neighbour.getNeighbour(direction);
		}
		
		return expandedMove;
    }

    /**
	 * Does dummy move, no validations, no rules applied. Used
	 * @param move
	 */
	public void move(String[] move) {
		Direction direction = Direction.stringToDirection(move[move.length -1]);
		
		// Sort the move
		ArrayList<Field> moveListExp = new ArrayList<Field>();
		// Remove direction first
		for (int i=0;i<move.length-1;i++) {
			//BOARDINDEX
			moveListExp.add(this.getField(Integer.parseInt(move[i])-1));
		}
		
		Collections.sort(moveListExp);
		
		ArrayList<Field> moveList = expandForSummito(moveListExp, direction);

		Collections.sort(moveList);
		
		// If only one ball selected, move it
		if (moveList.size() == 1) {
			Field f = moveList.get(0); 
			f.getNeighbour(direction).setBall(f.getBall());
			f.clearBall();
			return;
		}

		// if more than one ball is selected, do the move
		SelectionOrientation orientation = detemineSelectionOrientation(moveList);
		
		if ( (orientation == SelectionOrientation.LEFTRIGHT && (direction == Direction.LEFT || direction == Direction.RIGHT )) ||
				(orientation == SelectionOrientation.UPLEFTDOWNRIGHT && (direction == Direction.UPLEFT || direction == Direction.DOWNRIGHT )) ||
				(orientation == SelectionOrientation.UPRIGHTDOWNLEFT && (direction == Direction.UPRIGHT || direction == Direction.DOWNLEFT )) ){
			// the move is inline move

			boolean moveToABiggerFieldIndex = ( orientation == SelectionOrientation.LEFTRIGHT && direction == Direction.RIGHT ) ||
					( orientation == SelectionOrientation.UPLEFTDOWNRIGHT && direction == Direction.DOWNRIGHT ) ||
					( orientation == SelectionOrientation.UPRIGHTDOWNLEFT && direction == Direction.DOWNLEFT );

			Field frontBallField;

			// Determine first field to move
			if (moveToABiggerFieldIndex) {
				frontBallField = moveList.get(moveList.size() - 1);

			} else {
				frontBallField = moveList.get(0);
			}
			Field pushBall1Field = frontBallField.getNeighbour(direction);

			if (moveToABiggerFieldIndex) {
				for (int i = moveList.size() -1; i>=0; i--) {
					Field current = moveList.get(i);
					//CHANGED
					if (pushBall1Field !=null) {
						pushBall1Field.setBall(current.getBall());
					}
					current.clearBall();
					pushBall1Field=current;
				}
			} else {
				for (int i = 0; i< moveList.size(); i++) {
					Field current = moveList.get(i);
					//CHANGED
					if (pushBall1Field !=null) {
						pushBall1Field.setBall(current.getBall());
					}
					current.clearBall();
					pushBall1Field=current;
				}
			}

		} else {
			for (Field f: moveList) {
				f.getNeighbour(direction).setBall(f.getBall());
				f.clearBall();
			}
		}
	}
	/**
	 * The method returns the selection orientation of an ArrayList with selected fields for a turn.
	 *   
	 * 
	 * @param column The selection of fields
	 * @return The selection orientation as SelectionOrientation
	 */
	public SelectionOrientation detemineSelectionOrientation(ArrayList<Field> column) {
		SelectionOrientation orientation = SelectionOrientation.INVALID;
		
		Field field0 = column.get(0);
		Field field1 = column.get(1);
		if (field1 == field0.left || field1 == field0.right) {
			orientation = SelectionOrientation.LEFTRIGHT;
		} else if (field1 == field0.downRight ) {
			orientation = SelectionOrientation.UPLEFTDOWNRIGHT;
		} else if ( field1 == field0.downLeft ) {
			orientation = SelectionOrientation.UPRIGHTDOWNLEFT;
		} else {
			orientation = SelectionOrientation.INVALID;
		}
		
		return orientation;
	}
	
	}
