package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;

public interface GroupsDao {

	public void createGroup(String groupName) throws SQLException, DalException;
	public void updateGroup(String oldGroupName,String newGroupName) throws SQLException, DalException;
	public void deleteGroup(String groupName) throws SQLException, DalException;
	
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws SQLException;
	public Groups loadGroupByResourceId(Integer resourceId) throws SQLException;
	
}
