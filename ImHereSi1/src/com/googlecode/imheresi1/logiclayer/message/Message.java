package com.googlecode.imheresi1.logiclayer.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Message abstract Class
 * This class implements all methods associated with messages, except for the build Method.
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public abstract class Message {

	protected static final String EMAIL_PATH = "files/outputs/emails.log";
	protected static final String SMS_PATH = "files/outputs/sms.log";
	protected static final String INVITATION_PATH = "files/outputs/convites.log";	

	protected String path;
	
	public Message(String path){
		this.path = path;
	}
	
	/**
	 * Method to return the whole message, already formated and ready to be sent  
	 * @return string representing the formated message.
	 */
	public abstract String build();
	
	/**
	 * Method to effectively send a message, writes the content in the log. if no log exists, creates a new file
	 * @throws MessageException
	 */
	public void sendMessage() throws MessageException {
		try {
			FileInputStream file = new FileInputStream(this.path);
			FileWriter bOut = new FileWriter(new File(this.path), true);
			bOut.write(this.build());
			bOut.close();
			file.close();
		} catch (FileNotFoundException e) {
			try {
				FileOutputStream fs = new FileOutputStream(this.path);
				fs.write(this.build().getBytes());
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

}
