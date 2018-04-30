// Packages
package p2p;

// Imports
import exceptions.*;
import java.util.ArrayList;
import java.rmi.*;
import java.sql.SQLException; 

// Remote P2P server interface
public interface RemoteServerInterface extends Remote {

	// Register a new user
	public void registerUser(String username, String password) throws RepeatedUsernameException,
		RemoteException, 
		SQLException;

	// Delete registered user
	public void deleteUser(RemoteClientInterface client, String username, String password) throws UserNotFoundException,
		IncorrectPasswordException,
		IncorrectSessionException,
		RemoteException, 
		SQLException;

	// Log in as specified user through specified client
	public ArrayList<String> logIn(String username, String password, RemoteClientInterface client) throws UserNotFoundException,
		IncorrectPasswordException,
		RemoteException,
		SQLException;

	// Logs specified user out
	public void logOut(RemoteClientInterface client, String username, String password) throws UserNotFoundException,
		IncorrectPasswordException,
		IncorrectSessionException,
		RemoteException;

	// Search usernames containing a specific substring
	public ArrayList<String> searchUsers(String substring) throws RemoteException;

	// Invoked by online user to add another user as a friend
	public void addFriend(RemoteClientInterface client, String sourceUser, String password, String destinationUser)
		throws UserNotFoundException,
		IncorrectPasswordException,
		IncorrectSessionException,
		RepeatedFriendshipException,
		RemoteException, 
		SQLException;

	// Invoked by online user to unfriend another user
	public void deleteFriend(RemoteClientInterface client, String sourceUser, String password, String destinationUser)
		throws UserNotFoundException,
		IncorrectPasswordException,
		IncorrectSessionException,
		FriendNotFoundException,
		RemoteException,
		SQLException;

	// Invoked by online user to confirm or deny friendship to another user
	public void resolveRequest(RemoteClientInterface client, String password, String sourceUser, String destinationUser, boolean decision)
		throws UserNotFoundException,
		IncorrectPasswordException,
		IncorrectSessionException,
		RemoteException,
		SQLException;

	// Invoked by an online user to change their password
	public void changePassword(RemoteClientInterface client, String username, String oldPassword, String newPassword)
		throws UserNotFoundException,
		IncorrectPasswordException,
		IncorrectSessionException,
		RemoteException, 
		SQLException;

	// Invoked by an online user to start a chat with another user
	public RemoteClientInterface startChat(RemoteClientInterface client, String sourceUser, String destinationUser, String password)
		throws UserNotFoundException,
		IncorrectPasswordException,
		IncorrectSessionException,
		FriendNotFoundException,
		OfflineFriendException,
		RemoteException;
}
