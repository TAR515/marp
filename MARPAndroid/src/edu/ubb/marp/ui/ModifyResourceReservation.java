package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class ModifyResourceReservation extends Activity {
	/**
	 * The loading progress dialog
	 */
	private ProgressDialog loading;
	/**
	 * The requestid
	 */
	private long requestid;
	/**
	 * The resourceid of the user
	 */
	private int myresourceid;
	/**
	 * The resourceid of the resource which will be modified
	 */
	private int resourceID;
	/**
	 * The projectid in which the resources reservation will be modified
	 */
	private int projectID;
	/**
	 * The startweek of the project
	 */
	private int startWeek;
	/**
	 * The endweek of the project
	 */
	private int endWeek;
	/**
	 * The name of the project
	 */
	private String projectName;
	/**
	 * The booking of the resource
	 */
	private int[] booking;
	/**
	 * The start of the modify
	 */
	private int minWeek;
	/**
	 * The end of the modify
	 */
	private int maxWeek;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.modifyresourcereservation);

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

		resourceID = getIntent().getExtras().getInt("resourceid");
		projectName = getIntent().getExtras().getString("projectname");
		projectID = getIntent().getExtras().getInt("projectid");
		booking = getIntent().getExtras().getIntArray("booking");
		minWeek = getIntent().getExtras().getInt("minweek");
		maxWeek = getIntent().getExtras().getInt("maxweek");

		DatePicker startDatePicker = (DatePicker) findViewById(R.id.startweekdate);
		DatePicker endDatePicker = (DatePicker) findViewById(R.id.endweekdate);

		Date startWeekDate = Constants.convertWeekToRealDate(minWeek);
		Date endWeekDate = Constants.convertWeekToRealDate(maxWeek);

		startDatePicker.init(startWeekDate.getYear() + 1900, startWeekDate.getMonth(), startWeekDate.getDate(), null);

		endDatePicker.init(endWeekDate.getYear() + 1900, endWeekDate.getMonth(), endWeekDate.getDate(), null);

		Button addButton = (Button) findViewById(R.id.next);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				sendRequest();

			}
		});
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
	 * Send the request for querying the available resources
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

		DatePicker startDatePicker = (DatePicker) findViewById(R.id.startweekdate);
		DatePicker endDatePicker = (DatePicker) findViewById(R.id.endweekdate);

		Date startDate = new Date(startDatePicker.getYear() - 1900, startDatePicker.getMonth(), startDatePicker.getDayOfMonth());
		Date endDate = new Date(endDatePicker.getYear() - 1900, endDatePicker.getMonth(), endDatePicker.getDayOfMonth());

		startWeek = Constants.convertDateToWeek(startDate);
		endWeek = Constants.convertDateToWeek(endDate);

		if (endWeek - startWeek > 24)
			endWeek = startWeek + 24;

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());
		intent.putExtra("startweek", startWeek);
		intent.putExtra("endweek", endWeek);
		intent.putExtra("action", "update");
		intent.putExtra("projectname", projectName);
		intent.putExtra("resourceid", resourceID);

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * is called when a message box with OK button needs to be appeared
	 * 
	 * @param message
	 *            is the message box's message
	 * @param title
	 *            is the message box's title
	 */
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
	 * is called when a message box with Retry and Cancel buttons needs to be
	 * appeared
	 * 
	 * @param message
	 *            is the message of the message box
	 * @param title
	 *            is the title of the message box
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
					// finish();
					int[] results = intent.getIntArrayExtra("results");

					Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
					Bundle bundle = new Bundle();

					bundle.putString("ACTION", "update");
					bundle.putString("projectname", projectName);
					bundle.putInt("projectid", projectID);
					bundle.putInt("targetresourceid", resourceID);
					bundle.putInt("senderresourceid", myresourceid);
					bundle.putInt("startweek", startWeek);
					bundle.putInt("endweek", endWeek);
					bundle.putIntArray("results", results);

					int[] currentBooking = new int[endWeek - startWeek + 1];
					int j = 0;
					for (int i = startWeek - minWeek; i <= endWeek - minWeek; i++)
						currentBooking[j++] = booking[i];

					bundle.putIntArray("booking", currentBooking);

					myIntent.putExtras(bundle);
					startActivity(myIntent);
				} else {
					messageBoxShowRetry(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};

}
