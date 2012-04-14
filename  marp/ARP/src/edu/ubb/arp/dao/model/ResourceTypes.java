package edu.ubb.arp.dao.model;

import java.io.Serializable;

public class ResourceTypes implements Serializable {
	private static final long serialVersionUID = 1L;

	private int resourceTypesID;
	private String resourceTypeName;

	public ResourceTypes() {
		resourceTypesID = -1;
		resourceTypeName = null;
	}

	public ResourceTypes(int resourceTypesID, String resourceTypeName) {
		this.resourceTypesID = resourceTypesID;
		this.resourceTypeName = resourceTypeName;
	}

	public int getResourceTypesID() {
		return resourceTypesID;
	}

	public void setResourceTypesID(int resourceTypesID) {
		this.resourceTypesID = resourceTypesID;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((resourceTypeName == null) ? 0 : resourceTypeName.hashCode());
		result = prime * result + resourceTypesID;
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
		ResourceTypes other = (ResourceTypes) obj;
		if (resourceTypeName == null) {
			if (other.resourceTypeName != null)
				return false;
		} else if (!resourceTypeName.equals(other.resourceTypeName))
			return false;
		if (resourceTypesID != other.resourceTypesID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResourceTypes [resourceTypesID=" + resourceTypesID
				+ ", resourceTypeName=" + resourceTypeName + "]";
	}

}
