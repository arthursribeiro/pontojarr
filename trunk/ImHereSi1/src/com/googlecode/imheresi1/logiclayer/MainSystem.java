package com.googlecode.imheresi1.logiclayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Encoder;

import com.googlecode.imheresi1.database.Handler;
import com.googlecode.imheresi1.database.PersistenceManagerException;
import com.googlecode.imheresi1.database.XmlPersistence;
import com.googlecode.imheresi1.logiclayer.localization.PositionException;
import com.googlecode.imheresi1.logiclayer.message.Chat;
import com.googlecode.imheresi1.logiclayer.message.Email;
import com.googlecode.imheresi1.logiclayer.message.Invitation;
import com.googlecode.imheresi1.logiclayer.message.Message;
import com.googlecode.imheresi1.logiclayer.message.MessageException;
import com.googlecode.imheresi1.logiclayer.message.SMS;

/**
 * Class that implements the MainSystem type
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class MainSystem {

	public static final int OCULTAR = 1;
	public static final int EXIBIR = 2;

	private Map<String, User> loggedUsers;
	private List<User> createdUsers;
	private Handler persistenceManager;
	private Chat chat;
	private String invitationsDirectory = "";

	private static MainSystem singletonAttribute = null;
	private Map<String, List<String>> invitations;

	/**
	 * Constructor
	 */
	private MainSystem() {
		this.persistenceManager = Handler.getInstance(XmlPersistence.getInstance());
		this.invitations = this.persistenceManager.getInvitations();
		if (this.invitations == null)
			this.invitations = new HashMap<String, List<String>>();
		this.loggedUsers = new HashMap<String, User>();
		this.createdUsers = new ArrayList<User>();
	}
	
	/**
	 * Singleton method that guarantees a single instance.
	 * @return single instance
	 */
	public static MainSystem getInstance(){
		if(singletonAttribute == null){
			singletonAttribute = new MainSystem();
		}
		return singletonAttribute;
	}

	/**
	 * Method to set the directory in the System where the invitations predetermined text is stored
	 * @param value string representing the path to the directory
	 */
	public void setDirectory(String value) {
		this.invitationsDirectory = value;
	}

	/**
	 * Method to get the friend position given a userName and the friend's userName   
	 * @param userName string representing the userName of the user
	 * @param userNameToLocalize string representing the userName of the user's friend
	 * @return formated string representing the location of the user userNameToLocalize 
	 * @throws MainSystemException if the user does not exist
	 * @throws PositionException if the position was not possible to obtain
	 * @throws UserException if the given userName is not a friend
	 */
	public String getAFriendPosition(String userName,
			String userNameToLocalize) throws MainSystemException, UserException, PositionException {
		User user = this.getUserByUserName(userName);
		if(user.getFriendLocation(userNameToLocalize) == null) return null;
		return user.getFriendLocation(userNameToLocalize).toString();
	}

	/**
	 * Method to return a formatted string containing the invitations sent to a specific user
	 * @param username string representing the user
	 * @return formatted string
	 * @throws MainSystemException if the user does not exist
	 */
	public String toStringMyInvitations(String username) throws MainSystemException {
		User user;
		List<String> invitationList = null;
		
		user = this.getUserByUserName(username);
		invitationList = getInvitationList(user.getMail());

		String separator = System.getProperty("line.separator");
		String formatted = "================================================================="
				+ separator
				+ "Username                  Nome                            "
				+ separator
				+ "================================================================="
				+ separator;

		if (invitationList.size() == 0)
			return "";

		Iterator<String> iter = invitationList.iterator();
		while(iter.hasNext()){
			String userName = iter.next();
			User u;
			u = this.getUserByUserName(userName);
			formatted += userName + "                   " + u.getName();
		}
		return formatted;
	}

	/**
	 * Method that returns the list of invitations sent to a specific e-mail 
	 * @param mail string representing the user's e-mail
	 * @return List of userName's who sent an invitation to the e-mail
	 */
	private List<String> getInvitationList(String mail) {
		List<String> invitationList = new ArrayList<String>();

		Iterator<String> iter = this.invitations.keySet().iterator();
		while(iter.hasNext()) {
			String username = iter.next();
			if (this.invitations.get(username).contains(mail))
				invitationList.add(username);
		}

		return invitationList;
	}

	/**
	 * Method to confirm sharing between two user's given that an invitation was sent between the user's
	 * @param from string representing the user who sent the invitation
	 * @param with string representing the user who received the invitation
	 * @param mode mode of sharing (1: Not Visible, 2: Visible)
	 * @throws MainSystemException if the invitation was not sent
	 * @throws UserException if the user's are already friends
	 */
	public void confirmSharing(String from, String with, int mode)
			throws MainSystemException, UserException {
		if (!this.invitations.containsKey(with))
			throw new MainSystemException("Convite nao foi enviado.");
		if (!this.loggedUsers.containsKey(from))
			throw new MainSystemException("Permissao negada.");

		User f = this.loggedUsers.get(from);
		User w = this.getUserByUserName(with);

		if (!this.invitations.get(with).contains(f.getMail()))
			throw new MainSystemException("Convite nao foi enviado.");

		f.addFriend(w.getPublicInfo(), 2);
		w.addFriend(f.getPublicInfo(), mode);

		this.invitations.get(with).remove(f.getMail());
		this.persistenceManager.saveUser(w, w.getUserName());
		this.persistenceManager.saveUser(f, f.getUserName());
		this.persistenceManager.saveInvitations(invitations);
	}

	/**
	 * Method to set the position of the user through an IP.
	 * @param userName string representing the user's userName
	 * @param ip string representing the IP to set the user's position
	 * @throws MainSystemException if the user does not exist
	 * @throws PositionException if the values ate not valid or could not find the GeoIP database
	 */
	public void setLocal(String userName, String ip)
			throws MainSystemException, PositionException {
		User user = this.getUserByUserName(userName);
		user.setPosition();
		this.loggedUsers.put(user.getUserName(), user);
		this.refreshMyLocalization(user, ip);
		this.persistenceManager.saveUser(user, user.getUserName());
	}

	/**
	 * Method to set the position of the user through values representing the latitude and longitude.
	 * @param userName string representing the user's userName 
	 * @param latitude latitude value
	 * @param longitude longitude value
	 * @throws MainSystemException if the user does not exist
	 * @throws PositionException if the values ate not valid or could not find the GeoIP database
	 */
	public void setLocal(String userName, double latitude, double longitude)
			throws PositionException, MainSystemException {
		User user = this.getUserByUserName(userName);
		user.setPositionManual(latitude, longitude);
		this.loggedUsers.put(user.getUserName(), user);
		this.refreshMyLocalization(user, latitude, longitude);
		this.persistenceManager.saveUser(user, user.getUserName());
	}

	/**
	 * Method to remove a friendship connection between two user's
	 * @param userName string representing the user's userName
	 * @param friend string representing the friend's userName
	 * @throws MainSystemException if the user does not exist
	 * @throws UserException if friend is not userName's friend.
	 */
	public void removeFriend(String userName, String friend) 
			throws MainSystemException, UserException  {
		User user;
		user = this.getUserByUserName(userName);
		user.removeFriend(friend);
		this.persistenceManager.saveUser(user, user.getUserName());
	}

	/**
	 * Method to set the sharing mode of userName in relation to friend
	 * @param userName string representing the user's userName
	 * @param friend string representing the friend's userName
	 * @param mode mode of sharing (1: Not Visible, 2: Visible)
	 * @throws MainSystemException if the user does not exist 
	 * @throws UserException if the friend's userName is not a friend of the User's object
	 */
	public void setSharing(String userName, String friend, int mode) 
			throws MainSystemException, UserException {
		User user = this.getUserByUserName(friend);
		user.setSharingOption(userName, mode);
		this.persistenceManager.saveUser(user, user.getUserName());
	}

	/**
	 * Method to refuse sharing between two user's given that an invitation was sent between the user's
	 * @param from string representing the user who sent the invitation
	 * @param with string representing the user who received the invitation
	 * @throws MainSystemException if the invitation was not sent
	 */
	public void refuseSharing(String from, String with)
			throws MainSystemException {
		if (!this.invitations.containsKey(with))
			throw new MainSystemException("Convite nao foi enviado.");
		if (!this.loggedUsers.containsKey(from))
			throw new MainSystemException("Permissao negada.");

		User f = this.loggedUsers.get(from);

		if (!this.invitations.get(with).contains(f.getMail()))
			throw new MainSystemException("Convite nao foi enviado.");

		this.invitations.get(with).remove(f.getMail());
		this.persistenceManager.saveInvitations(invitations);
	}

	/**
	 * Method that returns a list of names, representing the friends of a given user
	 * @param userName string representing the user's userName
	 * @return list of names in a formatted manner
	 * @throws MainSystemException if the user does not exist 
	 */
	public String getFriends(String userName) throws MainSystemException {
		if (!this.loggedUsers.containsKey(userName))
			throw new MainSystemException("Permissao negada.");
		User u = getUserByUserName(userName);
		return u.getFriendsNames();
	}

	/**
	 * Method to log in a user in the System
	 * @param userName string representing the userName to login
	 * @param password string representing the user's password
	 * @param ip string representing the IP
	 * @return string representing the logged userName
	 * @throws UserException if the string represents a invalid IP or login and password do not match
	 * @throws PositionException if the values ate not valid or could not find the GeoIP database
	 * @throws MainSystemException 
	 */
	public String logIn(String userName, String password, String ip)
			throws UserException, PositionException, MainSystemException {
		User userToLogIn = this.getCreatedUserByUserName(userName);

		if (userToLogIn == null) {
			if (!persistenceManager.hasUser(userName))
				throw new UserException("Login/senha invalidos.");
			userToLogIn = persistenceManager.getUserByUserName(userName);
		}

		if (userToLogIn.willChangeIp(ip)) {
			if (!this.validIp(ip))
				throw new MainSystemException("IP invalido.");
			userToLogIn.setIp(ip);
			this.refreshMyLocalization(userToLogIn, ip);
		}

		if (password == null || !userToLogIn.getPassword().equals(encripta(password)))
			throw new UserException("Login/senha invalidos.");

		loggedUsers.put(userToLogIn.getUserName(), userToLogIn);
		return userToLogIn.getUserName();
	}
	
	/**
	 * Method that returns if a string represents a valid or invalid IP
	 * @param Ip string representing the IP
	 * @return true if it's a valid IP, false otherwise
	 */
	private boolean validIp(String Ip) {
		String expression = "^((0|1[0-9]{0,2}|2[0-9]{0,1}|2[0-4][0-9]|25[0-5]|[3-9][0-9]{0,1})\\.){3}(0|1[0-9]{0,2}|2[0-9]{0,1}|2[0-4][0-9]|25[0-5]|[3-9][0-9]{0,1})$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(Ip);

		return matcher.matches();
	}
	
	/**
	 * Method to encrypt a given string
	 * @param password string to be encrypted
	 * @return encrypted string
	 */
	private static String encripta(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes());
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(digest.digest());
		} catch (NoSuchAlgorithmException ns) {
			ns.printStackTrace();
			return password;
		}
	}
	
	/**
	 * Method the update a given user's localization given the latitude and longitude values
	 * @param user user to have the localization updated
	 * @param latitude latitude value
	 * @param longitude longitude value
	 * @throws PositionException if the values are invalid.
	 */
	private void refreshMyLocalization(User user, double latitude,
			double longitude) throws PositionException {
		
		Iterator<PublicInfo> iter = user.getFriendsPublicInfo().iterator();
		
		while(iter.hasNext()) {
			PublicInfo friendInfo = iter.next();
			User friend = this.persistenceManager.getUserByUserName(friendInfo
					.getLogin());
			PublicInfo pInfo = friend.getAFriendPublicInfo(user.getUserName());
			pInfo.setPositionManual(latitude, longitude);
			this.saveUser(friend);
		}
	}

	/**
	 * Method the update a given user's localization given the IP
	 * @param user user to have the localization updated
	 * @param ip string representing the IP
	 * @throws PositionException if the values ate not valid or could not find the GeoIP database
	 */
	private void refreshMyLocalization(User user, String ip)
			throws PositionException {
		
		List<PublicInfo> friendsPublicInfo = user.getFriendsPublicInfo();
		Iterator<PublicInfo> iter = friendsPublicInfo.iterator();
		while(iter.hasNext()){
			PublicInfo friendInfo = iter.next();
			User friend = this.persistenceManager.getUserByUserName(friendInfo
					.getLogin());
			PublicInfo pInfo = friend.getAFriendPublicInfo(user.getUserName());
			pInfo.setPosition(ip);
			this.saveUser(friend);
		}
	}

	/**
	 * Method to identify if the user is logged in the System
	 * @param userName string representing the userName to be checked
	 * @return boolean true if the user is logged, false otherwise
	 */
	public boolean isConected(String userName) {
		return this.loggedUsers.containsKey(userName);
	}

	/**
	 * Method to create a new user in the system
	 * @param userName string representing the new userName
	 * @param password string representing the new user's password
	 * @param email string representing the new user's email
	 * @param name string representing the new user's name
	 * @param phone string representing the new user's phone number
	 * @throws MainSystemException if obligatory data is nos passed or is invalid 
	 */
	public void createUser(String userName, String password, String email,
			String name, String phone) throws MainSystemException {

		if ((password == null) || (password.trim().equals("")))
			throw new MainSystemException("Senha eh um dado obrigatorio.");
		if (password.length() < 6)
			throw new MainSystemException("Senha deve ter no minimo 6 caracteres.");
		
		if ((email == null) || (email.trim().equals("")))
			throw new MainSystemException("E-mail eh um dado obrigatorio.");
		if (!this.validMail(email))
			throw new MainSystemException("E-mail invalido.");
		
		if ((userName == null) || (userName.trim().equals("")))
			throw new MainSystemException("Username eh um dado obrigatorio.");
		
		if ((name == null) || (name.trim().equals("")))
			throw new MainSystemException("Nome eh um dado obrigatorio.");
		
		if ((persistenceManager.hasUser(userName))
				|| (this.getCreatedUserByUserName(userName) != null))
			throw new MainSystemException("O username jah existe.");

		User newUser = new User(userName, encripta(password));
		newUser.setMail(email);
		newUser.setName(name);
		newUser.setPhone(phone);

		this.createdUsers.add(newUser);
	}

	/**
	 * Method that returns if a string represents a valid or invalid email
	 * @param email string representing the IP
	 * @return true if it's a valid email, false otherwise
	 */
	private boolean validMail(String email) {
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}
	
	/**
	 * Method that gets a User Object found by the given name and occurrence.
	 * The occurrence value determines which user to return in case of multiple users in which the names match the parameter name
	 * @param name string representing the searched user's name 
	 * @param occurrence number to determine the user to return
	 * @return user object representing the user found.
	 * @throws MainSystemException if the user does not exist
	 */
	public User getUserbyName(String name, int occurrence) throws MainSystemException {
		User foundUser = this.getCreatedUserByName(name, occurrence);

		if (foundUser != null)
			return foundUser;

		foundUser = persistenceManager.getUserByName(name, occurrence);
		if (foundUser == null)
			throw new MainSystemException("O usuario nao existe.");

		return foundUser;
	}

	/**
	 * Method that gets a User Object found by the given userName
	 * @param userName string representing the searched user's userName 
	 * @return user object representing the user found.
	 * @throws MainSystemException if the user does not exist
	 */
	public User getUserByUserName(String userName) throws MainSystemException {
		User foundUser = this.getCreatedUserByUserName(userName);

		if (foundUser != null)
			return foundUser;

		foundUser = persistenceManager.getUserByUserName(userName);
		if (foundUser == null)
			throw new MainSystemException("O usuario nao existe.");

		return foundUser;
	}

	/**
	 * Method to obtain a created user given a name and occurrence
	 * The occurrence value determines which user to return in case of multiple users in which the names match the parameter name
	 * @param name string representing the searched user's name 
	 * @param occurance number to determine the user to return
	 * @return user object representing the user found.
	 */
	private User getCreatedUserByName(String name, int occurance) {
		int found = 0;

		Iterator<User> iter = this.createdUsers.iterator();
		while(iter.hasNext()){
			User user = iter.next();
			if (user.getName().contains(name))
				found++;
			if (found == occurance)
				return user;
		}
		return null;
	}

	/**
	 * Method to obtain a created user given a userName
	 * @param userName string representing the searched user's userName 
	 * @return user object representing the user found.
	 */
	private User getCreatedUserByUserName(String userName) {
		Iterator<User> iter = this.createdUsers.iterator();
		while(iter.hasNext()) {
			User user = iter.next();
			if (user.getUserName().equals(userName))
				return user;
		}
		return null;
	}

	/**
	 * Method to sent an invitation from a given user to a given email
	 * @param from string representing the user's userName
	 * @param to string representing the email who will receive the invitation
	 * @throws MainSystemException if the user is not logged
	 * @throws MessageControllerException if the message was not possible to be sent
	 */
	public void sendInvitation(String from, String to)
			throws MainSystemException, MessageException {
		if (!this.loggedUsers.containsKey(from))
			throw new MainSystemException("Permissao negada.");
		if (this.invitations.containsKey(from))
			this.invitations.get(from).add(to);
		else {
			List<String> mails = new ArrayList<String>();
			mails.add(to);
			this.invitations.put(from, mails);
		}
		User u = this.loggedUsers.get(from);

		if(this.invitationsDirectory.equals("")) throw new MainSystemException("Diretorio dos convites não especificado.");
		
		Message m = new Invitation(u.getName(), u.getMail(), to, this.invitationsDirectory);
		this.persistenceManager.saveInvitations(this.invitations);
		m.sendMessage();
	}

	/**
	 * Method to log out a user from the System
	 * @param userName string representing the user's userName
	 * @throws MainSystemException if the user does not exist or was not logged
	 */
	public void logOut(String userName) throws MainSystemException {
		if (!loggedUsers.containsKey(userName))
			throw new MainSystemException("Sessao inexistente.");
		this.persistenceManager.saveUser(this.getUserByUserName(userName),
				userName);
		this.loggedUsers.remove(userName);
	}

	/**
	 * Method to send a email between two users
	 * @param from string representing the userName of the user who will send the email
	 * @param to string representing the userName of the user who will receive the email
	 * @param subject subject of the email
	 * @param msg email message
	 * @throws MainSystemException if the user does not exist
	 * @throws MessageControllerException if the message was not possible to be sent
	 */
	public void sendMail(String from, String to, String subject, String msg)
			throws MainSystemException, MessageException {
		User sender = getUserByUserName(from);
		User receiver = getUserByUserName(to);
		Message mail = new Email(sender.getMail(), receiver.getMail(), subject,
				msg);
		mail.sendMessage();
	}

	/**
	 * Method to send a SMS between two users
	 * @param from string representing the userName of the user who will send the SMS
	 * @param to string representing the userName of the user who will receive the SMS
	 * @param msg SMS message
	 * @throws MainSystemException if the user does not exist or does not have a phone number registered in the System  
	 * @throws MessageException if the message was not possible to be sent
	 */
	public void sendSMS(String from, String to, String msg)
			throws MainSystemException, MessageException {
		User sender = getUserByUserName(from);
		User receiver = getUserByUserName(to);
		if (receiver.getPhone().equals(""))
			throw new MainSystemException("Numero de telefone nao encontrado.");
		Message sms = new SMS(sender.getName(), receiver.getPhone(), msg);
		sms.sendMessage();
	}

	/**
	 * Method to initiate a Chat between two users
	 * @param u1 string representing a user in the Chat
	 * @param u2 string representing the other user in the Chat
	 * @throws MainSystemException if any of the users do not exist
	 */
	public void initChat(String u1, String u2) throws MainSystemException {
		try {
			User u = getUserByUserName(u1);
			u = getCreatedUserByUserName(u2);
			u = null;
		} catch (MainSystemException e) {
			throw new MainSystemException("Usuario não existe.");
		}
		chat = new Chat(u1, u2);
	}

	/**
	 * Method to end a chat 
	 * @throws MainSystemException if the chat was not initiated
	 */
	public void endChat() throws MainSystemException {
		if(chat == null) throw new MainSystemException("Chat nao foi iniciado.");
		chat = null;
	}

	/**
	 * Method to send a message in the current chat
	 * @param receiver string representing the user who received the message
	 * @param msg string representing the user who sent the message
	 * @throws MainSystemException if the chat was not initiated
	 * @throws MessageException if the message was not possible to be sent
	 */
	public void sendChat(String receiver, String msg) throws MainSystemException, MessageException  {
		if (chat == null)
			throw new MainSystemException("Chat nao foi iniciado.");
		chat.sendMsg(receiver, msg);
	}

	/**
	 * Method to receive a message in the current chat
	 * @param user string representing the user
	 * @throws MainSystemException if the chat was not initiated
	 */
	public String receiveChat(String user) throws MainSystemException {
		if (chat == null)
			throw new MainSystemException("Chat nao foi iniciado.");
		return chat.getLastMessage(user);
	}
	
	/**
	 * Method to reset the database
	 */
	public void resetBD() {
		persistenceManager.resetBD();
		persistenceManager.clearInvitations();
		this.loggedUsers.clear();
		this.createdUsers.clear();
	}

	/**
	 * Method to save a user in the System's database
	 * If the userName already exists: overrides the file. Otherwise it creates a new file.
	 * @param user User Object that contains the data to be saved.
	 */
	private void saveUser(User user) {
		persistenceManager.saveUser(user, user.getUserName());
	}

	/**
	 * Method to update a given user's name
	 * @param userName string representing the user's userName
	 * @param newName string representing the new name
	 * @throws UserException if no name or an invalid one is passed
	 * @throws MainSystemException if the user does not exist
	 * 
	 */
	public void updateName(String userName, String newName)
			throws UserException, MainSystemException {
		
		if ((newName == null) || (newName.trim().equals("")))
			throw new MainSystemException("Nome eh um dado obrigatorio.");
		
		User user = this.getUserByUserName(userName);
		user.setName(newName);
		
		List<PublicInfo> friendsPublicInfo = user.getFriendsPublicInfo();
		Iterator<PublicInfo> iter = friendsPublicInfo.iterator();
		while(iter.hasNext()){
			PublicInfo friendInfo = iter.next();
			User friend = this.persistenceManager.getUserByUserName(friendInfo
					.getLogin());
			PublicInfo pInfo = friend.getAFriendPublicInfo(user.getUserName());
			pInfo.setName(newName);
			this.saveUser(friend);
		}
	
		
		this.persistenceManager.saveUser(user, userName);
		this.loggedUsers.put(user.getUserName(), user);
	}

	/**
	 * Method to update a given user's email
	 * @param userName string representing the user's userName
	 * @param newMail string representing the new email
	 * @throws MainSystemException if the user does not exist
	 * @throws UserException if no mail or an invalid one is passed
	 */
	public void updateMail(String userName, String newMail)
			throws MainSystemException, UserException {
		User user = this.getUserByUserName(userName);
		user.setMail(newMail);
		
		List<PublicInfo> friendsPublicInfo = user.getFriendsPublicInfo();
		Iterator<PublicInfo> iter = friendsPublicInfo.iterator();
		while(iter.hasNext()){
			PublicInfo friendInfo = iter.next();
			User friend = this.persistenceManager.getUserByUserName(friendInfo
					.getLogin());
			PublicInfo pInfo = friend.getAFriendPublicInfo(user.getUserName());
			pInfo.setEmail(newMail);
			this.saveUser(friend);
		}
		
		this.persistenceManager.saveUser(user, userName);
		this.loggedUsers.put(user.getUserName(), user);
	}
	
	/**
	 * Method to update a given user's phone number
	 * @param userName string representing the user's userName
	 * @param newPhoneNumber string representing the new phone number
	 * @throws MainSystemException if the user does not exist
	 */
	public void updatePhone(String userName, String newPhoneNumber)
			throws MainSystemException {
		User user = this.getUserByUserName(userName);
		user.setPhone(newPhoneNumber);
		
		List<PublicInfo> friendsPublicInfo = user.getFriendsPublicInfo();
		Iterator<PublicInfo> iter = friendsPublicInfo.iterator();
		while(iter.hasNext()){
			PublicInfo friendInfo = iter.next();
			User friend = this.persistenceManager.getUserByUserName(friendInfo
					.getLogin());
			PublicInfo pInfo = friend.getAFriendPublicInfo(user.getUserName());
			pInfo.setTelephoneNumber(newPhoneNumber);
			this.saveUser(friend);
		}
		
		this.persistenceManager.saveUser(user, userName);
		this.loggedUsers.put(user.getUserName(), user);
	}
	
	/**
	 * Method to update a given user's password
	 * @param userName string representing the user's userName
	 * @param newPassword string representing the new password
	 * @throws MainSystemException if the user does not exist
	 */
	public void updatePass(String userName, String newPass)
			throws MainSystemException{
		
		if (newPass == null || newPass.trim().equals("") || newPass.length() < 6)
			throw new MainSystemException("Senha deve ter no minimo 6 caracteres.");
		
		User user = this.getUserByUserName(userName);
		user.updatePassword(encripta(newPass));
				
		this.persistenceManager.saveUser(user, userName);
		this.loggedUsers.put(user.getUserName(), user);
	}

	
	/**
	 * Method to remove all friends of a given user 
	 * @param user User object to be removed all of his friends
	 * @throws UserException if tried to remove a friend who is not user's friend
	 * @throws MainSystemException if tried to remove a friend userName who does not exist
	 */
	private void removeAllFriends(User user) throws UserException,
			MainSystemException {
		User auxUser;
		
		List<PublicInfo> friendsPublicInfo = user.getFriendsPublicInfo();
		Iterator<PublicInfo> iter = friendsPublicInfo.iterator();
		while(iter.hasNext()){
			PublicInfo pInfo = iter.next();
			auxUser = this.getUserByUserName(pInfo.getLogin());
			auxUser.removeFriend(user.getUserName());
			this.persistenceManager.saveUser(auxUser, auxUser.getUserName());
		}
	}

	/**
	 * Method to exit the System
	 */
	public void exitSystem() {
		Iterator<User> iter = this.createdUsers.iterator();
		while(iter.hasNext()){
			User user = iter.next();
			this.persistenceManager.saveUser(user, user.getUserName());
		}

		Iterator<String> iter2 = this.loggedUsers.keySet().iterator();
		while(iter2.hasNext()) {
			String userName = iter2.next();
			this.persistenceManager.saveUser(this.loggedUsers.get(userName),
					userName);
		}
		
		this.persistenceManager.saveInvitations(invitations);

		this.createdUsers.clear();
		this.loggedUsers.clear();
	}

	/**
	 * Method to remove a user from the System
	 * @param userName string representing the user's userName
	 * @throws MainSystemException if the user does not exist
	 */
	public void removeUser(String userName) throws MainSystemException{
		User userToRemove = this.getUserByUserName(userName);
		try {
			this.removeAllFriends(userToRemove);
		} catch (UserException e1) {
		//	e1.printStackTrace();
		}

		if (this.createdUsers.contains(userToRemove))
			this.createdUsers.remove(userToRemove);

		if (this.loggedUsers.containsKey(userName))
			this.loggedUsers.remove(userName);

		try {
			if (this.persistenceManager.hasUser(userName)) {
				this.persistenceManager.removeUser(userToRemove.getUserName());
			}

		} catch (PersistenceManagerException e) {
			throw new MainSystemException("O usuario nao existe.");
		}

	}

	/**
	 * Method to return a formatted string containing the users friends userNames
	 * @param userName string representing the user's userName
	 * @return formatted string with the friends userNames
	 * @throws MainSystemException if the user does not exist
	 */
	public String getFriendsList(String userName) throws MainSystemException {
		User user = this.getUserByUserName(userName);
		return user.toStringFriends();
	}

	/**
	 * Method to check if an invitation was sent between two users
	 * @param userName string representing the user who received the invitation
	 * @param uName string representing the user who sent the invitation
	 * @return true if the invitation was sent, false otherwise
	 */
	public boolean hasInvitation(String userName, String uName) {
		try {
			User key = this.getUserByUserName(uName);
			User value = this.getUserByUserName(userName);

			if((this.invitations.containsKey(key.getUserName())) &&
					(this.invitations.get(key.getUserName()).contains(value.getMail()))) 
				return true;
			
		} catch (MainSystemException e) {
			//e.printStackTrace();
		}

		return false;
	}

}