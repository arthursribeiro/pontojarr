package com.googlecode.imheresi1.presentationlayer.commandline;

import java.util.Scanner;

import com.googlecode.imheresi1.logiclayer.MainSystem;
import com.googlecode.imheresi1.logiclayer.MainSystemException;
import com.googlecode.imheresi1.logiclayer.UserException;
import com.googlecode.imheresi1.logiclayer.localization.PositionException;

public class CommandLineInterface {
	
	private static Scanner input;
	private static MainSystem mySystem;
	private static final String SEPARATOR = System.getProperty("line.separator");


	private static final String PROMPT_1 = "<<< Bem vindo ao I'm here! >>>" + SEPARATOR + 
	SEPARATOR +	"Escolha a opcao desejada:" + SEPARATOR + "1. Login" 	+ SEPARATOR + 
	"2. Criar Usuario" 	+ SEPARATOR + "3. Exit";
	private static final String PROMPT_2 = "Opcao: ";

	private static final int LOGIN = 1;
	private static final int CREATE_USER = 2;
	private static final int EXIT = 3;

	public CommandLineInterface(){
		mySystem = MainSystem.getInstance();
	}
	
	private static int getOption(String option){
		int chosenNumber;

		try{
			chosenNumber = Integer.parseInt(option);
		} catch (NumberFormatException e) {
			chosenNumber = -1;
		}

		return chosenNumber;
	}

	private static String createUser(Scanner entrada) {
		String userName, password, email, name, phone;
	
		System.out.println(SEPARATOR + "<<< Cadastro de usuario >>>" + SEPARATOR + 
				"Preencha o formulario abaixo:");
		
		//Form
		System.out.print(SEPARATOR + "Nome: ");
		name = entrada.nextLine().trim();
		System.out.print(SEPARATOR + "Email: ");
		email = entrada.nextLine().trim();
		System.out.print(SEPARATOR  + "Telefone: ");
		phone = entrada.nextLine().trim();
		System.out.print(SEPARATOR + "Username: ");
		userName = entrada.nextLine().trim();
		System.out.print(SEPARATOR + "Senha: ");
		password = entrada.nextLine().trim();
		
		
		try{
			mySystem.createUser(userName, password, email, name, phone);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return createUser(entrada);
		}	
		
		System.out.println(SEPARATOR + "Usuario criado com sucesso!");
		return getLocationData(userName, entrada, password);
	}

	private static String getLocationData(String userName, Scanner entrada, String password){
		System.out.print(SEPARATOR + "Ip: ");
		String ip = entrada.nextLine().trim();
		try {
			mySystem.logIn(userName, password, ip);
			mySystem.setLocal(userName, ip);
		} catch (UserException e) {
			System.out.println(SEPARATOR + e.getMessage()); //"Login/senha invalidos." ou "IP invalido."
			if(e.getMessage().equals("IP invalido."))
			return getLocationData(userName, entrada, password);
		} catch (PositionException e) {
			double latitude, longitude;
			System.out.println(e.getMessage()); //"Nao foi possivel obter a localizacao."
			System.out.println(SEPARATOR + "Digite sua localizacao manualmente");
			latitude = getDoubleValue("Latitude: ", entrada);
			longitude = getDoubleValue("Longitude: ", entrada);
			try {
				mySystem.setLocal(userName, latitude, longitude);
				System.out.println("<<< Login efetuado com sucesso >>>");
				return userName;
			} catch (PositionException e1) {
				System.out.println(SEPARATOR + e1.getMessage() + SEPARATOR);
			} catch (MainSystemException e1) {
				System.out.println(SEPARATOR + e1.getMessage() + SEPARATOR);
			}
		} catch (MainSystemException e) {
			System.out.println(SEPARATOR + e.getMessage() + SEPARATOR);
		}
		return null;
	}
	
	private static String logIn(Scanner entrada){
		String userNameToLogIn, passwordToLogin;
		System.out.println(SEPARATOR + "<<< Log In >>>");
		System.out.print(SEPARATOR + "Username: ");
		userNameToLogIn = entrada.nextLine().trim();
		System.out.print(SEPARATOR + "Senha: ");
		passwordToLogin = entrada.nextLine().trim();
		
		return getLocationData(userNameToLogIn, entrada, passwordToLogin);
	}
	
	private static double getDoubleValue(String prompt, Scanner entrada){
		while(true){
			System.out.print(SEPARATOR + prompt);
			try {
				String numero = entrada.nextLine();
				double valorDouble = Double.parseDouble(numero);
				return valorDouble;
			} catch(NumberFormatException n) {
			}
		}
	}
	
	public static void main(String[] args) {
		input = new Scanner(System.in);

		System.out.print(PROMPT_1 + SEPARATOR + PROMPT_2);
		int option = getOption(input.nextLine());

		while (option != EXIT) {
			switch (option) {
			case LOGIN:
				String username = logIn(input);
				if(username != null) {
					SystemSecondMenu secondMenu = new SystemSecondMenu(input, username);
					secondMenu.mainLoop();
				}
				break;
			case CREATE_USER:
				String userName = createUser(input);
				if(userName != null){
					SystemSecondMenu secondMenu = new SystemSecondMenu(input, userName);
					secondMenu.mainLoop();
				}
				break;
			case EXIT:
				System.out.println(SEPARATOR + "Obrigado, volte sempre!");
				System.exit(0);
				break;
			default:
				System.out.println(SEPARATOR + "Opcao invalida!");
				break;
			}

			System.out.print(SEPARATOR + PROMPT_1 + SEPARATOR + PROMPT_2);
			option = getOption(input.nextLine());
		}
	}
	
	
}