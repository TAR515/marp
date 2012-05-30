package edu.ubb.marp.ui;

import edu.ubb.marp.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class HelloTabActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Hellotab", "setcontent elott");
        setContentView(R.layout.hellotab);
        Log.i("Hellotab", "setcontent utan");
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        Log.i("Hellotab", "intent elott");
        intent = new Intent().setClass(this, ProjectActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("projects").setIndicator("Projects",
                          res.getDrawable(R.drawable.ic_tab_projects))
                      .setContent(intent);
        tabHost.addTab(spec);
        Log.i("Hellotab", "intent utan");

        // Do the same for the other tabs
        intent = new Intent().setClass(this, AssignmentsActivity.class);
        spec = tabHost.newTabSpec("albums").setIndicator("Assignments",
                          res.getDrawable(R.drawable.ic_tab_assignements))
                      .setContent(intent);
        tabHost.addTab(spec);

       /* intent = new Intent().setClass(this, SongsActivity.class);
        spec = tabHost.newTabSpec("songs").setIndicator("Songs",
                          res.getDrawable(R.drawable.ic_tab_songs))
                      .setContent(intent);
        tabHost.addTab(spec);*/

        tabHost.setCurrentTab(2);
    }
   
}