package edu.ubb.marp;


import java.util.ArrayList;
import java.util.Date;

import edu.ubb.marp.DatabaseContract.TABLE_PROJECTS;

import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProjectActivity extends ListActivity {
	private static final String tag = "ProjectActivity";
	
	private ProgressDialog loading;
	
	//private Intent sentIntent;
	private long requestid;
	private String[] ids;
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  Log.i(tag, "onCreate");
		  /*ListAdapter adapter= new ArrayAdapter<String>(this, R.layout.list_item, list);
		  setListAdapter(adapter);
		 
		  ListView lv = getListView();
		  lv.setTextFilterEnabled(true);
		
		  setList(a);
		  final ListView lvv = lv;*/
		  /*ListView lv=(ListView)findViewById(android.R.id.list);
		  lv.setOnItemClickListener(new OnItemClickListener() {
			  public void onItemClick(AdapterView<?> parent, View view,
					  int position, long id) {
				  Intent myIntent = new Intent(getApplicationContext(), ResourcesActivity.class);
				  Bundle bundle=new Bundle();
				  bundle.putString("projectid", ids[position]);
				  myIntent.putExtras(bundle);
				  startActivity(myIntent);
			  }
		  });*/
		  
		  setContentView(R.layout.projects);

  		loading=ProgressDialog.show(getApplicationContext(), "Loading", "Please wait...");
		  
			Uri.Builder uri = new Uri.Builder();
			uri.authority(DatabaseContract.PROVIDER_NAME);
			uri.path("1");
			//uri.appendPath("Projects");
			uri.scheme("content");

			Intent intent = new Intent(getApplicationContext(),
					MyService.class);
			intent.putExtra("ACTION", "QUERY");
			intent.setData(uri.build());
			//sentIntent=intent;
			requestid=new Date().getTime();
			intent.putExtra("requestid", requestid);
			startService(intent);
	  }
	  
	  @Override
	  protected void onStart(){
		  super.onStart();
		  
		  registerReceiver(broadcastReceiver, new IntentFilter(
					Constants.BROADCAST_ACTION));
	  }
	  
	  @Override
	  protected void onStop(){
		  super.onStop();
		  unregisterReceiver(broadcastReceiver);
	  }
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.project, menu);
	      return true;
	  }
	  
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	      // Handle item selection
	      switch (item.getItemId()) {
	          case R.id.myaccountid:
	        	  Intent myIntent = new Intent(this, MyAccountActivity.class);
	    		  this.startActivity(myIntent);
	              return true;
	          default:
	              return super.onOptionsItemSelected(item);
	      }
	  }
	  
		private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i(tag, "megjott a broadcast");
				//if(sentIntent.equals(intent)){
				if(requestid == intent.getLongExtra("originalReqeustid", 0)){
					loading.dismiss();
					
					Uri.Builder uri = new Uri.Builder();
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path("Projects");
					uri.scheme("content");

					ContentResolver cr = getContentResolver();
					Log.i(tag, "query elott");
					
					String projection[]={TABLE_PROJECTS.PROJECTNAME, TABLE_PROJECTS.ISLEADER, TABLE_PROJECTS.PROJECTID+" as _id"};
					
					Cursor c = cr.query(uri.build(), projection, null, null, TABLE_PROJECTS.PROJECTID);
					Log.i(tag, "query utan");
					
					ids=new String[c.getCount()];
					int i=0;
					if(c.moveToFirst()){
					int index=c.getColumnIndex("_id");
						Log.i(tag, "while elott");
						ids[i++]=c.getString(index);
						Log.i(tag, "lekerdezes "+ids[i-1]);
						while(c.moveToNext()){
							ids[i++]=c.getString(index);
							Log.i(tag, "lekerdezes "+ids[i-1]);
						}
						Log.i(tag, "while utan");
					}
					
					c.moveToFirst();
					startManagingCursor(c);

					String[] from = { TABLE_PROJECTS.PROJECTNAME, TABLE_PROJECTS.ISLEADER };
					//int[] to = { android.R.id.text1, android.R.id.text2 };
					int[] to = { R.id.text1, R.id.text2 };
					SimpleCursorAdapter projects = new SimpleCursorAdapter(getApplicationContext(), R.layout.projects_row, c, from, to);
					setListAdapter(projects);
				}
			}
		};
		
		protected void onListItemClick(ListView l, View v, int position, long id) {
			  Intent myIntent = new Intent(getApplicationContext(), ResourcesActivity.class);
			  Bundle bundle=new Bundle();
			  bundle.putString("projectid", ids[position]);
			  myIntent.putExtras(bundle);
			  startActivity(myIntent);
		}
}
