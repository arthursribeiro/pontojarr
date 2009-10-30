package com.googlecode.imheresi1.logiclayer.message;

/**
 * Class that implements Message and implements a SMS type
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class SMS extends Message {

	private String from;
	private String to;
	private String msg;

	/**
	 * Constructor
	 * Creates a new SMS object
	 * 
	 * @param from - string representing the user who sent the SMS
	 * @param to - string representing the user who will receive the SMS
	 * @param msg - string representing the message itself.
	 */
	public SMS(String from, String to, String msg) {
		super(SMS_PATH);
		this.from = from;
		this.to = to;
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
		sB.append(System.getProperty("line.separator"));
		sB.append(this.msg);
		sB.append(System.getProperty("line.separator"));
		sB.append(System.getProperty("line.separator"));
		sB.append("***************************************************************************************************");
		sB.append(System.getProperty("line.separator"));

		return sB.toString();
	}

}
