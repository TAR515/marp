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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyAccountActivity extends Activity {
	private static final String tag = "MyAccountActivity";

	private Intent sentIntent;
	private long requestid;
	private ProgressDialog loading;
	private Context context;
	private String passWord;
	ArrayList<ListRecord> users = new ArrayList<ListRecord>();

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ChangePassword c = new ChangePassword(this);
		setContentView(R.layout.myaccount);
		// setContentView(c.returnView());
		// setArrayList("Vizer Arnold", "Arni00", "0742764458",
		// "vizer_arnold@yahoo.com");

		context = this;

		sendRequest(true);
	}

	private void sendRequest(boolean load) {
		if (load)
			loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(Integer.toString(Constants.QUERYUSER));
		// uri.appendPath("Projects");
		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.putExtra("self", true);
		intent.setData(uri.build());
		// sentIntent=intent;
		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
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

	public void setArrayList(String name, String username, String telephone, String email) {
		ListRecord user = new ListRecord("Name", name);
		users.add(user);
		user = new ListRecord("Username", username);
		users.add(user);
		user = new ListRecord("Telephone", telephone);
		users.add(user);
		user = new ListRecord("E-mail", email);
		users.add(user);
		user = new ListRecord("Change Password", "");
		users.add(user);
	}

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
				sendRequest(true);
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	public void editDialog(String title, String editableText, int position) {

		AlertDialog alertDialog;
		final EditText editDialog = new EditText(this);

		editDialog.setText(editableText);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(editDialog);
		final int myPosition = position;
		// final String newText = editDialog.getText().toString();
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
					intent.putExtra("ACTION", "CHANGEUSERRESOURCENAME");
					intent.putExtra("newusername", editDialog.getText().toString());

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

	public void editPasswordDialog(String title) {

		AlertDialog alertDialog;
		final ChangePassword change = new ChangePassword(this);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(change.returnView());
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				loading = ProgressDialog.show(context, "Loading", "Please wait...");

				if ((change.getNewPass1().equals(change.getNewPass2()))
						&& (change.getOldPass().equals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(
								"username", "")))) {
					Intent intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEUSERPASSWORD");
					intent.putExtra("newpassword", change.getNewPass1());
					passWord = change.getNewPass1();

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
				} else {
					messageBoxShow("The old password is not correct, or the new passwords does not match", "Error"); // TODO
				}
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
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				if (intent.getBooleanExtra("Successful", false)) {
					if (!intent.getBooleanExtra("change", false)) {						
						loading.dismiss();
						Uri.Builder uri = new Uri.Builder();
						uri = new Uri.Builder();
						uri.authority(DatabaseContract.PROVIDER_NAME);
						uri.path(DatabaseContract.TABLE_USERS);
						uri.scheme("content");

						ContentResolver cr = getContentResolver();

						Log.i(tag, "query elott");
						Cursor c = cr.query(uri.build(), null,
								TABLE_USERS.USERNAME + "='"
										+ PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", "")
										+ "'", null, null);
						Log.i(tag, "query utan");

						if (c.moveToFirst()) {
							Log.i(tag, "ifben");
							// setArrayList("Vizer Arnold", "Arni00",
							// "0742764458",
							// "vizer_arnold@yahoo.com");
							String name = c.getString(c.getColumnIndex(TABLE_USERS.USERRESOURCENAME));
							String username = c.getString(c.getColumnIndex(TABLE_USERS.USERNAME));
							String tel = c.getString(c.getColumnIndex(TABLE_USERS.USERPHONENUMBER));
							String email = c.getString(c.getColumnIndex(TABLE_USERS.USEREMAIL));
							Log.i(tag, name + " " + username + " " + tel + " " + email);

							setArrayList(name, username, tel, email);

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
					} else {
						if (intent.getBooleanExtra("changePassword", false)) {
							SharedPreferences pref = PreferenceManager
									.getDefaultSharedPreferences(getApplicationContext());
							Editor editor = pref.edit();

							editor.putString("password", passWord);

							editor.apply();
						}
						
						sendRequest(false);
					}
				} else {
					loading.dismiss();
					messageBoxShowRetry(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}