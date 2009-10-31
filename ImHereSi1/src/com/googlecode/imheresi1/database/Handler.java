package com.googlecode.imheresi1.database;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.googlecode.imheresi1.logiclayer.User;

/**
 * Class that implements the databade Handler 
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */

public class Handler {

	private PersistenceManager bd; /*Object the holds the properly actions with files depending on type */
	private static Handler handler = null; /* Instance used in singleton */
	
	/**
	 * Private constructor for database handler
	 * @param bd Manage the properly file actions, depending on type.
	 */
	private Handler(PersistenceManager bd) {
	    this.bd = bd;
	}
	
	/**
	 * Singleton method that guarantees a single instance of database Handler 
	 * and create the files folder, if i doesn't exist.
	 * @param bd PersistenceManager object 
	 * @return Handler instance.
	 */
	public static Handler getInstance(PersistenceManager bd) {
		if (handler == null) {
			checkDir("files");
			checkDir("files/chats");
			checkDir("files/invitation");
			checkDir("files/outputs");
			checkDir("files/users");
			handler = new Handler(bd);
		}
		return handler;
	}
	
	/**
	 * Check if the path director exists, and creates it if it doesn't exist.
	 * @param path string representing the path
	 */
	private static void checkDir(String path) {
		File file = new File(path);
		if(!file.exists())
			file.mkdir();
	}
	
	/**
	 * Clear the directory files/path
	 * @param path path in the file directory
	 */
	private void clearDirectory(String path) {
		File file = new File("files/" + path);

		for (String i : file.list()) {
			File del = new File("files/" + path + "/" + i);
			del.delete();
		}
	}
	
	/**
	 * Method that deletes all the files in the System's database
	 */
	public void resetBD() {
		clearDirectory("users");
		clearDirectory("invitation");
		clearDirectory("outputs");
		clearDirectory("chats");
	}
	
	/**
	 * @see PersistenceManager#hasUser(String)
	 */
	public boolean hasUser(String user) {
		return bd.hasUser(user);
	}
	
	/**
	 * @see PersistenceManager#getUserByName(String, int)
	 */
	public User getUserByName(String name, int occurrence) {
		return bd.getUserByName(name, occurrence);
	}
	
	/**
	 * @see PersistenceManager#getUserByUserName(String)
	 */
	public User getUserByUserName(String userName) {
		return bd.getUserByUserName(userName);
	}
	
	/**
	 * @see PersistenceManager#saveUser(User, String)
	 */
	public void saveUser(User user, String userName) {
		bd.saveUser(user, userName);
	}
	
	/**
	 * @see PersistenceManager#removeUser(String)
	 */
	public void removeUser(String userName) throws PersistenceManagerException {
		bd.removeUser(userName);
	}
	
	/**
	 * @see PersistenceManager#clearInvitations()
	 */
	public void clearInvitations() {
		bd.clearInvitations();
	}
	
	/**
	 * @see PersistenceManager#getInvitations()
	 */
	public Map<String, List<String>> getInvitations() {
		return bd.getInvitations();
	}
	
	/**
	 * @see PersistenceManager#saveInvitations(Map)
	 */
	public void saveInvitations(Map<String, List<String>> invitations) {
		bd.saveInvitations(invitations);
	}
	
}
