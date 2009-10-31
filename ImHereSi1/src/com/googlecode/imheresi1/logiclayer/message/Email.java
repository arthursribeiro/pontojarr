package com.googlecode.imheresi1.logiclayer.message;

/**
 * Class that extends Message and handles the Email type
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */

public class Email extends Message {

	private String subject;
	private String msg;

	/**
	 * Constructor
	 * Creates a new Email object.
	 * 
	 * @param from string representing the user who sent the email
	 * @param to string representing the the user who will receive the email
	 * @param subject string representing the subject of the email
	 * @param msg string representing the email message itself.
	 */
	public Email(String from, String to, String subject, String msg) {
		super(EMAIL_PATH,from,to);
		this.subject = subject;
		this.msg = msg;
	}
	
	/**
	 * @see Message#buildBody()
	 */
	public String buildBody() {
		StringBuilder sB = new StringBuilder();

		sB.append("Subject: " + this.subject);
		sB.append(SYSTEM_SEPARATOR);
		sB.append(SYSTEM_SEPARATOR);
		sB.append(this.msg);
		sB.append(SYSTEM_SEPARATOR);
		
		return sB.toString();
	}

}
