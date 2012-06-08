package edu.ubb.marp.ui;

import java.util.Date;
import java.util.StringTokenizer;

import edu.ubb.marp.Constants;
import edu.ubb.marp.Constants.STRIPEACTIVITYACTIONS;
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
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableRow;
/**
 * 
 * @author Rakosi Alpar, Vizer Arnold
 *
 */
public class StripeActivity extends Activity {

	/**
	 * The loading progress dialog
	 */
	private ProgressDialog loading;
	/**
	 * The requestid
	 */
	private long requestid;
	/**
	 * The Bundle object, what received the object
	 */
	private Bundle bundle;
	/**
	 * The startweek of the activity
	 */
	private int startweek;
	/**
	 * The endweek of the activity
	 */
	private int endweek;
	/**
	 * The name of the project
	 */
	private String projectname;
	/**
	 * The id of the project
	 */
	private int projectID;
	/**
	 * The id of the target resource
	 */
	private int targetresourceid;
	/**
	 * The id of the sender resource
	 */
	private int senderresourceid;
	/**
	 * The context in which the application is running
	 */
	private Context context;
	/**
	 * The reason for starting this activity
	 */
	private STRIPEACTIVITYACTIONS action;
	/**
	 * The booking of the current resource
	 */
	private int[] booking;
	/**
	 * Is the user leader in the project
	 */
	private boolean isleader;
	/**
	 * The requestid
	 */
	private int currentrequestid;

	/**
	 * The elements of the Row
	 */
	RowElement[] elements;
	Button applyToAll;
	int columns;
	Display display;
	
	/**
	 * With this array the elements will be filled
	 */
	String loadstr[][];
	boolean klicked = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		if ((savedInstanceState == null) || (savedInstanceState.isEmpty())) {
			bundle = getIntent().getExtras();

			startweek = bundle.getInt("startweek");
			endweek = bundle.getInt("endweek");

			action = STRIPEACTIVITYACTIONS.valueOf(bundle.getString("ACTION"));
			switch (action) {
			case newproject:
				senderresourceid = bundle.getInt("resourceid");
				targetresourceid = senderresourceid;
				projectname = bundle.getString("projectname");
				break;
				
			case insert:
				projectID = bundle.getInt("projectid");
				senderresourceid=bundle.getInt("senderresourceid");
				targetresourceid=bundle.getInt("targetresourceid");
				isleader =bundle.getBoolean("isleader");
				break;

			case update:
				projectID = bundle.getInt("projectid");
				projectname = bundle.getString("projectname");
				targetresourceid = bundle.getInt("targetresourceid");
				senderresourceid = bundle.getInt("senderresourceid");
				booking = bundle.getIntArray("booking");
				break;
				
			case request:
				projectID = bundle.getInt("projectid");
				projectname = bundle.getString("projectname");
				targetresourceid = bundle.getInt("targetresourceid");
				senderresourceid = bundle.getInt("senderresourceid");
				currentrequestid = bundle.getInt("currentrequestid");
				booking = bundle.getIntArray("booking");
				break;
			}

			int[] results = bundle.getIntArray("results");
			int length=endweek-startweek+1;

				display = getWindowManager().getDefaultDisplay();

				loadstr = new String[length][2];

				int j = startweek;
				for (int i = 0; i < length; i++) {
					loadstr[i][0] = Constants.convertWeekToDate(j++);
					if (i < results.length)
						loadstr[i][1] = Integer.toString(results[i]);
					else
						loadstr[i][1] = Integer.toString(0);
				}

				setColumns(length, loadstr);

				
				LinearLayout l = new LinearLayout(context);
				for (int i = 0; i < columns; i++) {
					l.addView(elements[i].returnView());
				}
				for (int i = 0; i < columns; i++) {
					applyToAll = elements[i].getApplyToAllButton();
					final int from = i;
					final int id = elements[i].getGroupCheckedRadioButtonId();
					final RadioGroup rad = elements[i].getRadioGroup();
					elements[i].setElements(elements, columns, i);
					applyToAll.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							setElements(from, rad);
						}
					});
				}
				ScrollView scroll = new ScrollView(context);
				scroll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

				HorizontalScrollView horizontal = new HorizontalScrollView(context);

				scroll.addView(l);
				horizontal.addView(scroll);
				setContentView(horizontal);
		} else
			RestoreInstanceState(savedInstanceState);

		
	}

	/**
	 * Called when the Activity is reloaded
	 */
	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}
	/**
	 * Called before the activity goes on the background
	 */
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}
	
	/**
	 * initialize the array of row elements
	 * @param n the number of columns
	 * @param s the elements
	 */
	private void setColumns(int n, String[][] s) {
		columns = n;
		elements = new RowElement[columns];
		for (int i = 0; i < columns; i++) {
			elements[i] = new RowElement(this, display);
			elements[i].setText(s[i][0], s[i][1]);
			if ((action == STRIPEACTIVITYACTIONS.update)||(action == STRIPEACTIVITYACTIONS.request)) {
				String temp = booking[i] + " %";
				StringTokenizer st = new StringTokenizer(temp);
				elements[i].setPercentText(temp);
				elements[i].setNeededText("You want: " + temp);
				elements[i].setOriginalNeeded(Integer.parseInt(st.nextToken()));
			}
		}
	}
	
	/**
	 * set the elements to a state 
	 * @param from the index of the beginning
	 * @param group the radiogroup of an element to know which radio button is selected
	 */
	private void setElements(int from, RadioGroup group) {
		int id = group.getCheckedRadioButtonId();

		if (id == 1) {
			for (int i = from; i < columns; i++)
				elements[i].accept();
		} else {
			if (id == 2) {
				for (int i = from; i < columns; i++)
					elements[i].setInitialState();
			} else {
				if (id == 3) {
					for (int i = from; i < columns; i++) {
						if (elements[i].isRed) {
							elements[i].setYellow();
						}
					}

				} else {
					if (id == 4) {
						int ratio = elements[from].getCurrentNeededRatio();

						for (int i = from + 1; i < columns; i++) {
							elements[i].applyMyRatioToAll2(ratio);
						}

					}
				}
			}
		}
		elements[from].buttonDownShowHide();
	}
	
	/**
	 * cleare all the elements
	 */
	private void clearAll() {
		for (int i = 0; i < columns; i++) {
			elements[i].setInitialState();
		}
	}
	
	/**
	 *  create the menu option
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.newprojectmenu, menu);
		return true;
	}
	
	/**
	 * called when an item is selected from the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.send:
		
			boolean isOneRed = false;
			for (int i = 0; i < columns; i++) {
				if (elements[i].isRed()) {
					isOneRed = true;
				}
			}
			if (isOneRed) {
				messageBoxShow("There are uncompleted weeks", "Error");
			} else {
				Intent intent;
				int i;
				int[] updateratios;
				int[] requestratios;

				loading = ProgressDialog.show(this, "Loading", "Please wait...");

				switch (action) {
				case newproject:
					intent = new Intent(this, MyService.class);
					intent.putExtra("ACTION", "NEWPROJECT");
					intent.putExtra("resourceid", bundle.getInt("resourceid"));
					intent.putExtra("projectname", bundle.getString("projectname"));
					intent.putExtra("openedstatus", bundle.getBoolean("openedstatus"));
					intent.putExtra("startweek", bundle.getInt("startweek"));
					intent.putExtra("endweek", bundle.getInt("endweek"));
					intent.putExtra("deadline", bundle.getInt("deadline"));
					intent.putExtra("nextrelease", bundle.getString("nextrelease"));
					intent.putExtra("statusname", bundle.getString("statusname"));

					i = columns - 1;
					while ((i >= 0) && (!elements[i].isGreen) && (!elements[i].isYellow))
						i--;

					int currentColumns = i + 1;

					updateratios = new int[currentColumns];
					requestratios = new int[currentColumns];
					for (i = 0; i < currentColumns; i++) {
						requestratios[i] = Integer.parseInt(elements[i].getratio());
						if (requestratios[i] > 100)
							requestratios[i] -= 100;
						else
							requestratios[i] = 0;
						StringTokenizer st = new StringTokenizer(elements[i].getPercentText());
						updateratios[i] = Integer.parseInt(st.nextToken()) - requestratios[i];
					}
					intent.putExtra("updateratios", updateratios);
					intent.putExtra("requestratios", requestratios);

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
					
				case insert:
					intent = new Intent(this, MyService.class);
					intent.putExtra("ACTION", "ADDRESOURCETOPROJECT");
					intent.putExtra("projectid", projectID);
					intent.putExtra("startweek", startweek);
					intent.putExtra("endweek", endweek);
					intent.putExtra("targetresourceid", targetresourceid);
					intent.putExtra("senderresourceid", senderresourceid);
					intent.putExtra("isleader", isleader);

					updateratios = new int[columns];
					requestratios = new int[columns];
					for (i = 0; i < columns; i++) {
						requestratios[i] = Integer.parseInt(elements[i].getratio());
						if (requestratios[i] > 100)
							requestratios[i] -= 100;
						else
							requestratios[i] = 0;
						StringTokenizer st = new StringTokenizer(elements[i].getPercentText());
						updateratios[i] = Integer.parseInt(st.nextToken()) - requestratios[i];
					}
					intent.putExtra("updateratios", updateratios);
					intent.putExtra("requestratios", requestratios);

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
					
				case update:
					intent = new Intent(this, MyService.class);
					intent.putExtra("ACTION", "RESOURCERESERVATIONMODIFICATION");
					intent.putExtra("projectid", projectID);
					intent.putExtra("startweek", startweek);
					intent.putExtra("endweek", endweek);
					intent.putExtra("targetresourceid", targetresourceid);
					intent.putExtra("senderresourceid", senderresourceid);

					updateratios = new int[columns];
					requestratios = new int[columns];
					for (i = 0; i < columns; i++) {
						requestratios[i] = Integer.parseInt(elements[i].getratio());
						if (requestratios[i] > 100)
							requestratios[i] -= 100;
						else
							requestratios[i] = 0;
						StringTokenizer st = new StringTokenizer(elements[i].getPercentText());
						updateratios[i] = Integer.parseInt(st.nextToken()) - requestratios[i];
					}
					intent.putExtra("updateratios", updateratios);
					intent.putExtra("requestratios", requestratios);

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
					
				case request:
					Uri.Builder uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(Integer.toString(Constants.ACCEPTREQUESTCMD));
					uri.scheme("content");
					
					intent = new Intent(this, MyService.class);
					intent.setData(uri.build());
					
					intent.putExtra("ACTION", "REQUESTOPERATIONS");
					intent.putExtra("projectid", projectID);
					intent.putExtra("targetresourceid", targetresourceid);
					intent.putExtra("currentrequestid", currentrequestid);
					intent.putExtra("currentweek", startweek);
					
					StringTokenizer st = new StringTokenizer(elements[0].getPercentText());
					intent.putExtra("updateratio", Integer.parseInt(st.nextToken()));

					requestid = new Date().getTime();
					intent.putExtra("requestid", requestid);

					startService(intent);
					break;
				}
			}
			return true;
		case R.id.clear:
			clearAll();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * restore the state of the activity after rotating the screen
	 * @param savedInstanceState
	 */
	protected void RestoreInstanceState(Bundle savedInstanceState) {
		requestid = savedInstanceState.getLong("requestid");
		bundle = savedInstanceState.getBundle("bundle");
		startweek = savedInstanceState.getInt("startweek");
		endweek = savedInstanceState.getInt("endweek");
		targetresourceid = savedInstanceState.getInt("targetresourceid");
		senderresourceid = savedInstanceState.getInt("senderresourceid");
		columns = savedInstanceState.getInt("columns");
		projectname = savedInstanceState.getString("projectname");
		projectID = savedInstanceState.getInt("projectid");
		action = STRIPEACTIVITYACTIONS.valueOf(savedInstanceState.getString("ACTION"));

		elements = new RowElement[columns];

		for (int i = 0; i < columns; i++) {
			elements[i] = new RowElement(this, display);
			elements[i].setPercentText(savedInstanceState.getString("percent" + i));
			elements[i].setRatioText(savedInstanceState.getString("ratio" + i));
			elements[i].setNeededText(savedInstanceState.getString("needed" + i));
			elements[i].setDateText(savedInstanceState.getString("date" + i));
			boolean red = savedInstanceState.getBoolean("red" + i);
			boolean green = savedInstanceState.getBoolean("green" + i);
			boolean yellow = savedInstanceState.getBoolean("yellow" + i);
			if (red) {
				elements[i].setRed();
			} else {
				if (green) {
					elements[i].setGreen();
				} else {
					if (yellow) {
						elements[i].setYellow();
					}
				}
			}
		}
	}
	
	/**
	 * save the activity state before rotating the screen
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save the values you need from your textview into "outState"-object
		outState.putLong("requestid", requestid);
		outState.putBundle("bundle", bundle);
		outState.putInt("startweeek", startweek);
		outState.putInt("endweek", endweek);
		outState.putInt("targetresourceid", targetresourceid);
		outState.putInt("senderresourceid", senderresourceid);
		outState.putInt("columns", columns);
		outState.putString("projectname", projectname);
		outState.putInt("projectid", projectID);
		outState.putString("ACTION", action.toString());

		for (int i = 0; i < columns; i++) {
			outState.putString("percent" + i, elements[i].getPercentText());
			outState.putString("ratio" + i, elements[i].getRatioText());
			outState.putString("needed" + i, elements[i].getNeededText());
			outState.putString("date" + i, elements[i].getDateText());
			outState.putBoolean("red" + i, elements[i].isRed());
			outState.putBoolean("green" + i, elements[i].isGreen());
			outState.putBoolean("yellow" + i, elements[i].isYellow());
		}
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * is called when the items are sent
	 * @param message is the message of the message box
	 * @param title is the title of the message box
	 */
	public void messageBoxShow(String message, String title) {
		AlertDialog alertDialog;

		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alertDialog.show();
	}
	
	/**
	 * Receives the broadcasts
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
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