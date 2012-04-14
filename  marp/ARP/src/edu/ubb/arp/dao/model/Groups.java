package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.List;

public class Groups implements Serializable {
	private static final long serialVersionUID = 1L;

	private int groupID;
	private String groupName;
	private List<Resources> resource;

	public Groups() {
		this.groupID = -1;
		this.groupName = null;
		this.resource = null;
	}

	public Groups(int groupID, String groupName, List<Resources> resource) {
		this.groupID = groupID;
		this.groupName = groupName;
		this.resource = resource;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
		result = prime * result + groupID;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
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
		Groups other = (Groups) obj;
		if (groupID != other.groupID)
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Groups [groupID=" + groupID + ", groupName=" + groupName
				+ ", resource=" + resource + "]";
	}

}
