package ss.project.abalone.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ss.project.abalone.Game;
import ss.project.abalone.NetworkGame;
import ss.project.abalone.exception.ExitProgram;
import ss.project.abalone.exception.InvalidMoveException;
import ss.project.abalone.exception.InvalidSelectionException;
import ss.project.abalone.exception.JoinGameException;
import ss.project.abalone.protocol.ClientProtocol;
import ss.project.abalone.protocol.ProtocolMessages;
import ss.project.abalone.protocol.ServerProtocol;
//import ss.week7.challenge.chatbox.ClientHandler;

/**
 * Server TUI for Networked Abalone Application
 * 
 * Intended Functionality: interactively set up & monitor a new server
 * 
 * @author Dimitar Popov and Viktor Tonchev
 */

public class AbaloneServer implements Runnable, ServerProtocol {
 
	/** The ServerSocket of this AbaloneServer */
	private ServerSocket ssock;

	/** List of AbaloneClientHandlers, one for each connected client */
	private List<AbaloneClientHandler> clients;
	
	/** Next client number, increasing for every new connection */
	private int next_client_no;

	/** The view of this AbaloneServer */
	private AbaloneServerTUI view;

	/** The name of the Abalone */
	private static final String SERVERNAME = "MITKOANDVIKTORSERVER";

	/** The games on the server */
	private HashMap<String, NetworkGame> games;

	private HashSet<String> connectedUsers;
	
	/**
	 * Constructs a new AbaloneServer. Initializes the clients list, 
	 * the view and the next_client_no.
	 */
	public AbaloneServer() {
		clients = new ArrayList<>();
		games = new HashMap<String, NetworkGame>();
		connectedUsers = new HashSet<String>();
		view = new AbaloneServerTUI();
		next_client_no = 1;
	}
	
	/**
	 * Returns the name of the Abalone
	 * 
	 * @requires Abalone != null;
	 * @return the name of the Abalone.
	 */
	public String getServerName() {
		return SERVERNAME;
	}

	/**
	 * Opens a new socket by calling {@link #setup()} and starts a new
	 * AbaloneClientHandler for every connecting client.
	 * 
	 * If {@link #setup()} throws a ExitProgram exception, stop the program. 
	 * In case of any other errors, ask the user whether the setup should be 
	 * ran again to open a new socket.
	 */
	public void run() {
		boolean openNewSocket = true;
		while (openNewSocket) {
			try {
				// Sets up the Abalone application
				setup();

				while (true) {
					Socket sock = ssock.accept();
					String name = "Client " 
							+ String.format("%02d", next_client_no++);
					view.showMessage("New client [" + name + "] connected!");
					AbaloneClientHandler handler = 
							new AbaloneClientHandler(sock, this, name);
					new Thread(handler).start();
					clients.add(handler);
				}

			} catch (ExitProgram e1) {
				// If setup() throws an ExitProgram exception, 
				// stop the program.
				openNewSocket = false;
			} catch (IOException e) {
				System.out.println("A server IO error occurred: " 
						+ e.getMessage());

				if (!view.getBoolean("Do you want to open a new socket?")) {
					openNewSocket = false;
				}
			}
		}
		view.showMessage("See you later!");
	}

	/**
	 * Sets up a new Abalone using {@link #setupAbalone()} and opens a new 
	 * ServerSocket at localhost on a user-defined port.
	 * 
	 * The user is asked to input a port, after which a socket is attempted 
	 * to be opened. If the attempt succeeds, the method ends, If the 
	 * attempt fails, the user decides to try again, after which an 
	 * ExitProgram exception is thrown or a new port is entered.
	 * 
	 * @throws ExitProgram if a connection can not be created on the given 
	 *                     port and the user decides to exit the program.
	 * @ensures a serverSocket is opened.
	 */
	public void setup() throws ExitProgram {

		ssock = null;
		while (ssock == null) {
			int port = view.getInt("Please enter the server port.");

			// try to open a new ServerSocket
			try {
				view.showMessage("Attempting to open a socket at 127.0.0.1 "
						+ "on port " + port + "...");
				ssock = new ServerSocket(port, 0, 
						InetAddress.getByName("127.0.0.1"));
				view.showMessage("Server started at port " + port);
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on "
						+ "127.0.0.1" + " and port " + port + ".");

				if (!view.getBoolean("Do you want to try again?")) {
					throw new ExitProgram("User indicated to exit the "
							+ "program.");
				}
			}
		}
	}
	
	/**
	 * Removes a clientHandler from the client list.
	 * @requires client != null
	 */
	public void removeClient(AbaloneClientHandler client) {
		this.clients.remove(client);
	}

	// ------------------ Main --------------------------

	/** Start a new AbaloneServer */
	public static void main(String[] args) {
		AbaloneServer server = new AbaloneServer();
		System.out.println("Welcome to the Abalone Server! Starting...");
		new Thread(server).start();
	}

	// ------------------ Server Methods --------------------------
	/**
	 * 
	 * Processes connection request from a client app
	 * 
	 * @param username the username of the user
	 * @param handler The handler that sends the request to the server
	 * @return
	 */
	@Override
	public synchronized void doConnect(String username, AbaloneClientHandler handler) {
		String retStr = ProtocolMessages.OK;
		
		if ( connectedUsers.contains(username) ) {
			retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + " duplicate user name - " + username;
			view.showMessage("duplicate user [" + username + "] trying to connect");
		} else {
			connectedUsers.add(username);
			handler.setUser(username);
			view.showMessage("user [" + username + "] connected!");
		}
		
		try {
			handler.sendMessageToClient(retStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}

	@Override
	public synchronized void doDisconnect(AbaloneClientHandler abaloneClientHandler) {
		abaloneClientHandler.clearUser();
		return;
	}

	@Override
	public synchronized void doCreateGame(String userName, String gameName, String password, int capacity, AbaloneClientHandler handler) {
		String retStr = ProtocolMessages.OK;
		
		if ( games.containsKey(gameName) ) {
			retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + " duplicate game name - " + gameName;
			view.showMessage("Duplicate game [" + gameName + "] found when trying to create new game!");
		} else {
			NetworkGame game = new NetworkGame(gameName, password, capacity);

			games.put(gameName, game);
			view.showMessage("Game [" + gameName + "] created!");
			
			// Join to the game after the creation
			try {
				game.join(userName, handler);
				
				view.showMessage(userName + " joined the game  [" + gameName + "]!");
			} catch (JoinGameException e) {
				retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER +
						"Unable to jon the game after creating it " + e.getMessage();
				
			}
		}
		
		try {
			handler.sendMessageToClient(retStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}

	@Override
	public synchronized void doJoinGame( String userName, String gameName, String password, AbaloneClientHandler handler ) {
		String retStr = ProtocolMessages.OK;
		NetworkGame game = games.get(gameName);
		if (games.size() == 0) {
			retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + 
					"There are no games on this server. You may jon another one...";
		} else {
			
			if ( game == null ) {
				retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + 
						"There is no such game on the server - " + gameName;
			} else {
				if ( !game.checkPassword(password) ) {
					retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER +
							"The password for the game is invalid;";
				}
				
				try {
					game.join(userName, handler);
					
					view.showMessage(userName + " joined the game  [" + gameName + "]!");
				} catch (JoinGameException e) {
					retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER +
							"Unable to jon the game " + e.getMessage();
					
				}
			}
		}
		
		try {
			view.showMessage("Sending message to user [" + userName + "]!");
			handler.sendMessageToClient(retStr);
			view.showMessage("Message sent to user [" + userName + "]!");
		} catch (IOException e) {
				e.printStackTrace();
		}
		
		return;
	}
	
	@Override
	public synchronized void doListGames( AbaloneClientHandler handler ) {
		String retStr = "";
		
		if (games.size() == 0) {
			retStr = "There are no games on this server. You may jon another one...";
		} else {
			for (NetworkGame game: games.values() ) {
				retStr += game.getName() + ClientProtocol.DELIMITER + game.getJoinedPlayers() +
						 ClientProtocol.DELIMITER + game.getNumberOfPlayers() + ClientProtocol.DELIMITER +
						 ( game.hasPassword() ? "has_pass" : "no_pass");
				retStr += ClientProtocol.MULTIOBJECTDELIMITER;		 
			}
			retStr = retStr.substring(0, retStr.length() - 1); 
		}
		
		try {
			handler.sendMessageToClient(retStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}

	@Override
	public synchronized void doMove( String gameName, String[] move, AbaloneClientHandler handler  ) {
		String retStr = ProtocolMessages.OK;
		
		NetworkGame game = games.get( gameName );

		if ( game== null ) {
			retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER +
					"Fatal server error. Could not get the game for the move!";
			try {
				handler.sendMessageToClient(retStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;
		}
		
		view.showMessage("The game board before the move is:");
		view.showMessage(game.getBoard().toString());
		
		//Remove the empty strings from the move input
		ArrayList<String> corrMoveList = new ArrayList<String>();
		for(int i = 0;  i < move.length; i++) {
			if(!"".equals(move[i])) {
				corrMoveList.add(move[i]);
			}
		}
		String[] corrMove = new String[move.length];
		corrMove = corrMoveList.toArray(corrMove);
		
		try {
			game.move(corrMove);
		} catch (InvalidSelectionException | InvalidMoveException e) {
			retStr = ProtocolMessages.FAIL + ProtocolMessages.DELIMITER + e.getMessage();
			
			try {
				handler.sendMessageToClient(retStr);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return;
		}
		
		view.showMessage("The game board after the move is:");
		view.showMessage(game.getBoard().toString());

		// Send OK message
		// TODO add handling for the sending only in correct move
		try {
			handler.sendMessageToClient(retStr);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// propagate move to all clients
		
		retStr = ProtocolMessages.UPDATE + ProtocolMessages.DELIMITER ;
		retStr += handler.getUsername() + ProtocolMessages.DELIMITER;
		
		for (int i=0;i<move.length-1;i++) {
			//BOARDINDEX
			retStr += (Integer.parseInt(move[i])) + ProtocolMessages.DELIMITER;
		}

		retStr += move[move.length-1];
		
		Collection<AbaloneClientHandler> handlers = game.getClientHandlers();

		
		for( AbaloneClientHandler clientHandler : handlers) {
			try {
				clientHandler.sendMessageToClient(retStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// TODO send GOVER if game is over for an user after the turn
		if ( game.isOver() ) {
			for( AbaloneClientHandler clientHandler : handlers) {
				retStr = ProtocolMessages.GOVER + ProtocolMessages.DELIMITER + clientHandler.getUsername();
				try {
					clientHandler.sendMessageToClient(retStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Give turn to the next user
		AbaloneClientHandler next = game.getNextTurnClientHandler();
		doTurn(next.getUsername(), next);

//		try {
//			next.sendMessageToClient(retStr);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return;
	}

	
	@Override
	public synchronized void doQuit( AbaloneClientHandler handler ) {

		// TODO Generally speaking this method should be implemented in different way
		// There are open questions
		if (handler.isJoinedToGame()) {
			handler.quitActiveGame();
		}

		return;
	}

	@Override
	public synchronized void doTurn(String userName, AbaloneClientHandler handler) {
		String retStr = ProtocolMessages.TURN + ProtocolMessages.DELIMITER;
		retStr += userName;
		
		try {
			view.showMessage("Giving turn to  user [" + userName + "]!");
			handler.sendMessageToClient(retStr);
			view.showMessage("Turn given to user [" + userName + "]!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void doStart( AbaloneClientHandler handler) {
		String retStr = ProtocolMessages.START;
		
		try {
			view.showMessage("Sending start to  user [" + handler.getUsername() + "]!");
			handler.sendMessageToClient(retStr);
			view.showMessage("Start sent to user [" + handler.getUsername() + "]!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void doReady(String gameName, AbaloneClientHandler handler) {
		NetworkGame game = games.get( gameName );
		game.ready(handler.getUsername()); 
		
		String retStr = ProtocolMessages.OK;

		try {
			view.showMessage("Ready message received from user [" + handler.getUsername() + "]!");
			handler.sendMessageToClient(retStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Give turn to the next user if the last user joined
		if ( game.getReadyPlayers() == game.getNumberOfPlayers() ) {
			// TODO If next turn should be send to all the players, handle it here
			for (AbaloneClientHandler next : game.getClientHandlers()) {
				doStart(next);
			}
			
			doTurn(game.getNextTurnClientHandler().getUsername(), game.getNextTurnClientHandler());
		}
	}
	
	@Override
	public void doChat( String message, AbaloneClientHandler handler ) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void doGameOver(AbaloneClientHandler handler) {
		String retStr = ProtocolMessages.GOVER;
		
		try {
			view.showMessage("Sending game over to  user [" + handler.getUsername() + "]!");
			handler.sendMessageToClient(retStr);
			view.showMessage("Game over sent to user [" + handler.getUsername() + "]!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




}
