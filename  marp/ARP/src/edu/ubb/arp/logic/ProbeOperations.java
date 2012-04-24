package edu.ubb.arp.logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.GroupsDao;
import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;

public class ProbeOperations {
	DaoFactory instance;

	public void ProbeOperation() {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(methodName + "()");
		
		DaoFactory instance;
		
		try {
			instance = JdbcDaoFactory.getInstance();
			GroupsDao g = instance.getGroupsDao();
			ResourcesDao r = instance.getResourceDao();
			UsersDao u = instance.getUsersDao();
			ProjectsDao p = instance.getProjectsDao();
			
			List<Integer> w = new ArrayList<Integer>();
			w.add(9);
			w.add(10);
			w.add(11);
			
			List<Integer> rat = new ArrayList<Integer>();
			rat.add(90);
			rat.add(90);
			rat.add(90);
			
			
			int err = p.addResourceToProject("Project1", "Laptop2", w, rat);
			System.out.println(err);
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
