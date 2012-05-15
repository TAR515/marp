package edu.ubb.arp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import org.apache.log4j.Logger;

import edu.ubb.arp.logic.Dispatcher;

public class AndroidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONArray responseArray = null;
	private Dispatcher dp = null;
	protected Logger logger = Logger.getLogger(AndroidServlet.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

		PrintWriter out = response.getWriter();
		out.println("This is an Android Server.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("[!	Client ip: " + request.getRemoteAddr());
		System.out.println("	  User-Agent: " + request.getHeader("user-agent"));
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}

		System.out.println("  	Keres: " + sb.toString());

		try {
			JSONArray requestArray = new JSONArray();
			requestArray = JSONArray.fromObject(sb.toString());

			dp = new Dispatcher(requestArray);

			responseArray = dp.getResult();
		} catch (JSONException e) {
			dp = new Dispatcher(null);

			responseArray = dp.getResult();
		}
		
		/*session = request.getSession(true);
		if (session.isNew()) { // There is no session.
			System.out.println("NINCS SESSION, CSINALOK EGYET!!!");
			session.setMaxInactiveInterval(60);
			session.setAttribute("username", responseArray.getJSONObject(0).get("username"));
			session.setAttribute("password", responseArray.getJSONObject(0).get("password"));
		} else {
			System.out.println("VAN SESSION!!!");
		}*/
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			System.out.println("NINCS SESSION, CSINALOK EGYET!!!");
		    session = request.getSession(true);
		    session.setMaxInactiveInterval(10);
		} else {
			session.setMaxInactiveInterval(10);
		}

		/*
		 * JSONObject o = new JSONObject(); o.put("username", "Juuuh333"); o.put("command", 130); o.put("password", "fuck");
		 * 
		 * JSONArray a = new JSONArray(); a.add(o);
		 * 
		 * dp = new Dispatcher(a);
		 * 
		 * responseArray = dp.getResult();
		 */
		System.out.println(" 	Valasz: " + responseArray.toString());
		PrintWriter out = response.getWriter();
		out.println(responseArray);
	}

}
