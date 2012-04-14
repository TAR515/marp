package edu.ubb.arp.dao.model;

import java.io.Serializable;

public class Statuses implements Serializable {
	private static final long serialVersionUID = 1L;

	private int statusID;
	private String statusName;

	public Statuses() {
		statusID = -1;
		statusName = null;
	}

	public Statuses(int statusID, String statusName) {
		this.statusID = statusID;
		this.statusName = statusName;
	}

	public int getStatusID() {
		return statusID;
	}

	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + statusID;
		result = prime * result
				+ ((statusName == null) ? 0 : statusName.hashCode());
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
		Statuses other = (Statuses) obj;
		if (statusID != other.statusID)
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
		return "Statuses [statusID=" + statusID + ", statusName=" + statusName
				+ "]";
	}

}
