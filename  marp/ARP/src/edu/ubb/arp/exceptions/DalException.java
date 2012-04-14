package edu.ubb.arp.exceptions;

public class DalException extends Exception {
	private static final long serialVersionUID = 1L;

	public static final String NO_CONNECTION = "NO_CONNECTION";
	public static final String TRANSACTION_ROLLBACKED = "TRANSACTION_ROLLBACKED";

	private String code = null;

	public DalException(Throwable cause) {
		super(cause);
	}

	public DalException(String code, String message) {
		super(message);
		this.code = code;
	}

	public DalException(String code, String message, Throwable cause) {
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
