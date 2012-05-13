package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;

public interface GroupsDao {

	public int createGroup(String groupName) throws SQLException, DalException;
	public int updateGroup(String oldGroupName,String newGroupName) throws SQLException, DalException;
	public int deleteGroup(String groupName) throws SQLException, DalException;
	
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws SQLException;
	public Groups loadGroupByResourceId(Integer resourceId) throws SQLException;
	
}
