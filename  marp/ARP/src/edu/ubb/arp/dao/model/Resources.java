package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import edu.ubb.arp.datastructures.Booking;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *is the mirror of the Resources table , contains getters and setters , hashCode , equals and toString methods  
 */
public class Resources implements Serializable {
	private static final long serialVersionUID = 1L;

	private int resourceID;
	private String resourceName;
	private boolean active;
	private Users users;
	private ResourceTypes resourceTypes;
	private List<Groups> groups;
	private HashMap<Projects, Booking> booking;
	private HashMap<Requests, Integer> requests;
	private HashMap<Projects, Boolean> usersInProjects;
	/**
	 * constructor
	 */
	public Resources() {
		this.resourceID = -1;
		this.resourceName = null;
		this.active = false;
		this.users = null;
		this.resourceTypes = null;
		this.groups = null;
		this.booking = null;
		this.requests = null;
		this.usersInProjects = null;
	}
	/**
	 * constructor
	 * @param resourceID is the id of the resource
	 * @param resourceName is the name of the resource
	 * @param active is true if the resource is hired or false if not
	 * @param users 
	 * @param resourceTypes is the possible types of the resource
	 * @param groups 
	 * @param booking 
	 * @param requests
	 * @param usersInProjects 
	 */
	public Resources(int resourceID, String resourceName, boolean active, Users users, ResourceTypes resourceTypes,
			List<Groups> groups, HashMap<Projects, Booking> booking, HashMap<Requests, Integer> requests,
			HashMap<Projects, Boolean> usersInProjects) {
		this.resourceID = resourceID;
		this.resourceName = resourceName;
		this.active = active;
		this.users = users;
		this.resourceTypes = resourceTypes;
		this.groups = groups;
		this.booking = booking;
		this.requests = requests;
		this.usersInProjects = usersInProjects;
	}
	/**
	 * 
	 * @return returns the id of the resource
	 */
	public int getResourceID() {
		return resourceID;
	}
	/**
	 * sets the id of the resource
	 * @param resourceID is the id of the resource
	 */
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}
	/**
	 * 
	 * @return returns the resource name
	 */
	public String getResourceName() {
		return resourceName;
	}
	/**
	 * sets the name of the resource
	 * @param resourceName is name of the resource
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	/**
	 * 
	 * @return the user attribute of the resource
	 */
	public Users getUsers() {
		return users;
	}
	/**
	 * sets the user attribute
	 * @param users 
	 */
	public void setUsers(Users users) {
		this.users = users;
	}
	/**
	 * 
	 * @return returns resourceTypes
	 */
	public ResourceTypes getResourceTypes() {
		return resourceTypes;
	}
	/**
	 * sets resourceTypes attribute
	 * @param resourceTypes 
	 */
	public void setResourceTypes(ResourceTypes resourceTypes) {
		this.resourceTypes = resourceTypes;
	}
	/**
	 * 
	 * @return groups
	 */
	public List<Groups> getGroups() {
		return groups;
	}
	/**
	 * sets groups
	 * @param groups 
	 */
	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}
	/**
	 * 
	 * @return returns booking
	 */
	public HashMap<Projects, Booking> getBooking() {
		return booking;
	}
	/**
	 * sets booking
	 * @param booking
	 */
	public void setBooking(HashMap<Projects, Booking> booking) {
		this.booking = booking;
	}
	/**
	 * 
	 * @return returns requests
	 */
	public HashMap<Requests, Integer> getRequests() {
		return requests;
	}
	/**
	 * set requests
	 * @param requests
	 */
	public void setRequests(HashMap<Requests, Integer> requests) {
		this.requests = requests;
	}
	/**
	 * 
	 * @return returns usersInProjects
	 */
	public HashMap<Projects, Boolean> getUsersInProjects() {
		return usersInProjects;
	}
	/**
	 * sets usersInProjects
	 * @param usersInProjects
	 */
	public void setUsersInProjects(HashMap<Projects, Boolean> usersInProjects) {
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
	 * 
	 * @return returns active
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * sets active 
	 * @param active is true if the resource is hired or false if not
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * return a hashCode value of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((booking == null) ? 0 : booking.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((requests == null) ? 0 : requests.hashCode());
		result = prime * result + resourceID;
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
		result = prime * result + ((resourceTypes == null) ? 0 : resourceTypes.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		Resources other = (Resources) obj;
		if (active != other.active)
			return false;
		if (booking == null) {
			if (other.booking != null)
				return false;
		} else if (!booking.equals(other.booking))
			return false;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (requests == null) {
			if (other.requests != null)
				return false;
		} else if (!requests.equals(other.requests))
			return false;
		if (resourceID != other.resourceID)
			return false;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
			return false;
		if (resourceTypes == null) {
			if (other.resourceTypes != null)
				return false;
		} else if (!resourceTypes.equals(other.resourceTypes))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
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
		return "Resources [resourceID=" + resourceID + ", resourceName=" + resourceName + ", active=" + active + ", users="
				+ users + ", resourceTypes=" + resourceTypes + ", groups=" + groups + ", booking=" + booking + ", requests="
				+ requests + ", usersInProjects=" + usersInProjects + "]";
	}
	/**
	 * add group to groups
	 * @param group is the group to add to groups
	 */
	public void addGroupToGroups(Groups group) {
		if (groups != null) {
			groups.add(group);
		}
	}
	/**
	 * removes group from groups
	 * @param group is the group to remove from groups
	 */
	public void removeGroupFromGroups(Groups group) {
		if (group != null) {
			groups.remove(group);
		}
	}
	/**
	 * removes group from groups
	 * @param index is the index of the group to remove from groups
	 */
	public void removeGroupFromGroups(int index) {
		if (groups.get(index) != null) {
			groups.remove(index);
		}
	}

}
