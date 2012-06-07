package edu.ubb.arp.exceptions;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *
 */
public class BllExceptions extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static final String NO_SUCH_ALGORITHM_EXCEPTION = "NO_SUCH_ALGORITHM_EXCEPTION";
	
	private String code = null;
	/**
	 * constructor 
	 * @param cause is the cause if the throwable exception
	 */
	public BllExceptions(Throwable cause) {
		super(cause);
	}
	/**
	 * constructor
	 * @param code is the code of the error 
	 * @param message is the error message
	 */
	public BllExceptions(String code, String message) {
		super(message);
		this.code = code;
	}
	/**
	 * constructor
	 * @param code is the code of the error
	 * @param cause is the cause if the throwable exception
	 */
	public BllExceptions(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}
	/**
	 * constructor 
	 * @param code is the code of the error
	 * @param message is the error message
	 * @param cause is the cause if the throwable exceptio
	 */
	public BllExceptions(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	/**
	 * 
	 * @return returns code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return returns code and message
	 */
	public String getMessage() {
		if (code != null) {
			return code + ": " + super.getMessage();
		} else {
			return super.getMessage();
		}
	}
}
