package com.googlecode.imheresi1.logiclayer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

/**
 * Class that handles the PersistenceManagerImpl type
 * Type that writes the System's database
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class PersistenceManagerImpl implements PersistenceManager {

	private XStream xstream;
	private static PersistenceManagerImpl singletonAttribute = null;
	
	/**
	 * Private constructor to be used in getInstance, used in singleton
	 */
	private PersistenceManagerImpl(){
		xstream = new XStream();
	}
	
	/**
	 * Singleton method that guarantees a single instance.
	 * @return PersistenceManagerImpl single instance
	 */
	public static PersistenceManagerImpl getInstance(){
		if(singletonAttribute == null){
			singletonAttribute = new PersistenceManagerImpl();
		}
		return singletonAttribute;
	}

	/**
	 * @see PersistenceManager#hasUser(String)
	 */
	public boolean hasUser(String user) {
		try {
			FileInputStream a = new FileInputStream("files/users/" + user
					+ ".xml");
			try {
				a.close();
			} catch (IOException e) {
			}
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	/**
	 * @see PersistenceManager#clearInvitations()
	 */
	public void clearInvitations() {
		File file = new File("files/invitation/invitation.xml");
		file.delete();
	}

	/**
	 * @see PersistenceManager#getInvitations()
	 */
	public Map<String, List<String>> getInvitations() {
		FileReader reader;
		try {
			reader = new FileReader("files/invitation/invitation.xml");
			Map<String, List<String>> returnUser = (Map<String, List<String>>) xstream.fromXML(reader);
			reader.close();
			return returnUser;
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * @see PersistenceManager#getUserByName(String, int)
	 */
	public User getUserByName(String name, int occurrence) {
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<String> names = new ArrayList<String>();

		File file = new File("files/users");
 
		for (int i = 0; i < file.list().length; i++) {
			try {
				FileReader reader = new FileReader("files/users/"
						+ file.list()[i]);
				User a = (User) xstream.fromXML(reader);
				try {
					reader.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
				users.add(a);
			} catch (FileNotFoundException e) {
				// System.err.println("File not found");;
			}
		}

		Iterator<User> iter = users.iterator();
		while(iter.hasNext()) {
			User user = iter.next();
			if(user.getName().substring(0, name.length()).equals(name))
				names.add(user.getName().toLowerCase());
		}
		
		Object[] sorted = names.toArray();
		Arrays.sort(sorted);
		int occ = 0;
		for (Object i : sorted) {
			for (User u : users) {
				if (u.getName().toLowerCase().equals(i)) {
					occ++;
				}
				if (occ == occurrence) {
					return u;
				}
			}
		}
		
		return null;
	}

	/**
	 * @see PersistenceManager#getUserByUserName(String)
	 */
	public User getUserByUserName(String userName) {
		FileReader reader;
		try {
			reader = new FileReader("files/users/" + userName + ".xml");
			User returnUser = (User) xstream.fromXML(reader);
			reader.close();
			return returnUser;
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * @see PersistenceManager#resetBD()
	 */
	public void resetBD() {
		File file = new File("files/users");

		for (String i : file.list()) {
			File del = new File("files/users/" + i);
			del.delete();
		}
	}

	/**
	 * @see PersistenceManager#saveUser(User, String)
	 */
	public void saveUser(User user, String userName) {
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(new FileOutputStream("files/users/"
					+ userName + ".xml"));
			xstream.toXML(user, dos);
			dos.close();
		} catch (FileNotFoundException e1) {

		} catch (IOException e) {
			
		}
	}

	/**
	 * @see PersistenceManager#removeUser(String)
	 */
	public void removeUser(String userName) throws PersistenceManagerException {
		if (hasUser(userName)) {
			File file = new File("files/users/" + userName + ".xml");
			file.delete();
			return;
		}
		throw new PersistenceManagerException("File doesn't exist");
	}

	/**
	 * @see PersistenceManager#saveInvitations(Map)
	 */
	public void saveInvitations(Map<String, List<String>> invitations) {
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(new FileOutputStream("files/invitation/"
					+ "invitation.xml"));
			xstream.toXML(invitations, dos);
			dos.close();
		} catch (FileNotFoundException e1) {
			System.out.println(e1.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
}
