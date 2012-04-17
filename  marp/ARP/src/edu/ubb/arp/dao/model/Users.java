package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Users implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userID;
	private String userName;
	private byte[] password;
	private String phoneNumber;
	private String email;
	private List<Resources> resource = null;

	public Users() {
		this.userID = -1;
		this.userName = null;
		this.password = null;
		this.phoneNumber = null;
		this.email = null;
		this.resource = null;
	}

	public Users(int userID, String userName, byte[] password, boolean hired,
			String phoneNumber, String email, List<Resources> resource) {
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.resource = resource;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Resources> getResource() {
		return resource;
	}

	public void setResource(List<Resources> resource) {
		this.resource = resource;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	@Override
	public String toString() {
		return "Users [userID=" + userID + ", userName=" + userName + ", password=" + Arrays.toString(password)
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + ", resource=" + resource + "]";
	}

	

}
