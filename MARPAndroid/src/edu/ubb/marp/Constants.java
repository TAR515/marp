package edu.ubb.marp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.YuvImage;

public final class Constants {

	/**
	 * Identifies the action, which is taken by the service 
	 */
	public enum ACTIONS {
		LOGIN, QUERY, QUERYWITHOUTSTORING, NEWPROJECT, CHANGEPROJECT, RESOURCEMODIFICATIONS, USERMODIFICATIONS, REQUESTS, ADDRESOURCETOPROJECT, RESOURCERESERVATIONMODIFICATION, CHANGEUSERNAME, CHANGEUSERRESOURCENAME, CHANGEUSERPHONENUMBER, CHANGEUSEREMAIL, CHANGEUSERPASSWORD, SETRESOURCEACTIVE
	}

	/**
	 * Identifies the action, which is taken by the StripeActivity
	 */
	public enum STRIPEACTIVITYACTIONS {
		insert, update, newproject
	}

	/**
	 * Own broadcasts string
	 */
	public static String BROADCAST_ACTION = "edu.ubb.marp.DATABASEREFRESHED";

	/**
	 * Commandcode of querying a user
	 */
	public static final int QUERYUSER = 131;

	/**
	 * Code of the login command
	 */
	public static final int LOGINCMD = 0;
	/**
	 * Commandcode of querying the active projects
	 */
	public static final int PROJECTSCMD = 1;
	/**
	 * Commandcode of querying the assignments in a given project
	 */
	public static final int PROJECTRESOURCESCMD = 2;

	/**
	 * Commandcode of querying the assignments for a user
	 */
	public static final int LOADASSIGNMENTSCMD = 4;
	/**
	 * Commandcode of querying the requests
	 */
	public static final int LOADREQUESTSCMD = 6;

	/**
	 * Commandcode of querying the accessibility of a resource
	 */
	public static final int QUERYAVAILABLERESOURCESCODE = 303;

	/**
	 * Commandcode of changing a projects openedstatus
	 */
	public static final int CHANGEPROJECTOPENEDSTATUS = 221;
	/**
	 * Commandcode of changing a projects name
	 */
	public static final int CHANGEPROJECTNAME = 222;
	/**
	 * Commandcode of changing a projects deadline
	 */
	public static final int CHANGEPROJECTDEADLINE = 223;
	/**
	 * Commandcode of changing a projects next release
	 */
	public static final int CHANGEPROJECTNEXTRELEASE = 224;
	/**
	 * Commandcode of changing a projects current status
	 */
	public static final int CHANGEPROJECTCURRENTSTATUS = 225;

	/**
	 * Commandcode of inserting a new resource
	 */
	public static final int INSERTNEWRESOURCE = 301;
	/**
	 * Commandcode of updating a resource
	 */
	public static final int UPDATERESOURCE = 321;
	/**
	 * Commandcode of updating a resources status
	 */
	public static final int SETRESOURCEACTIVECMD = 322;

	/**
	 * Commandcode of inserting a new user
	 */
	public static final int INSERTNEWUSER = 101;

	/**
	 * Commandcode of creating a request for a user
	 */
	public static final int CREATENEWREQUESTFORUSER = 401;
	/**
	 * Commandcode of creating a request for a resource
	 */
	public static final int CREATENEWREQUESTFORRESOURCE = 402;
	/**
	 * Commandcode of removing a request from somebody
	 */
	public static final int REMOVEREQUESTFROMSOMEBODY = 411;
	/**
	 * Commandcode of removing expired requests
	 */
	public static final int REMOVEEXPIREDREQUESTS = 412;
	/**
	 * Commandcode of updating the request ratio of a user
	 */
	public static final int UPDATEREQUESTRATIOOFUSER = 421;

	/**
	 * Commandcode of updating a resources reservation
	 */
	public static final int UPDATERESOURCERESERVATION = 227;
	/**
	 * Commandcode of creating a new project
	 */
	public static final int NEWPROJECT = 201;
	/**
	 * Commandcode of adding a resource to a project
	 */
	public static final int ADDRESOURCETOPROJECTCMD = 203;
	/**
	 * Commandcode of querying the active resources
	 */
	public static final int LOADRESOURCES = 3;
	/**
	 * Commandcode of querying the inactive resources
	 */
	public static final int LOADINACTIVERESOURCES = 7;

	/**
	 * Commandcode of changing the username
	 */
	public static final int CHANGEUSERNAMECMD = 121;
	/**
	 * Commandcode of changing a users real name
	 */
	public static final int CHANGEUSERRESOURCENAMECMD = 125;
	/**
	 * Commandcode of changing a users password
	 */
	public static final int CHANGEUSERPASSWORDCMD = 122;
	/**
	 * Commandcode of changing the users email address
	 */
	public static final int CHANGEUSEREMAILCMD = 123;
	/**
	 * Commandcode of changing a users phonenumber
	 */
	public static final int CHANGEUSERPHONENUMBERCMD = 124;

	/**
	 * The start of the saving time
	 */
	private static Date startDate = new Date(107, 0, 1);

	/**
	 * Returns the error message of the error code.
	 * 
	 * @param code
	 *            The code of the error.
	 * @return The error message.
	 */
	public static String getErrorMessage(int code) {
		switch (code) {
		case 0:
			return "No connection";
		case -1:
			return "Unknown error";
		case -2:
			return "Transaction rollbacked";
		case -3:
			return "Rollback failed";
		case -4:
			return "Foreign key constraint";
		case -5:
			return "Wrong parameter";
		case -6:
			return "Operation could not been affected";
		case -7:
			return "Wrong password";
		case -8:
			return "Wrong username or password";
		case -9:
			return "Wrong request from client";

		case -10:
			return "Group already exists";
		case -11:
			return "Group not exists";

		case -12:
			return "Resource name already exists";
		case -13:
			return "Resource name not exists";

		case -14:
			return "Resource already exists";
		case -15:
			return "Resource not exists";

		case -16:
			return "Resource type already exists";
		case -17:
			return "Resource type not exists";

		case -18:
			return "Username already exists";
		case -19:
			return "Username not exists";

		case -20:
			return "User already exists";
		case -21:
			return "User not exists";

		case -22:
			return "Status already exists";
		case -23:
			return "Status not exists";

		case -24:
			return "Project already exists";
		case -25:
			return "Project not exists";

		case -26:
			return "Project name already exists";
		case -27:
			return "Project name not exists";

		case -28:
			return "Leader already exists";
		case -29:
			return "Leader not exists";

		case -40:
			return "No data";
		default:
			return "";
		}
	}

	/**
	 * Converts a weeks number to string.
	 * 
	 * @param week
	 *            The number of the week from 2007.1.1
	 * @return The real date as a string.
	 */
	public static String convertWeekToDate(int week) {
		Calendar calendar = new GregorianCalendar(2007, 0, 1);
		calendar.add(Calendar.WEEK_OF_YEAR, week - 1);
		Date myDate = calendar.getTime();

		String s = new String();
		s = s.concat(Integer.toString(myDate.getYear() + 1900));
		s = s.concat("." + (myDate.getMonth() + 1));
		s = s.concat("." + myDate.getDate());
		return s;
	}

	/**
	 * Converts a weeks number to a Date object.
	 * 
	 * @param week
	 *            The number of the week from 2007.1.1
	 * @return The real date as a Date object.
	 */
	public static Date convertWeekToRealDate(int week) {
		Calendar calendar = new GregorianCalendar(2007, 0, 1);
		calendar.add(Calendar.WEEK_OF_YEAR, week - 1);
		return calendar.getTime();
	}

	/**
	 * Converts a Date object to a week number.
	 * 
	 * @param d1
	 *            The date object, which has to be converted.
	 * @return The weeks number counted from 2007.1.1
	 */
	public static int convertDateToWeek(Date d1) {
		Date d2 = new Date(107, 0, 1);

		if (d1.after(d2)) {
			Date temp = d1;
			d1 = d2;
			d2 = temp;
		}
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTime(d1);
		GregorianCalendar c2 = new GregorianCalendar();
		c2.setTime(d2);
		for (int i = 1;; i++) {
			c1.add(GregorianCalendar.WEEK_OF_YEAR, 1);
			if (c1.after(c2))
				return i;
		}
	}
}
