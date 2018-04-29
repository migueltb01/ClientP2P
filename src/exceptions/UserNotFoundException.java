// Packgaes
package exceptions;

// This exception is thrown when a non-existent username is specified.
public class UserNotFoundException extends Exception {

	// Constructor
	public UserNotFoundException() {
		super("user not found");
	}
}
