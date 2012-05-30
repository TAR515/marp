package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.Constants.ACTIONS;
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

public class Login extends Activity {
	/** Called when the activity is first created. */
	// private Intent sentIntent;
	private long requestid;
	private final static String tag = "LoginActivity";

	private ProgressDialog loading;

	/** Username text field */
	EditText username;
	/** Password text field */
	EditText password;
	/** Login Button */
	Button login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Log.i(tag, "onCreate");

		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		login = (Button) findViewById(R.id.button1);

		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				verify();
				/*
				 * Intent myIntent = new Intent(getApplicationContext(),
				 * HelloTabActivity.class); startActivity(myIntent);
				 */
			}
		});

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (pref.getBoolean("remember", false)) {
			CheckBox remember = (CheckBox) findViewById(R.id.rememberme);
			username.setText(pref.getString("username", ""));
			password.setText(pref.getString("password", ""));
			remember.setChecked(pref.getBoolean("remember", false));
			verify();
		} else {
			username.setText("");
		}
	}

	@Override
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

	/** verifies if the password or the username is entered */
	public void verify() {
		if (username.getText().toString().equals("")
				|| password.getText().toString().equals("")) {
			messageBoxShow("No username or password", "Warning!");
		} else {
			/*
			 * f(username.getText().toString().equals("admin") &&
			 * (password.getText().toString().equals("admin"))){ Intent myIntent
			 * = new Intent(this, HelloTabActivity.class);
			 * this.startActivity(myIntent); }else{
			 * messageBoxShow("Wrong username or password", "Warning!"); }
			 */
			loading = ProgressDialog
					.show(this, "Login", "Please wait...", true);

			Uri.Builder uri = new Uri.Builder();

			Intent intent = new Intent(this, MyService.class);
			intent.putExtra("ACTION", "LOGIN");
			intent.putExtra("username", username.getText().toString());
			intent.putExtra("password", password.getText().toString());
			intent.setData(uri.build());
			// sentIntent =intent;
			requestid = new Date().getTime();
			intent.putExtra("requestid", requestid);

			startService(intent);
		}
	}

	/** this method is called when a messagebox needs to be appered */
	public void messageBoxShow(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				verify();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				username.setText("");
				password.setText("");
			}
		});
		alertDialog.show();
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "BroadcastReceiver");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				// if(sentIntent.equals((Intent)intent.getParcelableExtra("originalIntent"))){
				if (intent.getBooleanExtra("Successful", false)) {
					CheckBox remember = (CheckBox) findViewById(R.id.rememberme);

					SharedPreferences pref = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					Editor editor = pref.edit();

					editor.putString("username", username.getText().toString());
					editor.putString("password", password.getText().toString());
					editor.putBoolean("remember", remember.isChecked());

					editor.apply();

					loading.dismiss();
					Intent myIntent = new Intent(getApplicationContext(),
							HelloTabActivity.class);
					startActivity(myIntent);
				} else {
					loading.dismiss();
					SharedPreferences pref = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					if ((username.getText().toString().equals(pref.getString(
							"username", "")))
							&& (password.getText().toString().equals(pref
									.getString("password", "")))) {
						Intent myIntent = new Intent(getApplicationContext(),
								HelloTabActivity.class);
						startActivity(myIntent);
					} else {
						messageBoxShow(Constants.getErrorMessage(intent
								.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};
}
