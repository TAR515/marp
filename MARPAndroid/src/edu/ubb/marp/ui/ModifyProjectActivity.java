package edu.ubb.marp.ui;

import java.util.ArrayList;
import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_PROJECTS;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Spinner;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class ModifyProjectActivity extends Activity {

	/**
	 * The requestID
	 */
	private long requestid;
	/**
	 * The loading progress dialog
	 */
	private ProgressDialog loading;
	/**
	 * The context in which the application is running
	 */
	private Context context;
	/**
	 * The projectid which will be updated
	 */
	private int projectID;
	/**
	 * The name of the project
	 */
	private String projectName;
	/**
	 * The next release of the projec
	 */
	private String nextRelease;
	/**
	 * The current status of the project
	 */
	private String currentStatus;
	/**
	 * Is opened the project
	 */
	private int openedStatus;
	/**
	 * The deadline of the project
	 */
	private int deadLine;

	ArrayList<ListRecord> listItems = new ArrayList<ListRecord>();

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.myaccount);

		context = this;

		projectID = getIntent().getExtras().getInt("projectid");

		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_PROJECTS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		Cursor c = cr.query(uri.build(), null, TABLE_PROJECTS.PROJECTID + " = " + Integer.toString(projectID), null, null);

		if (c.moveToFirst()) {
			projectName = c.getString(c.getColumnIndex(TABLE_PROJECTS.PROJECTNAME));
			openedStatus = c.getInt(c.getColumnIndex(TABLE_PROJECTS.OPENEDSTATUS));
			deadLine = c.getInt(c.getColumnIndex(TABLE_PROJECTS.DEADLINE));
			nextRelease = c.getString(c.getColumnIndex(TABLE_PROJECTS.NEXTRELEASE));
			currentStatus = c.getString(c.getColumnIndex(TABLE_PROJECTS.STATUSNAME));

			setListItems(projectName, openedStatus, deadLine, nextRelease, currentStatus);
		}
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
	 * 
	 * @param name
	 *            name of the project
	 * @param openedStatus
	 *            is the status of the project, example Opened or Closed
	 * @param deadline
	 *            is the date when the project ends
	 * @param nextReliese
	 *            is the version number of the project
	 * @param currentStatus
	 *            is the current status of the project
	 */
	private void setListItems(String name, int openedStatus, int deadline, String nextReliese, String currentStatus) {
		listItems = new ArrayList<ListRecord>();

		ListRecord item = new ListRecord("Name", name);
		listItems.add(item);

		if (openedStatus == 1)
			item = new ListRecord("Opened Status", "Opened");
		else
			item = new ListRecord("Opened Status", "Closed");
		listItems.add(item);

		item = new ListRecord("Deadline", Constants.convertWeekToDate(deadline));
		listItems.add(item);

		item = new ListRecord("NextRelease", nextReliese);
		listItems.add(item);

		item = new ListRecord("Current Status", currentStatus);
		listItems.add(item);

		ListView listView = (ListView) findViewById(R.id.ListViewId);
		listView.setAdapter(new ListItemAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listItems));
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

				if (pos == 0 || pos == 3) {
					editDialog("Change " + listItems.get(pos).getItem(), listItems.get(pos).getSubitem(), pos);
				} else {
					if (pos == 1) {
						editOpenedStatus("Change" + listItems.get(pos).getItem(), listItems.get(pos).getSubitem(), pos);
					} else {
						if (pos == 2) {
							editDeadline("Change" + listItems.get(pos).getItem(), listItems.get(pos).getSubitem(), pos);
						} else {
							if (pos == 4) {
								editCurrentStatus("Change" + listItems.get(pos).getItem(), pos);
							}
						}
					}
				}
				return true;
			}
		});
	}

	/**
	 * 
	 * @param title
	 *            title of the edit box
	 * @param editableText
	 *            is the text, which will be edited
	 * @param position
	 *            is the position of the clicked list item
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

				Uri.Builder uri;
				switch (myPosition) {
				case 0: // Name
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(Integer.toString(Constants.CHANGEPROJECTNAME));
					uri.scheme("content");

					intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEPROJECT");
					intent.setData(uri.build());
					projectName = editDialog.getText().toString();
					intent.putExtra("newprojectname", projectName);
					intent.putExtra("projectid", projectID);

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
				case 3: // NextRelease
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(Integer.toString(Constants.CHANGEPROJECTNEXTRELEASE));
					uri.scheme("content");

					intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEPROJECT");
					intent.setData(uri.build());
					nextRelease = editDialog.getText().toString();
					intent.putExtra("newnextrelease", nextRelease);
					intent.putExtra("projectid", projectID);

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
	 * 
	 * @param title
	 *            is the title of the message box
	 * @param status
	 *            is the status of the project, which is Opened or Closed
	 * @param position
	 *            is the position of the list item
	 */
	public void editOpenedStatus(String title, String status, int position) {

		AlertDialog alertDialog;

		final CheckBox check = new CheckBox(this);
		if (status == "Opened") {
			check.setChecked(true);
		}
		check.setText("Opened");
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(check);
		final int myPosition = position;
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 1: // Opened Status
					Uri.Builder uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(Integer.toString(Constants.CHANGEPROJECTOPENEDSTATUS));
					uri.scheme("content");

					Intent intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEPROJECT");
					intent.setData(uri.build());
					if (check.isChecked())
						openedStatus = 1;
					else
						openedStatus = 0;
					intent.putExtra("openedstatus", check.isChecked());
					intent.putExtra("projectid", projectID);

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
	 * 
	 * @param title
	 *            is the message box title
	 * @param date
	 *            is the deadline date of the project
	 * @param position
	 *            is the position of the clicked list item
	 */
	public void editDeadline(String title, String date, int position) {

		AlertDialog alertDialog;
		final DatePicker d = new DatePicker(this);
		String deadline[] = date.split("\\.");
		d.init(Integer.parseInt(deadline[0]), Integer.parseInt(deadline[1]) - 1, Integer.parseInt(deadline[2]), null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(d);
		final int myPosition = position;

		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 2: // Deadline
					Uri.Builder uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(Integer.toString(Constants.CHANGEPROJECTDEADLINE));
					uri.scheme("content");

					Intent intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEPROJECT");
					intent.setData(uri.build());
					Date selectedDate = new Date(d.getYear() - 1900, d.getMonth(), d.getDayOfMonth());
					deadLine = Constants.convertDateToWeek(selectedDate);
					intent.putExtra("newdeadline", deadLine);
					intent.putExtra("projectid", projectID);

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
	 * 
	 * @param title
	 *            is the message box title
	 * @param position
	 *            is the position of the clicked list item
	 */
	public void editCurrentStatus(String title, int position) {
		String s[] = { "Accepted/Ready to Start", "Delivered", "Done", "Redy for delivery", "Specification", "Testing",
				"Under developement" };

		final Spinner spinner = new Spinner(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, s);
		spinner.setAdapter(adapter);

		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(spinner);
		final int myPosition = position;

		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 4: // Deadline
					Uri.Builder uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(Integer.toString(Constants.CHANGEPROJECTCURRENTSTATUS));
					uri.scheme("content");

					Intent intent = new Intent(getApplicationContext(), MyService.class);
					intent.putExtra("ACTION", "CHANGEPROJECT");
					intent.setData(uri.build());
					currentStatus = spinner.getSelectedItem().toString();
					intent.putExtra("newcurrentstatus", currentStatus);

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);
					intent.putExtra("projectid", projectID);

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
	 * @param message
	 *            is the message of the Message Box
	 * @param title
	 *            is the message box's title
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
					setListItems(projectName, openedStatus, deadLine, nextRelease, currentStatus);
				} else {
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}
