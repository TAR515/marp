package edu.ubb.arp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.GroupsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;
import edu.ubb.arp.logic.ResourceOperations;
import edu.ubb.arp.logic.UserOperation;

public class AndroidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected Logger logger = Logger.getLogger(AndroidServlet.class);

	UserOperation userOperation = new UserOperation();
	ResourceOperations resourceOperations = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		// EZ CSAK KIPROBALAS GYANANT VAN ITT !!! ( Marmint a servlet egessz resze... )

		
		
		JSONObject fromObject1 = new JSONObject();
		JSONObject fromObject2 = new JSONObject();
		JSONObject fromObject3 = new JSONObject();
		fromObject1.put("username", new String("KKK"));
		fromObject2.put("password", new Integer(1234));
		fromObject3.put("command", new Integer(1));
		
		JSONArray result = new JSONArray();
		result.add(fromObject1);
		result.add(fromObject2);
		result.add(fromObject3);
		 System.out.println(result);
		//Dispatcher d = new Dispatcher(result);
		 
		
		//JSONArray ggg = d.getResult();

		// System.out.println(ggg.toString());
		 
		 try {
			DaoFactory df = JdbcDaoFactory.getInstance();
			GroupsDao gd = df.getGroupsDao();
			
			gd.createGroup("Group10");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL probelm");
			e.printStackTrace();
		} catch (DalException e) {
			System.out.println(e.getErrorCode());
			e.printStackTrace();
		}
		 
		/*
		 * try { resourceOperations = new ResourceOperations();
		 * 
		 * List<Resources> loadResourcesByGroupId = resourceOperations.loadResourcesByGroupId(2);
		 * 
		 * out.print(loadResourcesByGroupId); } catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		//ProbeOperations pr = new ProbeOperations();
		//pr.ProbeOperation();
 
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("doPOST Method.");
	}

}
