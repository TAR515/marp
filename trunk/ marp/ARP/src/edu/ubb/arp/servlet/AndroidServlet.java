package edu.ubb.arp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import org.apache.log4j.Logger;

import edu.ubb.arp.logic.commands.Dispatcher;

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
		System.out.print("Client ip: " + request.getRemoteAddr());
		System.out.print(" User-Agent: " + request.getHeader("user-agent"));
	
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    }    
	    System.out.println(sb.toString() );
	    
	    try {
	        JSONArray requestArray = new JSONArray();
	        requestArray = JSONArray.fromObject(sb.toString());
	        
	        dp = new Dispatcher(requestArray);
			
	        responseArray = dp.getResult();
	    } catch(JSONException e) {
	    	dp = new Dispatcher(null);
			
	        responseArray = dp.getResult();
	    }
	    System.out.println(" Valasz: " + responseArray.toString());
        PrintWriter out = response.getWriter();
		out.println(responseArray);
	}

}
