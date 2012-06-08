package edu.ubb.marp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper class, which helps maintaining the database
 * @author Rakosi Alpar, Vizer Arnold
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	/**
	 * Public constructor, which initializes this object
	 * @param context The context, in which the object is working
	 * @param name The name of the database
	 * @param factory Factory for the cursors
	 * @param version Database version
	 */
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Proba(" +
				"_id integer primary key autoincrement, " +
				"nev varchar(30), " +
				"szuletes char(4), " +
				"eletkor integer)");
		
		db.execSQL("CREATE TABLE Projects(" +
				"projectid integer primary key," +
				"openedstatus bool," +
				"deadline integer," +
				"projectname varchar(45)," +
				"nextrelease varchar(10)," +
				"statusname varchar(45)," +
				"startweek integer,"+
				"isleader varchar(25))");
		
		db.execSQL("CREATE TABLE Resources(" +
				"resourceid integer," +
				"resourcename varchar(45)," +
				"resourcetypename varchar(45),"+
				"isactive bool,"+
				"username varchar(45))");
		
		db.execSQL("CREATE TABLE Users(" +
				"userid integer primary key," +
				"username varchar(45)," +
				"userphonenumber varchar(15)," +
				"useremail varchar(45)," +
				"userresourcename varchar(45))");
		
		db.execSQL("CREATE TABLE Leaders(" +
				"ResourceID integer references Resources(ResourceID)," +
				"ProjectID integer references Projects(ProjectID)," +
				"primary key(ResourceID, ProjectID))");
		
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
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
