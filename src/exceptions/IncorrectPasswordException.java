// Packages
package exceptions;

// This exception is thrown when the specified password does not match the specified
// user's. 
public class IncorrectPasswordException extends Exception {

	// Constructor
	public IncorrectPasswordException() {
		super("cannot perform operation - wrong password"); 
	}
}
