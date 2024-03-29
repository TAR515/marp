package edu.ubb.marp.ui;

import java.util.ArrayList;
import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.*;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class MyAccountActivity extends Activity {

	private long requestid;
	private ProgressDialog loading;
	private Context context;
	private String passWord, userName;
	ArrayList<ListRecord> users = new ArrayList<ListRecord>();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.myaccount);

		context = this;

		if ((savedInstanceState == null) || (savedInstanceState.isEmpty()))
			sendRequest(true);
		else
			RestoreInstanceState(savedInstanceState);
	}

	/**
	 * Send a request for the users date
	 */
	private void sendRequest(boolean load) {
		if (load)
			loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(Integer.toString(Constants.QUERYUSER));

		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.putExtra("self", true);
		intent.setData(uri.build());

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong("requestid", requestid);

		outState.putString("name", users.get(0).subitem);
		outState.putString("username", users.get(1).subitem);
		outState.putString("tel", users.get(2).subitem);
		outState.putString("email", users.get(3).subitem);

		super.onSaveInstanceState(outState);

	}

	/**
	 * Restores the state of the Activity
	 */
	protected void RestoreInstanceState(Bundle savedInstanceState) {
		requestid = savedInstanceState.getLong("requestid");

		setArrayList(savedInstanceState.getString("name"), savedInstanceState.getString("username"), savedInstanceState.getString("tel"),
				savedInstanceState.getString("email"));

		refresh();
	}

	/**
	 * is called when the list needs to be loaded
	 * 
	 * @param name
	 *            name of the user
	 * @param username
	 *            is the user name of the user
	 * @param telephone
	 *            is the telephone number of the user
	 * @param email
	 *            is the e-mail of the user
	 */
	public void setArrayList(String name, String username, String telephone, String email) {
		users = new ArrayList<ListRecord>();
		ListRecord user = new ListRecord("Name", name);
		users.add(0, user);
		user = new ListRecord("Username", username);
		users.add(1, user);
		user = new ListRecord("Telephone", telephone);
		users.add(2, user);
		user = new ListRecord("E-mail", email);
		users.add(3, user);
		user = new ListRecord("Change Password", "");
		users.add(4, user);
	}

	/**
	 * Refreshes the listitems
	 */
	public void refresh() {
		ListView listView = (ListView) findViewById(R.id.ListViewId);
		listView.setAdapter(new ListItemAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, users));
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				if (pos < 4) {
					editDialog("Change" + " " + users.get(pos).getItem(), users.get(pos).getSubitem(), pos);
				} else {
					if (pos == 4) {
						editPasswordDialog("Change Password");
					}
				}
				return true;
			}
		});
	}

	/**
	 * is called when a message box with OK button needs to be appeared
	 * 
	 * @param message
	 *            is the message of the message box
	 * @param title
	 *            is the title of the message box
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
	 * is called when a message box with Retry and Cancel Button needs to be
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
				sendRequest(true);
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * is called when the user want to change his name, user name, telephone
	 * number or e-mail
	 * 
	 * @param title
	 *            is the title of the edit box
	 * @param editableText
	 *            is the text which will be edited
	 * @param position
	 *            is the position of the clicked item on the list
	 */
	public void editDialog(String title, String editableText, int position) {

		AlertDialog alertDialog;
		final EditText editDialog = new EditText(this);

		editDialog.setText(editableText);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(editDialog);
		final int myPosition = position;

		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent intent;
				loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 0: // Name
					intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEUSERRESOURCENAME");
					intent.putExtra("newresourcename", editDialog.getText().toString());

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
				case 1: // Username
					intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEUSERNAME");
					intent.putExtra("newusername", editDialog.getText().toString());
					userName = editDialog.getText().toString();

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
				case 2: // Telephone
					intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEUSERPHONENUMBER");
					intent.putExtra("newphonenumber", editDialog.getText().toString());

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
				case 3: // E-mail
					intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEUSEREMAIL");
					intent.putExtra("newemail", editDialog.getText().toString());

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
				}
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * is called when the user want to change his password
	 * 
	 * @param title
	 *            is the title of the edit box
	 */
	public void editPasswordDialog(String title) {

		AlertDialog alertDialog;
		final ChangePassword change = new ChangePassword(this);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(change.returnView());
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if ((change.getNewPass1().equals(change.getNewPass2()))
						&& (change.getOldPass().equals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(
								"password", "")))) {
					loading = ProgressDialog.show(context, "Loading", "Please wait...");

					Intent intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEUSERPASSWORD");
					intent.putExtra("newpassword", change.getNewPass1());
					passWord = change.getNewPass1();

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
				} else {
					messageBoxShow("The old password is not correct, or the new passwords does not match", "Error");
				}
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * Queries the users data from the database
	 */
	private void queryData() {
		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_USERS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		Cursor c = cr.query(
				uri.build(),
				null,
				TABLE_USERS.USERNAME + "='"
						+ PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", "") + "'", null,
				null);

		if (c.moveToFirst()) {
			String name = c.getString(c.getColumnIndex(TABLE_USERS.USERRESOURCENAME));
			String username = c.getString(c.getColumnIndex(TABLE_USERS.USERNAME));
			String tel = c.getString(c.getColumnIndex(TABLE_USERS.USERPHONENUMBER));
			String email = c.getString(c.getColumnIndex(TABLE_USERS.USEREMAIL));

			setArrayList(name, username, tel, email);

			refresh();
		}
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
				if (intent.getBooleanExtra("Successful", false)) {
					if (!intent.getBooleanExtra("change", false)) {
						loading.dismiss();

						queryData();
					} else {
						if (intent.getBooleanExtra("changePassword", false)) {
							SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							Editor editor = pref.edit();

							editor.putString("password", passWord);

							editor.apply();
						}
						if (intent.getBooleanExtra("changeUsername", false)) {
							SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							Editor editor = pref.edit();

							editor.putString("username", userName);

							editor.apply();
						}

						sendRequest(false);
					}
				} else {
					loading.dismiss();
					if ((!intent.getBooleanExtra("change", false)) && (intent.getIntExtra("error", 10000) == 0)) {
						queryData();
					} else if (intent.getBooleanExtra("change", false))
						messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					else
						messageBoxShowRetry(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}