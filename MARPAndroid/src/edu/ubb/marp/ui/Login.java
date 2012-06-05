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
import android.graphics.DashPathEffect;
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
			}
		});

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if (pref.getBoolean("remember", false)) {
			CheckBox remember = (CheckBox) findViewById(R.id.rememberme);
			username.setText(pref.getString("username", ""));
			password.setText(pref.getString("password", ""));
			remember.setChecked(pref.getBoolean("remember", false));
			verify();
		} else {
			username.setText("");
		}
		
		/*Log.i("10", Constants.convertWeekToDate(10));
		Log.i("11", Constants.convertWeekToDate(11));
		Log.i("12", Constants.convertWeekToDate(12));
		Log.i("13", Constants.convertWeekToDate(13));
		Log.i("14", Constants.convertWeekToDate(14));
		Log.i("15", Constants.convertWeekToDate(15));
		Log.i("16", Constants.convertWeekToDate(16));
		Log.i("17", Constants.convertWeekToDate(17));
		Log.i("18", Constants.convertWeekToDate(18));
		Log.i("19", Constants.convertWeekToDate(19));
		Log.i("20", Constants.convertWeekToDate(20));
		Log.i("21", Constants.convertWeekToDate(21));
		Log.i("22", Constants.convertWeekToDate(22));
		Log.i("23", Constants.convertWeekToDate(23));
		Log.i("24", Constants.convertWeekToDate(24));
		Log.i("25", Constants.convertWeekToDate(25));
		Log.i("26", Constants.convertWeekToDate(26));
		Log.i("27", Constants.convertWeekToDate(27));
		Log.i("28", Constants.convertWeekToDate(28));
		Log.i("29", Constants.convertWeekToDate(29));*/

		/*
		 * Log.i("0", Constants.convertWeekToDate(0)); Log.i("1",
		 * Constants.convertWeekToDate(1)); Log.i("2",
		 * Constants.convertWeekToDate(2));
		 * 
		 * Date mydate=new Date(2007, 1, 1);
		 * 
		 * Log.i("0", Integer.toString(Constants.weeksBetween(mydate,
		 * Constants.convertWeekToDatee(0)))); Log.i("1",
		 * Integer.toString(Constants.weeksBetween(mydate,
		 * Constants.convertWeekToDatee(1)))); Log.i("2",
		 * Integer.toString(Constants.weeksBetween(mydate,
		 * Constants.convertWeekToDatee(2))));
		 * 
		 * Log.i("7", Integer.toString(Constants.weeksBetween(mydate, new
		 * Date(2007, 1, 7)))); Log.i("8",
		 * Integer.toString(Constants.weeksBetween(mydate, new Date(2007, 1,
		 * 8)))); Log.i("9", Integer.toString(Constants.weeksBetween(mydate, new
		 * Date(2007, 1, 9))));
		 */

		/*
		 * Log.i("0", Constants.convertWeekToDate(0)); Log.i("1",
		 * Constants.convertWeekToDate(1)); Log.i("2",
		 * Constants.convertWeekToDate(2)); Log.i("3",
		 * Constants.convertWeekToDate(3)); Log.i("4",
		 * Constants.convertWeekToDate(4)); Log.i("5",
		 * Constants.convertWeekToDate(5)); Log.i("6",
		 * Constants.convertWeekToDate(6)); Log.i("7",
		 * Constants.convertWeekToDate(7)); Log.i("8",
		 * Constants.convertWeekToDate(8)); Log.i("9",
		 * Constants.convertWeekToDate(9)); Log.i("10",
		 * Constants.convertWeekToDate(10)); Log.i("11",
		 * Constants.convertWeekToDate(11)); Log.i("12",
		 * Constants.convertWeekToDate(12)); Log.i("13",
		 * Constants.convertWeekToDate(13)); Log.i("14",
		 * Constants.convertWeekToDate(14)); Log.i("15",
		 * Constants.convertWeekToDate(15));
		 * 
		 * Log.i("38", Integer.toString(Constants.convertDateToWeek(new
		 * Date(2007, 3, 8)))); Log.i("39",
		 * Integer.toString(Constants.convertDateToWeek(new Date(2007, 3, 9))));
		 * Log.i("310", Integer.toString(Constants.convertDateToWeek(new
		 * Date(2007, 3, 10))));
		 */
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

	/** verifies if the password or the username is entered */
	public void verify() {
		if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
			messageBoxShow("No username or password", "Warning!");
		} else {
			/*
			 * f(username.getText().toString().equals("admin") &&
			 * (password.getText().toString().equals("admin"))){ Intent myIntent
			 * = new Intent(this, HelloTabActivity.class);
			 * this.startActivity(myIntent); }else{
			 * messageBoxShow("Wrong username or password", "Warning!"); }
			 */
			loading = ProgressDialog.show(this, "Login", "Please wait...", true);

			Uri.Builder uri = new Uri.Builder();

			Intent intent = new Intent(this, MyService.class);
			intent.putExtra("ACTION", "LOGIN");
			intent.putExtra("username", username.getText().toString().toLowerCase());
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

					SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					Editor editor = pref.edit();

					editor.putString("username", username.getText().toString().toLowerCase());
					editor.putString("password", password.getText().toString());
					editor.putBoolean("remember", remember.isChecked());

					editor.apply();

					loading.dismiss();
					Intent myIntent = new Intent(getApplicationContext(), HelloTabActivity.class);
					startActivity(myIntent);
				} else {
					loading.dismiss();
					if (intent.getIntExtra("error", 10000) == 0) {
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						if ((username.getText().toString().equals(pref.getString("username", "")))
								&& (password.getText().toString().equals(pref.getString("password", "")))) {
							Intent myIntent = new Intent(getApplicationContext(), HelloTabActivity.class);
							startActivity(myIntent);
						}else{
							messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
						}
					} else {
						messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};
}
