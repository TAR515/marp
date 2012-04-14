package edu.ubb.arp.dao;

import java.util.List;

import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.exceptions.DalException;

public interface ResourcesDao {
	
	public Resources LoadResourceByID(int id) throws DalException;
	public List<Resources> loadResourcesByGroup(Integer groupId) throws DalException;
}
