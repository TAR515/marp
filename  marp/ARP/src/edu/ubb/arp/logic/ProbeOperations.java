package edu.ubb.arp.logic;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;

public class ProbeOperations {
	DaoFactory instance;

	public void ProbeOperation() {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(methodName + "()");
		
		DaoFactory instance;
		
		try {
			instance = JdbcDaoFactory.getInstance();
			ResourcesDao r = instance.getResourceDao();
			r.addResourceToGroup("Laptop1", "Group2");
			
			
		} catch (DalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
