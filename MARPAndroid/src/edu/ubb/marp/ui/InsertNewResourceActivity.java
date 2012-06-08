package edu.ubb.marp.ui;

import java.util.Date;

import edu.ubb.marp.Constants;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.network.MyService;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class InsertNewResourceActivity extends Activity {
	private ProgressDialog loading;
	private long requestid;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newresource);

		Button button = (Button) findViewById(R.id.addResourceButton);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendRequest();
			}
		});
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
	 * Send the reqeust for inserting the new resource
	 */
	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		EditText resourceName = (EditText) findViewById(R.id.resourceName);
		CheckBox activeCheck = (CheckBox) findViewById(R.id.activeCheckBox);
		Spinner resourceType = (Spinner) findViewById(R.id.resourceTypeSpinner);

		if (resourceName.getText().toString().isEmpty())
			messageBoxShow("Please fill in all fields", "Error!");
		else {

			Uri.Builder uriSending = new Uri.Builder();
			uriSending.authority(DatabaseContract.PROVIDER_NAME);
			uriSending.path(Integer.toString(Constants.INSERTNEWRESOURCE));
			uriSending.scheme("content");

			Intent intent = new Intent(this, MyService.class);
			intent.putExtra("ACTION", "RESOURCEMODIFICATIONS");
			intent.setData(uriSending.build());
			intent.putExtra("resourcename", resourceName.getText().toString());
			intent.putExtra("active", activeCheck.isChecked());
			intent.putExtra("resourcetypename", resourceType.getSelectedItem().toString());
			intent.putExtra("resourcegroupname", "Group1");

			requestid = new Date().getTime();
			intent.putExtra("requestid", requestid);
			startService(intent);
		}
	}

	/**
	 * this method is called when a messagebox with 2 buttons needs to be
	 * appered
	 * */
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
					finish();
				} else {
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}
