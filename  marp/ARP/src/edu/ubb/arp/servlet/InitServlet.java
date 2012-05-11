package edu.ubb.arp.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import edu.ubb.arp.logic.TimerThread;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(AndroidServlet.class);

	  public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    logger.debug("SERVER STARTED");
	    
	    
	    TimerThread object = new TimerThread(); ;
		Thread thread = new Thread(object);
		thread.start();
	    
	  }
}
