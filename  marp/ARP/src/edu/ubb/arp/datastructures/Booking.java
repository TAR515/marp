package edu.ubb.arp.datastructures;

public class Booking {
	private int resourceID = -1;
	private int projectID = -1;
	private int week = -1;
	private int ratio = -1;
	private boolean isLeader = false;

	public Booking() {
		this.resourceID = -1;
		this.projectID = -1;
		this.week = -1;
		this.ratio = -1;
		this.isLeader = false;
	}

	public Booking(int resourceID, int projectID, int week, int ratio, boolean isLeader) {
		this.resourceID = resourceID;
		this.projectID = projectID;
		this.week = week;
		this.ratio = ratio;
		this.isLeader = isLeader;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public boolean isLeader() {
		return isLeader;
	}

	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}

	public int getResourceID() {
		return resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

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

	@Override
	public String toString() {
		return "Booking [resourceID=" + resourceID + ", projectID=" + projectID + ", week=" + week + ", ratio=" + ratio
				+ ", isLeader=" + isLeader + "]";
	}
	
	

}
