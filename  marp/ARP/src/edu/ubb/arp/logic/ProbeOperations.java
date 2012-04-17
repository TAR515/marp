package edu.ubb.arp.logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import edu.ubb.arp.dao.DaoFactory;

public class ProbeOperations {
	DaoFactory instance;
	private static Properties properties;
	
	static {
		properties = new Properties();
		try {
			
		  properties.load(new FileInputStream("/WEB-INF/ErrorMessages.properties"));
		} catch (IOException e) {
			System.out.println("Hiba a propertyben");
			e.printStackTrace();
		}
	}
	
	
	public void ProbeOperation() {
		//try {
			/*instance = JdbcDaoFactory.getInstance();
			UsersDao u = instance.getUsersDao();
			u.setActive("Turdean Arnold Robert", Boolean.FALSE);
			
			String b = "one";
			
			System.out.println(myMap.get(b));*/
			System.out.println(properties.getProperty("-10"));
			System.out.println(properties.getProperty("RESOURCE_NOT_EXTISTS"));
			
		/*} catch (DalException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(methodName);
	}
}
