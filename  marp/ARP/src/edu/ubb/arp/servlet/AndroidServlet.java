package edu.ubb.arp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import edu.ubb.arp.logic.commands.Dispatcher;

public class AndroidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected Logger logger = Logger.getLogger(AndroidServlet.class);


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		// EZ CSAK KIPROBALAS GYANANT VAN ITT !!! ( Marmint a servlet egessz resze... )

		
		
		JSONObject fromObject1 = new JSONObject();
		JSONObject fromObject2 = new JSONObject();
		JSONObject fromObject3 = new JSONObject();
		JSONObject fromObject4 = new JSONObject();
		fromObject1.put("username", new String("Juuuh333"));
		fromObject2.put("password", new Integer(1234));
		fromObject3.put("command", new Integer(131));
		//fromObject4.put("newusername", "Juuuh333");
		
		JSONArray result = new JSONArray();
		result.add(fromObject1);
		result.add(fromObject2);
		result.add(fromObject3);
		//result.add(fromObject4);
		 System.out.println(result);
		Dispatcher d = new Dispatcher(result);
		 
		JSONArray ggg = d.getResult();

	 System.out.println(ggg.toString());
		 
		 /*try {
			DaoFactory df = JdbcDaoFactory.getInstance();
			ProjectsDao p = df.getProjectsDao();
			
			List<Integer> asd = new ArrayList<Integer>();
			asd.add(1);
			
			
			List<Booking> er = p.loadBooking(1); 
			System.out.println(er.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL probelm");
			e.printStackTrace();
		} catch (DalException e) {
			System.out.println(e.getErrorCode());
			e.printStackTrace();
		}*/
		 
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
