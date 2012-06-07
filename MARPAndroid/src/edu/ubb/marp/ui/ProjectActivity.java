package edu.ubb.marp.ui;

import java.util.ArrayList;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 *
 */
public class ProjectActivity extends ListActivity {
	private static final String tag = "ProjectActivity";

	private ProgressDialog loading;


	private long requestid;
	private String[] ids;
	private String[] projectNames;
	private boolean[] isLeader;
	private boolean leaved;
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");
		
		
		leaved = false;

		setContentView(R.layout.projects);
		Log.i(tag, "setcontent utan");
		sendRequest();
	}
	/**
	 * 
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading",
				"Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(Integer.toString(Constants.PROJECTSCMD));
		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.setData(uri.build());
		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}
	/**
	 * 
	 */
	@Override
	protected void onStart() {
		super.onStart();
		
		if(leaved){
			leaved=false;
			sendRequest();
		}

		registerReceiver(broadcastReceiver, new IntentFilter(
				Constants.BROADCAST_ACTION));
	}
	/**
	 * 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}
	/**
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.project, menu);
		return true;
	}

	/** 
	 * is called when a message box needs to be appeared 
	 * @param message is the message of the message box
	 * @param title is the title of the message box
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
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = pref.edit();
			editor.putBoolean("remember", false);

			editor.apply();

			myIntent = new Intent(getApplicationContext(), Login.class);
			myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(myIntent);
			return true;
		case R.id.projectid:
			leaved=true;
			myIntent = new Intent(getApplicationContext(), NewProjectActivity.class);
			startActivity(myIntent);
			return true;
		case R.id.requestsid:
			myIntent = new Intent(getApplicationContext(), RequestsActivity.class);
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
	 * 
	 */
	private void queryData(){
		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path("Projects");
		uri.scheme("content");

		ContentResolver cr = getContentResolver();
		Log.i(tag, "query elott");

		String projection[] = { TABLE_PROJECTS.PROJECTNAME,
				TABLE_PROJECTS.ISLEADER,
				TABLE_PROJECTS.PROJECTID + " as _id" };

		Cursor c = cr.query(uri.build(), projection, null, null,
				TABLE_PROJECTS.PROJECTID);
		Log.i(tag, "query utan");

		ids = new String[c.getCount()];
		projectNames = new String[c.getCount()];
		isLeader = new boolean[c.getCount()];
		int i = 0;
		if (c.moveToFirst()) {
			int index = c.getColumnIndex("_id");
			int index2 = c.getColumnIndex(TABLE_PROJECTS.PROJECTNAME);
			int index3 = c.getColumnIndex(TABLE_PROJECTS.ISLEADER);
			Log.i(tag, "while elott");
			ids[i] = c.getString(index);
			projectNames[i] = c.getString(index2);
			isLeader[i++] = c.getString(index3).equals("Leader");
			Log.i(tag, "lekerdezes " + ids[i - 1]);
			while (c.moveToNext()) {
				ids[i] = c.getString(index);
				projectNames[i] = c.getString(index2);
				isLeader[i++] = c.getString(index3).equals("Leader");
				Log.i(tag, "lekerdezes " + ids[i - 1]);
			}
			Log.i(tag, "while utan");
		}

		c.moveToFirst();
		startManagingCursor(c);

		String[] from = { TABLE_PROJECTS.PROJECTNAME,
				TABLE_PROJECTS.ISLEADER };
		
		int[] to = { R.id.text1, R.id.text2 };
		SimpleCursorAdapter projects = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.projects_row, c,
				from, to);
		setListAdapter(projects);
	}
	/**
	 * 
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "megjott a broadcast");
			
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				if (intent.getBooleanExtra("Successful", false)) {
					loading.dismiss();
					queryData();
					
					Uri.Builder uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path("131");
					
					uri.scheme("content");

					Intent myIntent = new Intent(getApplicationContext(),
							MyService.class);
					myIntent.putExtra("ACTION", "QUERY");
					myIntent.setData(uri.build());

					
					myIntent.putExtra("requestid", (long)0);
					startService(myIntent);
				} else {
					loading.dismiss();
					if(intent.getIntExtra("error", 10000)==0){
						queryData();
					}else{
						messageBoxShow(Constants.getErrorMessage(intent
							.getIntExtra("error", 0)), "Error");
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
		
		Intent myIntent = new Intent(getApplicationContext(),
				ResourcesActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("projectid", ids[position]);
		bundle.putString("projectname", projectNames[position]);
		bundle.putBoolean("isleader", isLeader[position]);
		myIntent.putExtras(bundle);
		startActivity(myIntent);
	}
	/**
	 * the method shows a windows abut creators and the version number of the project
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
