package edu.ubb.arp.datastructures;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *contains getters and setters , hashCode , equals and toString methods  
 */
public class MyAccount {
	private String userName = null;
	private String resourceName = null;
	private String phoneNumber = null;
	private String email = null;
	
	/**
	 * constructor
	 * @param userName is the name of the user
	 * @param resourceName is the resource name of the user
	 * @param phoneNumber is the phone number of the user
	 * @param email is the email of the user
	 */
	public MyAccount(String userName, String resourceName, String phoneNumber, String email) {
		super();
		this.userName = userName;
		this.resourceName = resourceName;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	/**
	 * 
	 * @return returns userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * sets userName
	 * @param userName is the userName to be set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return returns resourceName;
	 */
	public String getResourceName() {
		return resourceName;
	}
	
	/**
	 * sets esourceName
	 * @param resourceName is the rresourceName to be set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * 
	 * @return returns phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * set phoneNumber
	 * @param phoneNumber is phoneNumber to be set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 
	 * @return returns email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * sets email
	 * @param email is the email to be set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * return a hashCode value of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
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
		MyAccount other = (MyAccount) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
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
		return "MyAccount [userName=" + userName + ", resourceName=" + resourceName + ", phoneNumber=" + phoneNumber + ", email="
				+ email + "]";
	}
	
	
}
