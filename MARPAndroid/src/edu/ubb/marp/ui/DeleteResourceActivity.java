package edu.ubb.marp.ui;

import java.util.Date;

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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_PROJECTS;
import edu.ubb.marp.database.DatabaseContract.TABLE_RESOURCES;
import edu.ubb.marp.network.MyService;
/**
 * 
 * @author Rakosi Alpar
 *
 */
public class DeleteResourceActivity extends Activity {
	private final static String tag = "DeleteResourceActivity";

	private ProgressDialog loading;
	private int projectid;
	private long requestid;
	private int[] resourceids;
	private String[] resourcenames;
	private int myresourceid;
	private String projectName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deleteresource);
			
			sendRequestForResources();
			
			Button nextButton = (Button) findViewById(R.id.nextResourceButton);
			nextButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					sendRequestToDeleteResource();
				}
			});
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

	/** this method is called when a messagebox needs to be appered */
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

	public void messageBoxShowForResources(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendRequestForResources();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.show();
	}
	
	public void messageBoxShowToDeleteResource(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendRequestToDeleteResource();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	private void sendRequestForResources() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.LOADRESOURCES));
		uriSending.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}
	
	private void sendRequestToDeleteResource(){
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.SETRESOURCEACTIVECMD));
		uriSending.scheme("content");

		Spinner sp=(Spinner)findViewById(R.id.resourcesDeleteSpinner);

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "RESOURCEMODIFICATIONS");
		intent.setData(uriSending.build());
		intent.putExtra("resourceid", resourceids[sp.getSelectedItemPosition()]);
		intent.putExtra("currentweek", Constants.convertDateToWeek(new Date()));
		intent.putExtra("active", false);

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// if(sentIntent.equals(intent)){
			Log.i(tag, "Broadcast received");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				loading.dismiss();
				if (intent.getBooleanExtra("Successful", false)) {
					if(intent.getBooleanExtra("Resources", false)){
					resourceids = intent.getIntArrayExtra("resourceid");
					resourcenames = intent.getStringArrayExtra("resourcename");
					
					Spinner sp = (Spinner)findViewById(R.id.resourcesDeleteSpinner);
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, resourcenames);
					sp.setAdapter(adapter);
					}else{
						finish();
					}
				} else {
					if(intent.getBooleanExtra("Resources", false)){
						messageBoxShowForResources(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}else{
						messageBoxShowToDeleteResource(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};
}
