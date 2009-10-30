package com.googlecode.imheresi1.logiclayer.localization;

/**
 * Class that handles the Position class exceptions
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class PositionException extends Exception {

	/**
	 * Constructor for the exception. 
	 * String passed is the error message
	 * @param motivo - reason for which the exception was thrown.
	 */
	public PositionException(String motivo) {
		super(motivo);
	}

}
