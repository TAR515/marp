package edu.ubb.arp.logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import edu.ubb.arp.dao.DaoFactory;

public class ProbeOperations {
	DaoFactory instance;

	
	public void ProbeOperation() {
		//try {
			/*instance = JdbcDaoFactory.getInstance();
			UsersDao u = instance.getUsersDao();
			u.setActive("Turdean Arnold Robert", Boolean.FALSE);
			
			String b = "one";
			
			System.out.println(myMap.get(b));*/
			
		/*} catch (DalException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(methodName);
	}
}
