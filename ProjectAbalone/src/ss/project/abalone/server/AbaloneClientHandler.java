package ss.project.abalone.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;

import ss.project.abalone.Direction;
import ss.project.abalone.NetworkGame;
import ss.project.abalone.protocol.ProtocolMessages;


/**
 * HotelClientHandler for the Hotel Server application.
 * This class can handle the communication with one
 * client. 
 * 
 * @author Dimitar Popov and Viktor Tonchev
 */
public class AbaloneClientHandler implements Runnable {

	/** The socket and In- and OutputStreams */
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;
	
	/** The connected HotelServer */
	private AbaloneServer srv;

	/** Name of this ClientHandler */
	private String name;
	
	/** Name of the connected user (if connected with an username) */
	private String username;

	/** True if joined to a game */
	private boolean bJoinedToAGame;
	
	private NetworkGame activeGame;
	
	/**
	 * Constructs a new AbaloneClientHandler. Opens the In- and OutputStreams.
	 * 
	 * @param sock The client socket
	 * @param srv  The connected server
	 * @param name The name of this ClientHandler
	 */
	public AbaloneClientHandler(Socket sock, AbaloneServer srv, String name) {
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(
					new OutputStreamWriter(sock.getOutputStream()));
			this.sock = sock;
			this.srv = srv;
			this.name = name;
			this.bJoinedToAGame = false;
			this.activeGame = null;
			
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * Continuously listens to client input and forwards the input to the
	 * {@link #handleCommand(String)} method.
	 */
	public void run() {
		String msg;
		try {
			msg = in.readLine();
			while (msg != null) {
				System.out.println("> [" + name + "] Incoming: " + msg);
				handleCommand(msg);
				//out.newLine();
				//out.flush();
				msg = in.readLine();
			}
			shutdown();
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * Handles commands received from the client by calling the according 
	 * methods at the Abalone Server. For example, when the message "c:dimitar" 
	 * is received, the method doConnect() of AbaloneServer should be called 
	 * and the output must be sent to the client.
	 * 
	 * If the received input is not valid, send an "Unknown Command" 
	 * message to the server.
	 * 
	 * @param msg command from client
	 * @throws IOException if an IO errors occur.
	 */
	private void handleCommand(String msg) throws IOException {
		 String [] array = msg.split(ProtocolMessages.DELIMITER);
		 String responseMessage = "";
		 
		 if (array.length == 0) {
			sendMessageToClient("Empty command received");
		 }
		 
		 String command = array[0];

		 if ( command.equals(ProtocolMessages.EXIT) ) {
			 
			 this.shutdown();
			 
		 } else if ( command.equals(ProtocolMessages.CONNECT) ) {
			 
			 if (array.length < 2) {
				 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "You should specify an username when connection to the server - c:<username>";
				 sendMessageToClient(responseMessage);
			 } else {
				 
				 srv.doConnect(array[1], this);
			 }

		 } else if ( command.equals(ProtocolMessages.CREATE) ) {
			 boolean bInvalidInput = false;
			 int capacity = 0;
			 
			 if (array.length != 4) {
				 bInvalidInput = true;
				 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Valid creation game command is in the format - create:<game_name>:<password>:<capacity>";
			 } else {

				 try {
					 capacity = Integer.parseInt(array[3]);
					 
					 if ( capacity > 4 || capacity < 2 ) {
						 bInvalidInput = true;
						 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Invalid capacity of the game given - " + capacity;
					 }
				 } catch (NumberFormatException e) {
					 bInvalidInput = true;
					 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Invalid capacity of the game given - " + array[3];
				}
			 }

			 if ( bInvalidInput ) {
				 sendMessageToClient(responseMessage);
			 } else {
				 srv.doCreateGame(this.username, array[1], array[2], capacity, this);
			 }
			 
		 } else if ( command.equals(ProtocolMessages.JOIN) ) {
			 
			 if (array.length != 3) {
	
				 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Valid join game command is in the format - join:<game_name>:<pass>";
				 sendMessageToClient(responseMessage);
			 } else {
				 srv.doJoinGame(this.username, array[1], array[2], this);
			 }
			 
		 } else if ( command.equals(ProtocolMessages.LIST) ) {
			 srv.doListGames(this);
		 } else if ( command.equals(ProtocolMessages.READY) ) {
			 if( activeGame != null ) {
				 srv.doReady(activeGame.getName(), this);
			 } else {
				 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Please create or join a game before sending ready command!";
			 }
		 } else if ( command.equals(ProtocolMessages.MOVE) ) {
			 // Validate the input and if invalid set bInvalidInput to true
			 boolean bInvalidInput = false;
			 if (array.length < 2) {
				 bInvalidInput = true;
				 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Valid move should contain at least one ball selected and a direction - move:<m1>:<m2>:<m3>:<direction>";
			 } else {
				// Maximum 3 balls and a direction can be specified 
				if (array.length > 5) {
					 bInvalidInput = true;
					 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Valid move should contain at most five ball selected and a direction - move:<m1>:<m2>:<m3>:<direction>";
				} else {
					// Check if the direction is valid
					String direction = array[array.length-1];
					
					if (Direction.stringToDirection(direction) == Direction.INVALID) {
						bInvalidInput = true; 
						responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Direction for the move is not valid:" + direction +" " + msg ;
					} else {
						// Validate that numbers are entered for the move
						for (int i = 1; i< array.length-1; i++) {
							try {
								int index = Integer.parseInt(array[i]);
							} catch (NumberFormatException e) {
								responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Invalid number in the command" +array[i];
								bInvalidInput = true;
								break;
							}
						}
					}
				}
			 }
			 // If the input is valid, then call doMove
			 if ( bInvalidInput ) {
				 sendMessageToClient(responseMessage);
			 } else {
			 
				String[] tempMove = msg.split(ProtocolMessages.DELIMITER);
		    	String [] move = Arrays.copyOfRange(tempMove, 1, tempMove.length);
				
		    	srv.doMove(activeGame.getName(), move ,this);
			 }	
		 } else if ( command.equals(ProtocolMessages.QUIT) ) {

			 srv.doQuit(this);
			 
		 } else if ( command.equals(ProtocolMessages.MESSAGE) ) {
			 if (array.length < 2) {
				 responseMessage = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + "Empty message not allowed - c:<username>";
				 sendMessageToClient(responseMessage);
			 } else {
				 srv.doChat(array[1], this);
			 }
		 
		 }
		 
	}
	/**
	 * 
	 * Sends a message to the client
	 * 
	 * @param message The message to be sent
	 */
	public void sendMessageToClient( String message ) throws IOException {
		out.write(message);
		out.newLine();
		out.flush();
	}

	/**
	 * Shut down the connection to this client by closing the socket and 
	 * the In- and OutputStreams.
	 */
	private void shutdown() {
		System.out.println("> [" + name + "] Shutting down.");
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		srv.removeClient(this);
	}

	public void setUser(String username) {
		this.username = username;
	}

	public void clearUser() {
		this.username = "";
	}
	
	public boolean isJoinedToGame( ) { 
		return this.bJoinedToAGame;
	}

	public void joinActiveGame(NetworkGame game) {
		this.bJoinedToAGame=true;
		this.activeGame = game;
	}

	public void quitActiveGame() {
		this.bJoinedToAGame=false;
		this.activeGame = null;
	}
	
	public String getUsername() {
		return username;
	}
}
