package edu.ubb.arp.exceptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DalException extends Exception {
	private static final long serialVersionUID = 1L;

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

	private String errorMessage = null;
	private int errorCode = 0;
	
	public DalException(Throwable cause) {
		super(cause);
	}

	public DalException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.errorMessage = properties.getProperty(Integer.toString(errorCode));
	}

	public DalException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.errorMessage = properties.getProperty(Integer.toString(errorCode));
	}

	public DalException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.errorMessage = properties.getProperty(Integer.toString(errorCode));
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		if (errorMessage != null && errorCode != 0) {
			return errorMessage + ": " + super.getMessage();
		} else {
			return super.getMessage();
		}
	}
	
	public static String errCodeToMessage(int errorCode) {
		return properties.getProperty(Integer.toString(errorCode));
	}
	
	public static int errMessageToCode(String errorMessage) {
		return Integer.parseInt(properties.getProperty(errorMessage));
	}

}
