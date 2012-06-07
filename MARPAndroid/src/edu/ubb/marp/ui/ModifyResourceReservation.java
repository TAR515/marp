package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_RESOURCES;
import edu.ubb.marp.database.DatabaseContract.TABLE_USERS;
import edu.ubb.marp.network.MyService;
import android.R.integer;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

public class ModifyResourceReservation extends Activity {
	private static final String tag = "ModifyResourceReservation";
	private ProgressDialog loading;
	private long requestid;
	private int myresourceid;
	private int resourceID;
	private int projectID;
	private int startWeek, endWeek;
	private String projectName;
	private int[] booking;
	private int minWeek, maxWeek;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");

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
		
		TextView toModify = (TextView)findViewById(R.id.tomodify);
		toModify.setText("Modify: "+getIntent().getExtras().getString("resourcename"));

		myresourceid = c.getInt(c.getColumnIndex(TABLE_RESOURCES.RESOURCEID));

		resourceID = getIntent().getExtras().getInt("resourceid");
		projectName = getIntent().getExtras().getString("projectname");
		projectID = getIntent().getExtras().getInt("projectid");
		booking = getIntent().getExtras().getIntArray("booking");
		minWeek = getIntent().getExtras().getInt("minweek");
		maxWeek = getIntent().getExtras().getInt("maxweek");

		//EditText startweek = (EditText) findViewById(R.id.startweekMod);
		//EditText endweek = (EditText) findViewById(R.id.endweekMod);
		
		DatePicker startDatePicker = (DatePicker) findViewById(R.id.startweekdate);
		DatePicker endDatePicker = (DatePicker) findViewById(R.id.endweekdate);
		
	//	Date startDate = Constants.convertWeekToRealDate2(minWeek);
	//	Date endDate = Constants.convertWeekToRealDate2(maxWeek);
		
		Log.i("Start Date min", ""+ minWeek);
		Log.i("End Date max", ""+ maxWeek);
		
		//Log.i("Start Date", ""+ startDate);		
		//Log.i("End Date", ""+ endDate);
		
		Date startWeekDate = Constants.convertWeekToRealDate(minWeek);
		Date endWeekDate = Constants.convertWeekToRealDate(maxWeek);
		
		//Log.i("Start Date", ""+ startWeekString);		
		//Log.i("End Date", ""+ endWeekString);
		
		//String startWeekElements[] = startWeekString.split("\\.");
		//String endWeekElements[] = endWeekString.split("\\.");
		
		//Log.i("Start Date", ""+ Constants.convertWeekToDate(minWeek));
		//Log.i("End Date", ""+ Constants.convertWeekToDate(maxWeek));
		//Log.i("Start Date Year", ""+ Integer.parseInt(startWeekElements[0]));	
		//Log.i("Start Date Month", ""+ Integer.parseInt(startWeekElements[1]));	
		//Log.i("Start Date Day", ""+ Integer.parseInt(startWeekElements[2]));	
		//startDatePicker.init(startDate.getYear(), startDate.getMonth(), startDate.getDay(), null);

		//startDatePicker.init(Integer.parseInt(startWeekElements[0]), Integer.parseInt(startWeekElements[1]), Integer.parseInt(startWeekElements[2]), null);
		startDatePicker.init(startWeekDate.getYear()+1900, startWeekDate.getMonth(), startWeekDate.getDate(), null);

		//endDatePicker.init(Integer.parseInt(endWeekElements[0]), Integer.parseInt(endWeekElements[1]),Integer.parseInt(endWeekElements[2]),null);
		endDatePicker.init(endWeekDate.getYear()+1900, endWeekDate.getMonth(), endWeekDate.getDate(), null);

		//startweek.setText(Integer.toString(minWeek));
		//endweek.setText(Integer.toString(maxWeek));

		Button addButton = (Button) findViewById(R.id.next);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// sendRequest();
				/*EditText startweek = (EditText) findViewById(R.id.startweekMod);
				EditText endweek = (EditText) findViewById(R.id.endweekMod);

				if ((startweek.getText().toString().isEmpty()) || (endweek.getText().toString().isEmpty()))
					messageBoxShow("Please fill in all fields", "Error!");
				else {
					/*
					 * Intent myIntent = new Intent(getApplicationContext(),
					 * StripeActivity.class); Bundle bundle = new Bundle();
					 * 
					 * int startWeek =
					 * Integer.parseInt(startweek.getText().toString()); int
					 * endWeek = Integer.parseInt(endweek.getText().toString());
					 * ; if (endWeek - startWeek > 24) endWeek = startWeek + 24;
					 * 
					 * bundle.putString("ACTION", "MODRESOURCERESERVATION");
					 * bundle.putString("projectname", projectName);
					 * bundle.putInt("projectid", projectID);
					 * bundle.putInt("targetresourceid", resourceID);
					 * bundle.putInt("senderresourceid", myresourceid);
					 * bundle.putString("startweek",
					 * startweek.getText().toString());
					 * bundle.putString("endweek", Integer.toString(endWeek));
					 * 
					 * myIntent.putExtras(bundle); startActivity(myIntent);
					 */
					sendRequest();
				//}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

	//	EditText startWeekText = (EditText) findViewById(R.id.startweekMod);
	//	EditText endWeekText = (EditText) findViewById(R.id.endweekMod);

		DatePicker startDatePicker = (DatePicker) findViewById(R.id.startweekdate);
		DatePicker endDatePicker = (DatePicker) findViewById(R.id.endweekdate);
	
		Date startDate = new Date(startDatePicker.getYear()-1900,startDatePicker.getMonth(),startDatePicker.getDayOfMonth());
		Date endDate = new Date(endDatePicker.getYear()-1900,endDatePicker.getMonth(),endDatePicker.getDayOfMonth());
		

		Log.i("Start Date", ""+ startDate);
		Log.i("End Date", ""+ endDate);
		
		startWeek = Constants.convertDateToWeek(startDate);
		endWeek = Constants.convertDateToWeek(endDate);
		
		Log.i("StartWeek", ""+startWeek);
		Log.i("EndWeek", ""+ endWeek);
		
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

	/** this method is called when a messagebox needs to be appered */
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

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "BroadcastReceiver");
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
