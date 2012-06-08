package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.*;
import edu.ubb.marp.network.MyService;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class ProjectActivity extends ListActivity {

	/**
	 * The loading progress dialog
	 */
	private ProgressDialog loading;

	/**
	 * The requestid
	 */
	private long requestid;
	/**
	 * The ids of the projects
	 */
	private String[] ids;
	/**
	 * The names of the projects
	 */
	private String[] projectNames;
	/**
	 * Is the user leader in that project
	 */
	private boolean[] isLeader;
	/**
	 * The user leaved the activity
	 */
	private boolean leaved;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		leaved = false;

		setContentView(R.layout.projects);
		sendRequest();
	}

	/**
	 * Sends a request for querying the active projects
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", "").equals("manager")) {
			uri.path(Integer.toString(Constants.ALLPROJECTSCMD));
		} else {
			uri.path(Integer.toString(Constants.PROJECTSCMD));
		}
		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.setData(uri.build());
		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();

		if (leaved) {
			leaved = false;
			sendRequest();
		}

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", "").equals("manager")) {
			inflater.inflate(R.menu.projectmanager, menu);
		} else {
			inflater.inflate(R.menu.project, menu);
		}
		return true;
	}

	/**
	 * is called when a message box needs to be appeared
	 * 
	 * @param message
	 *            is the message of the message box
	 * @param title
	 *            is the title of the message box
	 */
	public void messageBoxShow(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendRequest();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * is called when the user selects one of the menu items
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent myIntent;
		switch (item.getItemId()) {
		case R.id.myaccountid:
			myIntent = new Intent(this, MyAccountActivity.class);
			startActivity(myIntent);
			return true;
		case R.id.logout:
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = pref.edit();
			editor.putBoolean("remember", false);

			editor.apply();

			myIntent = new Intent(getApplicationContext(), Login.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(myIntent);
			return true;
		case R.id.projectid:
			leaved = true;
			myIntent = new Intent(getApplicationContext(), NewProjectActivity.class);
			startActivity(myIntent);
			return true;
		case R.id.about:
			aboutMessage();
			return true;
		case R.id.refreshProjects:
			sendRequest();
			return true;
		case R.id.addresourcemnuitem:
			myIntent = new Intent(getApplicationContext(), InsertNewResourceActivity.class);
			startActivity(myIntent);
			return true;
		case R.id.addusermenuitem:
			myIntent = new Intent(getApplicationContext(), InsertNewUserActivity.class);
			startActivity(myIntent);
			return true;
		case R.id.reactivateresource:
			myIntent = new Intent(getApplicationContext(), ReActivateResourceActivity.class);
			startActivity(myIntent);
			return true;
		case R.id.deleteresource:
			myIntent = new Intent(getApplicationContext(), DeleteResourceActivity.class);
			startActivity(myIntent);
			return true;
		case R.id.requestsmenuitem:
			myIntent = new Intent(getApplicationContext(), RequestsActivity.class);
			startActivity(myIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Queries the active projects from the database
	 */
	private void queryData() {
		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path("Projects");
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		String projection[] = { TABLE_PROJECTS.PROJECTNAME, TABLE_PROJECTS.ISLEADER, TABLE_PROJECTS.PROJECTID + " as _id" };

		Cursor c = cr.query(uri.build(), projection, null, null, TABLE_PROJECTS.PROJECTID);

		ids = new String[c.getCount()];
		projectNames = new String[c.getCount()];
		isLeader = new boolean[c.getCount()];
		int i = 0;
		if (c.moveToFirst()) {
			int index = c.getColumnIndex("_id");
			int index2 = c.getColumnIndex(TABLE_PROJECTS.PROJECTNAME);
			int index3 = c.getColumnIndex(TABLE_PROJECTS.ISLEADER);
			ids[i] = c.getString(index);
			projectNames[i] = c.getString(index2);
			isLeader[i++] = c.getString(index3).equals("Leader");
			while (c.moveToNext()) {
				ids[i] = c.getString(index);
				projectNames[i] = c.getString(index2);
				isLeader[i++] = c.getString(index3).equals("Leader");
			}
		}

		c.moveToFirst();
		startManagingCursor(c);

		String[] from = { TABLE_PROJECTS.PROJECTNAME, TABLE_PROJECTS.ISLEADER };

		int[] to = { R.id.text1, R.id.text2 };
		SimpleCursorAdapter projects = new SimpleCursorAdapter(getApplicationContext(), R.layout.projects_row, c, from, to);
		setListAdapter(projects);
	}

	/**
	 * Receives the broadcasts
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				if (intent.getBooleanExtra("Successful", false)) {
					loading.dismiss();
					queryData();

					Uri.Builder uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path("131");

					uri.scheme("content");

					Intent myIntent = new Intent(getApplicationContext(), MyService.class);
					myIntent.putExtra("ACTION", "QUERY");
					myIntent.setData(uri.build());

					myIntent.putExtra("requestid", (long) 0);
					startService(myIntent);
				} else {
					loading.dismiss();
					if (intent.getIntExtra("error", 10000) == 0) {
						queryData();
					} else {
						messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};

	/**
	 * 
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		leaved = true;

		Intent myIntent = new Intent(getApplicationContext(), ResourcesActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("projectid", ids[position]);
		bundle.putString("projectname", projectNames[position]);
		bundle.putBoolean("isleader", isLeader[position]);
		myIntent.putExtras(bundle);
		startActivity(myIntent);
	}

	/**
	 * the method shows a windows abut creators and the version number of the
	 * project
	 */
	public void aboutMessage() {

		AboutMessage about = new AboutMessage(this);

		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("About");
		alertDialog.setView(about.returnView());

		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});

		alertDialog.show();
	}
}
