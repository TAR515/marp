package edu.ubb.arp.logic.commands;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

public class Dispatcher extends BaseCommandOperations {
	private static final Logger logger = Logger.getLogger(Dispatcher.class);

	private JSONArray request = null;
	private JSONArray response = null;
	private Command command;

	public Dispatcher(JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";

		this.response = new JSONArray();
		this.request = request;
	
		if (request.size() < 3) { // The request is empty.
			logger.error(getClass().getName() + methodName + "The request is empty.");
			response = setError(0);
		}

	}

	public JSONArray getResult() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");

		if (response.isEmpty()) { // There were not SQLException
			Integer commandCode = request.getJSONObject(2).getInt("command");

			switch (commandCode) {
			case 1: // check user
				command = new CheckUserCommand(request);
				response = command.execute();
				break;
			default:
				System.out.println("default command");
				break;
			}
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;
	}

}
