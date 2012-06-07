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
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class NewProjectActivity2 extends Activity {
	private static final String tag = "NewProjectActivity";
	private ProgressDialog loading;
	private long requestid;

	private int myresourceid;
	private int resourceID;
	private int projectID;
	private int startWeek, endWeek, deadLine;
	private String projectName;
	private int[] booking;
	private int minWeek, maxWeek;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");

		setContentView(R.layout.newproject);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);

		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_USERS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		String projection[] = { TABLE_USERS.USERID };

		Cursor c = cr.query(uri.build(), projection, TABLE_USERS.USERNAME
				+ " = '" + pref.getString("username", "") + "'", null, null);

		c.moveToFirst();

		resourceID = c.getInt(c.getColumnIndex(TABLE_USERS.USERID));

		Button addButton = (Button) findViewById(R.id.addProjectButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText projectNameText = (EditText) findViewById(R.id.projectName);
				EditText startweekText = (EditText) findViewById(R.id.startweek);
				EditText deadlineText = (EditText) findViewById(R.id.deadline);
				EditText nextreleaseText = (EditText) findViewById(R.id.nextrelease);

				if ((projectNameText.getText().toString().isEmpty())
						|| (startweekText.getText().toString().isEmpty())
						|| (deadlineText.getText().toString().isEmpty())
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

		registerReceiver(broadcastReceiver, new IntentFilter(
				Constants.BROADCAST_ACTION));
	}
	/**
	 * Called before the activity gone on background.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}
	/**
	 * 
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending
				.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

		EditText startWeekText = (EditText) findViewById(R.id.startweek);
		EditText deadLineText = (EditText) findViewById(R.id.deadline);
		EditText projectNameText = (EditText) findViewById(R.id.projectName);

		startWeek = Integer.parseInt(startWeekText.getText().toString());
		deadLine = Integer.parseInt(deadLineText.getText().toString());
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
	 * is called when a message box with Retry and Cancel button needs to be appeared
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
	 * 
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "BroadcastReceiver");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				loading.dismiss();

				if (intent.getBooleanExtra("Successful", false)) {
					// finish();
					int[] results = intent.getIntArrayExtra("results");

					CheckBox openedStatus = (CheckBox) findViewById(R.id.openedStatus);
					EditText nextrelease = (EditText) findViewById(R.id.nextrelease);
					Spinner status = (Spinner) findViewById(R.id.statusSpinner);

					Intent myIntent = new Intent(getApplicationContext(),
							StripeActivity.class);
					Bundle bundle = new Bundle();

					bundle.putString("ACTION", "newproject");
					bundle.putString("projectname", projectName);
					bundle.putInt("resourceid", resourceID);
					bundle.putBoolean("openedstatus", openedStatus.isChecked());
					bundle.putInt("startweek", startWeek);
					bundle.putInt("endweek", endWeek);
					bundle.putInt("deadline", deadLine);
					bundle.putString("nextrelease", nextrelease.getText()
							.toString());
					bundle.putString("statusname", status.getSelectedItem()
							.toString());

					bundle.putIntArray("results", results);

					myIntent.putExtras(bundle);
					startActivity(myIntent);
				} else {
					messageBoxShow(Constants.getErrorMessage(intent
							.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}
