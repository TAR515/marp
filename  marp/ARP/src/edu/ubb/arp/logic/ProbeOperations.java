package edu.ubb.arp.logic;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.GroupsDao;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.BllExceptions;
import edu.ubb.arp.exceptions.DalException;

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
			
			
			//u.createUser("Arni33", HashCoding.hashString("1234"), "0713212397", "arni@gmail.com", "Smancz Arnold", Boolean.TRUE, "Team1");
			//u.updateUser("Arni33", HashCoding.hashString("1234"), "Team1", "Arni44", HashCoding.hashString("12345"), "0713212396","arni99@gmail.com", "Smancz Arnoldd", Boolean.FALSE, "Team2");
			//u.changePassword(1, HashCoding.hashString("123"), HashCoding.hashString("1234"));
			String[] st = new String[2];
			st[0] = "Team2";
			st[1] = "Team3";
			u.removeUserFromGroup("taim1100", st[0]);
			
			// g.createGroup("Group5");
			/*String[] a = new String[2];
			a[0] = "Group2";
			a[1] = "Group3";
			//r.addResourceToGroup("Laptop1", );
			*///r.addResourceToGroup("Laptop1", "Group2");
			
			
			
			
		} catch (DalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
