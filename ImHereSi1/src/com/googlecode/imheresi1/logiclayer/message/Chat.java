package com.googlecode.imheresi1.logiclayer.message;

/**
 * Class that implements Message and handles the Chat type
 * This class records the last message sent by each user.
 * Chat handles sinple user to user type conversations.
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */

public class Chat extends Message {

	private String user1;
	private String user2;
	private String[] lastMessages;

	private StringBuilder sB;

	/**
	 * Constructor
	 * Creates a new Chat object. 
	 * 
	 * @param u1 - user that will participate in the chat.
	 * @param u2 - the other user in the chat.
	 */
	public Chat(String u1, String u2) {
		super("");
		this.user1 = u1;
		this.user2 = u2;
		sB = new StringBuilder();
		lastMessages = new String[2];
		setPath();
	}

	/**
	 * Returns the last message the user has sent.
	 * @param username - user to get his last message sent 
	 * @return string representing the last message
	 */
	public String getLastMessage(String username){
		if(username.equals(user1)) return this.lastMessages[0];
		return this.lastMessages[1];
	}
	
	/**
	 * Adds a new chat message to the current chat.
	 * sets the last message attribute. Which saves each users last message sent.  
	 * @param receiver - the user that received the message.
	 * @param msg - the message the user received.
	 */
	public void addMsg(String receiver, String msg) {
		if (receiver.equals(user1)){
			sB.append(user2);
			this.lastMessages[1] = msg;
		}else {
			sB.append(user1);
			this.lastMessages[0] = msg;
		}
		sB.append(": " + msg);
		sB.append(System.getProperty("line.separator"));
	}

	/**
	 * @see Message#build()
	 */
	public String build() {
		return sB.toString();
	}

	/**
	 * Method to set the path name according to the user's envolved
	 */
	private void setPath() {
		if (user1.compareToIgnoreCase(user2) > 0)
			super.path = "files/chats/" + user2 + "-" + user1 + ".log";
		else
			super.path = "files/chats/" + user1 + "-" + user2 + ".log";
	}

}
