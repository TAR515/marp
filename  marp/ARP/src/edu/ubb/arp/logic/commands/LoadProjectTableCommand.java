package edu.ubb.arp.logic.commands;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.model.Users;

public class LoadProjectTableCommand extends BaseCommandOperations {
	private static final Logger logger = Logger.getLogger(LoadUserDataCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private UsersDao userDao = null;
	private Users user= null;
}
