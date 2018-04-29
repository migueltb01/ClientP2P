// Packages
package exceptions;

// This exception is thrown when someone tries to register a new user with an
// username that is already in use.
public class RepeatedUsernameException extends Exception {

	// Constructor
	public RepeatedUsernameException() {
		super("cannot register user - username already in use");
	}
}
