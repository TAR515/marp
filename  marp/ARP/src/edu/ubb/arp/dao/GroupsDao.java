package edu.ubb.arp.dao;

import java.util.ArrayList;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;

public interface GroupsDao {

	public int createGroup(String groupName) throws DalException;
	public int updateGroup(String oldGroupName,String newGroupName) throws DalException;
	public int deleteGroup(String groupName) throws DalException;
	
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws DalException;
	public Groups loadGroupByResourceId(Integer resourceId) throws DalException;
	
}
