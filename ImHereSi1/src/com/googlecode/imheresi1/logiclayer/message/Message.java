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
	protected static final String INVITATION_SENDER = "iam@email.com";
	protected static final String SYSTEM_SEPARATOR = System.getProperty("line.separator");
	protected static final String SEPARATOR = "***************************************************************************************************";
	
	private String sender;
	private String receiver;
	
	protected String path;
	
	public Message(String path,String from, String to){
		this.path = path;
		this.sender = from;
		this.receiver = to;
	}
	
	/**
	 * Method to return the whole message, already formated and ready to be sent  
	 * @return string representing the formated message.
	 */
	public abstract String buildBody();
	
	public String build(){
		StringBuilder sb = new StringBuilder();
		sb.append(buildHeader());
		sb.append(buildBody());
		sb.append(buildFooter());
		return sb.toString();
	}
	
	public String buildHeader(){
		StringBuilder sB = new StringBuilder();
		sB.append("From: " + this.sender);
		sB.append(SYSTEM_SEPARATOR);
		sB.append("to: " + this.receiver);
		sB.append(SYSTEM_SEPARATOR);
		return sB.toString();
	}
	
	public String buildFooter(){
		StringBuilder sB = new StringBuilder();
		sB.append(SYSTEM_SEPARATOR);
		sB.append(SEPARATOR);
		sB.append(SYSTEM_SEPARATOR);
		return sB.toString();
	}	
	
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
