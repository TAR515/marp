package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.network.MyService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class NewProjectActivity extends Activity {
	private static final String tag = "NewProjectActivity";
	private ProgressDialog loading;
	private long requestid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");

		setContentView(R.layout.newproject);

		Button addButton = (Button) findViewById(R.id.addProjectButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//sendRequest();
				EditText projectName = (EditText) findViewById(R.id.projectName);
				CheckBox openedStatus = (CheckBox) findViewById(R.id.openedStatus);
				EditText startweek = (EditText) findViewById(R.id.startweek);
				EditText deadline = (EditText) findViewById(R.id.deadline);
				EditText nextrelease = (EditText) findViewById(R.id.nextrelease);
				Spinner status = (Spinner) findViewById(R.id.statusSpinner);
				
				if ((projectName.getText().toString().isEmpty())
						|| (startweek.getText().toString().isEmpty())
						|| (deadline.getText().toString().isEmpty())
						|| (nextrelease.getText().toString().isEmpty()))
					messageBoxShow("Please fill in all fields", "Error!");
				else {
				
				Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
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
				startActivity(myIntent);
				}
			}
		});
	}

	/*@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(
				Constants.BROADCAST_ACTION));
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	public void sendRequest() {
		EditText projectName = (EditText) findViewById(R.id.projectName);
		CheckBox openedStatus = (CheckBox) findViewById(R.id.openedStatus);
		EditText startweek = (EditText) findViewById(R.id.startweek);
		EditText deadline = (EditText) findViewById(R.id.deadline);
		EditText nextrelease = (EditText) findViewById(R.id.nextrelease);
		Spinner status = (Spinner) findViewById(R.id.statusSpinner);

		if ((projectName.getText().toString().isEmpty())
				|| (startweek.getText().toString().isEmpty())
				|| (deadline.getText().toString().isEmpty())
				|| (nextrelease.getText().toString().isEmpty()))
			messageBoxShow("Please fill in all fields", "Error!");
		else {
			loading = ProgressDialog.show(this, "Login",
					"Please wait...", true);

			Uri.Builder uri = new Uri.Builder();

			Intent intent = new Intent(this, MyService.class);
			intent.putExtra("ACTION", "NEWPROJECT");
			intent.putExtra("projectname", projectName.getText().toString());
			intent.putExtra("openedstatus", openedStatus.isChecked());
			intent.putExtra("startweek", startweek.getText().toString());
			intent.putExtra("deadline", deadline.getText().toString());
			intent.putExtra("nextrelease", nextrelease.getText().toString());
			intent.putExtra("statusname", status.getSelectedItem().toString());
			intent.setData(uri.build());

			requestid = new Date().getTime();
			intent.putExtra("requestid", requestid);

			startService(intent);
		}
	}*/

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

	/*private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "BroadcastReceiver");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				loading.dismiss();
				if (intent.getBooleanExtra("Successful", false)) {
					//finish();
					Intent myIntent = new Intent(getApplicationContext(),
							StripeActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("projectid", ids[position]);
					myIntent.putExtras(bundle);
					startActivity(myIntent);
				} else {
					messageBoxShow(Constants.getErrorMessage(intent
							.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};*/
}
