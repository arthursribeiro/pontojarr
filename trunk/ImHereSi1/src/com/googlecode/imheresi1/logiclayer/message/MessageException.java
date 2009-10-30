package com.googlecode.imheresi1.logiclayer.message;

/**
 * Class that handles the Email class exceptions
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class MessageException extends Exception {

	/**
	 * Constructor for the exception. 
	 * String passed is the error message
	 * @param reason - reason for which the exception was thrown.
	 */
	public MessageException(String reason) {
		super(reason);
	}

}
