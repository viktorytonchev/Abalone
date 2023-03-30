package ss.project.abalone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ss.project.abalone.exception.InvalidSelectionException;

public class SimpleBoard {

	public static final int NUMBER_OF_FIELDS = 61;
	public static final int NUMBER_OF_ROWS = 9;
	public static final int INVALID_FIELD_INDEX = -1;
	protected Field[] fields;
	protected Map<Color, Integer> pushedOutBallsCount;

	public SimpleBoard() {
    	// Construct the board
    	fields = new Field[61];
    	pushedOutBallsCount = new HashMap();
    	
    	for(int i = 0;  i < NUMBER_OF_FIELDS; i++) {
    		this.fields[i] = new Field(i);
    	}

    	for(int i = 0;  i < NUMBER_OF_FIELDS; i++) {
    		Field field = fields[i];
    		
    		field.downLeft = getNeighbour(field, Direction.DOWNLEFT);
    		field.downRight = getNeighbour(field, Direction.DOWNRIGHT);
    		field.upperLeft = getNeighbour(field, Direction.UPLEFT);
    		field.upperRight = getNeighbour(field, Direction.UPRIGHT);
    		field.left = getNeighbour(field, Direction.LEFT);
    		field.right = getNeighbour(field, Direction.RIGHT);
    	}

	}

	/**
	 * This method is used only to init the board. Don't use it to get field neighbours
	 * during the game. Use corresponding method from the Field class
	 * @param field
	 * @param direction
	 * @return
	 */
	protected Field getNeighbour(Field field, Direction direction) {
		int index = calculateNeigbourIndex(field, direction);
		
		if ( index == INVALID_FIELD_INDEX ) {
			return null;
		} else {
			return getField(index);
		}
			
	}

	/**
	 * Returns the content of the field i.
	 * @requires i to be a valid field
	 * @ensures the result to be either EMPTY, XX or OO
	 * @param i the number of the field (see NUMBERING)
	 * @return the mark on the field
	 */
	public Field getField(int i) {
		return this.fields[i];
	}

	/**
	 * Returns true if the field i is empty.
	 * @requires i to be a valid field index
	 * @ensures true when there is no ball at the field
	 * @param i the index of the field (see NUMBERING)
	 * @return true if the field is empty
	 */
	public boolean isEmptyField(int i) {
		// TODO: implement, see exercise P-4.6
		return (this.getField(i).getBall() == null);
	}

	/**
	 * Returns a String representation of this board. In addition to the current
	 * situation, the String also shows the numbering of the fields.
	 *
	 * @return the game situation as String
	 */
	public String toString() {
		// TODO make smarter implementation by using row and ranges from the field
	    String s = "";
	    s = "           " + getField(0).toString() + "  "+ 
	                        getField(1).toString() + "  "+ 
	    		            getField(2).toString() + "  "+ 
	                        getField(3).toString() + "  "+
	                        getField(4).toString() + "  \n";
	    s+="                                          \n";
	    s+="         " + getField(5).toString() + "  "+ 
	                    getField(6).toString() + "  "+ 
			            getField(7).toString() + "  "+ 
		                getField(8).toString() + "  "+
		                getField(9).toString() + "  "+
				        getField(10).toString() + "\n";
	    s+="                                          \n";
	    s+="       " + getField(11).toString() + "  "+ 
	            	  getField(12).toString() + "  "+ 
	            	  getField(13).toString() + "  "+ 
	            	  getField(14).toString() + "  "+
	            	  getField(15).toString() + "  "+
	            	  getField(16).toString() + "  "+
	            	  getField(17).toString() + "\n";
	    s+="                                          \n";
	    s+="     "   + getField(18).toString() + "  "+ 
	            	  getField(19).toString() + "  "+ 
	            	  getField(20).toString() + "  "+ 
	            	  getField(21).toString() + "  "+
	            	  getField(22).toString() + "  "+
	            	  getField(23).toString() + "  "+
	            	  getField(24).toString() + "  "+
	            	  getField(25).toString() + "\n";
	    s+="                                          \n";
	    s+="   "     + getField(26).toString() + "  "+ 
	            	  getField(27).toString() + "  "+ 
	            	  getField(28).toString() + "  "+ 
	            	  getField(29).toString() + "  "+
	            	  getField(30).toString() + "  "+
	            	  getField(31).toString() + "  "+
	            	  getField(32).toString() + "  "+
	            	  getField(33).toString() + "  "+
	            	  getField(34).toString() + "\n";
	    s+="                                          \n";
	    s+= "     " + getField(35).toString() + "  "+ 
					getField(36).toString() + "  "+ 
					getField(37).toString() + "  "+ 
					getField(38).toString() + "  "+
					getField(39).toString() + "  "+
					getField(40).toString() + "  "+
					getField(41).toString() + "  "+
					getField(42).toString()+ "\n";
	    s+="                                          \n";
	    s+= "       " + getField(43).toString() + "  "+ 
					  getField(44).toString() + "  "+ 
					  getField(45).toString() + "  "+ 
					  getField(46).toString() + "  "+
					  getField(47).toString() + "  "+
					  getField(48).toString() + "  "+
					  getField(49).toString()+ "\n";
	    s+="                                          \n";
	    s+= "         " + getField(50).toString() + "  "+ 
					  getField(51).toString() + "  "+ 
					  getField(52).toString() + "  "+ 
					  getField(53).toString() + "  "+
					  getField(54).toString() + "  "+
					  getField(55).toString()+ "\n";
	    s+="                                          \n";
	    s+= "           " + getField(56).toString() + "  "+ 
		                getField(57).toString() + "  "+ 
			            getField(58).toString() + "  "+ 
		                getField(59).toString() + "  "+
		                getField(60).toString()+ "\n";
	    s+="                                          \n";
	    return s;
	}

	/**
	 * Empties all fields of this board (i.e. removes the balls from them).
	 * @ensures all fields don't have balls on them
	 */
	public void reset() {
		for(int i = 0; i < NUMBER_OF_FIELDS; i++) {
			this.getField(i).clearBall();;
		}
	}

	/**
	 * Sets the content of Field i to the ball b.
	 * @requires i to be a valid field index
	 * @ensures The ball of the Field i to be set to Ball b
	 * @param i the field index (see NUMBERING)
	 * @param b the ball to be placed at the field
	 */
	public void setBallAtField(int i, Ball ball) {
		this.fields[i].setBall(ball);
	}

	/**
	 * Initializes the Board for two players. The colors of the balls placed at the table are get from the players.
	 * @param p1 Player 1
	 * @param p2 Player 2
	 */
	public void initForTwo(Player p1, Player p2) {
		Color c1 = p1.getColor();
		Color c2 = p2.getColor();
		
		pushedOutBallsCount.put(c1, 0);
		pushedOutBallsCount.put(c2, 0);
		
		// First Player balls
		for (int i = 0; i < 5; i++) {
			fields[i].setBall(new Ball(p1.getColor()));
		}
		// Second row
		for (int i = 5; i < 11; i++) {
			fields[i].setBall(new Ball(p1.getColor()));
		}
	
		for (int i = 13; i <= 15; i++) {
			fields[i].setBall(new Ball(p1.getColor()));
		}
		
		// Second Player balls
		for (int i = 45; i <= 47; i++) {
			fields[i].setBall(new Ball(p2.getColor()));
		}
	
		for (int i = 50; i < 56; i++) {
			fields[i].setBall(new Ball(p2.getColor()));
		}
	
		for (int i = 56; i < 61; i++) {
			fields[i].setBall(new Ball(p2.getColor()));
		}
	}

	/**
	 * Initializes the Board for three players. The colors of the balls placed at the table are get from the players.
	 * @param p1 Player 1
	 * @param p2 Player 2
	 * @param p3 Player 3
	 */
	public void initForThree(Player p1, Player p2, Player p3) {
		pushedOutBallsCount.put(p1.getColor(), 0);
		pushedOutBallsCount.put(p2.getColor(), 0);
		pushedOutBallsCount.put(p3.getColor(), 0);
	
		// Player 1 fields
		fields[0].setBall(new Ball(p1.getColor()));
		fields[1].setBall(new Ball(p1.getColor()));
		fields[5].setBall(new Ball(p1.getColor()));
		fields[6].setBall(new Ball(p1.getColor()));
		fields[11].setBall(new Ball(p1.getColor()));
		fields[12].setBall(new Ball(p1.getColor()));
		fields[18].setBall(new Ball(p1.getColor()));
		fields[19].setBall(new Ball(p1.getColor()));
		fields[26].setBall(new Ball(p1.getColor()));
		fields[27].setBall(new Ball(p1.getColor()));
		fields[35].setBall(new Ball(p1.getColor()));
	
		// Player 2 fields
		fields[3].setBall(new Ball(p2.getColor()));
		fields[4].setBall(new Ball(p2.getColor()));
		fields[9].setBall(new Ball(p2.getColor()));
		fields[10].setBall(new Ball(p2.getColor()));
		fields[16].setBall(new Ball(p2.getColor()));
		fields[17].setBall(new Ball(p2.getColor()));
		fields[24].setBall(new Ball(p2.getColor()));
		fields[25].setBall(new Ball(p2.getColor()));
		fields[33].setBall(new Ball(p2.getColor()));
		fields[34].setBall(new Ball(p2.getColor()));
		fields[42].setBall(new Ball(p2.getColor()));
	
		// Player 3 fields
		for(int i = 50; i <= 60; i++) {
			fields[i].setBall(new Ball(p3.getColor()));
		}
		
	}

	/**
	 * Initializes the Board for four players. The colors of the balls placed at the table are get from the players.
	 * @param p1 Player 1
	 * @param p2 Player 2
	 * @param p3 Player 3
	 * @param p3 Player 4
	 */
	public void initForFour(Player p1, Player p2, Player p3, Player p4) {
		pushedOutBallsCount.put(p1.getColor(), 0);
		pushedOutBallsCount.put(p2.getColor(), 0);
		pushedOutBallsCount.put(p3.getColor(), 0);
		pushedOutBallsCount.put(p4.getColor(), 0);
	
		//Player 1 fields
	   	fields[0].setBall(new Ball(p1.getColor()));
		fields[1].setBall(new Ball(p1.getColor()));
		fields[2].setBall(new Ball(p1.getColor()));
		fields[3].setBall(new Ball(p1.getColor()));
		fields[6].setBall(new Ball(p1.getColor()));
		fields[7].setBall(new Ball(p1.getColor()));
		fields[8].setBall(new Ball(p1.getColor()));
		fields[13].setBall(new Ball(p1.getColor()));
		fields[14].setBall(new Ball(p1.getColor()));
	
		//Player 2 fields
		fields[10].setBall(new Ball(p2.getColor()));
		fields[17].setBall(new Ball(p2.getColor()));
		fields[25].setBall(new Ball(p2.getColor()));
		fields[34].setBall(new Ball(p2.getColor()));
		fields[16].setBall(new Ball(p2.getColor()));
		fields[24].setBall(new Ball(p2.getColor()));
		fields[33].setBall(new Ball(p2.getColor()));
		fields[23].setBall(new Ball(p2.getColor()));
		fields[32].setBall(new Ball(p2.getColor()));
	
		//Player 2 fields
		fields[57].setBall(new Ball(p3.getColor()));
		fields[58].setBall(new Ball(p3.getColor()));
		fields[59].setBall(new Ball(p3.getColor()));
		fields[60].setBall(new Ball(p3.getColor()));
		fields[52].setBall(new Ball(p3.getColor()));
		fields[53].setBall(new Ball(p3.getColor()));
		fields[54].setBall(new Ball(p3.getColor()));
		fields[46].setBall(new Ball(p3.getColor()));
		fields[47].setBall(new Ball(p3.getColor()));
	
		//Player 2 fields
		fields[26].setBall(new Ball(p4.getColor()));
		fields[35].setBall(new Ball(p4.getColor()));
		fields[43].setBall(new Ball(p4.getColor()));
		fields[50].setBall(new Ball(p4.getColor()));
		fields[27].setBall(new Ball(p4.getColor()));
		fields[36].setBall(new Ball(p4.getColor()));
		fields[44].setBall(new Ball(p4.getColor()));
		fields[28].setBall(new Ball(p4.getColor()));
		fields[37].setBall(new Ball(p4.getColor()));
		
	}

	private boolean hasNeighbour(Field field, Direction direction) {
		return calculateNeigbourIndex(field, direction ) == INVALID_FIELD_INDEX;
	}

	private boolean isIndexinRow(int index, int row) {
		boolean bReturn = false;
		
		switch (row) {
		case 0:
			bReturn = index>=0 && index<=4;
			break;
		case 1:
			bReturn = index>=5 && index<=10;
			break;
		case 2:
			bReturn = index>=11 && index<=17;
			break;
		case 3:
			bReturn = index>=18 && index<=25;
			break;
		case 4:
			bReturn = index>=26 && index<=34;
			break;
		case 5:
			bReturn = index>=35 && index<=42;
			break;
		case 6:
			bReturn = index>=43 && index<=49;
			break;
		case 7:
			bReturn = index>=50 && index<=55;
			break;
		case 8:
			bReturn = index>=56 && index<=60;
			break;
		default:
			bReturn=false;
			break;
		}
		return bReturn;
	}

	public int calculateNeigbourIndex(Field field, Direction direction) {
		int index = INVALID_FIELD_INDEX;
		int row = field.getRow();
		
		if (direction == Direction.DOWNRIGHT) {
			if (row >= 0 && row<=3 ) {
				index = field.getIndex() + 6 + field.getRow();
			} else if(row >= 4 && row<=7) {
				index = field.getIndex() + 9 -( field.getRow() - 4 ) ;
			} else {
				index = INVALID_FIELD_INDEX;
			}
		}
		
		if (direction == Direction.DOWNLEFT) {
			if (row >= 0 && row<=3 ) {
				index = field.getIndex() + 5 + field.getRow();
			} else if(row >= 4 && row<=7) {
				index = field.getIndex() + 8 -( field.getRow() - 4 ) ;
			} else {
				index = INVALID_FIELD_INDEX;
			}
		}
	
		
		if (direction == Direction.UPRIGHT) {
			if (row >= 1 && row<=4 ) {
				index = field.getIndex() - 4 - field.getRow();
			} else if(row >= 5 && row<=8) {
				index = field.getIndex() - 8 + ( field.getRow() - 5 ) ;
			} else {
				index = INVALID_FIELD_INDEX;
			}
		}
	
		if (direction == Direction.UPLEFT) {
			if (row >= 1 && row<=4 ) {
				index = field.getIndex() - 5 - field.getRow();
			} else if(row >= 5 && row<=8) {
				index = field.getIndex() - 9 +( field.getRow() - 5 ) ;
			} else {
				index = INVALID_FIELD_INDEX;
			}
		}
	
		if (direction == Direction.LEFT) {
			int indOfPar = field.getIndex();
			if (indOfPar == 0 || indOfPar == 5 || indOfPar == 11 || indOfPar == 18 || indOfPar == 26
					|| indOfPar == 35 || indOfPar == 43 || indOfPar == 50 || indOfPar == 56) {
				index = INVALID_FIELD_INDEX;
			}
			else {
				index = indOfPar - 1;
			}
		}
		
		if (direction == Direction.RIGHT) {
			int indOfPar = field.getIndex();
			if (indOfPar == 4 || indOfPar == 10 || indOfPar == 17 || indOfPar == 25 || indOfPar == 34
					|| indOfPar == 42 || indOfPar == 49 || indOfPar == 55 || indOfPar == 60) {
				index=  INVALID_FIELD_INDEX;
			}
			else {
				index = indOfPar + 1;
			}			
		}
	
		if (index!=INVALID_FIELD_INDEX) {
			// Check if the calculated theoretical index is in the same row it should be
			// In case the field is at the border of the table this calculation should be done
			// on top of the above calculations
			if (direction==Direction.DOWNLEFT || direction==Direction.DOWNRIGHT) {
				if ( !isIndexinRow(index, row + 1) ){
					index=INVALID_FIELD_INDEX;
				}
			}
			
			if (direction==Direction.UPLEFT || direction==Direction.UPRIGHT) {
				if ( !isIndexinRow(index, row - 1) ){
					index=INVALID_FIELD_INDEX;
				}
			}
			
		}
		
		return index;
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

	public int getPushedOutBalls(Color color) {
		if ( pushedOutBallsCount.get(color) == null ) {
			pushedOutBallsCount.put(color, 0);
		} 
		
		return  pushedOutBallsCount.get(color);
	}

	public void increasePushedOutBalls(Color color) {
		if ( pushedOutBallsCount.get(color) == null ) {
			pushedOutBallsCount.put(color, 0);
		} 
		
		int pushedCount =  pushedOutBallsCount.get(color) + 1;
		
		pushedOutBallsCount.replace(color, pushedCount);
	}

	public boolean isValidFieldIndex(int index) {
		if ( index>= 0 && index <=60) {
			return true;
		} else {
			return false;
		}
	}

}