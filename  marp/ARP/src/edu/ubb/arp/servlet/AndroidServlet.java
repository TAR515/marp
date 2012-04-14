package edu.ubb.arp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.logic.ResourceOperations;
import edu.ubb.arp.logic.UserOperation;

public class AndroidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected Logger logger = Logger.getLogger(AndroidServlet.class);
	
	UserOperation userOperation = new UserOperation();
	ResourceOperations resourceOperations = null;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		logger.debug("asdasd");
		// EZ CSAK KIPROBALAS GYANANT VAN ITT !!! ( Marmint a servlet egessz resze... )
		
//		Users loginUser = userOperation.loginUser("Sanzi");
//		Users loginUser2 = userOperation.loginUser("Sanzi2");
//		out.print("<h1>"+loginUser.getEmail()+"</h1>");
//		
//		JSONObject fromObject1 = new JSONObject();
//		JSONObject fromObject2 = new JSONObject();
//		fromObject1.put("BLA", JSONObject.fromObject(loginUser));
//		fromObject2.put("BLA", loginUser2);
//		
//		JSONArray result = new JSONArray();
//		result.add(fromObject1);
//		result.add(fromObject2);
		
		try {
		resourceOperations = new ResourceOperations();
		
		List<Resources> loadResourcesByGroupId = resourceOperations.loadResourcesByGroupId(2);

		out.print(loadResourcesByGroupId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userOperation = new UserOperation();
		
		Users u = userOperation.loginUser("Sanzi");
		out.print(u);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("doPOST Method.");
	}
	

}
