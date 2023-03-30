package ss.project.abalone.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


import ss.project.abalone.Direction;
import ss.project.abalone.exception.ExitProgram;
import ss.project.abalone.exception.ProtocolException;
import ss.project.abalone.exception.ServerUnavailableException;
import ss.project.abalone.protocol.ClientProtocol;
import ss.project.abalone.protocol.ProtocolMessages;
import ss.utils.TextIO;


public class AbaloneClientTUI implements AbaloneClientView {
	AbaloneClient client;
	private PrintWriter console;

	private boolean bWaitingCommandsFromServer = false;
	//Variable that states whether the client has been started in ComputerPlayer (AI) simple mode
	//private boolean bAIMode = false;
	private String AIMode;
	
	//Variable that states whether the client in ComputerPlayer(AI) mode has started.
	private boolean bAIStarted= false;
	
	private AbaloneClientAI ai;
	
	public AbaloneClientAI getAi() {
		return ai;
	}

	public boolean isAIStarted() {
		return bAIStarted;
	}

	public void startAI() {
		this.bAIStarted = bAIStarted;
	}
	/**
	 * Constructor for the class.
	 * @param client - the client that will be used
	 * @param bAIMode - signal whether the client that will be used has been started in ComputerPlayer mode
	 */
	public AbaloneClientTUI(AbaloneClient client) {
		this.client = client;
		console = new PrintWriter(System.out, true);
	}
	
	/**
	 * 
	 */
	@Override
	public void start() throws ExitProgram, ServerUnavailableException, IOException {
		try {
//			String server = "localhost";
//			String port = "8888";
//			String mode = "Human";

			String server = getString("Input the IP Adress of the server you want to connect to :");
			client.setServerAddress(server);
			String port = getString("Input the port on which you want to connect:");
			client.setServerPort(port);
			
			boolean correctMode = false;
			while (!correctMode) {
				String mode = getString(
						"Input the mode in which you want to run the client(\"Human\" for normal human player,\"AIDumb\" for a Dumb AI"
								+ " or \"AISmart\" for a Smart AI ):");
				if (mode.equals("AIDumb")) {
					client.setbAIMode(true);
					ai = new AbaloneClientAI(this, "Dumb");
					correctMode = true;
				} else if (mode.equals("AISmart")) {
					client.setbAIMode(true);
					ai = new AbaloneClientAI(this, "Smart");
					correctMode = true;
				} else if (mode.equals("Human")) {
					correctMode = true;
				}else {					
					System.out.println("Invalid input: please enter a valid mode.");
				}
			}
			try {
				client.createConnection(server, port);
			} catch (ExitProgram e) {
				e.printStackTrace();
				if(this.getBoolean("Do you want to try again?")) {
					client.closeConnection();
					client.start();
				}
				else {
					client.sendExit();
				}
				// TODO Auto-generated catch block

			} catch( UnknownHostException s) {
				s.printStackTrace();
				if(this.getBoolean("Do you want to try again?")) {
					client.closeConnection();
				}
				else {
					client.sendExit();
				}
			}	
			
			
			this.printHelpMenu();
			while(true) {
				if ( bWaitingCommandsFromServer ) {
					if (client.isbAIMode()) {
						ai.handleServerCommand();
					} else {
						this.handleServerCommand();
					}	
				} else {
				    String command = this.getString("Enter command:");
				    this.handleUserInput(command);
				}
			}
		} catch (ExitProgram e) {
			client.sendExit();
			e.printStackTrace();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	private void handleServerCommand() throws ServerUnavailableException, ProtocolException{
		
		// this.showMessage("Processing message from server ");
		
		String serverMessage = client.readLineFromServer();	
		// this.showMessage("Server message is " + serverMessage);

		String [] array = serverMessage.split(ProtocolMessages.DELIMITER);
		 
		 if (array.length == 0) {
			 this.showMessage("Empty command received!");
		 }
		 
		 String command = array[0];


 		switch (command) {

		 case ProtocolMessages.START:
			 try {
				 client.handleStart( );
			 } catch (ServerUnavailableException | ProtocolException e1) {
				 e1.printStackTrace();
			 } 
			 break;
		 case ProtocolMessages.UPDATE:
			 try {
				 client.handleUpdate( serverMessage );
			 } catch (ServerUnavailableException | ProtocolException e1) {
				 e1.printStackTrace();
			 } 
			 break;
		 case ProtocolMessages.GOVER:
			 if (array.length < 2) {
	  			 this.showMessage("Invalid command received from server - " + serverMessage);
			 }

			 try {
				 client.handleGameOver(array[1]);
			 } catch (ServerUnavailableException | ProtocolException e1) {
				 e1.printStackTrace();
			 } 
			 break;
		 case ProtocolMessages.TURN:
			 if (array.length < 2) {
	  			 this.showMessage("Invalid command received from server - " + serverMessage);
			 }
			 
			 try {
				 String username = array[1];
				 
				 client.handleTurn(username);
			 } catch (ServerUnavailableException | ProtocolException e1) {
				 e1.printStackTrace();
			 } 
			 break;
  		 default:
  			 this.showMessage("Invalid command received from server - " + serverMessage);
  			 return;
  		 }

	}

	@Override
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException, IOException {
		 String [] array = input.split(ProtocolMessages.DELIMITER);
		 
		 if (array.length == 0) {
			 this.showMessage("Empty command received");
		 }
		 
		 String command = array[0];


  		switch (command) {

  		// TODO
		 case ProtocolMessages.READY:
 			 try {
 				 client.handleReady();
 			 } catch (ServerUnavailableException | ProtocolException e1) {
 				 e1.printStackTrace();
 			 } 
 			 break;
 		 case ProtocolMessages.LIST:
 			 try {
 				 client.handleListGame();
 			 } catch (ServerUnavailableException | ProtocolException e1) {
 				 // TODO Auto-generated catch block
 				 e1.printStackTrace();
 			 } 
 			 break;
  		 case ProtocolMessages.EXIT:
  			 client.sendExit(); 
  			 break;
  		 case ProtocolMessages.MOVE:
			 // Validate the input and if invalid set bInvalidInput to true
			 if (array.length < 2) {
				 this.showMessage("Valid move should contain at least one ball selected and a direction - move:<m1>:<m2>:<m3>:<direction>");
				 return;
			 } else {
				// Maximum 3 balls should be selected 
				if (array.length > 5) {
					this.showMessage("Valid move should contain at most three balls selected and a direction - move:<m1>:<m2>:<m3>:<direction>");
					return;
				} else {
					// Check if the direction is valid
					String direction = array[array.length-1];
					
					if (Direction.stringToDirection(direction) == Direction.INVALID) {
						
						this.showMessage("Direction for the move is not valid:" + direction +" " + input); ;
						return;
					} else {
						// Validate that numbers are entered for the move
						for (int i = 1; i< array.length-1; i++) {
							try {
								Integer.parseInt(array[i]);
							} catch (NumberFormatException e) {
								this.showMessage( "Invalid number in the command" +array[i] );
								return;
							}
						}
					}
				}
			 } 

			 try {
				
				client.handleMove(Arrays.copyOfRange(array, 1, array.length ));
			} catch (ServerUnavailableException | ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 
			 break;
  		 case ProtocolMessages.JOIN:
			 
			 if (array.length != 3) {
				 this.showMessage( "Valid join game command is in the format - join:<game_name>:<pass>");
				 return;
			 }

			 try {
				 client.handleJoinGame(array[1], array[2]);
			 } catch (ServerUnavailableException | ProtocolException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 } 		
			 
  			 break;
  		 case ProtocolMessages.CREATE:
			 int capacity = 0;
			 if(client.isbAIMode()) {
				 this.showMessage("Invalid command: A bot cannot create a game");
				 return;
			 }else if (array.length != 4) {
				 this.showMessage( "Valid creation game command is in the format - create:<game_name>:<password>:<capacity>");
				 return;
			 } else {

				 try {
					 capacity = Integer.parseInt(array[3]);
					 
					 if ( capacity > 4 || capacity < 2 ) {
						 this.showMessage( "Invalid capacity of the game given - " + capacity ) ;
						 return;
					 }
				 } catch (NumberFormatException e) {

					 this.showMessage( "Invalid capacity of the game given - " + array[3] );
					 return;
				 }
			 }

			 try {
				 client.handleCreateGame(array[1], array[2], capacity);
			 } catch (ServerUnavailableException | ProtocolException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 } 		
			 
  			 break;
  		 case ProtocolMessages.CONNECT:
			 if (array.length < 2) {
				 this.showMessage("You should specify an username when connection to the server - c:<username>");
				 return;
			 }

			 if (client.getUserName() != null && client.getUserName().equals("")) {
				 this.showMessage("You are already connected as " + client.getUserName() + " to this server!");
				 return;
			 }

			 try {
				client.handleConnect(array[1]);
			} catch (ServerUnavailableException | ProtocolException e) {
				e.printStackTrace();
			} 
  			
			break;
  		 case "hint":
  			 this.showMessage(client.handleGiveHint());
  			 return;
  		 default:
  			 this.showMessage("Invalid command entered " + input);
  			 return;
  		 }

	}

	@Override
	public void showMessage(String message) {
		console.println(message);
		
	}

	@Override
	public InetAddress getIp() throws UnknownHostException {
		System.out.println("Enter IP address");
   	    String input = TextIO.getlnString();
   	    InetAddress address = InetAddress.getByName(input);
   	    return address;
	}

	@Override
	public String getString(String question) {
		System.out.println( question );
    	 String input = TextIO.getlnString();
		return input;
	}

	@Override
	public int getInt(String question) {
		System.out.println(question);
      	int input = TextIO.getlnInt();
		return input;
	}

	@Override
	public boolean getBoolean(String question) {
		System.out.println(question );
      	Boolean input = TextIO.getlnBoolean();
		return input;
	}

	@Override
	public void printHelpMenu() {
		System.out.println("Wellcome to the Abalone client!");
		System.out.println("");
		System.out.println("Below is the list of supported commands!");
		System.out.println("Connect to server - " + "c:<username>" );
		System.out.println("Disonnect from server - " + "exit" );
		System.out.println("Create game - " + "create:<game_name>:<password>:<capacity>" );
		System.out.println("Join game - " + "join:<game_name>:<pass>" );
		System.out.println("List game - " + "list" );
		System.out.println("Make a move - " + "move:<m1>:<m2>:<m3>:<direction>" );
		System.out.println("Quit game - " + "quit" );
		System.out.println("Connect to server - " + "c:<username>" );
	}

	/**
	 * Changes the view state to wait for server message.
	 * Message can be <turn>, <update> or <gover>
	 * 
	 */
	public void waitCommandsFromServer() {
		bWaitingCommandsFromServer=true;
	}

	/**
	 * Changes the view state to wait for user input
	 */
	public void waitUserInput() {
		bWaitingCommandsFromServer=false;
	}

}
