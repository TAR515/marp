package edu.ubb.arp.exceptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DalErrorMessages {
	private static Properties properties;
	
	static {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("c://arplogs//ErrorMessages.properties"));
		} catch (IOException e) {
			System.out.println("Hiba a propertyben");
			e.printStackTrace();
		}
	}
	
	public static String errCodeToMessage(int errorCode) {
		return properties.getProperty(Integer.toString(errorCode));
	}

	public static int errMessageToCode(String errorMessage) {
		return Integer.parseInt(properties.getProperty(errorMessage));
	}
}
