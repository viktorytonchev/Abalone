package ss.project.abalone;

public class GameInfo {
	private String gameName;
	private int numPlayers;
	public String getGameName() {
		return gameName;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getConnectedPlayers() {
		return connectedPlayers;
	}

	private int connectedPlayers;
	
	public GameInfo(String gameName, int connectedPlayers, int numPlayers) {
		this.gameName = gameName;
		this.connectedPlayers = numPlayers;
		this.numPlayers = numPlayers;
	}
}
