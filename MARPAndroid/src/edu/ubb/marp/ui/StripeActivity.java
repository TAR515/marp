package edu.ubb.marp.ui;

import java.util.Date;
import java.util.StringTokenizer;

import org.json.JSONArray;

import edu.ubb.marp.Constants;
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
	private String startweek;
	private String endweek;
	private String projectname;
	private int userid;
	private Context context;

	RowElement[] elements;
	Button applyToAll;
	int columns;
	Display display;
	/*String loadstr[][] = { { "2001.03.10", "0" }, { "2001.03.10", "10" }, { "2001.03.10", "20" }, { "2001.03.10", "30" },
			{ "2001.03.10", "40" }, { "2001.03.10", "50" }, { "2001.03.10", "60" }, { "2001.03.10", "70" }, { "2001.03.10", "80" },
			{ "2001.03.10", "90" } };*/
	String loadstr[][];
	boolean klicked = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(tag, "oncreate");

		context=this;
		
		if((savedInstanceState==null)||(savedInstanceState.isEmpty())){
			bundle = getIntent().getExtras();

			startweek = bundle.getString("startweek");
			endweek = bundle.getString("endweek");
			projectname = bundle.getString("projectname");

			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

			Uri.Builder uri = new Uri.Builder();
			uri = new Uri.Builder();
			uri.authority(DatabaseContract.PROVIDER_NAME);
			uri.path(DatabaseContract.TABLE_USERS);
			uri.scheme("content");

			ContentResolver cr = getContentResolver();

			String projection[] = { TABLE_USERS.USERID };

			Cursor c = cr.query(uri.build(), projection, TABLE_USERS.USERNAME + " = '" + pref.getString("username", "") + "'", null, null);

			c.moveToFirst();

			userid = c.getInt(c.getColumnIndex(TABLE_USERS.USERID));
			
			sendRequest();
		}
		else
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

	private void sendRequest() {
		loading = ProgressDialog.show(this, "Loading", "Please wait...");

		Uri.Builder uriSending = new Uri.Builder();
		uriSending.authority(DatabaseContract.PROVIDER_NAME);
		uriSending.path(Integer.toString(Constants.QUERYAVAILABLERESOURCESCODE));
		uriSending.scheme("content");

		Intent intent = new Intent(this, MyService.class);
		intent.putExtra("ACTION", "QUERYWITHOUTSTORING");
		intent.setData(uriSending.build());
		intent.putExtra("resourceid", userid);
		intent.putExtra("startweek", startweek);
		intent.putExtra("endweek", endweek);
		intent.putExtra("action", "newproject");
		intent.putExtra("projectname", projectname);
		requestid = new Date().getTime();
		intent.putExtra("requestid", requestid);
		startService(intent);
	}

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
			}else{
				Intent intent = new Intent(this, MyService.class);
				intent.putExtra("ACTION", "NEWPROJECT");
				intent.putExtra("projectname", bundle.getString("projectname"));
				intent.putExtra("openedstatus", bundle.getBoolean("openedstatus"));
				intent.putExtra("startweek", bundle.getString("startweek"));
				intent.putExtra("endweek", bundle.getString("endweek"));
				intent.putExtra("deadline", bundle.getString("deadline"));
				intent.putExtra("nextrelease", bundle.getString("nextrelease"));
				intent.putExtra("statusname", bundle.getString("statusname"));
			
				int i=columns-1;
				while((i>=0)&&(!elements[i].isGreen)&&(!elements[i].isYellow))
					i--;
				int currentColumns=i+1;
				
				int[] ratios=new int[currentColumns];
				boolean[] isRequest=new boolean[currentColumns];
				for(i=0;i<currentColumns;i++){
					StringTokenizer st = new StringTokenizer(elements[i].getPercentText());
					ratios[i]=Integer.parseInt(st.nextToken());
					isRequest[i] = elements[i].isYellow();
				}
				intent.putExtra("ratios", ratios);
				intent.putExtra("isrequest", isRequest);

				requestid = new Date().getTime();
				intent.putExtra("requestid", requestid);

				startService(intent);
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
		//super.onRestoreInstanceState(savedInstanceState);
		// Read values from the "savedInstanceState"-object and put them in your
		// textview
		Log.i(tag, "restore");

		requestid = savedInstanceState.getLong("requestid");
		bundle = savedInstanceState.getBundle("bundle");
		startweek = savedInstanceState.getString("startweeek");
		endweek = savedInstanceState.getString("endweek");
		userid = savedInstanceState.getInt("userid");
		columns = savedInstanceState.getInt("columns");
		projectname = savedInstanceState.getString("projectname");
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
		outState.putString("startweeek", startweek);
		outState.putString("endweek", endweek);
		outState.putInt("userid", userid);
		outState.putInt("columns", columns);
		outState.putString("projectname", projectname);
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
					// finish();
					int[] results = intent.getIntArrayExtra("results");

					if (results != null) {
						display = getWindowManager().getDefaultDisplay();

						loadstr = new String[results.length][2];

						String s;
						int j = Integer.parseInt(startweek);
						for (int i = 0; i < results.length; i++) {
							s=Constants.convertWeekToDate(j++);
							Log.i(tag, s);
							loadstr[i][0] = /*Constants.convertWeekToDate(j++)*/s;
							loadstr[i][1] = Integer.toString(results[i]);
						}

						setColumns(results.length, loadstr);

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
						scroll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
								TableRow.LayoutParams.MATCH_PARENT));

						HorizontalScrollView horizontal = new HorizontalScrollView(context);

						scroll.addView(l);
						horizontal.addView(scroll);
						setContentView(horizontal);
					}
				} else {
					messageBoxShow(Constants.getErrorMessage(intent.getIntExtra("error", 0)), "Error");
				}
			}
		}
	};
}