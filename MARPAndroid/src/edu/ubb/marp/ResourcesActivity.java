package edu.ubb.marp;

import java.util.Date;

import edu.ubb.marp.DatabaseContract.TABLE_BOOKING;
import edu.ubb.marp.DatabaseContract.TABLE_PROJECTS;
import edu.ubb.marp.DatabaseContract.TABLE_RESOURCES;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ResourcesActivity extends Activity {
	/** Called when the activity is first created. */
	private final static String tag = "ResourcesActivity";
	
	private ProgressDialog loading;
	
	//private Intent sentIntent;
	private long requestid;
	private String projectid;
	private Integer resourceIDs[];
	private int numberOfBroadcasts;
	
	protected int column;
	protected int row;
	/*String[][] s= {{"Resource","Year","Start","End","% of FTE","1","2","3","4","5","6","7","8","9","10"},{"Robi","2012","1","3","0.3","1","2","3","4","5","6","7","8","9","10"},
			{"Adorjan","2012","3","6","0.3","1","2","3","4","5","6","7","8","9","10"},{"Alpar","2012","2","3","0.3","1","2","3","4","5","6","7","8","9","10"},{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}
			,{"Sanyi","2012","6","9","0.3","1","2","3","4","5","6","7","8","9","10"}};*/
	
	protected String[][] data ;

	protected void setData(String[][] a,int n, int m){
		data = a;
		row = n;
		column = m;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setData(s,20,15);
		//projectid=savedInstanceState.getString("projectid");
		projectid=getIntent().getExtras().getString("projectid");
		
		numberOfBroadcasts=0;

  		loading=ProgressDialog.show(getApplicationContext(), "Loading", "Please wait...");
		  
			Uri.Builder uri = new Uri.Builder();
			uri.authority(DatabaseContract.PROVIDER_NAME);
			uri.path("2");
			//uri.appendPath("Projects");
			
			uri.appendPath(projectid);
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
	  
	   public void messageBoxShow(String message, String title){
	    	AlertDialog alertDialog;
	    	
	      	alertDialog = new AlertDialog.Builder(this).create();
	    	alertDialog.setTitle(title);
	    	alertDialog.setMessage(message);
	    	alertDialog.setButton("Retry",
	    		    new DialogInterface.OnClickListener() {
	    		        public void onClick(DialogInterface dialog, int which) {
	    		            
	    		        }
	    		    });
	    	alertDialog.setButton2("Cancel",
	    		    new DialogInterface.OnClickListener() {
	    		        public void onClick(DialogInterface dialog, int which) {
	    		            
	    		        }
	    		    });
	    	alertDialog.show();
	    }
	   
	   public void refresh(){
		   LinearLayout linear = new LinearLayout(this);
			linear.setOrientation(LinearLayout.VERTICAL);
			linear.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.MATCH_PARENT)
					);
			
			ScrollView vscroll = new ScrollView(this);
			vscroll.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT)
					);
			
			HorizontalScrollView hscroll = new HorizontalScrollView(this);
			hscroll.setLayoutParams(new TableRow.LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.MATCH_PARENT)
					);
			
			TableLayout table = new TableLayout(this);
			table.setLayoutParams(new TableLayout.LayoutParams(
			TableLayout.LayoutParams.MATCH_PARENT,
			TableLayout.LayoutParams.MATCH_PARENT)
			);
			table.setStretchAllColumns(true);
		   
		   for (int i=0;i<row;i++) {
				TableRow row = new TableRow(this);
				if(i==0){
					row.setLayoutParams(new TableRow.LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT,
							TableRow.LayoutParams.WRAP_CONTENT)
							);
				}else{
					row.setLayoutParams(new TableRow.LayoutParams(
							TableRow.LayoutParams.WRAP_CONTENT,
							TableRow.LayoutParams.WRAP_CONTENT)
							);
				}
				for (int j=0;j<column;j++) {
					Color color= new Color();
					TextView column = new TextView(this);
					
				   
					TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(1, 1, 1, 1);

					
					column.setLayoutParams(params);
					
					column.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
					Display display = getWindowManager().getDefaultDisplay(); 
					int width = display.getWidth()/4;  // deprecated
					
					column.setWidth(width); 
					column.setText(data[i][j]);
					//column.setTextSize(15);
					if((i==0)){
						column.setBackgroundColor(color.DKGRAY);
						column.setTextColor(color.WHITE);
					}else{
							column.setBackgroundColor(color.GRAY);
							column.setTextColor(color.BLACK);
						
					}
					final TextView text = column;
					column.setOnClickListener(new View.OnClickListener() {
				        
				        public void onClick(View v) {
				        	
				           messageBoxShow(text.getText().toString(), "click");
				        }

				    });
					row.addView(column);
					
				}
				
				if(i!=0){
					table.addView(row);
				}else{
					linear.addView(row);
				}
			}
			vscroll.addView(table);
			linear.addView(vscroll);
			hscroll.addView(linear);
			setContentView(hscroll);

	   }
	   
		private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				//if(sentIntent.equals(intent)){
				Log.i(tag, "Broadcast received");
				if(requestid == intent.getLongExtra("originalReqeustid", 0)){
					loading.dismiss();
					
					if(numberOfBroadcasts==0){
						numberOfBroadcasts=1;
					}else{
						numberOfBroadcasts=0;
					Log.i(tag, "ifben");
					Uri.Builder uri = new Uri.Builder();
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(DatabaseContract.TABLE_BOOKING);
					uri.scheme("content");

					ContentResolver cr = getContentResolver();
					Cursor cBooking = cr.query(uri.build(), null, TABLE_BOOKING.PROJECTID+"="+projectid, null, TABLE_BOOKING.PROJECTID);

					String projection[]={"MIN("+TABLE_BOOKING.WEEK+") as min","MAX("+TABLE_BOOKING.WEEK+") as max"}; 
					Cursor c=cr.query(uri.build(), projection, TABLE_BOOKING.PROJECTID+"="+projectid, null, null);
					c.moveToFirst();
					int minWeek=c.getInt(c.getColumnIndex("min"));
					int maxWeek=c.getInt(c.getColumnIndex("max"));
					
					uri.path(DatabaseContract.TABLE_RESOURCES+", "+DatabaseContract.TABLE_BOOKING);
					String projection2[]={"DISTINCT("+DatabaseContract.TABLE_RESOURCES+"."+TABLE_RESOURCES.RESOURCEID+") as "+TABLE_RESOURCES.RESOURCEID, TABLE_RESOURCES.RESOURCENAME, TABLE_RESOURCES.USERNAME};
					Cursor cResources=cr.query(uri.build(), projection2, DatabaseContract.TABLE_RESOURCES+"."+TABLE_RESOURCES.RESOURCEID+"="+DatabaseContract.TABLE_BOOKING+"."+TABLE_BOOKING.RESOURCEID+" AND "+ DatabaseContract.TABLE_BOOKING+"."+TABLE_BOOKING.PROJECTID+"="+projectid, null, TABLE_RESOURCES.RESOURCEID);
				
					row = cResources.getCount() + 1;
					column = maxWeek - minWeek + 2;
					
					data = new String[row][column];
					
					data[0][0]="Resource";
					
					for(int i=1; i<column;i++){
						data[0][i] = Constants.convertWeekToDate(minWeek + i - 1);
					}
					
					resourceIDs=new Integer[row-1];

					cResources.moveToFirst();
					for(int i=1;i<row;i++){
						resourceIDs[i-1] = cResources.getInt(cResources.getColumnIndex(TABLE_RESOURCES.RESOURCEID));
						
						data[i][0] = cResources.getString(cResources.getColumnIndex(TABLE_RESOURCES.USERNAME));
						if(data[i][0].isEmpty()){
							data[i][0] = cResources.getString(cResources.getColumnIndex(TABLE_RESOURCES.RESOURCENAME));
						}
						cResources.moveToNext();
					}
					
					for(int i=1;i<row;i++)
						for(int j=1;j<column;j++)
							data[i][j]=new String();
					
					if(cBooking.moveToFirst()){
						int week;
						float ratio;
						int resourceID;
						int i=0;
						
						week=cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.WEEK));
						ratio=cBooking.getFloat(cBooking.getColumnIndex(TABLE_BOOKING.RATIO));
						resourceID=cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.RESOURCEID));
						
						while(resourceIDs[i]!=resourceID)
							i++;
						data[i+1][week-minWeek+1]=Float.toString(ratio);
						
						while(cBooking.moveToNext()){
							week=cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.WEEK));
							ratio=cBooking.getFloat(cBooking.getColumnIndex(TABLE_BOOKING.RATIO));
							resourceID=cBooking.getInt(cBooking.getColumnIndex(TABLE_BOOKING.RESOURCEID));
							
							while((i<resourceIDs.length)&&(resourceIDs[i]!=resourceID))
								i++;
							if(i<resourceIDs.length)
								data[i+1][week-minWeek+1]=Float.toString(ratio);
						}
					}
					
					refresh();
					}
				}
			}
		};
}