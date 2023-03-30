package ss.project.abalone.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

import ss.project.abalone.ClientBoard;
import ss.project.abalone.Color;
import ss.project.abalone.GameInfo;
import ss.project.abalone.SimpleGame;
import ss.project.abalone.exception.ExitProgram;
import ss.project.abalone.exception.ProtocolException;
import ss.project.abalone.exception.ServerUnavailableException;
import ss.project.abalone.protocol.ClientProtocol;
import ss.project.abalone.protocol.ProtocolMessages;

/**
 * Client for Networked Hotel Application.
 * 
 * @author Dimitar Popov and Viktor Tonchev
 */
public class AbaloneClient implements ClientProtocol {

	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	private AbaloneClientTUI view;
	private String serverAddress;
	private String serverPort;
	
	/**
	 * 
	 * Information for the games on the server. Filled by the list command;
	 * 
	 */
	private HashMap< String, GameInfo > serverGames;
	/**
	 * Since server protocol does not handle information about the color of each of the 
	 * players when the game is started, we will get it from the first update message for that player
	 * by using the color of the random ball in his first move. Then the hash map remains the same until the 
	 * game is over 
	 */
	private HashMap< String, Color > playerInfo;
	
	private String userName;
	
	private SimpleGame game;

	/**
	 * @return the game
	 */
	public SimpleGame getGame() {
		return game;
	}

	private boolean bConnectedToAGame = false;
	private boolean bGameIsRunning = false;
	//Variable that states whether the client has been started in ComputerPlayer (AI) simple mode
	private boolean bAIMode = false;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private void setGame(String game) {
		//this.game = game;
		
	}
	
	/**
	 * Constructs a new Abalone Client. Initialises the view.
	 * @throws ExitProgram 
	 * @throws ProtocolException 
	 * @throws ServerUnavailableException  
	 */
	public AbaloneClient()  {
		serverGames = new HashMap<String, GameInfo>();
		playerInfo 	 = new HashMap<String, Color>();	
		view = new AbaloneClientTUI(this);
	}

	/**
	 * @return the serverPort
	 */
	public String getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * @param serverAddress the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * @return the bAIMode
	 */
	public boolean isbAIMode() {
		return bAIMode;
	}

	/**
	 * @param bAIMode the bAIMode to set
	 */
	public void setbAIMode(boolean bAIMode) {
		this.bAIMode = bAIMode;
	}

	/**
	 * Starts a new AbaloneClient by creating a connection, followed by the 
	 * Connect to Server handshake as defined in the protocol. After a successful 
	 * connection and handshake, the view is started. 
	 * When errors occur, or when the user terminates a server connection, the
	 * user is asked whether a new connection should be made.
	 * @throws ExitProgram 
	 * @throws ProtocolException 
	 * @throws ServerUnavailableException 
	 * @throws IOException 
	 */
	public void start() throws ExitProgram, ServerUnavailableException, ProtocolException, IOException {

		view.start();

//		try {
//			this.createConnection(serverAddress, serverPort);
//			view.start();
//		} catch (ExitProgram e) {
//			e.printStackTrace();
//			if(view.getBoolean("Do you want to try again?")) {
//				this.closeConnection();
//				this.start();
//			}
//			else {
//				this.sendExit();
//			}
//			// TODO Auto-generated catch block
//			
//		} catch(ServerUnavailableException s) {
//			s.printStackTrace();
//			if(view.getBoolean("Do you want to try again?")) {
//				this.closeConnection();
//				this.start();
//			}
//			else {
//				this.sendExit();
//			}
//		}		
	}



	/**
	 * Creates a connection to the server. Requests the IP and port to 
	 * connect to at the view (TUI).
	 * 
	 * The method continues to ask for an IP and port and attempts to connect 
	 * until a connection is established or until the user indicates to exit 
	 * the program.
	 * 
	 * @throws ExitProgram if a connection is not established and the user 
	 * 				       indicates to want to exit the program.
	 * @throws UnknownHostException 
	 * @ensures serverSock contains a valid socket connection to a server
	 */
	public void createConnection(String serverAddress, String serverPort) throws ExitProgram, UnknownHostException {
		clearConnection();
		while (serverSock == null) {
			//InetAddress addr = views.getIp();
			//int port = views.getInt("Enter port number");
			int port = Integer.parseInt(serverPort);
			InetAddress addr = InetAddress.getByName(serverAddress);

			// try to open a Socket to the server
			try {
				
				System.out.println("Attempting to connect to " + addr + ":" 
					+ port + "...");
				serverSock = new Socket(addr, port);
				in = new BufferedReader(new InputStreamReader(
						serverSock.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(
						serverSock.getOutputStream()));
				view.showMessage("Connected");
			} catch (IOException e) {
				System.out.println("ERROR: could not create a socket on " 
					+ addr.getHostAddress() + " and port " + port + ".");
				
				  if (view.getBoolean("Do you want to open a new socket?") == false) {					  
				        throw new ExitProgram("User indicated to exit."); 
				  }			 
			}
		}
	}

	/**
	 * Resets the serverSocket and In- and OutputStreams to null.
	 * 
	 * Always make sure to close current connections via shutdown() 
	 * before calling this method!
	 */
	public void clearConnection() {
		serverSock = null;
		in = null;
		out = null;
	}

	/**
	 * Sends a message to the connected server, followed by a new line. 
	 * The stream is then flushed.
	 * 
	 * @param msg the message to write to the OutputStream.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public synchronized void sendMessage(String msg) 
			throws ServerUnavailableException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ServerUnavailableException("Could not write "
						+ "to server.");
			}
		} else {
			throw new ServerUnavailableException("Could not write "
					+ "to server.");
		}
	}

	/**
	 * Reads and returns one line from the server.
	 * 
	 * @return the line sent by the server.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public String readLineFromServer() 
			throws ServerUnavailableException {
		if (in != null) {
			try {
				// Read and return answer from Server
				String answer = in.readLine();
				if (answer == null) {
					throw new ServerUnavailableException("Could not read "
							+ "from server.");
				}
				return answer;
			} catch (IOException e) {
				throw new ServerUnavailableException("Could not read "
						+ "from server.");
			}
		} else {
			throw new ServerUnavailableException("Could not read "
					+ "from server.");
		}
	}

	/**
	 * Reads and returns multiple lines from the server until the end of 
	 * the text is indicated using a line containing ProtocolMessages.EOT.
	 * 
	 * @return the concatenated lines sent by the server.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public String readMultipleLinesFromServer() 
			throws ServerUnavailableException {
		if (in != null) {
			try {
				// Read and return answer from Server
				StringBuilder sb = new StringBuilder();
				for (String line = in.readLine(); line != null
						&& !line.equals(ProtocolMessages.EOT); 
						line = in.readLine()) {
					sb.append(line + System.lineSeparator());
				}
				return sb.toString();
			} catch (IOException e) {
				throw new ServerUnavailableException("Could not read "
						+ "from server.");
			}
		} else {
			throw new ServerUnavailableException("Could not read "
					+ "from server.");
		}
	}

	/**
	 * Closes the connection by closing the In- and OutputStreams, as 
	 * well as the serverSocket.
	 */
	public void closeConnection() {
		System.out.println("Closing the connection...");
		try {
			in.close();
			out.close();
			serverSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleConnect(String userName) 
			throws ServerUnavailableException, ProtocolException {
			String message = ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + userName;
			
			this.sendMessage(message);
			String response = this.readLineFromServer();
			
			if ( response == null ) {
			    view.showMessage("> Null response received from server!");
			} else {
			    String [] array = response.split(ProtocolMessages.DELIMITER);
			    if ( array[0].equals( ProtocolMessages.OK ) ) {
			    	this.setUserName(userName);
				    view.showMessage("User " + userName + " connected to the server!");
			    } else if ( array[0].equals(ProtocolMessages.FAIL) ) {
				    view.showMessage("Error when connecting: " + response );
			    	
			    } else {
				    view.showMessage("Invalid response " + response + " received from server!");
			    }
				
			}
		    
	}

	@Override
	public void sendExit() throws ServerUnavailableException {
		this.sendMessage(ProtocolMessages.EXIT);
		this.closeConnection();
		
	}

	@Override
	public void handleDisconnect() throws ServerUnavailableException, ProtocolException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleReady() throws ServerUnavailableException, ProtocolException {
		String message = ProtocolMessages.READY;
		
		if ( bGameIsRunning ) {
			view.showMessage("You are already in a game that is running, so ready command is not allowed!");
		}
		

		this.sendMessage(message);
		String response = this.readLineFromServer();
		
		if ( response == null ) {
			view.showMessage("> Null response received from server!");
		} else {
			String [] array = response.split(ProtocolMessages.DELIMITER);
			if ( array[0].equals( ProtocolMessages.OK ) ) {
				view.showMessage("Please wait the server to give the turn to you...");
				view.waitCommandsFromServer();
				// If client is started in AI mode
				if (bAIMode) {
					view.startAI();		
				}

			} else if ( array[0].equals(ProtocolMessages.FAIL) ) {
				view.showMessage("Error while sending READY to server: " + response );
			} else {
				view.showMessage("Invalid response " + response + " received from server!");
			}
		}
	}	
	
	@Override
	public void handleCreateGame(String gameName, String password, int capacity)
			throws ServerUnavailableException, ProtocolException {
		
		String message = ProtocolMessages.CREATE + ProtocolMessages.DELIMITER + gameName + 
						 ProtocolMessages.DELIMITER + password +
						 ProtocolMessages.DELIMITER + capacity;

		this.sendMessage(message);
		String response = this.readLineFromServer();

		if ( response == null ) {
			view.showMessage("> Null response received from server!");
		} else {
			String [] array = response.split(ProtocolMessages.DELIMITER);
			if ( array[0].equals( ProtocolMessages.OK ) ) {
				// creation connects to the game. if something else decided, remove the lines from below	
				
	    		// Add to the game info, so join command may get it from there
		    	GameInfo info = new GameInfo(gameName,1, capacity);
		    	serverGames.put(gameName, info);				

				createClientGame( gameName );
				view.showMessage("You have created the game " + gameName + " on the server!" );
				view.showMessage(game.toString());

				view.showMessage("Please, send ready command when ready!" );
				
//				view.waitCommandsFromServer();
//				end creation connects to the game	
				
				
			} else if ( array[0].equals(ProtocolMessages.FAIL) ) {
				view.showMessage("Error while creating the game on server: " + response );
			} else {
				view.showMessage("Invalid response " + response + " received from server!");
			}
		}
	}



	@Override
	public void handleJoinGame(String gameName, String password) throws ServerUnavailableException, ProtocolException {
		String message = ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + gameName + 
				 ProtocolMessages.DELIMITER + password;
		
		GameInfo info = serverGames.get(gameName);
		
		// The game information should be obtained from the server with LIST command
		// If not present ask to send LIST first
		if (info == null ) {
			view.showMessage("Please send 'list' command to the server first to obtain information for the active games! ");
			return;
		}
		
		if ( bConnectedToAGame ) {
			view.showMessage("You are already connected to a game " + this.game.getName() + ". Please disconnect first!");
			return;
		}
			
		this.sendMessage(message);
		String response = this.readLineFromServer();

		if ( response == null ) {
			view.showMessage("> Null response received from server!");
		} else {
			String [] array = response.split(ProtocolMessages.DELIMITER);
			if ( array[0].equals( ProtocolMessages.OK ) ) {
				
				createClientGame( gameName );
				view.showMessage("You have joined the game " + gameName + " on the server!" );
				view.showMessage(game.toString());

				view.showMessage("Please, send ready command when ready!" );

				//view.showMessage("Please, wait the server to give the turn to you..." );
				
				// view.waitCommandsFromServer();
				
			} else if ( array[0].equals(ProtocolMessages.FAIL) ) {
				view.showMessage("Error while joining the game on server: " + response );
			} else {
				view.showMessage("Invalid response " + response + " received from server!");
			}
		}
		
	}

	private void createClientGame(String gameName) {
		setGame(gameName);
		bConnectedToAGame = true;
		GameInfo info = serverGames.get(gameName);
		
		this.game = new SimpleGame(gameName, info.getNumPlayers());
	}

	@Override
	public void handleMove(String[] move) throws ServerUnavailableException, ProtocolException {
		String message = ProtocolMessages.MOVE + ProtocolMessages.DELIMITER;

		if ( !bConnectedToAGame && !bGameIsRunning ) {
			view.showMessage("You are not connected to an active game, so you can't make a move!");
			return;
		}
		for (int i=0;i<move.length-1;i++) {
			message+=move[i] +":";
		}
		
		message+=move[move.length-1];
		
		this.sendMessage(message);
		String response = this.readLineFromServer();
		
		String [] array = response.split(ProtocolMessages.DELIMITER);
		if ( array[0].equals( ProtocolMessages.OK ) ) {
			// doUpdate( move );
			
			// view.showMessage("Board situation after the move is:" );
			// view.showMessage(game.toString());
			view.showMessage("Your move was accepted by the server!" );
			
			view.waitCommandsFromServer();
			
			
		} else if ( array[0].equals(ProtocolMessages.FAIL) ) {
			view.showMessage("Invalid move: " + array[1] );
		} else {
			view.showMessage("Invalid response " + response + " received from server!");
		}
	}
	
	/**
	 * 
	 * The method handles the update message received from server.
	 * WARNING: The server protocol is simple, so it does not send information about colors of
	 * everybody connected to a game. So the method will also fill the username and the color 
	 * in a HashMap, that is member of this class
	 * 
	 * @param updateMessage update message received from server
	 */
	public void handleUpdate(String updateMessage)  throws ServerUnavailableException, ProtocolException {
    	String [] update = updateMessage.split(ProtocolMessages.DELIMITER);
    	
    	if ( update.length < 3) {
		    view.showMessage("Invalid update message received from server - " + updateMessage);
		    return;
    	}

    	String userName = update[1];
    	
    	Color color = playerInfo.get(userName);
    	
    	// We are updating info for that user for the first time, so pick a random ball
    	// and get its color
    	if (color == null) {
    		//BOARDINDEX
    		Color ballColor = this.game.getBoard().getField(Integer.parseInt(update[2])-1).getBall().getColor();
    		playerInfo.put(userName, ballColor);
    		this.game.setPlayerName(ballColor, userName);
    	}
    	
    	// Remove command and user
    	String [] move = Arrays.copyOfRange(update, 2, update.length);
		this.game.doUpdate(move);
		
		view.showMessage("The situation in the  " + this.game.getName() + " after the move is:" );
		view.showMessage(game.toString());

		view.showMessage("Please, wait the server to give the turn to you..." );

	}

	@Override
	public void handleListGame() throws ServerUnavailableException, ProtocolException {
		String message = ProtocolMessages.LIST;
		
		this.sendMessage(message);
		String response = this.readLineFromServer();	

		if ( response == "" ) {
		    view.showMessage("There are no active games on this server at the moment!");
		} else {
			
			serverGames.clear();
			
		    String [] games = response.split(ProtocolMessages.MULTIOBJECTDELIMITER);
		    	
		    for (int i = 0;i<games.length;i++) {
		    	String [] gameInfo = games[i].split(ProtocolMessages.DELIMITER);
		    	if ( gameInfo.length == 4) {
		    		// Add to the game info, so join command may get it from there
			    	GameInfo info = new GameInfo(gameInfo[0], 
							 Integer.parseInt(gameInfo[1]), Integer.parseInt(gameInfo[2]));
			    	serverGames.put(gameInfo[0], info);
			    	
		    		view.showMessage("Game:" + gameInfo[0] + " ,connected users:" + gameInfo[1] + ",max users:"+ gameInfo[2]  + " " + gameInfo[3]);
		    	} else {
		    		view.showMessage("Invalid game info received from the server - " + games[i]);
		    	}
		    }
		}
	}

	@Override
	public void handleTurn(String username) throws ServerUnavailableException, ProtocolException {
		if (username.equals(this.userName)){
			if (!bAIMode) {
				view.showMessage("Player " + this.userName + ", please enter your turn:");
				view.waitUserInput();
			} else {
				view.showMessage("I, bot " + this.userName + ", am making a turn");

				view.getAi().makeMove();
			}

		} else {
			view.showMessage("Waiting for " + username + " turn....");
		}
	}

	@Override
	public void handleGameOver(String username) throws ServerUnavailableException, ProtocolException {
		if (username.equals(this.userName)){
			view.showMessage("Player " + this.userName + ", game is over for you");
			view.waitUserInput();
			resetGame();
		} else {
			view.showMessage("The game is over for the user " + username );
			// Not a good implementation, but it can't be done different with the current protocol
			// Should find a way how to handle team play
			// TODO handle Game Over
			
		}
		
	}

	/**
	 * Resets the game state for the client
	 */
	private void resetGame() {
		// TODO Auto-generated method stub
		bConnectedToAGame=false;
		bGameIsRunning=false;
		playerInfo.clear();
	}
	public String handleGiveHint() {
		Color playerColor = this.playerInfo.get(this.getUserName());
		String[] move = new String[2];
		boolean flag = false;
		ClientBoard board = this.game.getBoard();
		while(!flag) {
			double randomDouble = Math.random();
			randomDouble = randomDouble * 61 ;
			int i = (int) randomDouble; //This is the random index between 0 and 60
			if(!board.getField(i).isEmpty() && board.getField(i).getBall().getColor() == playerColor) {
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
		}
		String retString = "Suggested move: ";
		retString += "Move the field: " + move[0] + " in direction: " + move[1];
		return retString;
		
	}

	

	@Override
	public void handleQuitGame() throws ServerUnavailableException, ProtocolException {
		String message = ProtocolMessages.QUIT;		
		
		if ( !bConnectedToAGame ) {
			view.showMessage("You are not connected to a game, so you can't quit!");
			return;
		}
		
		this.sendMessage(message);
		this.bConnectedToAGame = false;
		this.setGame("");

		view.showMessage("You have quit from the game " + game.getName());
		return;
	}

	
	
	/**
	 * This method starts a new Abalone.
	 * 
	 * @param args 
	 * @throws ProtocolException 
	 * @throws ServerUnavailableException 
	 * @throws ExitProgram 
	 * @throws IOException 
	 */
	
	
	  public static void main(String[] args) throws ExitProgram,
	  ServerUnavailableException, ProtocolException, IOException { 
//		  String server = args[0];
//		  String port = args[1];
//		  String mode = args[2];
		  
		  new AbaloneClient().start();
	  }

	@Override
	public void handleStart() throws ServerUnavailableException, ProtocolException {
		bGameIsRunning = true;
		
		view.showMessage("Player " + this.userName + ", the game has started, please wait for your turn!");

		return;
	}

	public Color getPlayerColor() {
		
		return playerInfo.get(userName);
	}

	 

}
