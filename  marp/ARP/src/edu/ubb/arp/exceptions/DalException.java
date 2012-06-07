package edu.ubb.arp.exceptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *
 */
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
	/**
	 * constructor
	 * @param errorCode is the code of the error 
	 */
	public DalException(int errorCode) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = properties.getProperty(Integer.toString(errorCode));
	}
	/**
	 *constructor 
	 * @param cause is the cause if the throwable exception
	 */
	public DalException(Throwable cause) {
		super(cause);
	}
	/**
	 * constructor
	 * @param errorCode is the code of the error 
	 * @param message is the error message
	 */
	public DalException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.errorMessage = properties.getProperty(Integer.toString(errorCode));
	}
	/**
	 * constructor
	 * @param errorCode is the code of the error
	 * @param cause is the cause if the throwable exception
	 */
	public DalException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.errorMessage = properties.getProperty(Integer.toString(errorCode));
	}
	/**
	 * constructor
	 * @param errorCode is the code of the error
	 * @param message is the error message
	 * @param cause is the cause if the throwable exception
	 */
	public DalException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.errorMessage = properties.getProperty(Integer.toString(errorCode));
	}
	/**
	 * 
	 * @return returns the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * 
	 * @return returns errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @return returns code and message
	 */
	public String getMessage() {
		if (errorMessage != null && errorCode != 0) {
			return errorMessage + ": " + super.getMessage();
		} else {
			return super.getMessage();
		}
	}
	/**
	 * 
	 * @param errorCode is the code of the error
	 * @return returns  the matching error message of the error code
	 */
	public static String errCodeToMessage(int errorCode) {
		return properties.getProperty(Integer.toString(errorCode));
	}
	/**
	 * 
	 * @param errorMessage is the error message of the error 
	 * @return returns the matching error code of the error message
	 */
	public static int errMessageToCode(String errorMessage) {
		return Integer.parseInt(properties.getProperty(errorMessage));
	}

}
