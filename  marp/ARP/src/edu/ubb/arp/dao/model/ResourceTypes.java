package edu.ubb.arp.dao.model;

import java.io.Serializable;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *contains getters and setters , hashCode , equals and toString methods  
 */
public class ResourceTypes implements Serializable {
	private static final long serialVersionUID = 1L;

	private int resourceTypesID;
	private String resourceTypeName;
	/**
	 * constructor
	 */
	public ResourceTypes() {
		resourceTypesID = -1;
		resourceTypeName = null;
	}
	/**
	 * constructor
	 * @param resourceTypesID is the resourcetypeID of the resourceType
	 * @param resourceTypeName is the name of the resource type
	 */
	public ResourceTypes(int resourceTypesID, String resourceTypeName) {
		this.resourceTypesID = resourceTypesID;
		this.resourceTypeName = resourceTypeName;
	}
	/**
	 * 
	 * @return returns resourceTypesID
	 */
	public int getResourceTypesID() {
		return resourceTypesID;
	}
	/**
	 * set resourceTypesID 
	 * @param resourceTypesID is the resourcetypeID of the resourceType
	 */
	public void setResourceTypesID(int resourceTypesID) {
		this.resourceTypesID = resourceTypesID;
	}
	/**
	 * 
	 * @return returns the name of the resource type name
	 */
	public String getResourceTypeName() {
		return resourceTypeName;
	}
	/**
	 * set resourceTypeName
	 * @param resourceTypeName is the name of the resource type
	 */
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
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
		result = prime
				* result
				+ ((resourceTypeName == null) ? 0 : resourceTypeName.hashCode());
		result = prime * result + resourceTypesID;
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
	/**
	 * @return Returns a string representation of the object.
	 */
	@Override
	public String toString() {
		return "ResourceTypes [resourceTypesID=" + resourceTypesID
				+ ", resourceTypeName=" + resourceTypeName + "]";
	}

}
