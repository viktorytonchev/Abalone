package ss.project.abalone.protocol;

import ss.project.abalone.server.AbaloneClientHandler;

/**
 * Defines the methods that the Hotel Server should support. The results 
 * should be returned to the client.
 * 
 * @author Dimitar Popov and Viktor Tonchev
 */
public interface ServerProtocol {

	public void doConnect(String username, AbaloneClientHandler handler);
	public void doDisconnect( AbaloneClientHandler handler );
	public void doListGames( AbaloneClientHandler handler );
	public void doMove(String game, String[] move, AbaloneClientHandler handler );
	public void doQuit( AbaloneClientHandler handler );
	public void doChat(String message, AbaloneClientHandler handler);
	public void doCreateGame(String userName, String gameName, String password, int capacity, AbaloneClientHandler handler);
	public void doJoinGame(String usName, String gameName, String password, AbaloneClientHandler handler);
	public void doTurn(String userName, AbaloneClientHandler handler);
	public void doStart(AbaloneClientHandler handler);
	public void doGameOver(AbaloneClientHandler handler);
	
}
