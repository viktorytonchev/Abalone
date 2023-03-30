package ss.project.abalone;

public class Field implements Comparable<Field> {

	private int index;
	private int row;
	
    public Field upperLeft;
    public Field downLeft;
    public Field upperRight;
    public Field downRight;
    public Field right;
    public Field left;
    
	public static final int INVALID_INDEX = -1;
	
	private Ball ball;

	
	public Field( int index) {
		// TODO implement logic for row and col
		this.index = index;

		this.setRow(calculateRowByIndex(index));
		ball = null;
		
		// The pointers will be set by the board
		upperLeft = null;
		upperRight = null;
		left = null;
		right = null;
		downLeft = null;
		downRight = null;
	}
	
	//Copy constructor. Will be used for deep copy. (!!important) Deep copy sets the valiues 
	// of the upperLeft, upperRight, left, right, downLeft, downRight pointers to null
	// Deep copy of the board should set these references.
	public Field(Field field) {
		this.index = field.index;
		this.row = field.getRow();

		this.upperLeft = null;
		this.upperRight = null;
		this.left = null;
		this.right = null;
		this.downLeft = null;
		this.downRight = null;
		
		this.ball = new Ball(field.getBall());
	}
	
	public int calculateRowByIndex(int index) {
		int result = this.INVALID_INDEX;
		
		if (index>= 0 && index<=4) {
			result = 0;
		} else if (index>4  && index<=10) {
			result = 1;
		} else if (index>10  && index<=17) {
			result = 2;
		} else if (index>17  && index<=25) {
			result = 3;
		} else if (index>25  && index<=34) {
			result = 4;
		} else if (index>34  && index<=42) {
			result = 5;
		} else if (index>42  && index<=49) {
			result = 6;
		} else if (index>49  && index<=55) {
			result = 7;
		} else if (index>55  && index<=60) {
			result = 8;
		} 
		
		return result;
	}
	
	public boolean isEmpty() {
		return ball == null;
	}
	
	public Ball getBall() {
		return ball;
	}

	public void setBall( Ball newBall) {
		this.ball = newBall;
	}
	
	public void clearBall() {
		this.ball = null;
	}

	public int getIndex() {
		return index;
	}


	public int getRow() {
		return row;
	}

	private void setRow(int row) {
		this.row = row;
	}

	public String toString() {
		String s = ASCIIArtUtils.getBallASCIITExt(this);
		return s;
	}

	public String toString2() {
		String s = "\n    " + ( upperLeft == null ? -1  : upperLeft.getIndex() ) + 
				      "  " + ( upperRight == null ? -1  : upperRight.getIndex() ) +"\n";
		s += "  " + ( left == null ? -1  : left.getIndex()) + "  " + 
				      index + "  " + ( right == null ? -1  : right.getIndex()) +"\n";
		s += "    " + ( downLeft == null ? -1  : downLeft.getIndex() ) + 
		      "  " + ( downRight == null ? -1  : downRight.getIndex() ) +"\n";
		return s;
	}
	@Override
	public int compareTo(Field field) {
		return this.index - field.getIndex(); //-1 0 1
	}

	public Field getNeighbour(Direction direction) {
		
		Field field = null;
		
		switch ( direction ) {
		case UPLEFT:
			field = this.upperLeft;
			break;
		case UPRIGHT:
			field = this.upperRight;
			break;
		case LEFT:
			field =  this.left;
			break;
		case RIGHT:
			field = this.right;
			break;
		case DOWNLEFT:
			field = this.downLeft;
			break;
		case DOWNRIGHT:
			field = this.downRight;
			break;
		default:
			break;
		}
		return field;
	}
	
}
