package edu.ubb.arp.dao;

import java.util.ArrayList;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;

public interface GroupsDao {

	public void createGroup(String groupName) throws DalException;
	public void updateGroup(String oldGroupName,String newGroupName) throws DalException;
	public void deleteGroup(String groupName) throws DalException;
	
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws DalException;
	public Groups loadGroupByResourceId(Integer resourceId) throws DalException;
	
}
