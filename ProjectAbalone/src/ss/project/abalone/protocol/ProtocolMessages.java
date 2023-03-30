package ss.project.abalone.protocol;

/**
 * Protocol for Abalone Game Application.
 * 
 * @author Wim Kamerman
 */
public class ProtocolMessages {

	/**
	 * Delimiter used to separate arguments sent over the network.
	 */
	public static final String DELIMITER = ":";
	public static final String MULTIOBJECTDELIMITER = ",";

	/**
	 * Sent as last line in a multi-line response to indicate the end of the text.
	 */
	public static final String EOT = "--EOT--";

	/** Used for the server-client handshake */
	public static final String FAIL = "fail";
	public static final String OK = "ok";

	/**
	 * The following chars are both used by the TUI to receive user input, and the
	 * server and client to distinguish messages.
	 */
	public static final String EXIT = "exit";
	public static final String CONNECT = "c";
	public static final String CREATE = "create";
	public static final String JOIN = "join";
	public static final String LIST = "list";
	public static final String MOVE = "move";
	public static final String QUIT = "quit";
	public static final String UPDATE = "update";
	public static final String TURN = "turn";
	public static final String GOVER = "gover";
	public static final String MESSAGE = "send_message";
	public static final String READY = "ready";
	public static final String START = "start";
}
