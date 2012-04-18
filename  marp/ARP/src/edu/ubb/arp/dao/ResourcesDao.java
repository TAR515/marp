package edu.ubb.arp.dao;

import java.util.List;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.exceptions.DalException;

public interface ResourcesDao {
	/** Creates a new resource witch can't be a human(user).
	 * 
	 * @param resourceName
	 *            can be same with a user resource name but must be different from an another non human resource name
	 * @param active
	 *            can be set as true or false, false indicates that the resource aren't working
	 * @param resourceTypeName
	 *            indicates what kind of resource is it
	 * @param resourceGroupName
	 *            every resource must be at least in one group
	 * @return ErrorCode if the returned value is lower than 0 | Created resource id if the returned value is bigger than 0
	 * @throws DalException
	 *             The exception contains ErrorCode and ErrorMessage of the problem that arises */
	public int createResource(String resourceName, boolean active, String resourceTypeName, String resourceGroupName)
			throws DalException;

	public int createResource(Resources resource) throws DalException;
	
	public int setActive(String resourceName, boolean active) throws DalException;

	public int updateResource(String oldResourceName, boolean oldActive, String oldResourceTypeName, String oldResourceGroupName,
			String newResourceName, boolean newActive, String newResourceTypeName, String newResourceGroupName)
			throws DalException;

	public int addResourceToGroup(String resourceName, String groupName) throws DalException;
	
	public int addResourceToGroup(Resources resource, Groups group) throws DalException;

	public int addResourceToGroups(String resourceName, String[] groupNames) throws DalException;
	
	public int addResourceToGroups(Resources resource, List<Groups> groups) throws DalException;
	
	public int removeResourceFromGroup(String resourceName, String groupName) throws DalException;
	
	public int removeResourceFromGroup(Resources resource, Groups group) throws DalException;

	/*
	 * public Resources LoadResourceByID(int id) throws DalException;
	 * 
	 * public List<Resources> loadResourcesByGroup(Integer groupId) throws DalException;
	 */
}
