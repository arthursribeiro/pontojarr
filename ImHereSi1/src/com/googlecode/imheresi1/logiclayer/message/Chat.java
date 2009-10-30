package com.googlecode.imheresi1.logiclayer.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

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

public class Chat {

	private String user1;
	private String user2;
	private String[] lastMessages;
	private String path;

	/**
	 * Constructor
	 * Creates a new Chat object. 
	 * 
	 * @param u1 - user that will participate in the chat.
	 * @param u2 - the other user in the chat.
	 */
	public Chat(String u1, String u2) {
		this.user1 = u1;
		this.user2 = u2;
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

	public void sendMsg(String receiver, String msg) throws MessageException{
		StringBuilder sB = new StringBuilder();
		if (receiver.equals(user1)){
			sB.append(user2);
			this.lastMessages[1] = msg;
		} else if(receiver.equals(user2)){
			sB.append(user1);
			this.lastMessages[0] = msg;
		} else {
			throw new MessageException("Usuario não esta no Chat");
		}
		sB.append(": " + msg);
		sB.append(System.getProperty("line.separator"));
		send(sB);
	}
	
	public void send(StringBuilder sB) throws MessageException {
		try {
			FileInputStream file = new FileInputStream(this.path);
			FileWriter bOut = new FileWriter(new File(this.path), true);
			bOut.write(sB.toString());
			bOut.close();
			file.close();
		} catch (FileNotFoundException e) {
			try {
				FileOutputStream fs = new FileOutputStream(this.path);
				fs.write(sB.toString().getBytes());
				fs.close();
			} catch (IOException io) {
				throw new MessageException(
						"Nao foi possivel enviar a mensagem.");
			}
		} catch (IOException io) {
			throw new MessageException(
					"Nao foi possivel enviar a mensagem.");
		}
	}

	/**
	 * Method to set the path name according to the user's involved
	 */
	private void setPath() {
		if (user1.compareToIgnoreCase(user2) > 0)
			this.path = "files/chats/" + user2 + "-" + user1 + ".log";
		else
			this.path = "files/chats/" + user1 + "-" + user2 + ".log";
	}

}
