package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_PROJECTS;
import edu.ubb.marp.database.DatabaseContract.TABLE_REQUESTS;
import edu.ubb.marp.network.MyService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 *
 */
public class RequestsActivity extends ListActivity{
	private static final String tag = "RequestsActivity";

	private ProgressDialog loading;

	
	private long requestid;
	private int[] ids;
	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");

		setContentView(R.layout.projects);
		Log.i(tag, "setcontent utan");
		sendRequest();
	}
	/**
	 * 
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(Integer.toString(Constants.LOADREQUESTSCMD));
		uri.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERY");
		intent.setData(uri.build());
		
		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}
	/**
	 * Called when the activity is reloaded
	 */
	@Override
	protected void onStart() {
		super.onStart();

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
	 * is called when there is no connection
	 * @param message is the message of the message box
	 * @param title is the title of the message box
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
	private void queryData(){	
		Uri.Builder uri = new Uri.Builder();
		uri = new Uri.Builder();
		uri.authority(DatabaseContract.PROVIDER_NAME);
		uri.path(DatabaseContract.TABLE_REQUESTS);
		uri.scheme("content");

		ContentResolver cr = getContentResolver();
		Log.i(tag, "query elott");

		String projection[] = { TABLE_REQUESTS.REQUESTID + " as _id", TABLE_REQUESTS.SENDERUSERNAME, TABLE_REQUESTS.RESOURCERESOURCENAME };

		Cursor c = cr.query(uri.build(), projection, null, null,
				TABLE_PROJECTS.PROJECTID);
		Log.i(tag, "query utan");

		ids = new int[c.getCount()];
		int i = 0;
		if (c.moveToFirst()) {
			int index = c.getColumnIndex("_id");
			ids[i++] = c.getInt(index);
			while (c.moveToNext()) {
				ids[i++] = c.getInt(index);
			}
		}

		c.moveToFirst();
		startManagingCursor(c);

		String[] from = { TABLE_REQUESTS.SENDERUSERNAME,
				TABLE_REQUESTS.RESOURCERESOURCENAME };
		
		int[] to = { R.id.text1, R.id.text2 };
		SimpleCursorAdapter projects = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.projects_row, c,
				from, to);
		setListAdapter(projects);
	}
	/**
	 * 
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "megjott a broadcast");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				if (intent.getBooleanExtra("Successful", false)) {
					loading.dismiss();
					queryData();
				} else {
					loading.dismiss();
					if(intent.getIntExtra("error", 10000)==0){
						queryData();
					}else{
						messageBoxShow(Constants.getErrorMessage(intent
							.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};
	
	/**
	 * list view click listener , is called when there is a click on the list view
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent myIntent = new Intent(getApplicationContext(), RequestActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("requestid", ids[position]);
		myIntent.putExtras(bundle);
		startActivity(myIntent);
	}
}
