package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_USERS;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class NewProjectActivity extends Activity {
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
	private int resourceID;
	/**
	 * The startweek of the project
	 */
	private int startWeek;
	/**
	 * The endweek of the project
	 */
	private int endWeek;
	/**
	 * The deadline of the project
	 */
	private int deadLine;
	/**
	 * The name of the project
	 */
	private String projectName;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newproject);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_USERS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		String projection[] = { TABLE_USERS.USERID };

		Cursor c = cr.query(uri.build(), projection, TABLE_USERS.USERNAME + " = '" + pref.getString("username", "") + "'", null, null);

		c.moveToFirst();

		resourceID = c.getInt(c.getColumnIndex(TABLE_USERS.USERID));

		Button addButton = (Button) findViewById(R.id.addProjectButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				EditText projectNameText = (EditText) findViewById(R.id.projectName);

				EditText nextreleaseText = (EditText) findViewById(R.id.nextrelease);

				if ((projectNameText.getText().toString().isEmpty())

				|| (nextreleaseText.getText().toString().isEmpty()))
					messageBoxShow("Please fill in all fields", "Error!");
				else {

					sendRequest();
				}
			}
		});

	}

	/**
	 * Called when the activity is reloaded.
	 */
	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}

	/**
	 * Called when before the activity gone on background.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * Sends a request for querying a users reservations
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

		DatePicker startWeekDate = (DatePicker) findViewById(R.id.startWeekNewProject);
		DatePicker endWeekDate = (DatePicker) findViewById(R.id.endWeekNewProject);

		EditText projectNameText = (EditText) findViewById(R.id.projectName);

		Date d = new Date();
		d.setYear(startWeekDate.getYear() - 1900);
		d.setMonth(startWeekDate.getMonth());
		d.setDate(startWeekDate.getDayOfMonth());

		Date d2 = new Date();
		d2.setYear(endWeekDate.getYear() - 1900);
		d2.setMonth(endWeekDate.getMonth());
		d2.setDate(endWeekDate.getDayOfMonth());

		startWeek = Constants.convertDateToWeek(d);
		deadLine = Constants.convertDateToWeek(d2);

		projectName = projectNameText.getText().toString();

		if (deadLine - startWeek > 24)
			endWeek = startWeek + 24;
		else
			endWeek = deadLine;

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());
		intent.putExtra("startweek", startWeek);
		intent.putExtra("endweek", endWeek);
		intent.putExtra("action", projectName);
		intent.putExtra("projectname", projectName);
		intent.putExtra("resourceid", resourceID);

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * is called when a message box with Retry and Cancel button needs to be
	 * appeared
	 * 
	 * @param message
	 *            is the message of the message box
	 * @param title
	 *            is the message box's title
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

					int[] results = intent.getIntArrayExtra("results");

					CheckBox openedStatus = (CheckBox) findViewById(R.id.openedStatus);
					EditText nextrelease = (EditText) findViewById(R.id.nextrelease);
					Spinner status = (Spinner) findViewById(R.id.statusSpinner);

					Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
					Bundle bundle = new Bundle();

					bundle.putString("ACTION", "newproject");
					bundle.putString("projectname", projectName);
					bundle.putInt("resourceid", resourceID);
					bundle.putBoolean("openedstatus", openedStatus.isChecked());
					bundle.putInt("startweek", startWeek);
					bundle.putInt("endweek", endWeek);
					bundle.putInt("deadline", deadLine);
					bundle.putString("nextrelease", nextrelease.getText().toString());
					bundle.putString("statusname", status.getSelectedItem().toString());

					bundle.putIntArray("results", results);

					myIntent.putExtras(bundle);
					startActivity(myIntent);
				} else {
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}
