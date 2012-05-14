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
	
		if (request.getJSONObject(0).size() < 3) { // The request is empty.
			logger.error(getClass().getName() + methodName + "The request is empty.");
			response = setError(0);
		}

	}

	public JSONArray getResult() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");

		if (!errorCheck(response)) { // There were not SQLException
			try {
				Integer commandCode = getInt(0,"command",request);
				
				switch (commandCode) {
				case 0: // check user
					command = new CheckUserCommand(request);
					response = command.execute();
					break;
				case 101: // insert user
					command = new InsertNewUserCommand(request);
					response = command.execute();
					break;
				case 111: // fire / hire user
					command = new SetUserActiveCommand(request);
					response = command.execute();
					break;
				case 121: // change user name
					command = new ChangeUserNameCommand(request);
					response = command.execute();
					break;
				case 122: // change user password
					command = new ChangeUserPasswordCommand(request);
					response = command.execute();
					break;
				case 123: // change user email
					command = new ChangeUserEmailCommand(request);
					response = command.execute();
					break;
				case 124: // change user phone number
					command = new ChangeUserPhoneNumberCommand(request);
					response = command.execute();
					break;
				case 125: // change user resource name
					command = new ChangeUserResourceNameCommand(request);
					response = command.execute();
					break;
				case 130: // get active projects
					command = new LoadProjectsUserIsWorkingOnCommand(request);
					response = command.execute();
					break;
				case 131: // get user data
					command = new LoadUserDataCommand(request);
					response = command.execute();
					break;
				default:
					System.out.println("default command");
					break;
				}
			} catch (IllegalStateException e) {
				logger.error(getClass().getName() + methodName + e);
				response = setError(-1);
			}

			
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;
	}

}
