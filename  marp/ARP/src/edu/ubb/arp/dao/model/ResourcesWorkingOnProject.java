package edu.ubb.arp.dao.model;

public class ResourcesWorkingOnProject {
	private int projectID = -1;
	private String projectName = null;
	private boolean openedStatus = false;
	private int startWeek = -1;
	private int deadLine = -1;
	private String nextRelease = null;
	private String statusName = null;
	private boolean isLeader = false;
	
	public ResourcesWorkingOnProject() {
		super();
		this.projectID = -1;
		this.projectName = null;
		this.openedStatus = false;
		this.startWeek = -1;
		this.deadLine = -1;
		this.nextRelease = null;
		this.statusName = null;
		this.isLeader = false;
	}
	
	public ResourcesWorkingOnProject(int projectID, String projectName, boolean openStatus, int startWeek, int deadline,
			String nextRelease, String statusName, boolean isLeader) {
		super();
		this.projectID = projectID;
		this.projectName = projectName;
		this.openedStatus = openStatus;
		this.startWeek = startWeek;
		this.deadLine = deadline;
		this.nextRelease = nextRelease;
		this.statusName = statusName;
		this.isLeader = isLeader;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isOpenedStatus() {
		return openedStatus;
	}

	public void setOpenedStatus(boolean openedStatus) {
		this.openedStatus = openedStatus;
	}

	public int getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	public int getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(int deadLine) {
		this.deadLine = deadLine;
	}

	public String getNextRelease() {
		return nextRelease;
	}

	public void setNextRelease(String nextRelease) {
		this.nextRelease = nextRelease;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public boolean isLeader() {
		return isLeader;
	}

	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deadLine;
		result = prime * result + (isLeader ? 1231 : 1237);
		result = prime * result + ((nextRelease == null) ? 0 : nextRelease.hashCode());
		result = prime * result + (openedStatus ? 1231 : 1237);
		result = prime * result + projectID;
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + startWeek;
		result = prime * result + ((statusName == null) ? 0 : statusName.hashCode());
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
		ResourcesWorkingOnProject other = (ResourcesWorkingOnProject) obj;
		if (deadLine != other.deadLine)
			return false;
		if (isLeader != other.isLeader)
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
		if (statusName == null) {
			if (other.statusName != null)
				return false;
		} else if (!statusName.equals(other.statusName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResourcesWorkingOnProject [projectID=" + projectID + ", projectName=" + projectName + ", openStatus="
				+ openedStatus + ", startWeek=" + startWeek + ", deadline=" + deadLine + ", nextRelease=" + nextRelease
				+ ", statusName=" + statusName + ", isLeader=" + isLeader + "]";
	}
	
}
