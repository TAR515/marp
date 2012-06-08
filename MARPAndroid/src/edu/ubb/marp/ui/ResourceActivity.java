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
import android.content.ActivityNotFoundException;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class ResourceActivity extends Activity {

	/**
	 * The requestid
	 */
	private long requestid;
	/**
	 * The username of the resource
	 */
	private String targetUserName;
	/**
	 * The loading progress dialog
	 */
	private ProgressDialog loading;

	/**
	 * The list of the elements
	 */
	ArrayList<ListRecord> users = new ArrayList<ListRecord>();

	/**
	 * Called when the activity is first created.
	 * */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount);

		sendRequest();
	}

	/**
	 * Sends a request for querying the users data
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(Integer.toString(Constants.QUERYUSER));

		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.putExtra("self", false);
		targetUserName = getIntent().getExtras().getString("username");
		intent.putExtra("targetusername", targetUserName);
		intent.setData(uri.build());

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * the method starts a phone call to the given phone number
	 * 
	 * @param number
	 *            is the phone number
	 */
	private void call(String number) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + number));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			Log.e("Android dial", "Call failed", e);
		}
	}

	/**
	 * Called when the activity is reloaded
	 */
	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}

	/**
	 * Called before the activity goes on background
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * called when the activity starts and load the list view with the user
	 * details
	 * 
	 * @param name
	 *            is the name of the user
	 * @param username
	 *            is the user name of the user
	 * @param telephone
	 *            is the telephone number of the user
	 * @param email
	 *            is the e-mail of the user
	 */
	public void setArrayList(String name, String username, String telephone, String email) {
		ListRecord user = new ListRecord("Name", name);
		users.add(user);
		user = new ListRecord("Username", username);
		users.add(user);
		user = new ListRecord("Telephone", telephone);
		users.add(user);
		user = new ListRecord("E-mail", email);
		users.add(user);
	}

	/**
	 * Called when a message box needs to bee appeared
	 * 
	 * @param message
	 *            is the message of the Message Box
	 * @param title
	 *            is the title of the Message Box
	 */
	public void messageBoxShow(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * Called when the user long clicked on the telephone list item, to dial a
	 * human resource
	 * 
	 * @param message
	 *            is the message of the Message Box
	 * @param title
	 *            is the title of the Message Box
	 * @param number
	 *            is the phone number of the resource which will be dialed
	 */
	private void callmessageBox(String message, String title, String number) {
		AlertDialog alertDialog;
		final String n = number;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Call", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				call(n);
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
				if (intent.getBooleanExtra("Successful", false)) {
					loading.dismiss();
					Uri.Builder uri = new Uri.Builder();
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(DatabaseContract.TABLE_USERS);
					uri.scheme("content");

					ContentResolver cr = getContentResolver();

					Cursor c = cr.query(uri.build(), null, TABLE_USERS.USERNAME + "='" + targetUserName + "'", null, null);

					if (c.moveToFirst()) {
						String name = c.getString(c.getColumnIndex(TABLE_USERS.USERRESOURCENAME));
						String username = c.getString(c.getColumnIndex(TABLE_USERS.USERNAME));
						String tel = c.getString(c.getColumnIndex(TABLE_USERS.USERPHONENUMBER));
						String email = c.getString(c.getColumnIndex(TABLE_USERS.USEREMAIL));

						setArrayList(name, username, tel, email);

						ListView listView = (ListView) findViewById(R.id.ListViewId);
						listView.setAdapter(new ListItemAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, users));
						listView.setOnItemLongClickListener(new OnItemLongClickListener() {

							public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

								if (pos == 2) {
									ListRecord valami = users.get(2);
									callmessageBox("Are you sure to call this User?", "Call", valami.subitem);

								} else {
									if (pos == 3) {
										Intent i = new Intent(Intent.ACTION_SEND);

										i.setType("text/plain");
										i.putExtra(Intent.EXTRA_EMAIL, new String[] { users.get(3).subitem });
										i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
										i.putExtra(Intent.EXTRA_TEXT, "body of email");
										try {
											startActivity(Intent.createChooser(i, "Send mail..."));
										} catch (android.content.ActivityNotFoundException ex) {
											Toast.makeText(ResourceActivity.this, "There are no email clients installed.",
													Toast.LENGTH_SHORT).show();
										}
									}
								}
								return true;
							}
						});
					}
				} else {
					loading.dismiss();
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}