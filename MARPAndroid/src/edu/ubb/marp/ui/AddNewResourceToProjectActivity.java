package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_BOOKING;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddNewResourceToProjectActivity extends Activity {
	private final static String tag = "AddNewResourceToProjectActivity";

	private ProgressDialog loading;
	private int projectid;
	private long requestid;
	private int[] resourceids;
	private String[] resourcenames;
	private int myresourceid;
	private String projectName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addresource);

			projectid = getIntent().getExtras().getInt("projectid");
			projectName = getIntent().getExtras().getString("projectname");
			
			sendRequestForResources();
			
			TextView startWeekText = (TextView)findViewById(R.id.startweek);
			TextView endWeekText = (TextView)findViewById(R.id.endweek);
			
			Uri.Builder uri = new Uri.Builder();
			uri = new Uri.Builder();
			uri.authority(DatabaseContract.PROVIDER_NAME);
			uri.path(DatabaseContract.TABLE_PROJECTS);
			uri.scheme("content");

			ContentResolver cr = getContentResolver();

			String projection[] = { TABLE_PROJECTS.STARTWEEK, TABLE_PROJECTS.DEADLINE };

			Cursor c = cr.query(uri.build(), projection, TABLE_PROJECTS.PROJECTID + " = '" + projectid + "'", null, null);

			c.moveToFirst();
			
			startWeekText.setText(Integer.toString(c.getInt(c.getColumnIndex(TABLE_PROJECTS.STARTWEEK))));
			endWeekText.setText(Integer.toString(c.getInt(c.getColumnIndex(TABLE_PROJECTS.DEADLINE))));
			
			Button nextButton = (Button) findViewById(R.id.nextAddButton);
			nextButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//sendRequest();
					EditText startweekText = (EditText) findViewById(R.id.startweek);
					EditText endweekText = (EditText) findViewById(R.id.endweek);
					
					if ((startweekText.getText().toString().isEmpty())
							|| (endweekText.getText().toString().isEmpty()))
						messageBoxShow("Please fill in all fields", "Error!");
					else {
						sendRequestToAddNewResource();
					}
				}
			});
			
			final CheckBox ch = (CheckBox)findViewById(R.id.leaderCheckBox);
			
			Spinner sp = (Spinner)findViewById(R.id.resourcesSpinner);
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					String selectedResourceName = resourcenames[arg2];
					if((selectedResourceName.contains("Laptop"))||(selectedResourceName.contains("Room"))||(selectedResourceName.contains("Server")))
						ch.setEnabled(false);
					else
						ch.setEnabled(true);
				}

				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
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
	
	private void sendRequestToAddNewResource(){
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

		EditText startweekText = (EditText) findViewById(R.id.startweek);
		EditText endweekText = (EditText) findViewById(R.id.endweek);
		Spinner sp=(Spinner)findViewById(R.id.resourcesSpinner);

		int startWeek = Integer.parseInt(startweekText.getText().toString());
		int endWeek = Integer.parseInt(endweekText.getText().toString());
		//projectName = projectNameText.getText().toString();

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
	
	public void setMyResourceID(){
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
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// if(sentIntent.equals(intent)){
			Log.i(tag, "Broadcast received");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				loading.dismiss();
				if (intent.getBooleanExtra("Successful", false)) {
					if(intent.getBooleanExtra("Resources", false)){
					resourceids = intent.getIntArrayExtra("resourceid");
					resourcenames = intent.getStringArrayExtra("resourcename");
					
					Spinner sp = (Spinner)findViewById(R.id.resourcesSpinner);
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, resourcenames);
					sp.setAdapter(adapter);
					}else{
						int[] results = intent.getIntArrayExtra("results");
						
						Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
						Bundle bundle = new Bundle();
						
						EditText startweekText = (EditText) findViewById(R.id.startweek);
						EditText endweekText = (EditText) findViewById(R.id.endweek);
						CheckBox cb=(CheckBox)findViewById(R.id.leaderCheckBox);
						Spinner sp=(Spinner)findViewById(R.id.resourcesSpinner);
						
						int startWeek=Integer.parseInt(startweekText.getText().toString());
						int endWeek=Integer.parseInt(endweekText.getText().toString());
						if(endWeek-startWeek>24)
							endWeek=startWeek+24;
						
						boolean isLeader;
						if(cb.isEnabled())
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
					if(intent.getBooleanExtra("Resources", false)){
						messageBoxShowForResources(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}else{
						messageBoxShowToAddNewResource(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};
}
