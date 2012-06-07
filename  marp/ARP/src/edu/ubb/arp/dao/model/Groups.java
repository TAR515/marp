package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *is the mirror of the groups table , contains getters and setters , hashCode , equals and toString methods  
 */
public class Groups implements Serializable {
	private static final long serialVersionUID = 1L;

	private int groupID;
	private String groupName;
	private List<Resources> resource;
	/**
	 * constructor
	 */
	public Groups() {
		this.groupID = -1;
		this.groupName = null;
		this.resource = null;
	}
	/**
	 * constructor
	 * @param groupID is the id of the group
	 * @param groupName is the name of the group
	 * @param resource is a list of resources which are in the group
	 */
	public Groups(int groupID, String groupName, List<Resources> resource) {
		this.groupID = groupID;
		this.groupName = groupName;
		this.resource = resource;
	}
	/**
	 * 
	 * @return returns the groupID
	 */
	public int getGroupID() {
		return groupID;
	}
	/**
	 * 
	 * @param groupID sets the groupID
	 */
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	/**
	 * 
	 * @return returns the group name
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * set the name of the group
	 * @param groupName the name of the group to set to
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * 
	 * @return returns a list of resource belonging to the group
	 */
	public List<Resources> getResource() {
		return resource;
	}
	/**
	 * sets resources to group
	 * @param resource is a list of resources to be set to group 
	 */
	public void setResource(List<Resources> resource) {
		this.resource = resource;
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
		result = prime * result + groupID;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
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
	/**
	 * @return Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		return "Groups [groupID=" + groupID + ", groupName=" + groupName
				+ ", resource=" + resource + "]";
	}

}
