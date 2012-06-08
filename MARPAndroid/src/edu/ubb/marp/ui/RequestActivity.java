package edu.ubb.marp.ui;

import java.util.ArrayList;
import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_PROJECTS;
import edu.ubb.marp.database.DatabaseContract.TABLE_REQUESTS;
import edu.ubb.marp.database.DatabaseContract.TABLE_RESOURCES;
import edu.ubb.marp.network.MyService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class RequestActivity extends Activity {

	private long requestid;
	private ProgressDialog loading;
	private Context context;
	private int currentRequestID;
	private String projectName, senderUserName, targetResource, ratio;
	private int week, projectID, myresourceid, targetResourceID;

	ArrayList<ListRecord> listItems = new ArrayList<ListRecord>();

	/**
	 * Called when the activity is created
	 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.myaccount);

		context = this;

		currentRequestID = getIntent().getExtras().getInt("requestid");

		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_REQUESTS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		Cursor c = cr.query(uri.build(), null, TABLE_REQUESTS.REQUESTID + " = " + Integer.toString(currentRequestID), null, null);

		if (c.moveToFirst()) {
			senderUserName = c.getString(c.getColumnIndex(TABLE_REQUESTS.SENDERUSERNAME));
			targetResource = c.getString(c.getColumnIndex(TABLE_REQUESTS.RESOURCERESOURCENAME));
			targetResourceID = c.getInt(c.getColumnIndex(TABLE_REQUESTS.RESOURCEID));
			week = c.getInt(c.getColumnIndex(TABLE_REQUESTS.WEEK));
			ratio = c.getString(c.getColumnIndex(TABLE_REQUESTS.RATIO));
			projectID = c.getInt(c.getColumnIndex(TABLE_REQUESTS.PROJECTID));

			uri.path(DatabaseContract.TABLE_PROJECTS);
			c = cr.query(uri.build(), null, TABLE_PROJECTS.PROJECTID + " = " + Integer.toString(projectID), null, null);
			if (c.moveToFirst()) {
				projectName = c.getString(c.getColumnIndex(TABLE_PROJECTS.PROJECTNAME));
			}

			setListItems(senderUserName, targetResource, week, ratio);

			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			uri.path(DatabaseContract.TABLE_RESOURCES);
			String projection[] = { TABLE_RESOURCES.RESOURCEID };
			c = cr.query(uri.build(), projection, TABLE_RESOURCES.USERNAME + " = '" + pref.getString("username", "") + "'", null, null);
			c.moveToFirst();
			myresourceid = c.getInt(c.getColumnIndex(TABLE_RESOURCES.RESOURCEID));
		}
	}

	/**
	 * Called when the Activity is loaded
	 */
	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}

	/**
	 * Called before the activity goes on background
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * Sets the elements of the list
	 * 
	 * @param sender
	 *            The name of the sender
	 * @param target
	 *            The name of the target resource
	 * @param currentWeek
	 *            The current week
	 * @param currentRatio
	 *            The current ratio
	 */
	private void setListItems(String sender, String target, int currentWeek, String currentRatio) {
		listItems = new ArrayList<ListRecord>();

		ListRecord item = new ListRecord("Sender", sender);
		listItems.add(item);

		item = new ListRecord("Target resource", target);
		listItems.add(item);

		item = new ListRecord("Week", Constants.convertWeekToDate(week));
		listItems.add(item);

		item = new ListRecord("Ratio", currentRatio);
		listItems.add(item);

		item = new ListRecord("Accept", "");
		listItems.add(item);

		item = new ListRecord("Reject", "");
		listItems.add(item);

		ListView listView = (ListView) findViewById(R.id.ListViewId);
		listView.setAdapter(new ListItemAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listItems));

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				switch (pos) {
				case 4:
					sendRequest();
					break;
				case 5:
					sendRequestReject();
					break;
				}
			}
		});
	}

	/**
	 * Sends a request for querying the reservation of the target resource
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());
		intent.putExtra("startweek", week);
		intent.putExtra("endweek", week);
		intent.putExtra("action", "update");
		intent.putExtra("projectname", projectName);
		intent.putExtra("resourceid", targetResourceID);

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * Sends a request for rejecting a request
	 */
	private void sendRequestReject() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(Integer.toString(Constants.REJECTREQUESTCMD));
		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.setData(uri.build());

		intent.putExtra("ACTION", "REQUESTOPERATIONS");
		intent.putExtra("projectid", projectID);
		intent.putExtra("resourceid", myresourceid);
		intent.putExtra("currentrequestid", currentRequestID);

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);

		startService(intent);
	}

	/**
	 * Shows a messagebox
	 */
	public void messageBoxShowRetry(String message, String title) {
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
				loading.dismiss();

				if (intent.getBooleanExtra("Successful", false)) {
					if (intent.getBooleanExtra("change", false)) {
						finish();
					} else {
						int[] results = intent.getIntArrayExtra("results");
						int[] currentProjectResults = intent.getIntArrayExtra("ratioincurrentproject");

						Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
						Bundle bundle = new Bundle();

						bundle.putString("ACTION", "request");
						// bundle.putString("projectname", projectName);
						bundle.putInt("projectid", projectID);
						bundle.putInt("targetresourceid", targetResourceID);
						bundle.putInt("senderresourceid", myresourceid);
						bundle.putInt("startweek", week);
						bundle.putInt("endweek", week);
						bundle.putInt("currentrequestid", currentRequestID);
						bundle.putIntArray("results", results);
						bundle.putIntArray("booking", currentProjectResults);

						myIntent.putExtras(bundle);
						startActivity(myIntent);
					}
				} else {
					messageBoxShowRetry(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}
