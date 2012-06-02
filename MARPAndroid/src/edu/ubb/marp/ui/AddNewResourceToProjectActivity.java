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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewResourceToProjectActivity extends Activity {
	private final static String tag = "AddNewResourceToProjectActivity";

	private ProgressDialog loading;
	private int projectid;
	private long requestid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addresource);

			projectid = getIntent().getExtras().getInt("projectid");
			
			sendRequest();
			
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
			
			startWeekText.setText(c.getInt(c.getColumnIndex(TABLE_PROJECTS.STARTWEEK)));
			endWeekText.setText(c.getInt(c.getColumnIndex(TABLE_PROJECTS.DEADLINE)));
			
			Button nextButton = (Button) findViewById(R.id.next);
			nextButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//sendRequest();
					EditText startweekText = (EditText) findViewById(R.id.startweek);
					EditText endweekText = (EditText) findViewById(R.id.endweek);
					
					if ((startweekText.getText().toString().isEmpty())
							|| (endweekText.getText().toString().isEmpty()))
						messageBoxShow("Please fill in all fields", "Error!");
					else {
					
					/*Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
					Bundle bundle = new Bundle();
					
					int startWeek=Integer.parseInt(startweek.getText().toString());
					int endWeek;
					int deadLine=Integer.parseInt(deadline.getText().toString());
					if(deadLine-startWeek>24)
						endWeek=startWeek+24;
					else
						endWeek=deadLine;
					
					bundle.putString("ACTION", "NEWPROJECT");
					bundle.putString("projectname", projectName.getText().toString());
					bundle.putBoolean("openedstatus", openedStatus.isChecked());
					bundle.putString("startweek", startweek.getText().toString());
					bundle.putString("endweek", Integer.toString(endWeek));
					bundle.putString("deadline", deadline.getText().toString());
					bundle.putString("nextrelease", nextrelease.getText().toString());
					bundle.putString("statusname", status.getSelectedItem().toString());

					myIntent.putExtras(bundle);
					startActivity(myIntent);*/
						
						//sendRequest();
					}
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

	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");
		//TODO kuldjon el egy kerest, hogy milyen resourceok vannak, es majd toltse fel a spinner-t
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
				if (intent.getBooleanExtra("Successful", false)) {

					
				} else {
					loading.dismiss();
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}
