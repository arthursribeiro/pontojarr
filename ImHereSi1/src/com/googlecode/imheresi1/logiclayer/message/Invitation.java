package com.googlecode.imheresi1.logiclayer.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class that extends Message and handles the Invitation type
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class Invitation extends Message {

	private String fromName;
	private String fromMail;
	private String directory;

	/**
	 * Constructor
	 * Creates a new Invitation object
	 * 
	 * @param fromName string representing the name of the user who sent the invitation
	 * @param fromMail string representing the email of the user who sent the invitation 
	 * @param to string representing the user who receives the invitation
	 * @param dir string representing the directory in which the predetermined invitation text is stored 
	 */
	public Invitation(String fromName, String fromMail, String to, String dir) {
		super(INVITATION_PATH,INVITATION_SENDER,to);
		this.fromName = fromName;
		this.fromMail = fromMail;
		this.directory = dir;
	}

	/**
	 * @see Message#buildBody()
	 */
	public String buildBody() {
		StringBuilder sB = new StringBuilder();
		sB.append("Subject: " + this.fromName
				+ " gostaria de compartilhar sua localização com você");
		sB.append(SYSTEM_SEPARATOR);
		sB.append(SYSTEM_SEPARATOR);
		
		String content = getInviteText();
		
		content = content.replace("<nomeUsuario>", this.fromName);
		content = content.replace("<emailUsuario>", this.fromMail);
		
		sB.append(content);
		
		return sB.toString();
	}

	/**
	 * Method to get the predetermined invitation text stored as "convite.txt" in the directory set by the creator of the invitation. 
	 * @return string representing the invitation.
	 */
	private String getInviteText(){
		StringBuffer buffer = new StringBuffer();
		try {
			Scanner in = new Scanner(new File(this.directory+"/convite.txt"));
			while(in.hasNext()){
				buffer.append(in.nextLine());
				buffer.append(SYSTEM_SEPARATOR);
			}
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
		return buffer.toString();
	}
	
}
