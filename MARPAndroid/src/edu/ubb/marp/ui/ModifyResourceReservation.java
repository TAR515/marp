package edu.ubb.marp.ui;

import edu.ubb.marp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class ModifyResourceReservation extends Activity{
	private static final String tag = "ModifyResourceReservation";
	private ProgressDialog loading;
	private long requestid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "onCreate");

		setContentView(R.layout.modifyresourcereservation);

		Button addButton = (Button) findViewById(R.id.next);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//sendRequest();
				EditText startweek = (EditText) findViewById(R.id.startweekMod);
				EditText endweek = (EditText) findViewById(R.id.endweekMod);

				String projectName =getIntent().getExtras().getString("projectname");
				int resourceID = getIntent().getExtras().getInt("resourceid");
				
				if ((startweek.getText().toString().isEmpty())
						|| (endweek.getText().toString().isEmpty()))
					messageBoxShow("Please fill in all fields", "Error!");
				else {
				
				Intent myIntent = new Intent(getApplicationContext(), StripeActivity.class);
				Bundle bundle = new Bundle();
				
				int startWeek=Integer.parseInt(startweek.getText().toString());
				int endWeek=Integer.parseInt(endweek.getText().toString());;
				if(endWeek-startWeek>24)
					endWeek=startWeek+24;
				
				bundle.putString("ACTION", "MODRESOURCERESERVATION");
				bundle.putString("projectname", projectName);
				bundle.putInt("resourceid", resourceID);
				bundle.putString("startweek", startweek.getText().toString());
				bundle.putString("endweek", Integer.toString(endWeek));

				myIntent.putExtras(bundle);
				startActivity(myIntent);
				}
			}
		});
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

}
