package edu.ubb.marp.ui;

import java.util.ArrayList;

import edu.ubb.marp.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class RequestActivity extends ListActivity{
	ArrayList<String> list = new ArrayList<String>();
	String[] a = {"Projekt 1", "Projekt 2","Projekt 3","Projekt 4","Projekt 5"};
	String[] b = {"Projekt 6", "Projekt 7","Projekt 8","Projekt 9","Projekt 10"};
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  ListAdapter adapter= new ArrayAdapter<String>(this, R.layout.list_item, list);
		  setListAdapter(adapter);
		 
		  ListView lv = getListView();
		  lv.setTextFilterEnabled(true);
		
		  setList(a);
		  final ListView lvv = lv;
		  lv.setOnItemClickListener(new OnItemClickListener() {
			  public void onItemClick(AdapterView<?> parent, View view,
					  int position, long id) {
				  
				 //newIntent();
			  }
		  });
	  }
	
	  /**/
	  
	  
	  public void setList(String[] str){
		  list.clear();
		  for(int i=0;i<str.length;i++){
			  list.add(str[i]);
		  }
		  //list.add("Show More");
	  }
	  
	  public void concat(String[] str){
		  list.remove("Show More");
		  for(int i=0;i<str.length;i++){
			  list.add(str[i]);
		  }
		  //list.add("Show More");
	  }
	  /*@Override
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
	  }*/
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
	  /*
	  public void newIntent(){
		  Intent myIntent = new Intent(this, ResourcesFromProjectActivity.class);
		  this.startActivity(myIntent);
	  }*/
}
