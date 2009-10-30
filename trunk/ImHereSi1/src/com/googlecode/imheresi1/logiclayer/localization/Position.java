package com.googlecode.imheresi1.logiclayer.localization;

import java.io.IOException;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

/**
 * 
 * Class that handles the Position type
 * This type stores values for latitude and longitude to specify the location of a user 
 * GeoIP module is needed in order to use the constructor through IP. 
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class Position {

	private double latitude;
	private double longitude;
	private final String DIR = System.getProperty("user.dir");
	private final String SEPARATOR = System.getProperty("file.separator");
	private final String DATABASE = DIR + SEPARATOR + "GeoLiteCity.dat";

	/**
	 * Constructor for creating the Position object in an automatic way.
	 * Uses GeoIP to find the values of longitude and latitude through the given IP.
	 * Sets the values obtained in the object. 
	 * @param ip - IP to obtain the values of latitude and longitude from
	 * @throws PositionException if the values ate not valid or could not find the GeoIP database
	 */
	public Position(String ip) throws PositionException {
		try {
			LookupService lookUp = new LookupService(DATABASE,
					LookupService.GEOIP_MEMORY_CACHE);
			Location localizacao = lookUp.getLocation(ip);
			if (localizacao == null) {
				throw new PositionException(
						"Nao foi possivel obter a localizacao.");
			}
			this.latitude = localizacao.latitude;
			this.longitude = localizacao.longitude;
			lookUp.close();
		} catch (IOException e) {
			throw new PositionException("Banco de dados nao encontrado");
		}
	}

	/**
	 * Constructor for creating the Position object in a manual way.
	 * Manually setting the values of latitude and longitude
	 * @param lat - new latitude to store
	 * @param lon - new longitude to store
	 * @throws PositionException if either one of the values passed are not valid. 
	 */
	public Position(double latitude, double longitude) throws PositionException {
		setPosition(latitude, longitude);
	}

	/**
	 * Sets the values of longitude and latitude stored in the position object.
	 * @param lat - new latitude to store
	 * @param lon - new longitude to store
	 * @throws PositionException if either one of the values passed are not valid. 
	 */
	public void setPosition(double lat, double lon) throws PositionException {
		if (!isLocation(lat, lon))
			throw new PositionException("Latitude/Longitude invalidos.");
		this.latitude = lat;
		this.longitude = lon;
	}

	/**
	 * Validate the given values passed as attributes.
	 * Longitude is valid if in the range -180 < longitude < 180 (inclusive)
	 * Latitude is valid if in the range -90 < longitude < 90 (inclusive)  
	 * @param lat - latitude to validate
	 * @param lon - longitude to validate
	 * @return boolean - true if both lon and lat are valid. false otherwise.
	 */
	private boolean isLocation(double lat, double lon) {
		return ((lon >= -180 && lon <= 180) && (lat >= -90 && lat <= 90));
	}

	/**
	 * This method returns the current Latitude stored in the Position object. 
	 * @return latitude
	 */
	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * This method returns the current Longitude stored in the Position object. 
	 * @return longitude
	 */
	public double getLongitude() {
		return this.longitude;
	}

	/**
	 * Method to format the data (Latitude and Longitude) stored in the Position object for better visualization and understandment of the values.
	 * @return position to string
	 */
	public String toString() {
		return "Lat: " + this.latitude + ", Long: " + this.longitude;
	}

}
