package edu.ubb.arp.dao;

import java.util.ArrayList;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;

public interface GroupsDao {

	public Groups loadGroupByResourceId(Integer resourceId) throws DalException;
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws DalException;
	
}
