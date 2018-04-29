// Packages
package exceptions;

// This exception is thrown when the user tries to send a request to another user
// who is already their friend.
public class RepeatedFriendshipException extends Exception {

	// Constructor
	public RepeatedFriendshipException() {
		super("cannot send request - requested user is already friends with the requester");
	}
}
