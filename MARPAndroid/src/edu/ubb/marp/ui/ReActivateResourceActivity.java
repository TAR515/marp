package edu.ubb.marp.ui;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.network.MyService;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class ReActivateResourceActivity extends Activity {

	/**
	 * The loading progress dialog
	 */
	private ProgressDialog loading;
	/**
	 * The requestid
	 */
	private long requestid;
	/**
	 * The ids of the resources
	 */
	private int[] resourceids;
	/**
	 * The names of the resources
	 */
	private String[] resourcenames;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reactivateresource);

		sendRequestForResources();

		Button nextButton = (Button) findViewById(R.id.nextResourceButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendRequestToReActivateResource();
			}
		});
	}

	/**
	 * Called when the activity is reloaded.
	 */
	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}

	/**
	 * Called before the activity gone on background
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * this method is called when a message box needs to be appeared with OK
	 * button
	 * */
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
	 * called when a message box needs to be appeared with 2 buttons: Retry and
	 * Cancel
	 * 
	 * @param message
	 *            is the message of the message box
	 * @param title
	 *            is the title of the message box
	 */
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

	/**
	 * 
	 * @param message
	 *            is the message of the message box
	 * @param title
	 *            is the title of the message box
	 */
	public void messageBoxShowToReActivateResource(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				sendRequestToReActivateResource();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	/**
	 * Sends a request for querying the resources
	 */
	private void sendRequestForResources() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.LOADINACTIVERESOURCES));
		uriSending.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * Sends a request for reactivating a resource
	 */
	private void sendRequestToReActivateResource() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.SETRESOURCEACTIVECMD));
		uriSending.scheme("content");

		Spinner sp = (Spinner) findViewById(R.id.resourcesActivateSpinner);

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "RESOURCEMODIFICATIONS");
		intent.setData(uriSending.build());
		intent.putExtra("resourceid", resourceids[sp.getSelectedItemPosition()]);
		intent.putExtra("currentweek", Constants.convertDateToWeek(new Date()));
		intent.putExtra("active", true);

		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

	/**
	 * Receives the broadcasts
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				loading.dismiss();
				if (intent.getBooleanExtra("Successful", false)) {
					if (intent.getBooleanExtra("Resources", false)) {
						resourceids = intent.getIntArrayExtra("resourceid");
						resourcenames = intent.getStringArrayExtra("resourcename");

						Spinner sp = (Spinner) findViewById(R.id.resourcesActivateSpinner);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
								android.R.layout.simple_spinner_dropdown_item, resourcenames);
						sp.setAdapter(adapter);
					} else {
						finish();
					}
				} else {
					if (intent.getBooleanExtra("Resources", false)) {
						messageBoxShowForResources(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					} else {
						messageBoxShowToReActivateResource(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
					}
				}
			}
		}
	};
}
