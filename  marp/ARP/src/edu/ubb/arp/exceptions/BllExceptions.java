package edu.ubb.arp.exceptions;

public class BllExceptions extends Exception {
	private static final long serialVersionUID = 1L;
	
	public static final String NO_SUCH_ALGORITHM_EXCEPTION = "NO_SUCH_ALGORITHM_EXCEPTION";
	
	private String code = null;
	
	public BllExceptions(Throwable cause) {
		super(cause);
	}

	public BllExceptions(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public BllExceptions(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}
	
	public BllExceptions(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		if (code != null) {
			return code + ": " + super.getMessage();
		} else {
			return super.getMessage();
		}
	}
}
