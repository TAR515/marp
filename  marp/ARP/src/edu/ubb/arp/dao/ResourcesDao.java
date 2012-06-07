package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *interface contains methods which work with the Resources
 */
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
			throws SQLException, DalException;
	/**
	 * sets a resource active attribute to true/false
	 * @param resourceName is the name of the resource which's active attribute will be updated
	 * @param active is true if the resource is hired , false if fired
	 * @param currentWeek the actual number of week
	 * @return returns the resource's id  if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int setActive(int resourceID, boolean active, int currentWeek) throws SQLException, DalException;
	/**
	 * updates a resource's attributes
	 * @param oldResourceName is the old name of the resource 
	 * @param oldActive is the old active attribute of the resource
	 * @param oldResourceTypeName is the old resource type name attribute of the resource
	 * @param oldResourceGroupName is the old group name attribute of the resource
	 * @param newResourceName is the new name of the resource
	 * @param newActive is the new active attribute of the resource
	 * @param newResourceTypeName is the new resource type name attribute of the resource
	 * @param newResourceGroupName is the new group name attribute of the resource
	 * @return returns the resource's id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateResource(String oldResourceName, boolean oldActive, String oldResourceTypeName, String oldResourceGroupName,
			String newResourceName, boolean newActive, String newResourceTypeName, String newResourceGroupName)
			throws SQLException, DalException;
	/**
	 * adds a resource to a group
	 * @param resourceName is the name of the resource to add to the group
	 * @param groupName is the name of the group to which the resource will be added
	 * @return returns the resource's id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addResourceToGroup(String resourceName, String groupName) throws SQLException, DalException;
	/**
	 * adds  a resource to a group
	 * @param resource is the resources which will be added to the group
	 * @param group is the group to which the resource will be added
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void addResourceToGroup(Resources resource, Groups group) throws SQLException, DalException;
	/**
	 * adds a resource to multiple groups
	 * @param resourceName is the name of the resource which will be added to the groups
	 * @param groupNames is a list a groups in which the resource will added to
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void addResourceToGroups(String resourceName, String[] groupNames) throws SQLException, DalException;
	/**
	 * adds a resource to multiple groups
	 * @param resource is the resources which will be added to the group
	 * @param groups is a list a groups in which the resource will added to
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void addResourceToGroups(Resources resource, List<Groups> groups) throws SQLException, DalException;
	/**
	 * removes a resource from a group
	 * @param resourceName is the name of the resource which will be removed from a group
	 * @param groupName is the name of the group from which the resource will be removed from 
	 * @return return the id of the resource if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeResourceFromGroup(String resourceName, String groupName) throws SQLException, DalException;
	/**
	 * removes a resource from a group
	 * @param resource is the resource  which will be removed from a group
	 * @param group is the group from which the resource will be removed from 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void removeResourceFromGroup(Resources resource, Groups group) throws SQLException, DalException;
	/**
	 * 
	 * @param resourceID is a list of resources which which's attributes will be loaded 
	 * @return a list of resources if there was no error else returns an error message
	 * @throws DalException if there is no connection
	 * @throws SQLException if a stored procedure returns an error message
	 */
	public List<Resources> loadResources(List<Integer> resourceID) throws DalException, SQLException;
	/**
	 * 
	 * @param resourceID is the id of the resource which's booking will be loaded
	 * @param startWeek is the start week from where the booking will be loaded
	 * @param endWeek is the last week of the loaded booking of the resource
	 * @param projectName is the name of the project in which the resource's booking will be loaded 
	 * @param action is insert if the resource will be inserted in booking , update if the resource's ratio will be updated or new if a new project will be created
	 * @return returns the booking of the resource from start week to end week in all projects he works in if there was no error , otherwise an error message 
	 * @throws DalException  if there is no connection
	 * @throws SQLException if a stored procedure returns an error message
	 */
	public List<Integer> loadResourceTotalEngages(int resourceID, int startWeek, int endWeek, String projectName, String action)
			throws DalException, SQLException;
	/**
	 * 
	 * @param resourceID is the id of the resource which's booking in the current project  will be loaded 
	 * @param startWeek is the start week from where the booking will be loaded
	 * @param endWeek is the last week of the loaded booking of the resource
	 * @param projectName is the name of the project in which the resource's booking will be loaded 
	 * @return returns the booking of the resource from start week to end week in the project if there was no error , otherwise an error message 
	 * @throws DalException if there is no connection
	 * @throws SQLException if a stored procedure returns an error message
	 */
	public List<Integer> loadResourceCurrentProjectEngages(int resourceID, int startWeek, int endWeek, String projectName)
			throws DalException, SQLException;
	/*
	 * public Resources LoadResourceByID(int id) throws DalException;
	 * 
	 * public List<Resources> loadResourcesByGroup(Integer groupId) throws DalException;
	 */
	/**
	 * 
	 * @return all active resources names and id's
	 * @throws SQLException SQLException if a stored procedure returns an error message
	 */
	public List<Resources> LoadAllActiveResources()throws SQLException;
}
