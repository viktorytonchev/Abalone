package ss.project.abalone.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.BeforeEach;

import ss.project.abalone.Ball;
import ss.project.abalone.Board;
import ss.project.abalone.ClientBoard;
import ss.project.abalone.Color;
import ss.project.abalone.Direction;
import ss.project.abalone.Field;
import ss.project.abalone.Game;
import ss.project.abalone.HumanPlayer;
import ss.project.abalone.Move;
import ss.project.abalone.Player;
import ss.project.abalone.PlayerQuitException;
import ss.project.abalone.exception.InvalidMoveException;
import ss.project.abalone.exception.InvalidSelectionException;


public class GameTest {
    private Board board;
    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;

    @BeforeEach
    public void setUp() {
		p1 = new HumanPlayer("Mitko", Color.YELLOW);
		p2 = new HumanPlayer("Vlado", Color.BLACK);
		p3 = new HumanPlayer("Alex", Color.BLUE);
		p4 = new HumanPlayer("Viktor", Color.RED);
		
		p1.setTeammate(p2);
		p2.setTeammate(p1);
		p3.setTeammate(p4);
		p4.setTeammate(p3);
		
		Game game = new Game(p1, p2, p3, p4);
        board = game.getBoard();
    }

    //@Test
    public void testUpperRightSumitoMoves() {
        board.reset();
        
    	board.setBallAtField(5, new Ball(Color.RED));
    	board.setBallAtField(11, new Ball(Color.BLACK));
    	board.setBallAtField(18, new Ball(Color.BLACK));

    	board.setBallAtField(06, new Ball(Color.RED));
    	board.setBallAtField(12, new Ball(Color.BLACK));
    	board.setBallAtField(19, new Ball(Color.BLACK));
    	board.setBallAtField(27, new Ball(Color.BLACK));

    	board.setBallAtField(07, new Ball(Color.RED));
    	board.setBallAtField(13, new Ball(Color.RED));
    	board.setBallAtField(20, new Ball(Color.BLACK));
    	board.setBallAtField(28, new Ball(Color.BLACK));
    	board.setBallAtField(36, new Ball(Color.BLACK));
    	
    	System.out.println(board.toString());

    	System.out.println("Now we will move two balls from 08 and 14 upper left");
    	ArrayList<Field> column = new ArrayList<>();
    	
    	column.clear();
    	column.add(board.getField(5));
    	column.add(board.getField(11));
    	column.add(board.getField(18));
    	

    	try {
    		board.move(p2, column, Direction.UPRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	assertTrue(board.getField(18).isEmpty());
    	assertTrue(board.getField(0).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(5).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(11).getBall().getColor() == Color.BLACK);
    	
    	column.clear();
    	
    	column.add(board.getField(5));
    	column.add(board.getField(11));
    	column.add(board.getField(00));
    	

    	try {
    		board.move(p2, column, Direction.UPRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	assertTrue(board.getField(11).isEmpty());
    	assertTrue(board.getField(0).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(5).getBall().getColor() == Color.BLACK);
    	assertEquals(board.getPushedOutBalls(Color.RED), 1);

    	// 2nd column
    	column.clear();
    	column.add(board.getField(6));
    	column.add(board.getField(12));
    	column.add(board.getField(19));
    	column.add(board.getField(27));
    	

    	try {
    		board.move(p2, column, Direction.UPRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	assertTrue(board.getField(27).isEmpty());
    	assertTrue(board.getField(1).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(6).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(12).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(19).getBall().getColor() == Color.BLACK);
    	
    	column.clear();
    	
    	column.add(board.getField(19));
    	column.add(board.getField(1));
    	column.add(board.getField(12));
    	column.add(board.getField(6));
    	

    	try {
    		board.move(p2, column, Direction.UPRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	assertTrue(board.getField(7).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(1).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(12).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(19).isEmpty());
    	assertEquals(board.getPushedOutBalls(Color.RED), 2);
    	
    	column.clear();
    	
    	column.add(board.getField(07));
    	column.add(board.getField(13));
    	column.add(board.getField(20));
    	column.add(board.getField(28));
    	column.add(board.getField(36));
    	

    	try {
    		board.move(p2, column, Direction.UPRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	assertTrue(board.getField(2).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(7).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(13).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(20).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(28).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(36).isEmpty());
    	assertEquals(board.getPushedOutBalls(Color.RED), 2);
    	
    }
    
    @Test
    public void testLeftSumitoMoves() {
        board.reset();
        
    	board.setBallAtField(0, new Ball(Color.RED));
    	board.setBallAtField(1, new Ball(Color.BLACK));
    	board.setBallAtField(2, new Ball(Color.BLACK));

    	board.setBallAtField(5, new Ball(Color.RED));
    	board.setBallAtField(6, new Ball(Color.BLACK));
    	board.setBallAtField(7, new Ball(Color.BLACK));
    	board.setBallAtField(8, new Ball(Color.BLACK));

    	board.setBallAtField(11, new Ball(Color.RED));
    	board.setBallAtField(12, new Ball(Color.RED));
    	board.setBallAtField(13, new Ball(Color.BLACK));
    	board.setBallAtField(14, new Ball(Color.BLACK));
    	board.setBallAtField(15, new Ball(Color.BLACK));

    	board.setBallAtField(19, new Ball(Color.RED));
    	board.setBallAtField(20, new Ball(Color.BLACK));
    	board.setBallAtField(21, new Ball(Color.BLACK));
    	board.setBallAtField(22, new Ball(Color.BLACK));
    	
    	board.setBallAtField(27, new Ball(Color.RED));
    	board.setBallAtField(28, new Ball(Color.RED));
    	board.setBallAtField(29, new Ball(Color.BLACK));
    	board.setBallAtField(30, new Ball(Color.BLACK));
    	board.setBallAtField(31, new Ball(Color.BLACK));
    	
    	board.setBallAtField(44, new Ball(Color.RED));
    	board.setBallAtField(45, new Ball(Color.BLUE));
    	board.setBallAtField(46, new Ball(Color.BLACK));
    	board.setBallAtField(47, new Ball(Color.BLACK));
    	board.setBallAtField(48, new Ball(Color.BLACK));
    	
    	System.out.println("Now testing left sumito moves");
    	System.out.println(board.toString());

    	ArrayList<Field> column = new ArrayList<>();
    	
    	column.clear();
    	column.add(board.getField(0));
    	column.add(board.getField(1));
    	column.add(board.getField(2));
    	

    	try {
    		board.move(p2, column, Direction.LEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(board.getField(0).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(1).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(2).isEmpty());
    	assertEquals(board.getPushedOutBalls(Color.RED), 1);
    	
    	column.clear();
    	
    	column.add(board.getField(5));
    	column.add(board.getField(6));
    	column.add(board.getField(7));
    	column.add(board.getField(8));
    	

    	try {
    		board.move(p2, column, Direction.LEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(board.getField(5).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(6).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(7).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(8).isEmpty());
    	assertEquals(board.getPushedOutBalls(Color.RED), 2);


    	// 2nd column
    	column.clear();
    	column.add(board.getField(11));
    	column.add(board.getField(12));
    	column.add(board.getField(13));
    	column.add(board.getField(14));
    	column.add(board.getField(15));
    	

    	try {
    		board.move(p2, column, Direction.LEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(board.getField(11).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(12).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(13).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(14).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(15).isEmpty());
    	assertEquals(board.getPushedOutBalls(Color.RED), 3);
    	
    	column.clear();
    	
    	column.add(board.getField(19));
    	column.add(board.getField(20));
    	column.add(board.getField(21));
    	column.add(board.getField(22));
    	

    	try {
    		board.move(p2, column, Direction.LEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(board.getField(18).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(19).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(20).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(21).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(22).isEmpty());
    	assertEquals(board.getPushedOutBalls(Color.RED), 3);
    	
    	column.clear();
    	
    	column.add(board.getField(27));
    	column.add(board.getField(28));
    	column.add(board.getField(29));
    	column.add(board.getField(30));
    	column.add(board.getField(31));
    	

    	try {
    		board.move(p2, column, Direction.LEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	assertTrue(board.getField(26).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(27).getBall().getColor() == Color.RED);
    	assertTrue(board.getField(28).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(29).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(30).getBall().getColor() == Color.BLACK);
    	assertTrue(board.getField(31).isEmpty());
    	assertEquals(board.getPushedOutBalls(Color.RED), 3);

    	System.out.println(board.toString());
    	System.out.println("End of testing left sumito moves");
    	
    }
    
    @Test
    public void testValidateUpperRightSumitoMoves() {
        board.reset();
        
    	board.setBallAtField(5, new Ball(Color.RED));
    	board.setBallAtField(11, new Ball(Color.BLACK));
    	board.setBallAtField(18, new Ball(Color.BLACK));

    	board.setBallAtField(6, new Ball(Color.RED));
    	board.setBallAtField(12, new Ball(Color.BLACK));
    	board.setBallAtField(19, new Ball(Color.BLACK));
    	board.setBallAtField(27, new Ball(Color.BLACK));

    	board.setBallAtField(7, new Ball(Color.RED));
    	board.setBallAtField(13, new Ball(Color.RED));
    	board.setBallAtField(20, new Ball(Color.BLACK));
    	board.setBallAtField(28, new Ball(Color.BLACK));
    	board.setBallAtField(36, new Ball(Color.BLACK));
    	
    	board.setBallAtField(8, new Ball(Color.RED));
    	board.setBallAtField(14, new Ball(Color.YELLOW));
    	board.setBallAtField(21, new Ball(Color.BLACK));
    	board.setBallAtField(29, new Ball(Color.BLACK));
    	board.setBallAtField(37, new Ball(Color.BLACK));

    	board.setBallAtField(9, new Ball(Color.YELLOW));
    	board.setBallAtField(15, new Ball(Color.RED));
    	board.setBallAtField(22, new Ball(Color.BLACK));
    	board.setBallAtField(30, new Ball(Color.BLACK));
    	board.setBallAtField(38, new Ball(Color.BLACK));

    	board.setBallAtField(10, new Ball(Color.BLACK));
    	board.setBallAtField(16, new Ball(Color.RED));
    	board.setBallAtField(23, new Ball(Color.BLACK));
    	board.setBallAtField(31, new Ball(Color.BLACK));
    	board.setBallAtField(39, new Ball(Color.BLACK));
    	
    	board.setBallAtField(17, new Ball(Color.YELLOW));
    	board.setBallAtField(24, new Ball(Color.BLACK));
    	board.setBallAtField(32, new Ball(Color.BLACK));
    	board.setBallAtField(40, new Ball(Color.BLACK));
    	board.setBallAtField(47, new Ball(Color.BLACK));

    	System.out.println(board.toString());

    	ArrayList<Field> column = new ArrayList<>();
    	ArrayList<Field> retColumn = null;
    	
    	column.clear();

    	column.add(board.getField(11));
    	column.add(board.getField(18));
    	
    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(retColumn.size() == 3);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.RED);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.BLACK);

    	// 2nd column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(12));
    	column.add(board.getField(19));
    	column.add(board.getField(27));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(retColumn.size() == 4);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.RED);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(3).getBall().getColor() == Color.BLACK);
    	
    	// 3rd column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(20));
    	column.add(board.getField(28));
    	column.add(board.getField(36));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(retColumn.size() == 5);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.RED);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.RED);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(3).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(4).getBall().getColor() == Color.BLACK);

    	// 4 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(21));
    	column.add(board.getField(29));
    	column.add(board.getField(37));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
  	
    	// 5 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(22));
    	column.add(board.getField(30));
    	column.add(board.getField(38));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	// 5 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(47));
    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	// 5 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(24));
    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	// 2 balls at this direction

    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(27));
    	column.add(board.getField(19));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
   	
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(29));
    	column.add(board.getField(21));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    	
    	// Three with own
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(47));
    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    	// 1 ball
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(24));
    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(24));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "you can't push another with a single ball";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    	
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(11));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "you can't push another with a single ball";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    }

    
    @Test
    public void testValidateDownLeftSumitoMoves() {
        board.reset();
        
    	board.setBallAtField(5, new Ball(Color.BLACK));
    	board.setBallAtField(11, new Ball(Color.BLACK));
    	board.setBallAtField(18, new Ball(Color.RED));

    	board.setBallAtField(6, new Ball(Color.BLACK));
    	board.setBallAtField(12, new Ball(Color.BLACK));
    	board.setBallAtField(19, new Ball(Color.BLACK));
    	board.setBallAtField(27, new Ball(Color.RED));

    	board.setBallAtField(7, new Ball(Color.BLACK));
    	board.setBallAtField(13, new Ball(Color.BLACK));
    	board.setBallAtField(20, new Ball(Color.BLACK));
    	board.setBallAtField(28, new Ball(Color.RED));
    	board.setBallAtField(36, new Ball(Color.RED));
    	
    	board.setBallAtField(8, new Ball(Color.BLACK));
    	board.setBallAtField(14, new Ball(Color.BLACK));
    	board.setBallAtField(21, new Ball(Color.BLACK));
    	board.setBallAtField(29, new Ball(Color.BLUE));
    	board.setBallAtField(37, new Ball(Color.RED));

    	board.setBallAtField(9, new Ball(Color.BLACK));
    	board.setBallAtField(15, new Ball(Color.BLACK));
    	board.setBallAtField(22, new Ball(Color.BLACK));
    	board.setBallAtField(30, new Ball(Color.RED));
    	board.setBallAtField(38, new Ball(Color.BLUE));

    	board.setBallAtField(10, new Ball(Color.BLACK));
    	board.setBallAtField(16, new Ball(Color.BLACK));
    	board.setBallAtField(23, new Ball(Color.BLACK));
    	board.setBallAtField(31, new Ball(Color.RED));
    	board.setBallAtField(39, new Ball(Color.BLACK));
    	
    	board.setBallAtField(17, new Ball(Color.BLACK));
    	board.setBallAtField(24, new Ball(Color.BLACK));
    	board.setBallAtField(32, new Ball(Color.BLACK));
    	board.setBallAtField(40, new Ball(Color.BLACK));
    	board.setBallAtField(47, new Ball(Color.YELLOW));

    	System.out.println(board.toString());

    	ArrayList<Field> column = new ArrayList<>();
    	ArrayList<Field> retColumn = null;
    	
    	column.clear();

    	column.add(board.getField(05));
    	column.add(board.getField(11));
    	
    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(retColumn.size() == 3);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.RED);

    	// 2nd column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(12));
    	column.add(board.getField(19));
    	column.add(board.getField(06));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(retColumn.size() == 4);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(3).getBall().getColor() == Color.RED);
    	
    	// 3rd column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(20));
    	column.add(board.getField(07));
    	column.add(board.getField(13));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(retColumn.size() == 5);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(3).getBall().getColor() == Color.RED);
    	assertTrue(retColumn.get(4).getBall().getColor() == Color.RED);

    	// 4th column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(8));
    	column.add(board.getField(14));
    	column.add(board.getField(21));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(retColumn.size() == 5);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(3).getBall().getColor() == Color.BLUE);
    	assertTrue(retColumn.get(4).getBall().getColor() == Color.RED);

    	// 5 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(9));
    	column.add(board.getField(15));
    	column.add(board.getField(22));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    	
    	assertTrue(retColumn.size() == 5);
    	assertTrue(retColumn.get(0).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(1).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(2).getBall().getColor() == Color.BLACK);
    	assertTrue(retColumn.get(3).getBall().getColor() == Color.RED);
    	assertTrue(retColumn.get(4).getBall().getColor() == Color.BLUE);

    	// 6 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(10));
    	column.add(board.getField(16));
    	column.add(board.getField(23));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	// 7 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(17));
    	column.add(board.getField(24));
    	column.add(board.getField(32));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	// 8 column
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(24));
    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	// 2 balls at this direction

    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
   	
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    	
    	// Three with own
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(24));
    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    	// 1 ball
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(24));
    	column.add(board.getField(32));
    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "push your own or your teammate balls with a move";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}

    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(24));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "you can't push another with a single ball";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    	
    	column.clear();
    	retColumn.clear();

    	column.add(board.getField(40));

    	try {
    		retColumn = board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
		} catch (InvalidSelectionException e) {
        	String expectedMessage = "you can't push another with a single ball";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));

		}
    }


    
    //@Test
    public void testDownLeftMoves() {
        board.reset();
        
    	board.initForTwo(p1, p2);

    	System.out.println(board.toString());

    	System.out.println("Now we will move one ball");
    	
    	ArrayList<Field> column = new ArrayList<>();
    	column.add(board.getField(13));
    	
    	try {
    		board.move(p1, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
    	assertTrue(board.getField(13).isEmpty());
    	assertTrue(board.getField(20).getBall().getColor() == p1.getColor());
    	
    	System.out.println(board.toString());

    	System.out.println("Now we will move two balls from 08 and 14 down left");

    	column.clear();
    	column.add(board.getField(8));
    	column.add(board.getField(14));

    	try {
    		board.move(p1, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(board.getField(8).isEmpty());
    	assertTrue(board.getField(14).getBall().getColor() == p1.getColor());
    	assertTrue(board.getField(21).getBall().getColor() == p1.getColor());
    	
    	System.out.println(board.toString());
    	
    	System.out.println("Now we will move three balls from 04 and 09  and 15 down left");
    	column.clear();
    	column.add(board.getField(4));
    	column.add(board.getField(15));
    	column.add(board.getField(9));

    	try {
    		board.move(p1, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	assertTrue(board.getField(04).isEmpty());
    	assertTrue(board.getField(9).getBall().getColor() == p1.getColor());
    	assertTrue(board.getField(15).getBall().getColor() == p1.getColor());
    	assertTrue(board.getField(22).getBall().getColor() == p1.getColor());
    	
  	
    	System.out.println(board.toString());

    	System.out.println("Now we will move three balls from 20 and 21  and 22 down left");
    	column.clear();
    	column.add(board.getField(20));
    	column.add(board.getField(22));
    	column.add(board.getField(21));

    	try {
    		board.move(p1, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());
      
    	System.out.println("Now we will move three balls from 14 and 15 down left");
    	column.clear();
    	column.add(board.getField(15));
    	column.add(board.getField(14));

    	try {
    		board.move(p1, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	
    	System.out.println("Now we will try invalid move of ball 55 down left");
    	column.clear();
    	column.add(board.getField(50));

    	try {
    		board.move(p2, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());
    	
    	System.out.println("Now we will try invalid move of ball 51 and 56 down right");
    	column.clear();
    	column.add(board.getField(51));
    	column.add(board.getField(56));

    	try {
    		board.move(p2, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}


    	System.out.println(board.toString());

    	System.out.println("Now we will try invalid move of ball 45, 51 and 56 down right");
    	column.clear();
    	column.add(board.getField(56));
    	column.add(board.getField(51));
    	column.add(board.getField(45));

    	try {
    		board.move(p2, column, Direction.DOWNLEFT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());
    	
    	// Validate empty field
//    	try {
//			board.validatePlayerSelection(p2, column, direction);
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e.getMessage());
//		}
    }

    
    //@Test
    public void TestDownRightMoves() {
        board.reset();
    	board.initForTwo(p3, p4);
    	
    	
    	System.out.println(board.toString());

    	System.out.println("Now we will move one ball");
    	
    	ArrayList<Field> column = new ArrayList<>();
    	column.add(board.getField(15));
    	
    	try {
    		board.move(p1, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	System.out.println("Now we will move two balls from 08 and 14 down left");

    	column.clear();
    	column.add(board.getField(7));
    	column.add(board.getField(14));

    	try {
    		board.move(p1, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());
    	
    	System.out.println("Now we will move three balls from 04 and 09  and 15 down left");
    	column.clear();
    	column.add(board.getField(0));
    	column.add(board.getField(13));
    	column.add(board.getField(6));

    	try {
    		board.move(p1, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	System.out.println("Now we will move three balls from 20 and 21  and 22 down left");
    	column.clear();
    	column.add(board.getField(23));
    	column.add(board.getField(22));
    	column.add(board.getField(21));

    	try {
    		board.move(p1, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());
      
    	System.out.println("Now we will move three balls from 14 and 15 down left");
    	column.clear();
    	column.add(board.getField(13));
    	column.add(board.getField(14));

    	try {
    		board.move(p1, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());

    	
    	System.out.println("Now we will try invalid move of ball 55 down left");
    	column.clear();
    	column.add(board.getField(55));

    	try {
    		board.move(p2, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());
    	
    	System.out.println("Now we will try invalid move of ball 54 and 60 down right");
    	column.clear();
    	column.add(board.getField(54));
    	column.add(board.getField(60));

    	try {
    		board.move(p2, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}


    	System.out.println(board.toString());

    	System.out.println("Now we will try invalid move of ball 47, 54 and 60 down right");
    	column.clear();
    	column.add(board.getField(56));
    	column.add(board.getField(51));
    	column.add(board.getField(45));

    	try {
    		board.move(p2, column, Direction.DOWNRIGHT);
		} catch (InvalidMoveException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

    	System.out.println(board.toString());
    	
    }

    
    //@Test
    public void testIsFieldIndex() {
    	//TODO
    }

    //@Test
    public void testClientBoard() {
    	ClientBoard board = new ClientBoard();
    	
    	board.initForTwo(new HumanPlayer("Vlado", Color.YELLOW),new HumanPlayer("Vlado", Color.YELLOW));
    	for (int i=0;i<61;i++) {
    		System.out.println(board.getField(i).toString2());
    	}
    }
    
    //@Test
    public void testClientBoardULDL() {
    	ClientBoard board = new ClientBoard();
    	
    	board.reset();
    	board.setBallAtField(14, new Ball(Color.BLACK));
    	board.setBallAtField(21, new Ball(Color.BLACK));
    	board.setBallAtField(29, new Ball(Color.BLACK));
    	System.out.println(board.toString());  
    	
    	ArrayList<Field> column = new ArrayList<>();
    	
    	column.clear();
    	column.add(board.getField(14));
    	column.add(board.getField(21));
    	column.add(board.getField(29));
    	

    	board.move(new String[]{"14","21","29", "DR"});
    	System.out.println(board.toString());
    	
    	board.move(new String[]{"30","22","38", "UL"});

    	System.out.println(board.toString());
    	
    	board.move(new String[]{"14","21","29", "R"});
    	System.out.println(board.toString());
    	
    	board.move(new String[]{"15","22","30", "L"});
    	System.out.println(board.toString());
    	
    	board.move(new String[]{"14","21","29", "UR"});
    	System.out.println(board.toString());
    	
    	board.move(new String[]{"08","14","21", "DL"});
    	System.out.println(board.toString());
    }

    //@Test
    public void testValidatePlayerSelection() {
        board.reset();
        
    	board.setBallAtField(5, new Ball(Color.RED));
    	board.setBallAtField(11, new Ball(Color.BLACK));
    	board.setBallAtField(18, new Ball(Color.BLACK));

    	board.setBallAtField(06, new Ball(Color.YELLOW));
    	board.setBallAtField(12, new Ball(Color.BLACK));
    	board.setBallAtField(19, new Ball(Color.BLACK));
    	board.setBallAtField(27, new Ball(Color.BLACK));

    	board.setBallAtField(07, new Ball(Color.RED));
    	board.setBallAtField(13, new Ball(Color.RED));
    	board.setBallAtField(20, new Ball(Color.BLACK));
    	board.setBallAtField(28, new Ball(Color.BLACK));
    	board.setBallAtField(36, new Ball(Color.BLACK));
    	
    	board.setBallAtField(22, new Ball(Color.YELLOW));
    	board.setBallAtField(23, new Ball(Color.YELLOW));
    	board.setBallAtField(24, new Ball(Color.YELLOW));
    	board.setBallAtField(21, new Ball(Color.BLACK));
    	board.setBallAtField(25, new Ball(Color.BLACK));

    	System.out.println(board.toString());
    	
    	ArrayList<Field> column = new ArrayList<>();
    	

    	column.add(board.getField(18));
    	column.add(board.getField(27));
    	column.add(board.getField(19));
    	column.add(board.getField(13));
    	
    	try {
    		board.validateAndExpandPlayerSelection2(p1, column, Direction.DOWNLEFT);
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "You should select between 1 and three balls";
            String actualMessage = e.getMessage();

            System.out.println(actualMessage);
        
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}
    	
    	column.clear();
    	column.add(board.getField(18));
    	column.add(board.getField(14));
    	column.add(board.getField(19));

    	try {
    		board.validateAndExpandPlayerSelection2(p1, column, Direction.DOWNLEFT);
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "does not have a ball on it. Please make";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}
    	
    	column.clear();
    	column.add(board.getField(18));
    	column.add(board.getField(11));
    	column.add(board.getField(5));

    	try {
    		board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "please select only balls from your color and the color ";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}

    	column.clear();
    	column.add(board.getField(22));
    	column.add(board.getField(23));
    	column.add(board.getField(24));

    	try {
    		board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = ", you can't select only balls belonging to your teammate ";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}

    	column.clear();
    	column.add(board.getField(11));
    	column.add(board.getField(18));
    	column.add(board.getField(19));

    	try {
    		board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "The selection of your move should contain only adjacent balls";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}

    	column.clear();
    	column.add(board.getField(27));
    	column.add(board.getField(12));

    	try {
    		board.validateAndExpandPlayerSelection2(p2, column, Direction.DOWNLEFT);
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "The selection of your move should contain only adjacent balls";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}
    	
    	column.clear();
    	column.add(board.getField(22));
    	column.add(board.getField(21));

    	try {
    		board.validateAndExpandPlayerSelection2(p2, column, Direction.LEFT);
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "the first ball in your selection should be yours ";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}

    	column.clear();
    	column.add(board.getField(19));
    	column.add(board.getField(12));
    	column.add(board.getField(06));

    	try {
    		ArrayList<Field> summitoMove = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
    		assertTrue(summitoMove.get(0).equals(board.getField(6)));
    		assertTrue(summitoMove.get(1).equals(board.getField(12)));
    		assertTrue(summitoMove.get(2).equals(board.getField(19)));
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "ERROR";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}

    	column.clear();
    	column.add(board.getField(11));
    	column.add(board.getField(18));

    	try {
    		ArrayList<Field> summitoMove = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
    		assertTrue(summitoMove.get(0).equals(board.getField(5)));
    		assertTrue(summitoMove.get(1).equals(board.getField(11)));
    		assertTrue(summitoMove.get(2).equals(board.getField(18)));
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "ERROR";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}
    	
    	
    	// validate summito
    	column.clear();
    	column.add(board.getField(11));
    	column.add(board.getField(18));

    	try {
    		ArrayList<Field> summitoMove = board.validateAndExpandPlayerSelection2(p2, column, Direction.UPRIGHT);
    		assertTrue(summitoMove.get(0).equals(board.getField(5)));
    		assertTrue(summitoMove.get(1).equals(board.getField(11)));
    		assertTrue(summitoMove.get(2).equals(board.getField(18)));
    	} catch (InvalidSelectionException e ){
        	String expectedMessage = "the first ball in your selection should be yours ";
            String actualMessage = e.getMessage();
            
            System.out.println(actualMessage);
            
            assertTrue(actualMessage.contains(expectedMessage));
    		
    	}
    } 	
    
}

