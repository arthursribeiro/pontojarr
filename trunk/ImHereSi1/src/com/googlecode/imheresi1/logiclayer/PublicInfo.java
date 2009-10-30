package com.googlecode.imheresi1.logiclayer;

import com.googlecode.imheresi1.logiclayer.localization.Position;
import com.googlecode.imheresi1.logiclayer.localization.PositionException;

/**
 * Class that implements the PublicInfo type
 * This Class holds the data that are public to friends of the user.
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class PublicInfo {

	private String name;
	private String login;
	private Position position;
	private String telephoneNumber;
	private String email;

	/**
	 * Method that returns the Position object that the PublicInfo contains. 
	 * @return Position - The position of the user
	 * @throws PositionException if the position was not possible to obtain.
	 */
	public Position getPosition() throws PositionException {
		if (this.position == null) {
			throw new PositionException("Nao foi possivel obter a localizacao.");
		}
		return this.position;
	}

	/**
	 * Method that returns the user's name that the PublicInfo contains. 
	 * @return name - string representing the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Method that returns the user's login that the PublicInfo contains. 
	 * @return login - string representing the login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Method that sets the user's login for the PublicInfo to hold. 
	 * @param log - string representing the new login.
	 */
	public void setLogin(String log) {
		this.login = log;
	}

	/**
	 * Method that returns the user's phone number that the PublicInfo contains. 
	 * @return phone number - string representing the phone number
	 */
	public String getTelephoneNumber() {
		return this.telephoneNumber;
	}

	/**
	 * Method that returns the user's email that the PublicInfo contains. 
	 * @return email - string representing the email
	 */
	public String getEMail() {
		return this.email;
	}

	/**
	 * Method that sets the user's name for the PublicInfo to hold. 
	 * @param newName - string representing the new name.
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Method that sets the user's position for the PublicInfo to hold. 
	 * @param ip - string representing the IP for the automatic creation of a new position.
	 * @throws PositionException if the values ate not valid or could not find the GeoIP database
	 */
	public void setPosition(String ip) throws PositionException {
		this.position = new Position(ip);
	}

	/**
	 * Method that sets the user's position for the PublicInfo to hold. 
	 * @param latitude - latitude for the new position object
	 * @param longitude - longitude for the new position object
	 * @throws PositionException if the values are invalid.
	 */
	public void setPositionManual(double latitude, double longitude)
			throws PositionException {
		if (this.position == null) {
			this.position = new Position(latitude, longitude);
		} else {
			this.position.setPosition(latitude, longitude);
		}
	}

	/**
	 * Method that sets the user's telephone number for the PublicInfo to hold. 
	 * @param newTelephoneNumber - string representing the new telephone number.
	 */
	public void setTelephoneNumber(String newTelephoneNumber) {
		this.telephoneNumber = newTelephoneNumber;
	}

	/**
	 * Method that sets the user's email for the PublicInfo to hold. 
	 * @param newEmail - string representing the new email.
	 */
	public void setEmail(String newEmail) {
		this.email = newEmail;
	}
}
