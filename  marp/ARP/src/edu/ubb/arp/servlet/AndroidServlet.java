package edu.ubb.arp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.logic.commands.Dispatcher;

public class AndroidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected Logger logger = Logger.getLogger(AndroidServlet.class);


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("This is an Android Server.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    }    

        JSONArray requestArray = new JSONArray();
        requestArray = JSONArray.fromObject(sb.toString());
        
        Dispatcher dp = new Dispatcher(requestArray);
		
        JSONArray responseArray = dp.getResult();

		PrintWriter out = response.getWriter();
		out.println(responseArray);
	}

}
