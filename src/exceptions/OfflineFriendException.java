// Packages
package exceptions;

// This exception is thrown when the user tries to start a chat with a friend who
// is not online.
public class OfflineFriendException extends Exception {

	// Constructor
	public OfflineFriendException() {
		super("cannot start chat - specified user is offline");
	}
}
