package com.googlecode.imheresi1.database;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.googlecode.imheresi1.logiclayer.User;

public class Handler {

	private PersistenceManager bd;
	private static Handler handler = null;
	
	private Handler(PersistenceManager bd) {
	    this.bd = bd;
	}
	
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
	
	private static void checkDir(String path) {
		File file = new File(path);
		if(!file.exists())
			file.mkdir();
	}
	
	private void clearUsers() {
		File file = new File("files/users");

		for (String i : file.list()) {
			File del = new File("files/users/" + i);
			del.delete();
		}
	}
	
	private void clearChats() {
		File file = new File("files/chats");
		
		for(String i : file.list()) {
			File del = new File("files/chats/" + i);
			del.delete();
		}
		
	}
	
	private void clearInvitation() {
        File file = new File("files/invitation");
		
		for(String i : file.list()) {
			File del = new File("files/invitation/" + i);
			del.delete();
		}
	}
	
	private void clearOutputs() {
        File file = new File("files/outputs");
		
		for(String i : file.list()) {
			File del = new File("files/outputs/" + i);
			del.delete();
		}
	}
	
	/**
	 * Method that deletes all the files in the System's database
	 */
	public void resetBD() {
		clearUsers();
		clearChats();
		clearInvitation();
		clearOutputs();
	}
	
	public boolean hasUser(String user) {
		return bd.hasUser(user);
	}
	
	public User getUserByName(String name, int occurrence) {
		return bd.getUserByName(name, occurrence);
	}
	
	public User getUserByUserName(String userName) {
		return bd.getUserByUserName(userName);
	}
	
	public void saveUser(User user, String userName) {
		bd.saveUser(user, userName);
	}
	
	public void removeUser(String userName) throws PersistenceManagerException {
		bd.removeUser(userName);
	}
	
	public void clearInvitations() {
		bd.clearInvitations();
	}
	
	public Map<String, List<String>> getInvitations() {
		return bd.getInvitations();
	}
	
	public void saveInvitations(Map<String, List<String>> invitations) {
		bd.saveInvitations(invitations);
	}
	
}
