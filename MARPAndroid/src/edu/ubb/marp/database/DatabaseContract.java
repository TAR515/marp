package edu.ubb.marp.database;

import android.content.UriMatcher;

public final class DatabaseContract {
	public static final String PROVIDER_NAME = "edu.ubb.marp.database.provider";
	public static final String AUTHORITY = "edu.ubb.marp.database.provider";
	public static final String DATABASE_NAME = "MARPDatabase";
	public static final int DATABASE_VERSION = 1;

	public static final int MULTIPLE = 1;
	public static final int ROW = 2;

	public static final String TABLE_PROJECTS = "Projects";

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

	public static final String TABLE_RESOURCES = "Resources";

	public static final class TABLE_RESOURCES {
		public static final String RESOURCEID = "resourceid";
		public static final String RESOURCENAME = "resourcename";
		public static final String RESOURCETYPENAME = "resourcetypename";
		public static final String ISACTIVE = "isactive";
		public static final String USERNAME = "username";
	}

	public static final String TABLE_USERS = "Users";

	public static final class TABLE_USERS {
		public static final String USERID = "userid";
		public static final String USERNAME = "username";
		//public static final String PASSWORD = "password";
		//public static final String HIRED = "hired";
		public static final String USERPHONENUMBER = "userphonenumber";
		public static final String USEREMAIL = "useremail";
		public static final String USERRESOURCENAME = "userresourcename";
	}

	public static final String TABLE_RESOURCEISUSER = "ResourceIsUser";

	public static final class TABLE_RESOURCEISUSER {
		public static final String RESOURCEID = "ResourceID";
		public static final String USERID = "UserID";
	}

	public static final String TABLE_LEADERS = "Leaders";

	public static final class TABLE_LEADERS {
		public static final String RESOURCEID = "ResourceID";
		public static final String PROJECTID = "UserID";
	}
	
	public static final String TABLE_BOOKINGASSIGNMENTS = "BookingAssignments";
	public static final String TABLE_BOOKING = "Booking";

	public static final class TABLE_BOOKING {
		public static final String RESOURCEID = "resourceid";
		public static final String PROJECTID = "projectid";
		public static final String WEEK = "week";
		public static final String RATIO = "ratio";
		public static final String ISLEADER = "isleader";
	}

	public static final String TABLE_REQUESTS = "Requests";

	/*public static final class TABLE_REQUESTS {
		public static final String REQUESTID = "RequestID";
		public static final String WEEK = "Week";
		public static final String RATIO = "Ratio";
		public static final String SENDERID = "SenderID";
		public static final String RESOURCEID = "ResourceID";
		public static final String PROJECTID = "ProjectID";
		public static final String REJECTED = "Rejected";
		public static final String VISIBLE = "Visible";
	}*/
	public static final class TABLE_REQUESTS {
		public static final String REQUESTID = "RequestID";
		public static final String WEEK = "Week";
		public static final String RATIO = "Ratio";
		public static final String SENDER = "Sender";
		public static final String RESOURCE = "Resource";
		public static final String PROJECTID = "ProjectID";
	}

	public static final String TABLE_GROUPS = "Groups";

	public static final class TABLE_GROUPS {
		public static final String GROUPID = "GroupID";
		public static final String GROUPNAME = "GroupName";
	}

	public static final String TABLE_RESOURCESGROUPS = "ResourcesGroups";

	public static final class TABLE_RESOURCESGROUPS {
		public static final String RESOURCEID = "ResourceID";
		public static final String GROUPID = "GroupID";
	}

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
		sUriMatcher.addURI(AUTHORITY, TABLE_GROUPS, MULTIPLE);
		sUriMatcher.addURI(AUTHORITY, TABLE_GROUPS + "/#", ROW);
	}
}
