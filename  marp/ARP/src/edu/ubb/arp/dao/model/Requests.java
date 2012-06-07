package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.HashMap;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *is the mirror of the Requests table , contains getters and setters , hashCode , equals and toString methods  
 */
public class Requests implements Serializable {
	private static final long serialVersionUID = 1L;

	private int requestID;
	private int week;
	private int ratio;
	private Projects project;
	private Resources sender;
	private Resources resource;
	private HashMap<Resources, Integer> resources;
	/**
	 * constructor
	 */
	public Requests() {
		this.requestID = -1;
		this.week = -1;
		this.ratio = -1;
		this.project = null;
		this.sender = null;
		this.resource = null;
		this.resources = null;
	}
	/**
	 * constructor
	 * @param requestID is the id of the request
	 * @param week is the week which the request is for
	 * @param ratio is the ratio which is needed 
	 * @param project is the project in which the the resource is needed
	 * @param sender is who sent the request
	 * @param resource is the resource which is needed in the project
	 * @param resources 
	 */
	public Requests(int requestID, int week, int ratio,
			Projects project, Resources sender, Resources resource,
			HashMap<Resources, Integer> resources) {
		this.requestID = requestID;
		this.week = week;
		this.ratio = ratio;
		this.project = project;
		this.sender = sender;
		this.resource = resource;
		this.resources = resources;
	}
	/**
	 * 
	 * @return returns the requestID of the request
	 */
	public int getRequestID() {
		return requestID;
	}
	/**
	 * sets the id of the request
	 * @param requestID is the id of the request
	 */
	public void setRequestID(int requestID) {
		this.requestID = requestID;
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
	 * @param week is the week the request is for  
	 */
	public void setWeek(int week) {
		this.week = week;
	}
	/**
	 * 
	 * @return the ratio attribute of the request
	 */
	public int getRatio() {
		return ratio;
	}
	/**
	 * set the ratio attribute of the request
	 * @param ratio is the ratio which is needed
	 */
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
	/**
	 * 
	 * @return returns the project attribute of the request
	 */
	public Projects getProject() {
		return project;
	}
	/**
	 * sets the project attribute of the request
	 * @param project is the project in which the the resource is needed
	 */
	public void setProject(Projects project) {
		this.project = project;
	}
	/**
	 * 
	 * @return returns the sender of the request
	 */
	public Resources getSender() {
		return sender;
	}
	/**
	 * sets the sender of the request
	 * @param sender is the sender of the request
	 */
	public void setSender(Resources sender) {
		this.sender = sender;
	}
	/**
	 * 
	 * @return the resource attribute of the request
	 */
	public Resources getResource() {
		return resource;
	}
	/**
	 * 
	 * @param resource is the resource which is needed in the project
	 */
	public void setResource(Resources resource) {
		this.resource = resource;
	}
	/**
	 * 
	 * @return returns the requests resources attribute
	 */
	public HashMap<Resources, Integer> getResources() {
		return resources;
	}
	/**
	 * 	sets the resources attribute of the request
	 * @param resources 
	 */
	public void setResources(HashMap<Resources, Integer> resources) {
		this.resources = resources;
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
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result + ratio;
		result = prime * result + requestID;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
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
		Requests other = (Requests) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (ratio != other.ratio)
			return false;
		if (requestID != other.requestID)
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
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
		return "Requests [requestID=" + requestID + ", week=" + week + ", ratio=" + ratio + ", project=" + project + ", sender="
				+ sender + ", resource=" + resource + ", resources=" + resources + "]";
	}
}
