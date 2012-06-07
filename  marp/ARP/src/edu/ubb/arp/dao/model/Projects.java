package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.HashMap;

import edu.ubb.arp.datastructures.Booking;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *is the mirror of the projects table , contains getters and setters , hashCode , equals and toString methods  
 */
public class Projects implements Serializable {
	private static final long serialVersionUID = 1L;

	private int projectID;
	private boolean openedStatus;
	private int startWeek;
	private int deadLine;
	private String projectName;
	private String nextRelease;
	private Statuses currentStatus;
	private HashMap<Resources, Booking> booking;
	private HashMap<Resources, Boolean> usersInProjects;
	/**
	 * constructor
	 */
	public Projects() {
		this.projectID = -1;
		this.openedStatus = false;
		this.startWeek = -1;
		this.deadLine = -1;
		this.projectName = null;
		this.nextRelease = null;
		this.currentStatus = null;
		this.booking = null;
		this.usersInProjects = null;
	}
	/**
	 * constructor
	 * @param projectID is the id of the project
	 * @param openedStatus is the status of the project
	 * @param startWeek is the starting week of the project
	 * @param deadLine is the deadline of the project
	 * @param projectName is the name of the project
	 * @param nextRelease is the next release version of the project
	 * @param currentStatus is the current status of the project
	 * @param booking is HashMap object which contains resources and booking objects
	 * @param usersInProjects is a HashMap object which contains resources object and boolean values 
	 */
	public Projects(int projectID, boolean openedStatus, int startWeek, int deadLine, String projectName, String nextRelease,
			Statuses currentStatus, HashMap<Resources, Booking> booking, HashMap<Resources, Boolean> usersInProjects) {
		this.projectID = projectID;
		this.openedStatus = openedStatus;
		this.startWeek = startWeek;
		this.deadLine = deadLine;
		this.projectName = projectName;
		this.nextRelease = nextRelease;
		this.currentStatus = currentStatus;
		this.booking = booking;
		this.usersInProjects = usersInProjects;
	}
	/**
	 * 
	 * @return returns the projectID of the project
	 */
	public int getProjectID() {
		return projectID;
	}
	/**
	 * returns the projectID of the project
	 * @param projectID is the id of the project
	 */
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	/**
	 * 
	 * @return true if the project is opened or false if closed
	 */
	public boolean isOpenedStatus() {
		return openedStatus;
	}
	/**
	 * sets projects openStatus to true or false
	 * @param openedStatus is true if project is opened or false if project is closed
	 */
	public void setOpenedStatus(boolean openedStatus) {
		this.openedStatus = openedStatus;
	}
	/**
	 * 
	 * @return returns the starting week of the project 
	 */
	public int getStartWeek() {
		return startWeek;
	}
	/**
	 * set the starting week of the project
	 * @param startWeek is the starting week of the project
	 */
	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}
	/**
	 * 
	 * @return returns the deadline of the project
	 */
	public int getDeadLine() {
		return deadLine;
	}
	/**
	 * set the deadline of  the project
	 * @param deadLine is the deadline of the project
	 */
	public void setDeadLine(int deadLine) {
		this.deadLine = deadLine;
	}
	/**
	 * 
	 * @return the projectName of the project
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * set the projectName of the project
	 * @param projectName is the name of the project
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 
	 * @return returns the projects next release version
	 */
	public String getNextRelease() {
		return nextRelease;
	}
	/**
	 * set the projects nextRelease version
	 * @param nextRelease is the projects nextRelease version
	 */
	public void setNextRelease(String nextRelease) {
		this.nextRelease = nextRelease;
	}
	/**
	 * 
	 * @return returns the currentStatus of the project
	 */
	public Statuses getCurrentStatus() {
		return currentStatus;
	}
	/**
	 * sets the current status of the project
	 * @param currentStatus is the current status of the project
	 */
	public void setCurrentStatus(Statuses currentStatus) {
		this.currentStatus = currentStatus;
	}
	/**
	 * 
	 * @return returns the booking of the project
	 */
	public HashMap<Resources, Booking> getBooking() {
		return booking;
	}
	/**
	 * sets the projects booking attribute
	 * @param booking is HashMap object which contains resources and booking objects
	 */
	public void setBooking(HashMap<Resources, Booking> booking) {
		this.booking = booking;
	}
	/**
	 * 
	 * @return returns the userInProject attribute of the project
	 */
	public HashMap<Resources, Boolean> getUsersInProjects() {
		return usersInProjects;
	}
	/**
	 * 
	 * @param usersInProjects is a HashMap object which contains resources object and boolean values
	 */
	public void setUsersInProjects(HashMap<Resources, Boolean> usersInProjects) {
		this.usersInProjects = usersInProjects;
	}
	/**
	 * 
	 * @return serialVersionUID
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
		result = prime * result + ((booking == null) ? 0 : booking.hashCode());
		result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
		result = prime * result + deadLine;
		result = prime * result + ((nextRelease == null) ? 0 : nextRelease.hashCode());
		result = prime * result + (openedStatus ? 1231 : 1237);
		result = prime * result + projectID;
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + startWeek;
		result = prime * result + ((usersInProjects == null) ? 0 : usersInProjects.hashCode());
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
		Projects other = (Projects) obj;
		if (booking == null) {
			if (other.booking != null)
				return false;
		} else if (!booking.equals(other.booking))
			return false;
		if (currentStatus == null) {
			if (other.currentStatus != null)
				return false;
		} else if (!currentStatus.equals(other.currentStatus))
			return false;
		if (deadLine != other.deadLine)
			return false;
		if (nextRelease == null) {
			if (other.nextRelease != null)
				return false;
		} else if (!nextRelease.equals(other.nextRelease))
			return false;
		if (openedStatus != other.openedStatus)
			return false;
		if (projectID != other.projectID)
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (startWeek != other.startWeek)
			return false;
		if (usersInProjects == null) {
			if (other.usersInProjects != null)
				return false;
		} else if (!usersInProjects.equals(other.usersInProjects))
			return false;
		return true;
	}
	/**
	 * @return Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		return "Projects [projectID=" + projectID + ", openedStatus=" + openedStatus + ", startWeek=" + startWeek + ", deadLine="
				+ deadLine + ", projectName=" + projectName + ", nextRelease=" + nextRelease + ", currentStatus=" + currentStatus
				+ ", booking=" + booking + ", usersInProjects=" + usersInProjects + "]";
	}

}
