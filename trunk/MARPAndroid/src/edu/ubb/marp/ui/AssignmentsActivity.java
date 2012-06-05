package edu.ubb.marp.ui;
/*proba*/
import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_BOOKING;
import edu.ubb.marp.database.DatabaseContract.TABLE_RESOURCES;
import edu.ubb.marp.database.DatabaseContract.TABLE_USERS;
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
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AssignmentsActivity extends Activity {
	private final static String tag = "AssignmentsActivity";

	private ProgressDialog loading;

	// private Intent sentIntent;
	private long requestid;

	protected int column;
	protected int row;
	
	private int currentWeek;
	private int myResourceID;
	
	protected String[][] data;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*TextView textview = new TextView(this);
        textview.setText("This is the Assignements tab");
        setContentView(textview);*/
        
        sendRequest();
    }

	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(Integer.toString(Constants.LOADASSIGNMENTSCMD));
		uri.scheme("content");
		
		currentWeek = Constants.convertDateToWeek(new Date());

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.putExtra("currentweek", currentWeek);
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

	public void refresh() {
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

		ScrollView vscroll = new ScrollView(this);
		vscroll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

		HorizontalScrollView hscroll = new HorizontalScrollView(this);
		hscroll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

		TableLayout table = new TableLayout(this);
		table.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
		table.setStretchAllColumns(true);

		for (int i = 0; i < row; i++) {
			TableRow row = new TableRow(this);
			if (i == 0) {
				row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			} else {
				row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
			}
			for (int j = 0; j < column; j++) {
				Color color = new Color();
				TextView column = new TextView(this);

				TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(1, 1, 1, 1);

				column.setLayoutParams(params);

				column.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
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

				// column.setTextSize(15);
				if ((i == 0)) {
					column.setBackgroundColor(color.DKGRAY);
					column.setTextColor(color.WHITE);
				} else {
					column.setBackgroundColor(color.GRAY);
					column.setTextColor(color.BLACK);

				}
				/*final TextView text = column;
				final int currentRow = i;
				column.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// messageBoxShow(text.getText().toString(), "click");
						if (isUser[currentRow]) {
							Intent myIntent = new Intent(getApplicationContext(), ResourceActivity.class);
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
							Intent myIntent = new Intent(getApplicationContext(), ModifyResourceReservation.class);
							Bundle bundle = new Bundle();
							bundle.putString("projectname", projectName);
							bundle.putInt("projectid", Integer.parseInt(projectid));
							bundle.putInt("resourceid", resourceIDs[currentRow - 1]);
							bundle.putString("resourcename", data[currentRow][0]);
							// bundle.putStringArray("bookings",
							// data[currentRow]);

							int startPos = 1;
							int endPos = data[0].length - 1;
							while (data[currentRow][startPos].isEmpty())
								startPos++;
							while (data[currentRow][endPos].isEmpty())
								endPos--;

							int[] booking = new int[endPos - startPos + 1];
							int l = 0;
							for (int k = startPos; k <= endPos; k++)
								booking[l++] = Integer.parseInt(data[currentRow][k].split("\\.")[0]);

							bundle.putIntArray("booking", booking);
							bundle.putInt("minweek", minWeek + startPos - 1);
							bundle.putInt("maxweek", minWeek + endPos - 1);

							myIntent.putExtras(bundle);
							startActivity(myIntent);
							return true;
						}
					});*/
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

	private void queryData() {
		/*SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_USERS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();

		String projection[] = { TABLE_USERS.USERID };

		Cursor c = cr.query(uri.build(), projection, TABLE_USERS.USERNAME + " = '" + pref.getString("username", "") + "'", null,
				null);

		c.moveToFirst();

		myResourceID = c.getInt(c.getColumnIndex(TABLE_USERS.USERID));
		
		uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_BOOKING);
		uri.scheme("content");

		Cursor cBooking = cr.query(uri.build(), null, TABLE_BOOKING.RESOURCEID + " = " + Integer.toString(myResourceID) +" AND "+TABLE_BOOKING.WEEK+" >= "+Integer.toString(currentWeek)+" AND "+TABLE_BOOKING.WEEK+" < " + Integer.toString(currentWeek+3), null, TABLE_BOOKING.PROJECTID);

		uri.path(DatabaseContract.TABLE_PROJECTS);
		String projection2[] = {
				"DISTINCT(" + DatabaseContract.TABLE_RESOURCES + "." + TABLE_RESOURCES.RESOURCEID + ") as " + TABLE_RESOURCES.RESOURCEID,
				TABLE_RESOURCES.RESOURCENAME, TABLE_RESOURCES.USERNAME };
		Cursor cResources = cr.query(uri.build(), projection2, DatabaseContract.TABLE_RESOURCES + "." + TABLE_RESOURCES.RESOURCEID + "="
				+ DatabaseContract.TABLE_BOOKING + "." + TABLE_BOOKING.RESOURCEID + " AND " + DatabaseContract.TABLE_BOOKING + "."
				+ TABLE_BOOKING.PROJECTID + "=" + projectid, null, TABLE_RESOURCES.RESOURCEID);

		row = cResources.getCount() + 1;
		column = maxWeek - minWeek + 2;

		data = new String[row][column];

		data[0][0] = "Resource";

		for (int i = 1; i < column; i++) {
			data[0][i] = Constants.convertWeekToDate(minWeek + i - 1);
			Log.i(tag, Integer.toString(minWeek+i-1)+" "+Constants.convertWeekToDate(minWeek + i - 1));
		}

		resourceIDs = new int[row - 1];

		cResources.moveToFirst();
		isUser = new boolean[row];
		for (int i = 1; i < row; i++) {
			resourceIDs[i - 1] = cResources.getInt(cResources.getColumnIndex(TABLE_RESOURCES.RESOURCEID));

			data[i][0] = cResources.getString(cResources.getColumnIndex(TABLE_RESOURCES.USERNAME));
			if (data[i][0].isEmpty()) {
				data[i][0] = cResources.getString(cResources.getColumnIndex(TABLE_RESOURCES.RESOURCENAME));
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
			ratio = cBooking.getFloat(cBooking.getColumnIndex(TABLE_BOOKING.RATIO));
			resourceID = cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.RESOURCEID));

			while (resourceIDs[i] != resourceID)
				i++;
			data[i + 1][week - minWeek + 1] = Float.toString(ratio);

			while (cBooking.moveToNext()) {
				week = cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.WEEK));
				ratio = cBooking.getFloat(cBooking.getColumnIndex(TABLE_BOOKING.RATIO));
				resourceID = cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.RESOURCEID));

				while ((i < resourceIDs.length) && (resourceIDs[i] != resourceID))
					i++;
				if (i < resourceIDs.length)
					data[i + 1][week - minWeek + 1] = Float.toString(ratio);
			}
		}

		refresh();*/
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// if(sentIntent.equals(intent)){
			/*Log.i(tag, "Broadcast received");
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
					if ((numberOfBroadcasts == 1) && (intent.getIntExtra("error", 10000) == 0)) {
						queryData();
					} else
						messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}*/
		}
	};
	
	
}
