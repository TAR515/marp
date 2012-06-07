package edu.ubb.arp.datastructures;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *is the mirror of the booking table , contains getters and setters , hashCode , equals and toString methods  
 */
public class Booking {
	private int resourceID = -1;
	private int projectID = -1;
	private int week = -1;
	private int ratio = -1;
	private boolean isLeader = false;
	/**
	 * constructor
	 */
	public Booking() {
		this.resourceID = -1;
		this.projectID = -1;
		this.week = -1;
		this.ratio = -1;
		this.isLeader = false;
	}
	/**
	 * constructor
	 * @param resourceID is the id of the booked resource
	 * @param projectID is the id of the project where the resource is booked
	 * @param week is the week in which the resource is booked
	 * @param ratio is the ratio with which the resource is booked
	 * @param isLeader is true if the resource is leader or false if is not
	 */
	public Booking(int resourceID, int projectID, int week, int ratio, boolean isLeader) {
		this.resourceID = resourceID;
		this.projectID = projectID;
		this.week = week;
		this.ratio = ratio;
		this.isLeader = isLeader;
	}
	/**
	 * 
	 * @return returns week
	 */
	public int getWeek() {
		return week;
	}
	/**
	 * sets week 
	 * @param week is the week to be set
	 */
	public void setWeek(int week) {
		this.week = week;
	}
	/**
	 * 
	 * @return returns ratio
	 */
	public double getRatio() {
		return ratio;
	}
	/**
	 * set ratio
	 * @param ratio is the ratio to be set
	 */
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
	/**
	 * 
	 * @return returns true if isLeader is true else returns false
	 */
	public boolean isLeader() {
		return isLeader;
	}
	/**
	 * set isLeader
	 * @param isLeader  is the isLeader to be set
	 */
	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	/**
	 * 
	 * @return returns ResourceID
	 */
	public int getResourceID() {
		return resourceID;
	}
	/**
	 * set ResourceID
	 * @param resourceID is the resourceID to be set
	 */
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}
	/**
	 * 
	 * @return returns projectID
	 */
	public int getProjectID() {
		return projectID;
	}
	/**
	 * set projectID
	 * @param projectID is the projectID to be set
	 */
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	/**
	 * return a hashCode value of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isLeader ? 1231 : 1237);
		result = prime * result + projectID;
		result = prime * result + ratio;
		result = prime * result + resourceID;
		result = prime * result + week;
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
		Booking other = (Booking) obj;
		if (isLeader != other.isLeader)
			return false;
		if (projectID != other.projectID)
			return false;
		if (ratio != other.ratio)
			return false;
		if (resourceID != other.resourceID)
			return false;
		if (week != other.week)
			return false;
		return true;
	}
	/**
	 * @return Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		return "Booking [resourceID=" + resourceID + ", projectID=" + projectID + ", week=" + week + ", ratio=" + ratio
				+ ", isLeader=" + isLeader + "]";
	}
	
	

}
