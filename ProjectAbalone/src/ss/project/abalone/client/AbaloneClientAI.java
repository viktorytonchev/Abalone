package ss.project.abalone.client;

import java.util.Arrays;

import ss.project.abalone.Board;
import ss.project.abalone.ClientBoard;
import ss.project.abalone.Color;
import ss.project.abalone.exception.ProtocolException;
import ss.project.abalone.exception.ServerUnavailableException;
import ss.project.abalone.protocol.ProtocolMessages;
/**
 * This is the class that takes care of the Computer Player implementation. Since the protocol management
 * and the discussions with the protocol group about the overall design of the computer player were quite chaotic,
 * we decided to implement the ComputerPlayer as a mode of the client. In this way the user that starts the client
 * can choose on which game to join the client on, which seemed like an more user-friendly approach.
 * For these reasons, the computer player is not a class that implements player, but rather a mode of the client,
 * in which a HumanPlayer is still used, but moves are made automatically. Therefore, our ComputerPlayer uses a
 * HumanPlayer, but acts on its own.
 * This class realizes the ComputerPlayer functionality.
 * @author Dimitar Popov and Viktor Tonchev
 *
 */
public class AbaloneClientAI {
	AbaloneClientTUI view;
	//Depending on the value of this variable ("Dumb" or "Smart"), the dumb or smart implementation will be used
	private String AIMode;
	public AbaloneClientAI(AbaloneClientTUI view, String AIMode) {
		this.view = view;
		this.AIMode = AIMode;
	}
	
	/**
	 * This method implements the Client in ComputerPlayer(AI) handling of server commands.
	 * @throws ServerUnavailableException
	 * @throws ProtocolException
	 */
	public void handleServerCommand() throws ServerUnavailableException, ProtocolException{

		// process (if command is turn, do move) else process command
		String serverMessage = view.client.readLineFromServer();	


		String [] array = serverMessage.split(ProtocolMessages.DELIMITER);
		 
		 if (array.length == 0) {
			 view.showMessage("Empty command received!");
		 }
		 
		 String command = array[0];


 		switch (command) {

		 case ProtocolMessages.START:
			 try {
				 view.client.handleStart( );
			 } catch (ServerUnavailableException | ProtocolException e1) {
				 // TODO Auto-generated catch block
				 e1.printStackTrace();
			 } 
			 break;
		 case ProtocolMessages.UPDATE:
			 try {
				 view.client.handleUpdate( serverMessage );
			 } catch (ServerUnavailableException | ProtocolException e1) {
				 // TODO Auto-generated catch block
				 e1.printStackTrace();
			 } 
			 break;
		 case ProtocolMessages.GOVER:
			 if (array.length < 2) {
	  			 view.showMessage("Invalid command received from server - " + serverMessage);
			 }

			 try {
				 view.client.handleGameOver(array[1]);
			 } catch (ServerUnavailableException | ProtocolException e1) {
				 // TODO Auto-generated catch block
				 e1.printStackTrace();
			 } 
			 break;
		 case ProtocolMessages.TURN:
			 if (array.length < 2) {
	  			 view.showMessage("Invalid command received from server - " + serverMessage);
			 }
			 
			 try {
				 String username = array[1];
				 //Here is the MOVE. Remove the below line
				 //throw new ServerUnavailableException("");
				 view.client.handleTurn(username);
				 
			 } catch (ServerUnavailableException e1/*| ProtocolException e1*/) {
				 // TODO Auto-generated catch block
				 e1.printStackTrace();
			 } 
			 break;
  		 default:
  			 view.showMessage("Invalid command received from server - " + serverMessage);
  			 return;
  		 }

	}
	/**
	 * Makes a smart or dumb move implementing the smart or dumb strategy depending on the user input given by the user in the TUI
	 */
	public void makeMove() {
		// Get ComputerPlayer (AI) color
		//Since the ComputerPlayer (AI) cannot create a game and can join only a game with for two players,
		//it will always join a game that has already been created by a real HumanPlayer (which has gotten the color yellow,
		//because he/she created the game. Hence, the ComputerPlayer (AI) will get the the color BLUE (the only color to get left).
		Color playerColor = Color.BLUE;
		ClientBoard board = view.client.getGame().getBoard();
		if(this.AIMode.equals("Dumb")) {
			makeDumbMove();
		}else {
			makeSmartMove();
		}
		
	}
	/**
	 * Calculates an AI Dumb Move. This is a very predictable approach.
	 */
	public void makeDumbMove() {
		// Get ComputerPlayer (AI) color
		//Since the ComputerPlayer (AI) cannot create a game and can join only a game with for two players,
		//it will always join a game that has already been created by a real HumanPlayer (which has gotten the color yellow,
		//because he/she created the game. Hence, the ComputerPlayer (AI) will get the the color BLUE (the only color to get left).
		Color playerColor = Color.BLUE;
		ClientBoard board = view.client.getGame().getBoard();
		String[] move = new String[2];
		boolean flag = false;
		for(int i = 0;  i < 60; i++) {
			if(!board.getField(i).isEmpty() && board.getField(i).getBall().getColor() == playerColor) {				
				if(board.getField(i).upperLeft != null && board.getField(i).upperLeft.isEmpty()) {
					move[0] = Integer.toString(i+1);
					move[1] = "UL";
					flag = true;
					break;
				}else if(board.getField(i).upperRight != null && board.getField(i).upperRight.isEmpty()) {
					move[0] = Integer.toString(i+1);
					move[1] = "UR";
					flag = true;
					break;
				}else if(board.getField(i).left != null && board.getField(i).left.isEmpty()) {
					move[0] = Integer.toString(i+1);
					move[1] = "L";
					flag = true;
					break;
				}else if(board.getField(i).right != null && board.getField(i).right.isEmpty()) {
					move[0] = Integer.toString(i+1);
					move[1] = "R";
					flag = true;
					break;
				}else if(board.getField(i).downLeft != null && board.getField(i).downLeft.isEmpty()) {
					move[0] = Integer.toString(i+1);
					move[1] = "DL";
					flag = true;
					break;
				}else if(board.getField(i).downRight != null && board.getField(i).downRight.isEmpty()) {
					move[0] = Integer.toString(i+1);
					move[1] = "DR";
					flag = true;
					break;
				}
			}		
		}
		try {					
			if(flag) {
				view.client.handleMove(move);
			}
		} catch (ServerUnavailableException | ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * Implements a calculation of an AI Smart Move
	 * This method actually is not Smart at all (so the name "Smart AI" is not very fitting), 
	 * it is just not that predictable because the "AI" chooses a random ball every time it selects a random ball
	 * of the given color instead of just picking the first ball of the given color it finds.
	 */
	public void makeSmartMove() {
		// Get ComputerPlayer (AI) color
		//Since the ComputerPlayer (AI) cannot create a game and can join only a game with for two players,
		//it will always join a game that has already been created by a real HumanPlayer (which has gotten the color yellow,
		//because he/she created the game. Hence, the ComputerPlayer (AI) will get the the color BLUE (the only color to get left).
		Color playerColor = Color.BLUE;
		ClientBoard board = view.client.getGame().getBoard();
		String[] move = new String[2];
		boolean flag = false;
		while(!flag) {
			double randomDouble = Math.random();
			randomDouble = randomDouble * 61 ;
			int i = (int) randomDouble; //This is the random index between 0 and 60
			if(!board.getField(i).isEmpty() && board.getField(i).getBall().getColor() == playerColor) {
					if(board.getField(i).upperLeft != null && board.getField(i).upperLeft.isEmpty()) {
						move[0] = Integer.toString(i+1);
						move[1] = "UL";
						flag = true;
						break;
					}else if(board.getField(i).upperRight != null && board.getField(i).upperRight.isEmpty()) {
						move[0] = Integer.toString(i+1);
						move[1] = "UR";
						flag = true;
						break;
					}else if(board.getField(i).left != null && board.getField(i).left.isEmpty()) {
						move[0] = Integer.toString(i+1);
						move[1] = "L";
						flag = true;
						break;
					}else if(board.getField(i).right != null && board.getField(i).right.isEmpty()) {
						move[0] = Integer.toString(i+1);
						move[1] = "R";
						flag = true;
						break;
					}else if(board.getField(i).downLeft != null && board.getField(i).downLeft.isEmpty()) {
						move[0] = Integer.toString(i+1);
						move[1] = "DL";
						flag = true;
						break;
					}else if(board.getField(i).downRight != null && board.getField(i).downRight.isEmpty()) {
						move[0] = Integer.toString(i+1);
						move[1] = "DR";
						flag = true;
						break;
					}
				}		
			}
		
		try {					
			if(flag) {
				view.client.handleMove(move);
			}
		} catch (ServerUnavailableException | ProtocolException e) {
			e.printStackTrace();
		} 
	}
	
	
//	public static String provideHint(Board board) {
//		Color playerColor = Color.BLUE;
//		String[] move = new String[2];
//		boolean flag = false;
//		while(!flag) {
//			double randomDouble = Math.random();
//			randomDouble = randomDouble * 61 ;
//			int i = (int) randomDouble; //This is the random index between 0 and 60
//			if(!board.getField(i).isEmpty() && board.getField(i).getBall().getColor() == playerColor) {
//				if(!board.getField(i).isEmpty() && board.getField(i).getBall().getColor() == playerColor) {				
//					if(board.getField(i).upperLeft != null && board.getField(i).upperLeft.isEmpty()) {
//						move[0] = Integer.toString(i+1);
//						move[1] = "UL";
//						flag = true;
//						break;
//					}else if(board.getField(i).upperRight != null && board.getField(i).upperRight.isEmpty()) {
//						move[0] = Integer.toString(i+1);
//						move[1] = "UR";
//						flag = true;
//						break;
//					}else if(board.getField(i).left != null && board.getField(i).left.isEmpty()) {
//						move[0] = Integer.toString(i+1);
//						move[1] = "L";
//						flag = true;
//						break;
//					}else if(board.getField(i).right != null && board.getField(i).right.isEmpty()) {
//						move[0] = Integer.toString(i+1);
//						move[1] = "R";
//						flag = true;
//						break;
//					}else if(board.getField(i).downLeft != null && board.getField(i).downLeft.isEmpty()) {
//						move[0] = Integer.toString(i+1);
//						move[1] = "DL";
//						flag = true;
//						break;
//					}else if(board.getField(i).downRight != null && board.getField(i).downRight.isEmpty()) {
//						move[0] = Integer.toString(i+1);
//						move[1] = "DR";
//						flag = true;
//						break;
//					}
//				}		
//			}
//		}
//		String retString = "Suggested move: ";
//		retString += "Move the field "
//		
//	}
}
