package com.googlecode.imheresi1.logiclayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.googlecode.imheresi1.logiclayer.localization.Position;
import com.googlecode.imheresi1.logiclayer.localization.PositionException;

/**
 * Class that implements the User type
 * Holds the private and public data of the user as well as the friends PublicInfo data
 * 
 * @see PublicInfo
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class User {

	private List<PublicInfo> friends;
	private List<String> visibleFriends;
	private String password;
	private String ip;
	private PublicInfo myPublicInfo;

	/**
	 * Constructor
	 * Creates a new User object
	 * 
	 * @param userName new User's userName
	 * @param password new User's password
	 */
	public User(String userName, String password) {
		this.password = password;
		this.myPublicInfo = new PublicInfo();
		this.myPublicInfo.setLogin(userName);
		this.friends = new ArrayList<PublicInfo>();
		this.visibleFriends = new ArrayList<String>();
		this.ip = "000.0.0.0";
	}

	/**
	 * Method that returns the object public data
	 * @return User's object public info data
	 */
	public PublicInfo getPublicInfo() {
		return this.myPublicInfo;
	}

	/**
	 * Method that adds a new friend (PublicInfo data) to the User's friend list
	 * @param friend PublicInfo data of the new friend
	 * @param mode mode to represent if the data is visible or invisible
	 * @throws UserException if the userName is already a friend
	 */
	public void addFriend(PublicInfo friend, int mode) throws UserException {
		if (this.friends.contains(friend))
			throw new UserException("Usuario ja eh amigo.");
		
		this.friends.add(friend);
		
		if (mode == 2)
			this.visibleFriends.add(friend.getLogin());
	}

	/**
	 * Method that updates the password that the User object holds
	 * @param newPass string representing the new password
	 */
	public void updatePassword(String newPass) {
		this.password = newPass;
	}

	/**
	 * Method that returns if the userName is a visible friend to the user or not  
	 * @param friendUserName string representing the userName of the friend 
	 * @return true if the friend is visible, false otherwise
	 * @throws UserException if the userName passed is not known to user.
	 */
	public boolean isVisible(String friendUserName) throws UserException {		
		Iterator <PublicInfo> iter = this.friends.iterator();
		while(iter.hasNext()){
			PublicInfo pInfo = iter.next();
			if (pInfo.getLogin().equals(friendUserName))
			    return this.visibleFriends.contains(friendUserName);
		}
		throw new UserException("Usuario desconhecido.");
	}

	/**
	 * Method that returns a formatted string containing all of the user friends name.
	 * @return string representing the names in a formatted way
	 */
	public String getFriendsNames() {
		String[] names = new String[this.friends.size()];

		for (int i = 0; i < names.length; i++) {
			names[i] = this.friends.get(i).getName();
		}

		Arrays.sort(names);
		return Arrays.toString(names);
	}

	/**
	 * Method that returns the list of PublicInfo's this User contains.
	 * In other words: returns the list of friends.
	 * @return Collection of PublicInfo's representing the User's friends
	 */
	public List<PublicInfo> getFriendsPublicInfo() {
		return this.friends;
	}

	/**
	 * Method that set's a new IP to the user.
	 * @param ip string representing the new IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Method that sets the user's mail.
	 * @param email string representing the new email.
	 */
	public void setMail(String email) {
		this.myPublicInfo.setEmail(email);
	}
	
	/**
	 * Method that sets the user's name
	 * @param name string representing the new name.
	 */
	public void setName(String name) {
		this.myPublicInfo.setName(name);
	}

	/**
	 * Method that sets the user's phone number
	 * @param phone string representing the new phone number
	 */
	public void setPhone(String phone) {
		this.myPublicInfo.setTelephoneNumber(phone);
	}

	/**
	 * Method that sets the user's position using the User's IP
	 * @throws PositionException if the values ate not valid or could not find the GeoIP database
	 */
	public void setPosition() throws PositionException {
		this.myPublicInfo.setPosition(this.ip);
	}

	/**
	 * Method that sets the user's position
	 * @param latitude latitude for the new position object
	 * @param longitude longitude for the new position object
	 * @throws PositionException if the values are invalid.
	 */
	public void setPositionManual(double latitude, double longitude)
			throws PositionException {
		this.myPublicInfo.setPositionManual(latitude, longitude);
	}

	/**
	 * Method that returns the user's Position object 
	 * @return The position of the user
	 * @throws PositionException if the position was not possible to obtain.
	 */
	public Position getPosition() throws PositionException {
		return this.myPublicInfo.getPosition();
	}

	/**
	 * Method that returns the user's password
	 * @return string representing the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Method that returns the user's name
	 * @return string representing the User's name
	 */
	public String getName() {
		return this.myPublicInfo.getName();
	}

	/**
	 * Method that returns the user's email
	 * @return string representing the User's email
	 */
	public String getMail() {
		return this.myPublicInfo.getEMail();
	}

	/**
	 * Method that returns the user's telephone number
	 * @return string representing the User's telephone number
	 */
	public String getPhone() {
		return this.myPublicInfo.getTelephoneNumber();
	}

	/**
	 * Method that returns the user's userName
	 * @return string representing the User's userName
	 */
	public String getUserName() {
		return this.myPublicInfo.getLogin();
	}

	/**
	 * Method that determines if the giver userName is a friend of the User Object
	 * @param username string representing the userName to be determined
	 * @return true if the userName is a friend, false otherwise
	 */
	private boolean isMyFriend(String username) {
		Iterator<PublicInfo> iter = this.friends.iterator();
		while(iter.hasNext()){
			PublicInfo pInfo = iter.next();
			if(pInfo.getLogin().equals(username))
				return true;
		}
		return false;
	}

	/**
	 * Method that sets the sharing option between two users
	 * @param friend string representing the friend userName
	 * @param mode new mode to be set. 
	 * @throws UserException if the friend's userName is not a friend of the User's object
	 */
	public void setSharingOption(String friend, int mode) throws UserException {
		if (!isMyFriend(friend))
			throw new UserException("Usuario desconhecido.");
		if (mode == 2) {
			if (!this.visibleFriends.contains(friend))
				this.visibleFriends.add(friend);
		} else {
			if (this.visibleFriends.contains(friend))
				this.visibleFriends.remove(friend);
		}
	}

	/**
	 * Method that removes a friend from the friend's list
	 * @param friend string representing the friend userName
	 * @throws UserException if the userName is not a friend.
	 */
	public void removeFriend(String friend) throws UserException {
		Iterator<PublicInfo> iter = this.friends.iterator();
		while(iter.hasNext()){
			PublicInfo pInfo = iter.next();
			if(pInfo.getLogin().equals(friend)) {
				this.friends.remove(pInfo);
				if (this.visibleFriends.contains(pInfo.getLogin()))
					this.visibleFriends.remove(pInfo.getLogin());
				return;
			}
		}
		throw new UserException("Usuario desconhecido.");
	}

	/**
	 * Method that returns the Position object of the given friend.
	 * @param friend string representing the friend userName
	 * @return Position object of the friend
	 * @throws PositionException if the position was not possible to obtain.
	 * @throws UserException if the given userName is not a friend
	 */
	public Position getFriendLocation(String friend) throws UserException, PositionException {
		if (!isMyFriend(friend))
			throw new UserException("Usuario desconhecido.");
		
		Iterator<PublicInfo> iter = this.friends.iterator();
		while(iter.hasNext()){
			PublicInfo pInfo = iter.next();
			if (pInfo.getLogin().equals(friend) && this.visibleFriends.contains(pInfo.getLogin()))
				return pInfo.getPosition();
		}
		
		return null;
	}

	/**
	 * Method to check if the ip will change
	 * @param ip2 newIp to check
	 * @return true if it will change, false otherwise
	 */
	public boolean willChangeIp(String ip2) {
		return !this.ip.equals(ip2);
	}

	/**
	 * Method that returns the PublicInfo object of the given friend.
	 * @param userName string representing the friend userName
	 * @return PublicInfo object of the friend
	 */
	public PublicInfo getAFriendPublicInfo(String userName) {
		Iterator<PublicInfo> iter = this.friends.iterator();
		while(iter.hasNext()){
			PublicInfo pInfo = iter.next();
			if(pInfo.getLogin().equals(userName)){
				return pInfo;
			}
		}
		return null;
	}

	/**
	 * Method that formats the list of friends userName
	 * @return formated string representing the list of userNames
	 */
	public String toStringFriends() {
		String separator = System.getProperty("line.separator"); 
		String returnString = "=================================================================" + separator
					        + "Username                      Nome                            " + separator
					        + "================================================================="  + separator;
		
		if(this.friends.size() == 0) return "";
		
		Iterator<PublicInfo> iter = this.friends.iterator();
		while(iter.hasNext()){
			PublicInfo pInfo = iter.next();
			returnString += pInfo.getLogin() + "                  " + pInfo.getName() + separator;
		}
		return returnString;
	}
}
