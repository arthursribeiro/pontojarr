package com.googlecode.imheresi1.logiclayer;

/**
 * Class that handles the PersistenceManager class exceptions
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class PersistenceManagerException extends Exception {

	/**
	 * Constructor for the exception. 
	 * String passed is the error message
	 * @param motivo  - reason for which the exception was thrown.
	 */
	public PersistenceManagerException(String reason) {
		super(reason);
	}

}
