package edu.ubb.arp.dao;

import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.exceptions.DalException;

public interface ResourcesDao {

	public void createResource(String resourceName,boolean active, String resourceTypeName, String resourceGroupName) throws DalException;
	public void createResource(Resources resource) throws DalException;
	
	
	public void setActive(String resourceName, boolean active) throws DalException;
	
	public void updateResource(String oldResourceName,boolean oldActive, String oldResourceTypeName, String oldResourceGroupName,
			String newResourceName,boolean newActive, String newResourceTypeName, String newResourceGroupName) throws DalException;

	/*public Resources LoadResourceByID(int id) throws DalException;

	public List<Resources> loadResourcesByGroup(Integer groupId) throws DalException;*/
}
