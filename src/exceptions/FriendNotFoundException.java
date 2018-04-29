// Packages
package exceptions;

// This exception is thrown when the user tries to unfriend an user they are not
// friends with.
public class FriendNotFoundException extends Exception {

	// Constructor
	public FriendNotFoundException() {
		super("cannot unfriend user - specified user is not in friends list");
	}
}
