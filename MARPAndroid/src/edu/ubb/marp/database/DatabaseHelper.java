package edu.ubb.marp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String tag = "Database";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Proba(" +
				"_id integer primary key autoincrement, " +
				"nev varchar(30), " +
				"szuletes char(4), " +
				"eletkor integer)");
		
		/*db.execSQL("CREATE TABLE Projects(" +
				"ProjectID integer primary key," +
				"OpenedStatus bool," +
				"DeadLine integer," +
				"ProjectName varchar(45)," +
				"NextRelease varchar(10)," +
				"CurrentStatus varchar(45)," +
				"Role varchar(45))");*/
		
		db.execSQL("CREATE TABLE Projects(" +
				"projectid integer primary key," +
				"openedstatus bool," +
				"deadline integer," +
				"projectname varchar(45)," +
				"nextrelease varchar(10)," +
				"statusname varchar(45)," +
				"startweek integer,"+
				"isleader varchar(25))");
				//"_id integer)");
		
		/*db.execSQL("CREATE TABLE Resources(" +
				"resourceid integer primary key," +
				"resourcename varchar(45)," +
				"resourcetypename varchar(45),"+
				"isactive bool,"+
				"username varchar(45))");*/
		db.execSQL("CREATE TABLE Resources(" +
				"resourceid integer," +
				"resourcename varchar(45)," +
				"resourcetypename varchar(45),"+
				"isactive bool,"+
				"username varchar(45))");
		
		db.execSQL("CREATE TABLE Users(" +
				"userid integer primary key," +
				"username varchar(45)," +
				//"password varchar(45)," +
				//"hired bool," +
				"userphonenumber varchar(15)," +
				"useremail varchar(45)," +
				"userresourcename varchar(45))");

		/*db.execSQL("CREATE TABLE ResourceIsUser(" +
				"ResourceID integer references Resources(ResourceID)," +
				"UserID integer references Users(UserID)," +
				"primary key(ResourceID, UserID))");*/
		
		db.execSQL("CREATE TABLE Leaders(" +
				"ResourceID integer references Resources(ResourceID)," +
				"ProjectID integer references Projects(ProjectID)," +
				"primary key(ResourceID, ProjectID))");
		
		/*db.execSQL("CREATE TABLE Booking(" +
				"resourceid integer references Resources(ResourceID)," +
				"projectid integer references Projects(ProjectID)," +
				"week integer," +
				"ratio float," +
				"isleader bool," +
				"primary key(resourceid, projectid, week))");*/
		
		db.execSQL("CREATE TABLE Booking(" +
				"resourceid integer references Resources(ResourceID)," +
				"projectid integer references Projects(ProjectID)," +
				"week integer," +
				"ratio float," +
				"isleader bool)");
		
		db.execSQL("CREATE TABLE BookingAssignments(" +
				"resourceid integer," +
				"projectid integer," +
				"week integer," +
				"ratio float," +
				"isleader bool)");
		
		/*db.execSQL("CREATE TABLE Requests(" +
				"RequestID integer primary key," +
				"Week integer," +
				"Ratio float," +
				"Sender varchar(45)," +
				"Resource varchar(45)," +
				"ProjectID integer," +
				"Rejected bool" +
				"Visible bool)");*/
		/*db.execSQL("CREATE TABLE Requests(" +
				"RequestID integer primary key," +
				"Week integer," +
				"Ratio float," +
				"Sender varchar(45)," +
				"Resource varchar(45)," +
				"ProjectID integer)");*/
		db.execSQL("CREATE TABLE Requests(" +
				"requestid integer primary key," +
				"week integer," +
				"ratio float," +
				"senderid integer," +
				"resourceid integer," +
				"senderusername varchar(45)," +
				"resourceresourcename varchar(45)," +
				"projectid integer)");
		
		db.execSQL("CREATE TABLE Groups(" +
				"GroupID integer," +
				"GroupName varchar(45))");
		
		db.execSQL("CREATE TABLE ResourcesGroups(" +
				"ResourceID integer," +
				"GroupID integer," +
				"primary key(ResourceID, GroupID))");
		
		Log.i(tag, "Database created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
