package edu.ubb.marp.ui;

import java.lang.reflect.Modifier;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class ResourcesActivity extends Activity {

	private final static String tag = "ResourcesActivity";

	private ProgressDialog loading;

	private long requestid;
	private String projectid;
	private String projectName;
	private boolean isLeader;
	private int resourceIDs[];
	private int numberOfBroadcasts;
	private int minWeek;
	private boolean leaved;

	private boolean[] isUser;
	protected int column;
	protected int row;

	protected String[][] data;

	/**
	 * Called when the activity is first created.
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(tag, "onCreate");

		leaved = false;

		if ((savedInstanceState == null) || (savedInstanceState.isEmpty())) {
			projectid = getIntent().getExtras().getString("projectid");
			projectName = getIntent().getExtras().getString("projectname");
			isLeader = getIntent().getExtras().getBoolean("isleader");

			sendRequest();
		} else
			RestoreInstanceState(savedInstanceState);
	}

	/**
	 * 
	 */
	private void sendRequest() {

		numberOfBroadcasts = 0;

		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path("2");

		uri.appendPath(projectid);
		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.setData(uri.build());

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);

	}

	/**
	 * called when the activity is reloaded
	 */
	@Override
	protected void onStart() {
		super.onStart();

		if (leaved) {
			leaved = false;
			sendRequest();
		}

		registerReceiver(broadcastReceiver, new IntentFilter(
				Constants.BROADCAST_ACTION));
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
	 * Called when the user click on the menu button
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isLeader) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.resources, menu);
		}
		return true;
	}

	/**
	 * Called when the user select one of the menu items
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent myIntent;
		Bundle bundle;
		switch (item.getItemId()) {
		case R.id.newid:
			leaved = true;

			myIntent = new Intent(getApplicationContext(),
					AddNewResourceToProjectActivity.class);
			bundle = new Bundle();
			bundle.putInt("projectid", Integer.parseInt(projectid));
			bundle.putString("projectname", projectName);

			myIntent.putExtras(bundle);
			startActivity(myIntent);
			return true;
		case R.id.modifyProject:
			myIntent = new Intent(getApplicationContext(),
					ModifyProjectActivity.class);
			bundle = new Bundle();
			bundle.putInt("projectid", Integer.parseInt(projectid));
			bundle.putString("projectname", projectName);

			myIntent.putExtras(bundle);
			startActivity(myIntent);
			return true;
		case R.id.refreshResources:
			sendRequest();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Called when the user rotate the screen and save the current state of the
	 * activity
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		Log.i(tag, "onsave");
		for (int i = 0; i < row; i++)
			outState.putStringArray(Integer.toString(i), data[i]);
		outState.putInt("row", row);
		outState.putInt("column", column);
		outState.putBooleanArray("isUser", isUser);
		outState.putLong("requestid", requestid);
		outState.putString("projectid", projectid);
		outState.putString("projectname", projectName);
		outState.putIntArray("resourceIDs", resourceIDs);

		super.onSaveInstanceState(outState);

	}

	/**
	 * Called when the user rotate the screen and reload the saved state of the
	 * activity
	 * 
	 * @param savedInstanceState
	 *            where the data will be saved
	 */
	protected void RestoreInstanceState(Bundle savedInstanceState) {
		
		Log.i(tag, "restore");

		row = savedInstanceState.getInt("row");
		column = savedInstanceState.getInt("column");
		isUser = savedInstanceState.getBooleanArray("isUser");
		requestid = savedInstanceState.getLong("requestid");
		projectid = savedInstanceState.getString("projectid");
		projectName = savedInstanceState.getString("projectname");
		resourceIDs = savedInstanceState.getIntArray("resourceIDs");

		data = new String[row][];
		for (int i = 0; i < row; i++)
			data[i] = savedInstanceState.getStringArray(Integer.toString(i));

		refresh();
	}

	/**
	 * Called when there is no internet connection
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
	 * 
	 */
	public void refresh() {
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));

		ScrollView vscroll = new ScrollView(this);
		vscroll.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));

		HorizontalScrollView hscroll = new HorizontalScrollView(this);
		hscroll.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));

		TableLayout table = new TableLayout(this);
		table.setLayoutParams(new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT));
		table.setStretchAllColumns(true);

		for (int i = 0; i < row; i++) {
			TableRow row = new TableRow(this);
			if (i == 0) {
				row.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT));
			} else {
				row.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.WRAP_CONTENT,
						TableRow.LayoutParams.WRAP_CONTENT));
			}
			for (int j = 0; j < column; j++) {
				Color color = new Color();
				TextView column = new TextView(this);

				TableRow.LayoutParams params = new TableRow.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(1, 1, 1, 1);

				column.setLayoutParams(params);

				column.setGravity(Gravity.CENTER_VERTICAL
						| Gravity.CENTER_HORIZONTAL);
				Display display = getWindowManager().getDefaultDisplay();
				int width = display.getWidth() / 4; // deprecated
				int height = display.getHeight() / 10;
				column.setWidth(width);
				column.setHeight(height);
				if (data[i][j] == "0.0" | data[i][j] == "0") {
					column.setTextColor(Color.RED);
				} else {
					column.setTextColor(Color.BLACK);
				}
				column.setText(data[i][j]);

				if ((i == 0)) {
					column.setBackgroundColor(color.DKGRAY);
					column.setTextColor(color.WHITE);
				} else {
					column.setBackgroundColor(color.GRAY);
					column.setTextColor(color.BLACK);

				}
				final TextView text = column;
				final int currentRow = i;
				column.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						if (isUser[currentRow]) {
							Intent myIntent = new Intent(
									getApplicationContext(),
									ResourceActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("username", data[currentRow][0]);
							myIntent.putExtras(bundle);
							startActivity(myIntent);
						}
					}
				});

				if (isLeader)
					column.setOnLongClickListener(new View.OnLongClickListener() {

						public boolean onLongClick(View v) {
							Intent myIntent = new Intent(
									getApplicationContext(),
									ModifyResourceReservation.class);
							Bundle bundle = new Bundle();
							bundle.putString("projectname", projectName);
							bundle.putInt("projectid",
									Integer.parseInt(projectid));
							bundle.putInt("resourceid",
									resourceIDs[currentRow - 1]);
							bundle.putString("resourcename",
									data[currentRow][0]);

							int startPos = 1;
							int endPos = data[0].length - 1;
							while (data[currentRow][startPos].isEmpty())
								startPos++;
							while (data[currentRow][endPos].isEmpty())
								endPos--;

							int[] booking = new int[endPos - startPos + 1];
							int l = 0;
							for (int k = startPos; k <= endPos; k++)
								booking[l++] = Integer
										.parseInt(data[currentRow][k]
												.split("\\.")[0]);

							bundle.putIntArray("booking", booking);
							bundle.putInt("minweek", minWeek + startPos - 1);
							bundle.putInt("maxweek", minWeek + endPos - 1);

							leaved = true;

							myIntent.putExtras(bundle);
							startActivity(myIntent);
							return true;
						}
					});
				row.addView(column);

			}

			if (i != 0) {
				table.addView(row);
			} else {
				linear.addView(row);
			}
		}
		vscroll.addView(table);
		linear.addView(vscroll);
		hscroll.addView(linear);
		setContentView(hscroll);
	}

	/**
	 * 
	 */
	private void queryData() {
		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_BOOKING);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();
		Cursor cBooking = cr.query(uri.build(), null, TABLE_BOOKING.PROJECTID
				+ "=" + projectid, null, TABLE_BOOKING.PROJECTID);

		String projection[] = { "MIN(" + TABLE_BOOKING.WEEK + ") as min",
				"MAX(" + TABLE_BOOKING.WEEK + ") as max" };
		Cursor c = cr.query(uri.build(), projection, TABLE_BOOKING.PROJECTID
				+ "=" + projectid, null, null);
		c.moveToFirst();
		minWeek = c.getInt(c.getColumnIndex("min"));
		int maxWeek = c.getInt(c.getColumnIndex("max"));

		uri.path(DatabaseContract.TABLE_RESOURCES + ", "
				+ DatabaseContract.TABLE_BOOKING);
		String projection2[] = {
				"DISTINCT(" + DatabaseContract.TABLE_RESOURCES + "."
						+ TABLE_RESOURCES.RESOURCEID + ") as "
						+ TABLE_RESOURCES.RESOURCEID,
				TABLE_RESOURCES.RESOURCENAME, TABLE_RESOURCES.USERNAME };
		Cursor cResources = cr.query(uri.build(), projection2,
				DatabaseContract.TABLE_RESOURCES + "."
						+ TABLE_RESOURCES.RESOURCEID + "="
						+ DatabaseContract.TABLE_BOOKING + "."
						+ TABLE_BOOKING.RESOURCEID + " AND "
						+ DatabaseContract.TABLE_BOOKING + "."
						+ TABLE_BOOKING.PROJECTID + "=" + projectid, null,
				TABLE_RESOURCES.RESOURCEID);

		row = cResources.getCount() + 1;
		column = maxWeek - minWeek + 2;

		data = new String[row][column];

		data[0][0] = "Resource";

		for (int i = 1; i < column; i++) {
			data[0][i] = Constants.convertWeekToDate(minWeek + i - 1);
		}

		resourceIDs = new int[row - 1];

		cResources.moveToFirst();
		isUser = new boolean[row];
		for (int i = 1; i < row; i++) {
			resourceIDs[i - 1] = cResources.getInt(cResources
					.getColumnIndex(TABLE_RESOURCES.RESOURCEID));

			data[i][0] = cResources.getString(cResources
					.getColumnIndex(TABLE_RESOURCES.USERNAME));
			if (data[i][0].isEmpty()) {
				data[i][0] = cResources.getString(cResources
						.getColumnIndex(TABLE_RESOURCES.RESOURCENAME));
				isUser[i] = false;
			} else
				isUser[i] = true;
			cResources.moveToNext();
		}

		for (int i = 1; i < row; i++)
			for (int j = 1; j < column; j++)
				data[i][j] = new String();

		if (cBooking.moveToFirst()) {
			int week;
			float ratio;
			int resourceID;
			int i = 0;

			week = cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.WEEK));
			ratio = cBooking.getFloat(cBooking
					.getColumnIndex(TABLE_BOOKING.RATIO));
			resourceID = cBooking.getInt(cBooking
					.getColumnIndex(TABLE_BOOKING.RESOURCEID));

			while (resourceIDs[i] != resourceID)
				i++;
			data[i + 1][week - minWeek + 1] = Float.toString(ratio);

			while (cBooking.moveToNext()) {
				week = cBooking.getInt(cBooking
						.getColumnIndex(TABLE_BOOKING.WEEK));
				ratio = cBooking.getFloat(cBooking
						.getColumnIndex(TABLE_BOOKING.RATIO));
				resourceID = cBooking.getInt(cBooking
						.getColumnIndex(TABLE_BOOKING.RESOURCEID));

				while ((i < resourceIDs.length)
						&& (resourceIDs[i] != resourceID))
					i++;
				if (i < resourceIDs.length)
					data[i + 1][week - minWeek + 1] = Float.toString(ratio);
			}
		}

		refresh();
	}

	/**
	 * 
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			Log.i(tag, "Broadcast received");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				if (intent.getBooleanExtra("Successful", false)) {

					if (numberOfBroadcasts == 0) {
						numberOfBroadcasts = 1;
					} else {
						numberOfBroadcasts = 0;
						Log.i(tag, "ifben");
						loading.dismiss();

						queryData();
					}
				} else {
					loading.dismiss();
					if ((numberOfBroadcasts == 0)
							&& (intent.getIntExtra("error", 10000) == 0)) {
						queryData();
					} else
						messageBoxShow(Constants.getErrorMessage(intent
								.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}