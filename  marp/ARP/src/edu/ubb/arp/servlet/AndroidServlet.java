package edu.ubb.arp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import org.apache.log4j.Logger;

import sun.security.action.GetIntegerAction;

import edu.ubb.arp.logic.Dispatcher;
import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.BaseCommandOperationsInterface;

public class AndroidServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(AndroidServlet.class);
	
	private Dispatcher dp = null;
	private BaseCommandOperationsInterface baseCommands;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

		PrintWriter out = response.getWriter();
		out.println("This is an Android Server.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray responseArray = new JSONArray();
		baseCommands = new BaseCommandOperations();
		
		System.out.print("---------------- " + getCurrentDate() + " - Client ip: " + request.getRemoteAddr() + " ----------------");
		System.out.println("				User-Agent: " + request.getHeader("user-agent") + "\n");
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}

		System.out.println("KERES: \n" + sb.toString());
		
		JSONArray requestArray = stringBuilderToJSONArray(sb);
		try {
			HttpSession session = request.getSession(false);
			if (session == null) {
				System.out.println("NINCS SESSION, CSINALOK EGYET!!!");
			    session = request.getSession(true);
			    session.setMaxInactiveInterval(30);
			  
			    int originalCommand = baseCommands.getInt(0, "command", requestArray);
			    requestArray = baseCommands.changeInt(0, "command", 0, requestArray);
			    
			    responseArray = executeCommand(requestArray);
			    System.out.println("response: " + responseArray.toString() + " originalCOmm: " + originalCommand);
			    if (originalCommand != 0) { // If the orriginal command were not the user check.
			    	System.out.println("Original is not 0");
			    	if (baseCommands.checkResponseIfLoginSuccessfull(responseArray)) { // User exists
			    		System.out.println("User exists!");
			    		System.out.println("OriginalCommand: " + originalCommand);
			    		requestArray = baseCommands.changeInt(0, "command", originalCommand, requestArray);
			    		System.out.println("REQUEST ARRAY: " + requestArray.toString());
			    		responseArray = executeCommand(requestArray);
				    	
				    	String userName = baseCommands.getString(0, "username", requestArray);
				    	String password = baseCommands.getString(0, "password", requestArray);
				    	
				    	session.setAttribute("username", userName);
				    	session.setAttribute("password", password);
			    	} else {
			    		System.out.println("User exists!");
			    		System.out.println("User not exists");
				    	responseArray = baseCommands.setError(-8);
				    	session.invalidate();
			    	}
			    }
			}  else {
				System.out.println("VAN SESSION!!!");
				session.setMaxInactiveInterval(30);
				
				responseArray = executeCommand(requestArray);
			}
		} catch(Exception e) {
			logger.error(getClass().getName() + " doPost " + e);
			responseArray = baseCommands.setError(-9);
		}

	
		System.out.println("VALASZ: \n" + responseArray.toString());
		System.out.println("---------------- " + getCurrentDate() + " - Client ip: " + request.getRemoteAddr() + " ----------------");
		PrintWriter out = response.getWriter();
		out.println(responseArray);	
	}
	
	private JSONArray stringBuilderToJSONArray(StringBuilder stringBuilder) {
		JSONArray retValue = new JSONArray();
		
		try {
			retValue = JSONArray.fromObject(stringBuilder.toString());
		} catch (JSONException e) {
			dp = new Dispatcher(null);
			retValue = baseCommands.setError(-1);
		} 
		
		return retValue;
	}
	
	private JSONArray executeCommand(JSONArray request) {
		JSONArray retVal = new JSONArray();
		
		try {
			dp = new Dispatcher(request);
			retVal = dp.getResult();
		} catch (JSONException e) {
			dp = new Dispatcher(null);
			retVal = dp.getResult();
		}
		
		return retVal;
	}
	
	public String getCurrentDate() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss");
		return formatter.format(currentDate.getTime());
	}
}
