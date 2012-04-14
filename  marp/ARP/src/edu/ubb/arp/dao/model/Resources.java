package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import edu.ubb.arp.datastructures.Booking;

public class Resources implements Serializable {
	private static final long serialVersionUID = 1L;

	private int resourceID;
	private String resourceName;
	private List<Users> users;
	private ResourceTypes resourceTypes;
	private List<Groups> groups;
	private HashMap<Projects, Booking> booking;
	private HashMap<Requests, Boolean> requests;
	private List<Projects> leaderOfProjects;

	public Resources() {
		this.resourceID = -1;
		this.resourceName = null;
		this.users = null;
		this.resourceTypes = null;
		this.groups = null;
		this.booking = null;
		this.requests = null;
		this.leaderOfProjects = null;
	}

	public Resources(int resourceID, String resourceName, List<Users> users,
			ResourceTypes resourceTypes, List<Groups> groups,
			HashMap<Projects, Booking> booking,
			HashMap<Requests, Boolean> requests, List<Projects> leaderOfProjects) {
		this.resourceID = resourceID;
		this.resourceName = resourceName;
		this.users = users;
		this.resourceTypes = resourceTypes;
		this.groups = groups;
		this.booking = booking;
		this.requests = requests;
		this.leaderOfProjects = leaderOfProjects;
	}

	public int getResourceID() {
		return resourceID;
	}

	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public ResourceTypes getResourceTypes() {
		return resourceTypes;
	}

	public void setResourceTypes(ResourceTypes resourceTypes) {
		this.resourceTypes = resourceTypes;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}

	public HashMap<Projects, Booking> getBooking() {
		return booking;
	}

	public void setBooking(HashMap<Projects, Booking> booking) {
		this.booking = booking;
	}

	public HashMap<Requests, Boolean> getRequests() {
		return requests;
	}

	public void setRequests(HashMap<Requests, Boolean> requests) {
		this.requests = requests;
	}

	public List<Projects> getLeaderOfProjects() {
		return leaderOfProjects;
	}

	public void setLeaderOfProjects(List<Projects> leaderOfProjects) {
		this.leaderOfProjects = leaderOfProjects;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booking == null) ? 0 : booking.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime
				* result
				+ ((leaderOfProjects == null) ? 0 : leaderOfProjects.hashCode());
		result = prime * result
				+ ((requests == null) ? 0 : requests.hashCode());
		result = prime * result + resourceID;
		result = prime * result
				+ ((resourceName == null) ? 0 : resourceName.hashCode());
		result = prime * result
				+ ((resourceTypes == null) ? 0 : resourceTypes.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		Resources other = (Resources) obj;
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
		if (leaderOfProjects == null) {
			if (other.leaderOfProjects != null)
				return false;
		} else if (!leaderOfProjects.equals(other.leaderOfProjects))
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
		return true;
	}

	@Override
	public String toString() {
		return "Resources [resourceID=" + resourceID + ", resourceName="
				+ resourceName + ", users=" + users + ", resourceTypes="
				+ resourceTypes + ", groups=" + groups + ", booking=" + booking
				+ ", requests=" + requests + ", leaderOfProjects="
				+ leaderOfProjects + "]";
	}
	
	/*private void add(Groups group){
		if(groups==null){
			groups = new ArrayList<Groups>();
		}
		groups.add(group);
	}
*/
}
