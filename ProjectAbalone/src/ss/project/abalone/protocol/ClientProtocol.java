package ss.project.abalone.protocol;

import ss.project.abalone.exception.ProtocolException;
import ss.project.abalone.exception.ServerUnavailableException;

/**
 * Defines the methods that the Hotel Client should support.
 * 
 * @author Dimitar Popov and Viktor Tonchev
 */
public interface ClientProtocol {

	public static final String DELIMITER = ":";
	public static final String MULTIOBJECTDELIMITER = ",";
	
	public void handleConnect(String userName) throws ServerUnavailableException, ProtocolException;
	public void handleDisconnect() throws ServerUnavailableException, ProtocolException;
	public void handleCreateGame(String game, String password, int capacity) throws ServerUnavailableException, ProtocolException;
	public void handleJoinGame(String gameName, String password) throws ServerUnavailableException, ProtocolException;
	public void handleListGame() throws ServerUnavailableException, ProtocolException;
	public void handleTurn(String userName) throws ServerUnavailableException, ProtocolException;
	public void handleGameOver(String userName) throws ServerUnavailableException, ProtocolException;
	public void handleQuitGame() throws ServerUnavailableException, ProtocolException;
	public void handleMove(String[] move) throws ServerUnavailableException, ProtocolException;
	public void handleReady() throws ServerUnavailableException, ProtocolException;
	public void handleStart() throws ServerUnavailableException, ProtocolException;

	public void sendExit() throws ServerUnavailableException;

}
