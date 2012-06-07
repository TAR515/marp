package edu.ubb.arp.dao.model;

import java.io.Serializable;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *is the mirror of the statuses table , contains getters and setters , hashCode , equals and toString methods  
 */
public class Statuses implements Serializable {
	private static final long serialVersionUID = 1L;

	private int statusID;
	private String statusName;
	/**
	 * constructor
	 */
	public Statuses() {
		statusID = -1;
		statusName = null;
	}
	/**
	 * constructor
	 * @param statusID is the id of the status
	 * @param statusName is the name of the status
	 */
	public Statuses(int statusID, String statusName) {
		this.statusID = statusID;
		this.statusName = statusName;
	}
	/**
	 * 
	 * @return returns statusID
	 */
	public int getStatusID() {
		return statusID;
	}
	/**
	 * set statusID
	 * @param statusID is the id of the status
	 */
	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}
	/**
	 * 
	 * @return returns the status name
	 */
	public String getStatusName() {
		return statusName;
	}
	/**
	 * sets statusName
	 * @param statusName is the name of the status
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
		result = prime * result + statusID;
		result = prime * result
				+ ((statusName == null) ? 0 : statusName.hashCode());
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
		Statuses other = (Statuses) obj;
		if (statusID != other.statusID)
			return false;
		if (statusName == null) {
			if (other.statusName != null)
				return false;
		} else if (!statusName.equals(other.statusName))
			return false;
		return true;
	}
	/**
	 * @return Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		return "Statuses [statusID=" + statusID + ", statusName=" + statusName
				+ "]";
	}

}
