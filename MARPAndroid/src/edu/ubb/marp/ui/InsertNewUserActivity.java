package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.network.MyService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class InsertNewUserActivity extends Activity {
	private static final String tag = "InsertNewUserActivity";
	private ProgressDialog loading;
	private long requestid;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newuser);

		Button button = (Button) findViewById(R.id.addUserButton);
		button.setOnClickListener(new View.OnClickListener() {
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
	 * Send the reqeust for inserting the new user
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		EditText userName = (EditText) findViewById(R.id.userName);
		EditText password = (EditText) findViewById(R.id.password);
		EditText phonenumber = (EditText) findViewById(R.id.phonenumber);
		EditText email = (EditText) findViewById(R.id.email);
		EditText resourcename = (EditText) findViewById(R.id.resourcename);
		CheckBox activeCheck = (CheckBox) findViewById(R.id.activeCheckBoxUser);

		if ((userName.getText().toString().isEmpty()) || (password.getText().toString().isEmpty())
				|| (phonenumber.getText().toString().isEmpty()) || (email.getText().toString().isEmpty())
				|| (resourcename.getText().toString().isEmpty()))
			messageBoxShow("Please fill in all fields", "Error!");
		else {

			Uri.Builder uriSending = new Uri.Builder();
			uriSending.authority(DatabaseContract.PROVIDER_NAME);
			uriSending.path(Integer.toString(Constants.INSERTNEWUSER));
			uriSending.scheme("content");

			Intent intent = new Intent(this, MyService.class);
			intent.putExtra("ACTION", "USERMODIFICATIONS");
			intent.setData(uriSending.build());
			intent.putExtra("targetusername", userName.getText().toString());
			intent.putExtra("targetpassword", password.getText().toString());
			intent.putExtra("phonenumber", phonenumber.getText().toString());
			intent.putExtra("email", email.getText().toString());
			intent.putExtra("resourcename", resourcename.getText().toString());
			intent.putExtra("active", activeCheck.isChecked());

			requestid = new Date().getTime();
			intent.putExtra("requestid", requestid);
			startService(intent);
		}
	}

	/**
	 * is called when a message box needs to be appeared with 2 buttons
	 * 
	 * @param message
	 *            is the message is the message box
	 * @param title
	 *            is the title of the message box
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
					finish();
				} else {
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}
