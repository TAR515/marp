package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;
/**
 *  
 * @author VargaAdorjan
 *interface contains methods which work with the Groups 
 */
public interface GroupsDao {
	/**
	 * 
	 * @param groupName the name of the group to create
	 * @return returns an error message if there was an error , otherwise returns the id of the created group
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int createGroup(String groupName) throws SQLException, DalException;
	/**
	 * 
	 * @param oldGroupName the name of the group to modify
	 * @param newGroupName the new name of the group
	 * @return returns an error message if there was an error , otherwise returns the id of the modified group
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateGroup(String oldGroupName,String newGroupName) throws SQLException, DalException;
	/**
	 * 
	 * @param groupName the name of the group to delete
	 * @return returns an error message if there was an error , otherwise returns the id of the deleted group
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int deleteGroup(String groupName) throws SQLException, DalException;
	/**
	 * 
	 * @param resourceId is the id of the resource , which's groups i want to load
	 * @return	the groups of the resource 
	 * @throws SQLException if there is no connection
	 */
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws SQLException;
	/**
	 * 
	 * @param resourceId resourceId is the id of the resource , which's groups i want to load
	 * @return the group of the resource 
	 * @throws SQLException if there is no connection
	 */ 
	public Groups loadGroupByResourceId(Integer resourceId) throws SQLException;
	
}
