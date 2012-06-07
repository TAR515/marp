package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.Arrays;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *is the mirror of the users table , contains getters and setters , hashCode , equals and toString methods  
 */
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userID;
	private String userName;
	private byte[] password;
	private String phoneNumber;
	private String email;
	private Resources resource = null;
	/**
	 * constructor
	 */
	public Users() {
		this.userID = -1;
		this.userName = null;
		this.password = null;
		this.phoneNumber = null;
		this.email = null;
		this.resource = null;
	}
	/**
	 * constructor
	 * @param userID is the id of the user
	 * @param userName is the name of the user
	 * @param password is the password of the user
	 * @param hired is true if user is hired and false if not
	 * @param phoneNumber is the phone number of the user
	 * @param email is the email of the user
	 * @param resource 
	 */
	public Users(int userID, String userName, byte[] password, boolean hired, String phoneNumber, String email, Resources resource) {
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.resource = resource;
	}
	/**
	 * 
	 * @return returns the id of the user
	 */
	public int getUserID() {
		return userID;
	}
	/**
	 * sets the id of the user
	 * @param userID is the id of the user
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}
	/**
	 * 
	 * @return returns the name of the user
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * sets the name of the user
	 * @param userName is the name of the user
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 
	 * @return returns the password of the user
	 */
	public byte[] getPassword() {
		return password;
	}
	/**
	 * sets the password of the user
	 * @param password  is the password of the user
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}
	/**
	 * 
	 * @return returns the phone number of the user
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * sets the phone number of the user
	 * @param phoneNumber is the phone number of the user
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * 
	 * @return returns the email of the user
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * sets the email of the user
	 * @param email is the email of the user
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}
	/**
	 * 
	 * @return returns serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * return a hashCode value of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + userID;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	/**
	 * Indicates whether some other object is "equal to" this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (userID != other.userID)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	/**
	 * @return Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		return "Users [userID=" + userID + ", userName=" + userName + ", password=" + Arrays.toString(password)
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + ", resource=" + resource + "]";
	}

}
