package edu.ubb.marp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.YuvImage;

public final class Constants {
	public enum ACTIONS {
		LOGIN, QUERY, QUERYWITHOUTSTORING, NEWPROJECT, CHANGEPROJECT, RESOURCEMODIFICATIONS, REQUESTS, ADDRESOURCETOPROJECT, RESOURCERESERVATIONMODIFICATION, CHANGEUSERNAME, CHANGEUSERRESOURCENAME, CHANGEUSERPHONENUMBER, CHANGEUSEREMAIL, CHANGEUSERPASSWORD
	}

	public enum STRIPEACTIVITYACTIONS {
		insert, update, newproject
	}

	public static String BROADCAST_ACTION = "edu.ubb.marp.DATABASEREFRESHED";

	public static final int QUERYUSER = 131;
	
	public static final int LOADASSIGNMENTSCMD = 4;

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

	public static final int UPDATERESOURCERESERVATION = 227;
	public static final int NEWPROJECT = 201;
	public static final int ADDRESOURCETOPROJECTCMD = 203;
	public static final int LOADRESOURCES = 3;

	public static final int CHANGEUSERNAMECMD = 121;
	public static final int CHANGEUSERRESOURCENAMECMD = 125;
	public static final int CHANGEUSERPASSWORDCMD = 122;
	public static final int CHANGEUSEREMAILCMD = 123;
	public static final int CHANGEUSERPHONENUMBERCMD = 124;

	private static Date startDate = new Date(2007, 0, 1);

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

	public static String convertWeekToDate2(int week) {
		// Date myDate = new Date(2007, 1, 1);
		long aWeek = 604800000;
		// myDate.setTime(myDate.getTime() + (week * aWeek));
		Date myDate = new Date(startDate.getTime() + ((week-1) * aWeek));
		

		String s = new String();
		s = s.concat(Integer.toString(myDate.getYear()));
		s = s.concat("." + (myDate.getMonth()+1));
		s = s.concat("." + myDate.getDate());

		return s;
	}

	public static Date convertWeekToRealDate(int week){
		Calendar calendar = new GregorianCalendar(2007, 0, 1, 0, 0, 0);
		calendar.add(Calendar.WEEK_OF_YEAR, week-1);
		Date myDate = calendar.getTime();
		return myDate;
	}
	
	public static String convertWeekToDate(int week){
		Calendar calendar = new GregorianCalendar(2007, 0, 1);
		calendar.add(Calendar.WEEK_OF_YEAR, week-1);
		Date myDate = calendar.getTime();
		
		String s = new String();
		s = s.concat(Integer.toString(myDate.getYear()+1900));
		s = s.concat("." + (myDate.getMonth()+1));
		s = s.concat("." + myDate.getDate());
		return s;
	}
	
	public static Date convertWeekToRealDate2(int week){
		long aWeek = 604800000;
		// myDate.setTime(myDate.getTime() + (week * aWeek));
		Date myDate = new Date(startDate.getTime() + ((week-1) * aWeek));
		return myDate;
	}
	public static int convertDateToWeek(Date date) {
		return weeksBetween(startDate,date)+1;
	}

	private static int weeksBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24 * 7));
	}
}
