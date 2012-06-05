package edu.ubb.marp.ui;

import java.util.ArrayList;
import java.util.Date;

import edu.ubb.marp.R;
import edu.ubb.marp.network.MyService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ModifyProjectActivity extends Activity{

	ArrayList<ListRecord> listItems = new ArrayList<ListRecord>();
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.myaccount);
		setListItems("Projekt 1", "Opened", "2009.01.01", "1.1", "Under Developement");
		ListView listView = (ListView) findViewById(R.id.ListViewId);
		listView.setAdapter(new ListItemAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listItems));
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
			
				if(pos == 0 || pos == 3){
					editDialog("Change "+listItems.get(pos).getItem(),listItems.get(pos).getSubitem(), pos);
				}else{
					if(pos == 1){
						editOpenedStatus("Change" +listItems.get(pos).getItem(), listItems.get(pos).getSubitem(), pos);
					}else{
						if(pos == 2){
							editDeadline("Change" +listItems.get(pos).getItem(), listItems.get(pos).getSubitem(), pos);
						}
						else{
							if (pos == 4){
								editCurrentStatus("Change" +listItems.get(pos).getItem(),pos);
							}
						}
					}
				}
				return true;
			}
		});
	}
	
	private void setListItems(String name,String openedStatus,String deadline, String nextReliese,String currentStatus ){
		listItems = new ArrayList<ListRecord>();
		ListRecord item = new ListRecord("Name",name);
		listItems.add(item);
		item = new ListRecord("Opened Status",openedStatus);
		listItems.add(item);
		item = new ListRecord("Deadline",deadline);
		listItems.add(item);
		item = new ListRecord("NextReliese",nextReliese);
		listItems.add(item);
		item = new ListRecord("Current Status",currentStatus);
		listItems.add(item);
		item = new ListRecord("Modify","");	
		listItems.add(item);
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
				//Intent intent;
			//	loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 0: // Name
					
					break;
				case 3: // NextReliese
					
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
	public void editOpenedStatus(String title,String status, int position) {

		AlertDialog alertDialog;
		
		CheckBox check = new CheckBox(this);
		if(status == "Opened"){
			check.setChecked(true);
		}
		check.setText("Opened");
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(check);
		final int myPosition = position;
		// final String newText = editDialog.getText().toString();
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//Intent intent;
			//	loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 1: // Opened Status
					
		
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
	public void editDeadline(String title,String date, int position) {

		AlertDialog alertDialog;
		DatePicker d = new DatePicker(this);
		String deadline[] = date.split("\\.");
		d.init(Integer.parseInt(deadline[0]), Integer.parseInt(deadline[1])-1, Integer.parseInt(deadline[2]), null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(d);
		final int myPosition = position;
		// final String newText = editDialog.getText().toString();
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//Intent intent;
			//	loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 2: // Deadline
					
		
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
	public void editCurrentStatus(String title, int position) {
		String s[] = {"Accepted/Ready to Start", "Delivered", "Done", "Redy for delivery", "Specification", "Testing", "Under developement"};
		
		Spinner spinner = new Spinner(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_dropdown_item, s);
		spinner.setAdapter(adapter);
		
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setView(spinner);
		final int myPosition = position;
		
		// final String newText = editDialog.getText().toString();
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//Intent intent;
			//	loading = ProgressDialog.show(context, "Loading", "Please wait...");

				switch (myPosition) {
				case 2: // Deadline
					
		
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
}
