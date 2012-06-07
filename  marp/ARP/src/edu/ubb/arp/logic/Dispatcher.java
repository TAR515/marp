package edu.ubb.arp.logic;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.CheckUserCommand;
import edu.ubb.arp.logic.commands.Command;
import edu.ubb.arp.logic.commands.LoadAllProjectsCommand;
import edu.ubb.arp.logic.commands.LoadAllNotActiveResourcesCommand;
import edu.ubb.arp.logic.commands.LoadAssignmentsCommand;
import edu.ubb.arp.logic.commands.LoadProjectTableCommand;
import edu.ubb.arp.logic.commands.LoadProjectsUserIsWorkingOnCommand;
import edu.ubb.arp.logic.commands.LoadRequestsCommand;
import edu.ubb.arp.logic.commands.LoadAllActiveResourcesCommand;
import edu.ubb.arp.logic.commands.LoadUserDataCommand;
import edu.ubb.arp.logic.commands.projects.AddResourceToprojectCommand;
import edu.ubb.arp.logic.commands.projects.ChangeProjectCurrentStatusCommand;
import edu.ubb.arp.logic.commands.projects.ChangeProjectDeadlineCommand;
import edu.ubb.arp.logic.commands.projects.ChangeProjectNameCommand;
import edu.ubb.arp.logic.commands.projects.ChangeProjectNextReleaseCommand;
import edu.ubb.arp.logic.commands.projects.ChangeProjectOpenedStatusCommand;
import edu.ubb.arp.logic.commands.projects.InsertNewProjectCommand;
import edu.ubb.arp.logic.commands.projects.RemoveResourceFromProjectCommand;
import edu.ubb.arp.logic.commands.projects.RemoveUserFromProjectCommand;
import edu.ubb.arp.logic.commands.projects.UpdateResourceRatioCommand;
import edu.ubb.arp.logic.commands.projects.UpdateResourceRatioWithRequestCommand;
import edu.ubb.arp.logic.commands.projects.UpdateUserIsLeaderCommand;
import edu.ubb.arp.logic.commands.requests.RemoveExpiredRequestsCommand;
import edu.ubb.arp.logic.commands.requests.RemoveRequestFromSomebodyCommand;
import edu.ubb.arp.logic.commands.requests.UpdateRequestRatioOfResourceCommand;
import edu.ubb.arp.logic.commands.requests.UpdateRequestRatioOfUserCommand;
import edu.ubb.arp.logic.commands.resources.AddResourceToGroupCommand;
import edu.ubb.arp.logic.commands.resources.InsertNewResourceCommand;
import edu.ubb.arp.logic.commands.resources.LoadResourceEngagedCommand;
import edu.ubb.arp.logic.commands.resources.RemoveResourceFromGroupCommand;
import edu.ubb.arp.logic.commands.resources.SetResourceActiveCommand;
import edu.ubb.arp.logic.commands.resources.UpdateResourceCommand;
import edu.ubb.arp.logic.commands.users.AddUserToGroupCommand;
import edu.ubb.arp.logic.commands.users.ChangeUserEmailCommand;
import edu.ubb.arp.logic.commands.users.ChangeUserNameCommand;
import edu.ubb.arp.logic.commands.users.ChangeUserPasswordCommand;
import edu.ubb.arp.logic.commands.users.ChangeUserPhoneNumberCommand;
import edu.ubb.arp.logic.commands.users.ChangeUserResourceNameCommand;
import edu.ubb.arp.logic.commands.users.InsertNewUserCommand;
import edu.ubb.arp.logic.commands.users.RemoveUserFromGroupCommand;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *
 */
public class Dispatcher extends BaseCommandOperations {
	private static final Logger logger = Logger.getLogger(Dispatcher.class);

	private JSONArray request = null;
	private JSONArray response = null;
	private Command command;
	/**
	 * constructor
	 * @param request
	 */
	public Dispatcher(JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";

		this.response = new JSONArray();
		this.request = request;
	
		if (request == null || request.getJSONObject(0).size() < 3) { // The request is empty.
			logger.error(getClass().getName() + methodName + "The request is empty.");
			response = setError(0);
		}
	}

	/**
	 * 
	 * @return returns the result of the request 
	 */
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
				case 1: // get active projects
					command = new LoadProjectsUserIsWorkingOnCommand(request);
					response = command.execute();
					break;	
				case 2: 
					command = new LoadProjectTableCommand(request);
					response = command.execute();
					break;	
				case 3: 
					command = new LoadAllActiveResourcesCommand(request);
					response = command.execute();
					break;	
				case 4: 
					command = new LoadAssignmentsCommand(request);
					response = command.execute();
					break;	
				case 5:
					command = new LoadAllProjectsCommand(request);
					response = command.execute();
					break;
				case 6:
					command = new LoadRequestsCommand(request);
					response = command.execute();
					break;
				case 7:
					command = new LoadAllNotActiveResourcesCommand(request);
					response = command.execute();
					break;
				// Users
				case 101: // insert user
					command = new InsertNewUserCommand(request);
					response = command.execute();
					break;
				case 102: // add user to group
					command = new AddUserToGroupCommand(request);
					response = command.execute();
					break;
				case 112: // remove user from group
					command = new RemoveUserFromGroupCommand(request);
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
				case 131: // get user data
					command = new LoadUserDataCommand(request);
					response = command.execute();
					break;
				//------------------------------------------------------
				// Project commands
				case 201: // create new project
					command = new InsertNewProjectCommand(request);
					response = command.execute();
					break;
				case 203: // add resource to project
					command = new AddResourceToprojectCommand(request);
					response = command.execute();
					break;
				case 211: // remove user from project
					command = new RemoveUserFromProjectCommand(request);
					response = command.execute();
					break;
				case 212: // remove resource from project
					command = new RemoveResourceFromProjectCommand(request);
					response = command.execute();
					break;
				case 221: // change project opened status
					command = new ChangeProjectOpenedStatusCommand(request);
					response = command.execute();
					break;
				case 222: // change project name
					command = new ChangeProjectNameCommand(request);
					response = command.execute();
					break;
				case 223: // change project deadline
					command = new ChangeProjectDeadlineCommand(request);
					response = command.execute();
					break;
				case 224: // change project next release
					command = new ChangeProjectNextReleaseCommand(request);
					response = command.execute();
					break;
				case 225: // change project current status
					command = new ChangeProjectCurrentStatusCommand(request);
					response = command.execute();
					break;
				case 227: // update resource ratio in project
					command = new UpdateResourceRatioCommand(request);
					response = command.execute();
					break;
				case 228: // update user isleader in project
					command = new UpdateUserIsLeaderCommand(request);
					response = command.execute();
					break;
				case 229: // update resource ratio in project
					command = new UpdateResourceRatioWithRequestCommand(request);
					response = command.execute();
					break;
				//------------------------------------------------------
				// Resource commands
				case 301: // insert new resource
					command = new InsertNewResourceCommand(request);
					response = command.execute();
					break;
				case 302: // add resource to group 
					command = new AddResourceToGroupCommand(request);
					response = command.execute();
					break;
				case 303: // load resource engages (ratio) 
					command = new LoadResourceEngagedCommand(request);
					response = command.execute();
					break;
				case 312: // remove resource from group 
					command = new RemoveResourceFromGroupCommand(request);
					response = command.execute();
					break;
				case 321: // update resource 
					command = new UpdateResourceCommand(request);
					response = command.execute();
					break;
				case 322: // buy/sell resource 
					command = new SetResourceActiveCommand(request);
					response = command.execute();
					break;
				//------------------------------------------------------
				// Requests commands
				case 411: // remove request from somebody  
					command = new RemoveRequestFromSomebodyCommand(request);
					response = command.execute();
					break;
				case 412: // remove expired requests  
					command = new RemoveExpiredRequestsCommand(request);
					response = command.execute();
					break;
				case 421: // update request ratio of user  
					command = new UpdateRequestRatioOfUserCommand(request);
					response = command.execute();
					break;
				case 422: // update request ratio of ratio  
					command = new UpdateRequestRatioOfResourceCommand(request);
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
