package ss.project.abalone;

import java.util.ArrayList;

public class Move {
	
	private ArrayList<Field> selection;
	private Direction direction;
	
	public Move() {
		// TODO Auto-generated constructor stub
		selection = new ArrayList<Field>();
		direction = Direction.INVALID;
	}

	public ArrayList<Field> getSelection() {
		return selection;
	}

	public void setSelection(ArrayList<Field> selection) {
		this.selection = selection;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
