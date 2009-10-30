package com.googlecode.imheresi1.logiclayer;

import java.util.List;
import java.util.Map;

/**
 * Interface that dictates methods that a Persistence Manager object needs to implement
 * Persistence Manager is an object used only to save the data that the ImHere process uses.
 * 
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public interface PersistenceManager {

	/**
	 * Method to check if a user by the given userName exists in the System
	 * @param user - string representing the userName to check if the user exists
	 * @return boolean - true if the System contains a user by that userName
	 */
	boolean hasUser(String user);

	/**
	 * Method that gets a User Object found by the given name and occurrence.
	 * The occurrence value determines which user to return in case of multiple users in which the names match the parameter name
	 * 
	 * @see User
	 *  
	 * @param name - string representing the searched user's name 
	 * @param occurrence - number to determine the user to return
	 * @return User - user object representing the user found.
	 */
	User getUserByName(String name, int occurrence);

	/**
	 * Method that gets a User Object found by the given userName
	 *  
	 * @see User
	 *  
	 * @param name - string representing the searched user's userName 
	 * @return user - object representing the user found.
	 */
	User getUserByUserName(String userName) ;

	/**
	 * Method that deletes all the files in the System's database
	 */
	void resetBD();

	/**
	 * Method that deletes all invitations sent through the System
	 */
	void clearInvitations();
	
	/**
	 * Method that returns an object representing all the invitations sent. 
	 * @return Map - map that links a string (representing the userName of the user who sent the invitation) to a list of strings (representing all the usernames to which an invitation was sent).
	 */
	Map<String, List<String>> getInvitations();
	
	/**
	 * Method to save a new invitation in the invitation file
	 * @param invitations - invitations map that represents the new invitations
	 * the given map links a string (representing the userName of the user who sent the invitation) to a list of strings (representing all the usernames to which an invitation was sent).
	 */
	void saveInvitations(Map<String, List<String>> invitations);
	
	/**
	 * Saves the user in the database.
	 * If the userName already exists: overrides the file. Otherwise it creates a new file.
	 * 
	 * @see User
	 * @param user - User Object that contains the data to be saved.
	 * @param userName - User's userName. Which dictates the file name.
	 */
	void saveUser(User user, String userName);

	/**
	 * Deletes the file that refers to the specified user. 
	 * @param userName - user to be removed form the System.
	 * @throws PersistenceManagerException In case the user doesn't exists.
	 */
	void removeUser(String userName) throws PersistenceManagerException;

}
