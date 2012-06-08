package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_PROJECTS;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class AddNewResourceToProjectActivity extends Activity {

	/**
	 * The loading progress dialog
	 */
	private ProgressDialog loading;
	/**
	 * The projectid
	 */
	private int projectid;
	/**
	 * The requestid
	 */
	private long requestid;
	/**
	 * The resourceids
	 */
	private int[] resourceids;
	/**
	 * The resourcenames
	 */
	private String[] resourcenames;
	/**
	 * The users resourceid
	 */
	private int myresourceid;
	/**
	 * The projects name
	 */
	private String projectName;

	/**
	 * Create the activity and loads the layout and resources
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addresource);

		projectid = getIntent().getExtras().getInt("projectid");
		projectName = getIntent().getExtras().getString("projectname");

		sendRequestForResources();

		DatePicker startDatePicker = (DatePicker) findViewById(R.id.startWeekDateAddResource);
		DatePicker endDatePicker = (DatePicker) findViewById(R.id.endWeekDateAddResource);

		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_PROJECTS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		String projection[] = { TABLE_PROJECTS.STARTWEEK, TABLE_PROJECTS.DEADLINE };

		Cursor c = cr.query(uri.build(), projection, TABLE_PROJECTS.PROJECTID + " = '" + projectid + "'", null, null);

		c.moveToFirst();

		Date d1 = new Date();
		Date d2 = new Date();
		d1 = Constants.convertWeekToRealDate(Integer.parseInt(Integer.toString(c.getInt(c.getColumnIndex(TABLE_PROJECTS.STARTWEEK)))));
		d2 = Constants.convertWeekToRealDate(Integer.parseInt(Integer.toString(c.getInt(c.getColumnIndex(TABLE_PROJECTS.STARTWEEK)))));
		startDatePicker.init(d1.getYear() + 1900, d1.getMonth(), d1.getDate(), null);
		endDatePicker.init(d2.getYear() + 1900, d2.getMonth(), d2.getDate(), null);

		Button nextButton = (Button) findViewById(R.id.nextAddButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				sendRequestToAddNewResource();

			}
		});

		final CheckBox ch = (CheckBox) findViewById(R.id.leaderCheckBox);

		Spinner sp = (Spinner) findViewById(R.id.resourcesSpinner);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String selectedResourceName = resourcenames[arg2];
				if ((selectedResourceName.contains("Laptop")) || (selectedResourceName.contains("Room"))
						|| (selectedResourceName.contains("Server")))
					ch.setEnabled(false);
				else
					ch.setEnabled(true);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/**
	 * this method is called when a messagebox needs to be appeared with 1
	 * button
	 * */
	public void messageBoxShow(String message, String title) {
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * this method is called when a messagebox needs to be appeared with 2
	 * buttons
	 * 
	 * @param message
	 *            Is the message of the messagebox
	 * @param title
	 *            Is the title of the messagebox
	 * 
	 */
	public void messageBoxShowForResources(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendRequestForResources();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.show();
	}

	/**
	 * This method is called when a messagebox needs to be appeared with 2
	 * buttons to add new resource
	 * 
	 * @param message
	 *            Is the message of the messagebox
	 * @param title
	 *            Is the title of the messagebox
	 */
	public void messageBoxShowToAddNewResource(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendRequestToAddNewResource();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * Sends a request for the available resources
	 */
	private void sendRequestForResources() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.LOADRESOURCES));
		uriSending.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * Sends a request for adding this new resource to a project
	 */
	private void sendRequestToAddNewResource() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

		DatePicker startDatePicker = (DatePicker) findViewById(R.id.startWeekDateAddResource);
		DatePicker endDatePicker = (DatePicker) findViewById(R.id.endWeekDateAddResource);

		Spinner sp = (Spinner) findViewById(R.id.resourcesSpinner);

		Date startDate = new Date(startDatePicker.getYear() - 1900, startDatePicker.getMonth(), startDatePicker.getDayOfMonth());
		Date endDate = new Date(endDatePicker.getYear() - 1900, endDatePicker.getMonth(), endDatePicker.getDayOfMonth());

		int startWeek = Constants.convertDateToWeek(startDate);
		int endWeek = Constants.convertDateToWeek(endDate);

		if (endWeek - startWeek > 24)
			endWeek = startWeek + 24;

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());
		intent.putExtra("startweek", startWeek);
		intent.putExtra("endweek", endWeek);
		intent.putExtra("action", "insert");
		intent.putExtra("projectname", projectName);
		intent.putExtra("resourceid", resourceids[sp.getSelectedItemPosition()]);

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * Sets the myresourceid variable with the users resourceid
	 */
	public void setMyResourceID() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_RESOURCES);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		String projection[] = { TABLE_RESOURCES.RESOURCEID };

		Cursor c = cr.query(uri.build(), projection, TABLE_RESOURCES.USERNAME + " = '" + pref.getString("username", "") + "'", null, null);

		c.moveToFirst();

		TextView toModify = (TextView) findViewById(R.id.tomodify);
		toModify.setText("Modify: " + getIntent().getExtras().getString("resourcename"));

		myresourceid = c.getInt(c.getColumnIndex(TABLE_RESOURCES.RESOURCEID));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();

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
					if (intent.getBooleanExtra("Resources", false)) {
						resourceids = intent.getIntArrayExtra("resourceid");
						resourcenames = intent.getStringArrayExtra("resourcename");

						Spinner sp = (Spinner) findViewById(R.id.resourcesSpinner);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
								android.R.layout.simple_spinner_dropdown_item, resourcenames);
						sp.setAdapter(adapter);
					} else {
						int[] results = intent.getIntArrayExtra("results");

						Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
						Bundle bundle = new Bundle();

						DatePicker startDatePicker = (DatePicker) findViewById(R.id.startWeekDateAddResource);
						DatePicker endDatePicker = (DatePicker) findViewById(R.id.endWeekDateAddResource);

						CheckBox cb = (CheckBox) findViewById(R.id.leaderCheckBox);
						Spinner sp = (Spinner) findViewById(R.id.resourcesSpinner);

						Date startDate = new Date(startDatePicker.getYear() - 1900, startDatePicker.getMonth(),
								startDatePicker.getDayOfMonth());
						Date endDate = new Date(endDatePicker.getYear() - 1900, endDatePicker.getMonth(), endDatePicker.getDayOfMonth());

						int startWeek = Constants.convertDateToWeek(startDate);
						int endWeek = Constants.convertDateToWeek(endDate);

						if (endWeek - startWeek > 24)
							endWeek = startWeek + 24;

						boolean isLeader;
						if (cb.isEnabled())
							isLeader = cb.isChecked();
						else
							isLeader = false;

						bundle.putString("ACTION", "insert");
						bundle.putInt("startweek", startWeek);
						bundle.putInt("endweek", endWeek);
						bundle.putBoolean("isleader", isLeader);
						bundle.putInt("projectid", projectid);
						bundle.putInt("senderresourceid", myresourceid);
						bundle.putInt("targetresourceid", resourceids[sp.getSelectedItemPosition()]);

						bundle.putIntArray("results", results);

						myIntent.putExtras(bundle);
						startActivity(myIntent);
					}
				} else {
					if (intent.getBooleanExtra("Resources", false)) {
						messageBoxShowForResources(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					} else {
						messageBoxShowToAddNewResource(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};
}
