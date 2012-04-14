package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import edu.ubb.arp.datastructures.Booking;

public class Projects implements Serializable {
	private static final long serialVersionUID = 1L;

	private int projectID;
	private boolean openedStatus;
	private int deadLine;
	private String projectName;
	private String nextRelease;
	private Statuses currentStatus;
	private HashMap<Resources, Booking> booking;
	private List<Resources> projectLeaders;

	public Projects() {
		this.projectID = -1;
		this.openedStatus = false;
		this.deadLine = -1;
		this.projectName = null;
		this.nextRelease = null;
		this.currentStatus = null;
		this.booking = null;
		this.projectLeaders = null;
	}

	public Projects(int projectID, boolean openedStatus, int deadLine,
			String projectName, String nextRelease, Statuses currentStatus,
			HashMap<Resources, Booking> booking, List<Resources> projectLeaders) {
		this.projectID = projectID;
		this.openedStatus = openedStatus;
		this.deadLine = deadLine;
		this.projectName = projectName;
		this.nextRelease = nextRelease;
		this.currentStatus = currentStatus;
		this.booking = booking;
		this.projectLeaders = projectLeaders;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public boolean isOpenedStatus() {
		return openedStatus;
	}

	public void setOpenedStatus(boolean openedStatus) {
		this.openedStatus = openedStatus;
	}

	public int getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(int deadLine) {
		this.deadLine = deadLine;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getNextRelease() {
		return nextRelease;
	}

	public void setNextRelease(String nextRelease) {
		this.nextRelease = nextRelease;
	}

	public Statuses getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Statuses currentStatus) {
		this.currentStatus = currentStatus;
	}

	public HashMap<Resources, Booking> getBooking() {
		return booking;
	}

	public void setBooking(HashMap<Resources, Booking> booking) {
		this.booking = booking;
	}

	public List<Resources> getProjectLeaders() {
		return projectLeaders;
	}

	public void setProjectLeaders(List<Resources> projectLeaders) {
		this.projectLeaders = projectLeaders;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booking == null) ? 0 : booking.hashCode());
		result = prime * result
				+ ((currentStatus == null) ? 0 : currentStatus.hashCode());
		result = prime * result + deadLine;
		result = prime * result
				+ ((nextRelease == null) ? 0 : nextRelease.hashCode());
		result = prime * result + (openedStatus ? 1231 : 1237);
		result = prime * result + projectID;
		result = prime * result
				+ ((projectLeaders == null) ? 0 : projectLeaders.hashCode());
		result = prime * result
				+ ((projectName == null) ? 0 : projectName.hashCode());
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
		if (projectLeaders == null) {
			if (other.projectLeaders != null)
				return false;
		} else if (!projectLeaders.equals(other.projectLeaders))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Projects [projectID=" + projectID + ", openedStatus="
				+ openedStatus + ", deadLine=" + deadLine + ", projectName="
				+ projectName + ", nextRelease=" + nextRelease
				+ ", currentStatus=" + currentStatus + ", booking=" + booking
				+ ", projectLeaders=" + projectLeaders + "]";
	}

}
