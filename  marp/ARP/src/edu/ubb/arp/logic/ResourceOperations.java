package edu.ubb.arp.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.GroupsDao;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.exceptions.DalException;

public class ResourceOperations {
	
	private static final Logger logger = Logger.getLogger(ResourceOperations.class);
	
	private ResourcesDao resourcesDao;
	private GroupsDao groupsDao;
	
	
	public ResourceOperations() throws Exception{
		DaoFactory instance;
		try {
			instance = JdbcDaoFactory.getInstance();
			resourcesDao = instance.getResourceDao();
			groupsDao = instance.getGroupsDao();
		} catch (DalException e) {
			logger.error("error while init ResourceOperation class",e);
			throw new Exception("error while init ResourceOperation class",e);
		}
	
	}

	public Resources loadResources(Integer resourceId) throws Exception{
		Resources retValue = new Resources();
		
		try {
			retValue = resourcesDao.LoadResourceByID(resourceId);
			ArrayList<Groups> loadGroupsByResourceId = groupsDao.loadGroupsByResourceId(resourceId);
			retValue.setGroups(loadGroupsByResourceId);
		 
		} catch (DalException e) {
			logger.error("error while init ResourceOperation class",e);
			throw new Exception("error while init ResourceOperation class",e);
		}
		return retValue;
	}
	
	public void processResource(Integer resId) throws Exception{
		loadResources(resId);
	}
	
	public List<Resources> loadResourcesByGroupId(Integer groupId) throws Exception{
		List<Resources> loadResourcesByGroup = new ArrayList<Resources>(); 
		try {
			loadResourcesByGroup = resourcesDao.loadResourcesByGroup(groupId);
			
			if(loadResourcesByGroup!=null && !loadResourcesByGroup.isEmpty()){
				for(Resources res:loadResourcesByGroup){
					ArrayList<Groups> loadGroupsByResourceId = groupsDao.loadGroupsByResourceId(res.getResourceID());
					res.setGroups(loadGroupsByResourceId);
				}
			}
			
		} catch (DalException e) {
			logger.error("error while init ResourceOperation class",e);
			throw new Exception("error while init ResourceOperation class",e);
		}
		return loadResourcesByGroup;
	}
}
