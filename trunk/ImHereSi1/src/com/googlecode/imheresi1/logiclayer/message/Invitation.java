package com.googlecode.imheresi1.logiclayer.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class that implements an interface Message and handles the Invitation type
 * 
 * @author Arthur de Souza Ribeiro
 * @author Jose Laerte
 * @author Raquel Rolim
 * @author Raissa Sarmento
 * 
 */
public class Invitation extends Message {

	private String fromName;
	private String to;
	private String fromMail;
	private String directory;

	/**
	 * Constructor
	 * Creates a new Invitation object
	 * 
	 * @param fromName - string representing the name of the user who sent the invitation
	 * @param fromMail - string representing the email of the user who sent the invitation 
	 * @param to - string representing the user who receives the invitation
	 * @param dir - string representing the directory in which the predetermined invitation text is stored 
	 */
	public Invitation(String fromName, String fromMail, String to, String dir) {
		super(INVITATION_PATH);
		this.fromName = fromName;
		this.to = to;
		this.fromMail = fromMail;
		this.directory = dir;
	}

	/**
	 * @see Message#build()
	 */
	public String build() {
		StringBuilder sB = new StringBuilder();
		sB.append("From: iam@email.com");
		sB.append(System.getProperty("line.separator"));
		sB.append("to: " + this.to);
		sB.append(System.getProperty("line.separator"));
		sB.append("Subject: " + this.fromName
				+ " gostaria de compartilhar sua localização com você");
		sB.append(System.getProperty("line.separator"));
		sB.append(System.getProperty("line.separator"));
		
		String content = getInviteText();
		
		content = content.replace("<nomeUsuario>", this.fromName);
		content = content.replace("<emailUsuario>", this.fromMail);
		
		sB.append(content);
		sB.append(System.getProperty("line.separator"));
		sB.append("***************************************************************************************************");
		sB.append(System.getProperty("line.separator"));

		return sB.toString();
	}

	/**
	 * Method to get the predetermined invitation text stored as "convite.txt" in the directory set by the creator of the invitation. 
	 * @return string - representing the invitation.
	 */
	private String getInviteText(){
		StringBuffer buffer = new StringBuffer();
		try {
			Scanner in = new Scanner(new File(this.directory+"/convite.txt"));
			while(in.hasNext()){
				buffer.append(in.nextLine());
				buffer.append(System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
		return buffer.toString();
	}
	
}
