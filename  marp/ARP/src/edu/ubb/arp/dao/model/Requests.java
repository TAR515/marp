package edu.ubb.arp.dao.model;

import java.io.Serializable;
import java.util.HashMap;

public class Requests implements Serializable {
	private static final long serialVersionUID = 1L;

	private int requestID;
	private int date;
	private double ratio;
	private boolean rejected;
	private Projects project;
	private Resources sender;
	private Resources resource;
	private HashMap<Resources, Boolean> resources;

	public Requests() {
		this.requestID = -1;
		this.date = -1;
		this.ratio = -1;
		this.rejected = false;
		this.project = null;
		this.sender = null;
		this.resource = null;
		this.resources = null;
	}

	public Requests(int requestID, int date, double ratio, boolean rejected,
			Projects project, Resources sender, Resources resource,
			HashMap<Resources, Boolean> resources) {
		this.requestID = requestID;
		this.date = date;
		this.ratio = ratio;
		this.rejected = rejected;
		this.project = project;
		this.sender = sender;
		this.resource = resource;
		this.resources = resources;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public Projects getProject() {
		return project;
	}

	public void setProject(Projects project) {
		this.project = project;
	}

	public Resources getSender() {
		return sender;
	}

	public void setSender(Resources sender) {
		this.sender = sender;
	}

	public Resources getResource() {
		return resource;
	}

	public void setResource(Resources resource) {
		this.resource = resource;
	}

	public HashMap<Resources, Boolean> getResources() {
		return resources;
	}

	public void setResources(HashMap<Resources, Boolean> resources) {
		this.resources = resources;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + date;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		long temp;
		temp = Double.doubleToLongBits(ratio);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (rejected ? 1231 : 1237);
		result = prime * result + requestID;
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		result = prime * result
				+ ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
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
		Requests other = (Requests) obj;
		if (date != other.date)
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (Double.doubleToLongBits(ratio) != Double
				.doubleToLongBits(other.ratio))
			return false;
		if (rejected != other.rejected)
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
		return true;
	}

	@Override
	public String toString() {
		return "Requests [requestID=" + requestID + ", date=" + date
				+ ", ratio=" + ratio + ", rejected=" + rejected + ", project="
				+ project + ", sender=" + sender + ", resource=" + resource
				+ ", resources=" + resources + "]";
	}

}
