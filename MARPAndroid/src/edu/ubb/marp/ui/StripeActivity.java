package edu.ubb.marp.ui;

import java.util.Date;
import java.util.StringTokenizer;

import org.json.JSONArray;

import edu.ubb.marp.Constants;
import edu.ubb.marp.Constants.STRIPEACTIVITYACTIONS;
import edu.ubb.marp.R;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.TABLE_PROJECTS;
import edu.ubb.marp.database.DatabaseContract.TABLE_USERS;
import edu.ubb.marp.network.MyService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableRow;

public class StripeActivity extends Activity {
	private static final String tag = "StripeActivity";

	private ProgressDialog loading;
	private long requestid;
	private Bundle bundle;
	private int startweek;
	private int endweek;
	private String projectname;
	private int projectID;
	private int targetresourceid, senderresourceid;
	private Context context;
	private STRIPEACTIVITYACTIONS action;
	private int[] booking;
	private boolean isleader;

	RowElement[] elements;
	Button applyToAll;
	int columns;
	Display display;
	/*
	 * String loadstr[][] = { { "2001.03.10", "0" }, { "2001.03.10", "10" }, {
	 * "2001.03.10", "20" }, { "2001.03.10", "30" }, { "2001.03.10", "40" }, {
	 * "2001.03.10", "50" }, { "2001.03.10", "60" }, { "2001.03.10", "70" }, {
	 * "2001.03.10", "80" }, { "2001.03.10", "90" } };
	 */
	String loadstr[][];
	boolean klicked = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "oncreate");

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
			}

			int[] results = bundle.getIntArray("results");
			int length=endweek-startweek+1;

				display = getWindowManager().getDefaultDisplay();

				loadstr = new String[length][2];

				int j = startweek;
				for (int i = 0; i < length; i++) {
					loadstr[i][0] = Constants.convertWeekToDate(j++);
					if (results.length != 0)
						loadstr[i][1] = Integer.toString(results[i]);
					else
						loadstr[i][1] = Integer.toString(0);
				}

				setColumns(length, loadstr);

				// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
							Log.i("melyiket piszkalom : ", "" + from);
							Log.i("ID : ", "" + id);
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

		/*
		 * display = getWindowManager().getDefaultDisplay(); setColumns(10,
		 * loadstr);
		 * 
		 * this.requestWindowFeature(Window.FEATURE_NO_TITLE); LinearLayout l =
		 * new LinearLayout(this); for (int i = 0; i < columns; i++) {
		 * l.addView(elements[i].returnView()); } for (int i = 0; i < columns;
		 * i++) { applyToAll = elements[i].getApplyToAllButton(); final int from
		 * = i; final int id = elements[i].getGroupCheckedRadioButtonId(); final
		 * RadioGroup rad = elements[i].getRadioGroup();
		 * elements[i].setElements(elements, columns, i);
		 * applyToAll.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) { Log.i("melyiket piszkalom : ", "" + from);
		 * Log.i("ID : ", "" + id); setElements(from, rad); } }); } ScrollView
		 * scroll = new ScrollView(this); scroll.setLayoutParams(new
		 * TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,
		 * TableRow.LayoutParams.MATCH_PARENT));
		 * 
		 * HorizontalScrollView horizontal = new HorizontalScrollView(this);
		 * 
		 * scroll.addView(l); horizontal.addView(scroll);
		 * setContentView(horizontal);
		 */
	}

	/*
	 * private void sendRequest() { loading = ProgressDialog.show(this,
	 * "Loading", "Please wait...");
	 * 
	 * Uri.Builder uriSending = new Uri.Builder();
	 * uriSending.authority(DatabaseContract.PROVIDER_NAME);
	 * uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
	 * uriSending.scheme("content");
	 * 
	 * Intent intent = new Intent(this, MyService.class);
	 * intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
	 * intent.setData(uriSending.build()); intent.putExtra("startweek",
	 * startweek); intent.putExtra("endweek", endweek);
	 * intent.putExtra("action", action.toString());
	 * intent.putExtra("projectname", projectname);
	 * 
	 * switch (action) { case newproject: intent.putExtra("action",
	 * "newproject"); break;
	 * 
	 * case update: intent.putExtra("resourceid", targetresourceid); break; }
	 * 
	 * requestid = new Date().getTime(); intent.putExtra("requestid",
	 * requestid); startService(intent); }
	 */

	@Override
	protected void onStart() {
		super.onStart();

		registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}

	private void setColumns(int n, String[][] s) {
		columns = n;
		elements = new RowElement[columns];
		for (int i = 0; i < columns; i++) {
			elements[i] = new RowElement(this, display);
			elements[i].setText(s[i][0], s[i][1]);
			if (action == STRIPEACTIVITYACTIONS.update) {
				String temp = booking[i] + " %";
				elements[i].setPercentText(temp);
				elements[i].setNeededText("You want: " + temp);
			}
		}
	}

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
						Log.i("lefoglalt szazalek", "" + ratio);

						for (int i = from + 1; i < columns; i++) {
							elements[i].applyMyRatioToAll(ratio);
						}

					}
				}
			}
		}
		elements[from].buttonDownShowHide();
	}

	private void clearAll() {
		for (int i = 0; i < columns; i++) {
			elements[i].setInitialState();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.newprojectmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.send:
			/*
			 * implement the menu click method
			 */
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

	protected void RestoreInstanceState(Bundle savedInstanceState) {
		// super.onRestoreInstanceState(savedInstanceState);
		// Read values from the "savedInstanceState"-object and put them in your
		// textview
		Log.i(tag, "restore");

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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save the values you need from your textview into "outState"-object
		Log.i(tag, "onsave");
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

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "BroadcastReceiver");
			if (requestid == intent.getLongExtra("originalReqeustid", 0)) {
				loading.dismiss();

				if (intent.getBooleanExtra("Successful", false)) {
					/*
					 * Intent myIntent = new Intent(getApplicationContext(),
					 * Login.class);
					 * myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 * startActivity(myIntent);
					 */
					finish();
				} else {
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}