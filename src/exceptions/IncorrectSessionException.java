// Packages
package exceptions;

// This exception is thrown when someone tries to execute actions regarding some
// user's account from a different client to which the user is logged in from, 
// or when such actions are invoked for an offline user.
public class IncorrectSessionException extends Exception {

	// Constructor
	public IncorrectSessionException() {
		super("cannot perform operation - the operation has been invoked "
			+ "from a different client to which this user is logged "
			+ "from, or for an offline user");
	}
}
