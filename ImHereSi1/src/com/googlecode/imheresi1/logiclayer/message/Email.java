package com.googlecode.imheresi1.logiclayer.message;

/**
 * Class that implements Message and handles the Email type
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */

public class Email extends Message {

	private String from;
	private String to;
	private String subject;
	private String msg;

	/**
	 * Constructor
	 * Creates a new Email object.
	 * 
	 * @param from - string representing the user who sent the email
	 * @param to - string representing the the user who will receive the email
	 * @param subject - string representing the subject of the email
	 * @param msg - string representing the email message itself.
	 */
	public Email(String from, String to, String subject, String msg) {
		super(EMAIL_PATH);
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.msg = msg;
	}

	/**
	 * @see Message#build()
	 */
	public String build() {

		StringBuilder sB = new StringBuilder();

		sB.append("From: " + this.from);
		sB.append(System.getProperty("line.separator"));
		sB.append("to: " + this.to);
		sB.append(System.getProperty("line.separator"));
		sB.append("Subject: " + this.subject);
		sB.append(System.getProperty("line.separator"));
		sB.append(System.getProperty("line.separator"));
		sB.append(this.msg);
		sB.append(System.getProperty("line.separator"));
		sB.append(System.getProperty("line.separator"));
		sB.append("***************************************************************************************************");
		sB.append(System.getProperty("line.separator"));
		return sB.toString();
	}

}
