package edu.ubb.marp.ui;

import java.util.ArrayList;

import edu.ubb.marp.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class HumanResourceInfoActivity extends Activity {

		
		ArrayList<ListRecord> users = new ArrayList<ListRecord>();
	    
		/** Called when the activity is first created. */
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.myaccount);
	        
			setArrayList("Vizer Arnold", "Arni00", "0742764458", "vizer_arnold@yahoo.com");
	       
	        
	        ListView listView = (ListView) findViewById(R.id.ListViewId);
	        listView.setAdapter(new ListItemAdapter(this, android.R.layout.simple_list_item_1, users));
	        listView.setOnItemClickListener(new OnItemClickListener() {
				  public void onItemClick(AdapterView<?> parent, View view,
						  int position, long id) {
					  //messageBoxShow("asdfaad", "dfsdfsf");
					  
				  }
				  
			  });
	        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
	                    int pos, long id) {
	                // TODO Auto-generated method stub
	            	if (pos == 2){
	            		ListRecord valami = users.get(2);
	            		call(valami.subitem);
	            	}else{
	            		if(pos == 3){
	            			Intent i = new Intent(Intent.ACTION_SEND);
	            			
	            			i.setType("text/plain");
	            			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{users.get(3).subitem});
	            			i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
	            			i.putExtra(Intent.EXTRA_TEXT   , "body of email");
	            			try {
	            			    startActivity(Intent.createChooser(i, "Send mail..."));
	            			} catch (android.content.ActivityNotFoundException ex) {
	            			    Toast.makeText(HumanResourceInfoActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
	            			}
	            		}
	            	}
	                return true;
	            }
	        }); 
	        
		}
		
		public void setArrayList(String name, String username, String telephone, String email){
			ListRecord user = new ListRecord("Name", name);
	        users.add(user);
	        user = new ListRecord("Username", username);
	        users.add(user);
	        user = new ListRecord("Telephone", telephone);
	        users.add(user);
	        user = new ListRecord("E-mail", email);
	        users.add(user);
		}
		
		private void call(String number) {
		    try {
		        Intent callIntent = new Intent(Intent.ACTION_CALL);
		        callIntent.setData(Uri.parse("tel:"+number));
		        startActivity(callIntent);
		    } catch (ActivityNotFoundException e) {
		        Log.e("helloandroid dialing example", "Call failed", e);
		    }
		}
		
		
	}
	

