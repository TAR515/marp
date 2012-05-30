package edu.ubb.marp;

import java.util.Date;

public final class Constants {
	public enum ACTIONS {
		LOGIN, QUERY, QUERYWITHOUTSTORING, NEWPROJECT, CHANGEPROJECT, RESOURCEMODIFICATIONS, REQUESTS
	}
	
	public enum STRIPEACTIVITYACTIONS{
		NEWPROJECT, MODRESOURCERESERVATION
	}

	public static String BROADCAST_ACTION = "edu.ubb.marp.DATABASEREFRESHED";
	
	public static final int QUERYUSER = 131;

	public static final int QUERYAVAILABLERESOURCESCODE = 303;
	public static final int CHANGEPROJECTOPENEDSTATUS = 221;
	public static final int CHANGEPROJECTNAME = 222;
	public static final int CHANGEPROJECTDEADLINE = 223;
	public static final int CHANGEPROJECTNEXTRELEASE = 224;
	public static final int CHANGEPROJECTCURRENTSTATUS = 225;
	
	public static final int INSERTNEWRESOURCE = 301;
	public static final int ADDRESOURCETOGROUP = 302;
	public static final int REMOVERESOURCEFROMGROUP = 312;
	public static final int UPDATERESOURCE = 321;
	public static final int BUYSELLRESOURCE = 322;
	
	public static final int CREATENEWREQUESTFORUSER = 401;
	public static final int CREATENEWREQUESTFORRESOURCE = 402;
	public static final int REMOVEREQUESTFROMSOMEBODY = 411;
	public static final int REMOVEEXPIREDREQUESTS = 412;
	public static final int UPDATEREQUESTRATIOOFUSER = 421;
	public static final int UPDATEREQUESTRATIOOFRATIO = 422;

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
		default:
			return "";
		}
	}

	public static String convertWeekToDate(int week) {
		Date myDate = new Date(2007, 1, 1);
		long aWeek = 604800000;
		myDate.setTime(myDate.getTime() + (week * aWeek));

		String s = new String();
		s = s.concat(Integer.toString(myDate.getYear()));
		s = s.concat("." + myDate.getMonth());
		s = s.concat("." + myDate.getDate());

		return s;
	}
}
