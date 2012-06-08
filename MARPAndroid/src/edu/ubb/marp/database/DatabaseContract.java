package edu.ubb.marp.database;

import android.content.UriMatcher;

/**
 * Class for collecting the constant values related to the database
 * 
 * @author Rakosi Alpar, Vizer Arnold
 */
public final class DatabaseContract {

	/**
	 * The applications providers name
	 */
	public static final String PROVIDER_NAME = "edu.ubb.marp.database.provider";
	/**
	 * The applications providers authority
	 */
	public static final String AUTHORITY = "edu.ubb.marp.database.provider";
	/**
	 * The name of the database
	 */
	public static final String DATABASE_NAME = "MARPDatabase";
	/**
	 * Database version
	 */
	public static final int DATABASE_VERSION = 1;

	/**
	 * Code of the query type, when multiple rows are queried
	 */
	public static final int MULTIPLE = 1;
	/**
	 * Code of the query type, when only 1 row is queried
	 */
	public static final int ROW = 2;

	/**
	 * Name of the projects table
	 */
	public static final String TABLE_PROJECTS = "Projects";

	/**
	 * The column names of the projects table
	 */
	public static final class TABLE_PROJECTS {
		public static final String PROJECTID = "projectid";
		public static final String OPENEDSTATUS = "openedstatus";
		public static final String DEADLINE = "deadline";
		public static final String PROJECTNAME = "projectname";
		public static final String NEXTRELEASE = "nextrelease";
		public static final String STATUSNAME = "statusname";
		public static final String STARTWEEK = "startweek";
		public static final String ISLEADER = "isleader";
	}

	/**
	 * Name of the resources table
	 */
	public static final String TABLE_RESOURCES = "Resources";

	/**
	 * The column names of the resources table
	 */
	public static final class TABLE_RESOURCES {
		public static final String RESOURCEID = "resourceid";
		public static final String RESOURCENAME = "resourcename";
		public static final String RESOURCETYPENAME = "resourcetypename";
		public static final String ISACTIVE = "isactive";
		public static final String USERNAME = "username";
	}

	/**
	 * Name of the users table
	 */
	public static final String TABLE_USERS = "Users";

	/**
	 * The column names of the users table
	 */
	public static final class TABLE_USERS {
		public static final String USERID = "userid";
		public static final String USERNAME = "username";
		public static final String USERPHONENUMBER = "userphonenumber";
		public static final String USEREMAIL = "useremail";
		public static final String USERRESOURCENAME = "userresourcename";
	}

	/**
	 * Name of the resourceisuser table
	 */
	public static final String TABLE_RESOURCEISUSER = "ResourceIsUser";

	/**
	 * The column names of the resourceisuser table
	 */
	public static final class TABLE_RESOURCEISUSER {
		public static final String RESOURCEID = "ResourceID";
		public static final String USERID = "UserID";
	}

	/**
	 * Name of the leaders table
	 */
	public static final String TABLE_LEADERS = "Leaders";

	/**
	 * The column names of the leaders table
	 */
	public static final class TABLE_LEADERS {
		public static final String RESOURCEID = "ResourceID";
		public static final String PROJECTID = "UserID";
	}

	/**
	 * Name of the bookingassignments table
	 */
	public static final String TABLE_BOOKINGASSIGNMENTS = "BookingAssignments";
	/**
	 * Name of the booking table
	 */
	public static final String TABLE_BOOKING = "Booking";

	/**
	 * The column names of the booking and bookingassignments tables
	 */
	public static final class TABLE_BOOKING {
		public static final String RESOURCEID = "resourceid";
		public static final String PROJECTID = "projectid";
		public static final String WEEK = "week";
		public static final String RATIO = "ratio";
		public static final String ISLEADER = "isleader";
	}

	/**
	 * Name of the requests table
	 */
	public static final String TABLE_REQUESTS = "Requests";

	/**
	 * The column names of the booking and requests tables
	 */
	public static final class TABLE_REQUESTS {
		public static final String REQUESTID = "requestid";
		public static final String WEEK = "week";
		public static final String RATIO = "ratio";
		public static final String SENDERID = "senderid";
		public static final String RESOURCEID = "resourceid";
		public static final String SENDERUSERNAME = "senderusername";
		public static final String RESOURCERESOURCENAME = "resourceresourcename";
		public static final String PROJECTID = "projectid";
	}

	/**
	 * UriMatcher object, which classifies a URI
	 */
	public static final UriMatcher sUriMatcher;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(AUTHORITY, TABLE_PROJECTS, MULTIPLE);
		sUriMatcher.addURI(AUTHORITY, TABLE_PROJECTS + "/#", ROW);
		sUriMatcher.addURI(AUTHORITY, TABLE_RESOURCES, MULTIPLE);
		sUriMatcher.addURI(AUTHORITY, TABLE_RESOURCES + "/#", ROW);
		sUriMatcher.addURI(AUTHORITY, TABLE_USERS, MULTIPLE);
		sUriMatcher.addURI(AUTHORITY, TABLE_USERS + "/#", ROW);
		sUriMatcher.addURI(AUTHORITY, TABLE_BOOKING, MULTIPLE);
		sUriMatcher.addURI(AUTHORITY, TABLE_BOOKING + "/#", ROW);
		sUriMatcher.addURI(AUTHORITY, TABLE_REQUESTS, MULTIPLE);
		sUriMatcher.addURI(AUTHORITY, TABLE_REQUESTS + "/#", ROW);
	}
}
