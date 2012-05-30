package edu.ubb.marp;

import java.util.ArrayList;
import java.util.Random;

import edu.ubb.marp.Constants.ACTIONS;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.network.MyService;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity implements OnClickListener {

	private static final String tag = "TestActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * DatabaseHelper adat = new DatabaseHelper(getApplicationContext(),
		 * "Valami nev", null, 1); SQLiteDatabase mdb =
		 * adat.getWritableDatabase(); mdb.close();
		 */

		setContentView(R.layout.main);

		Button gomb = (Button) findViewById(R.id.button1);
		gomb.setOnClickListener(this);

		gomb = (Button) findViewById(R.id.button2);
		gomb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(tag, "Frissites eleje");
				ContentResolver cr = getContentResolver();
				Uri.Builder rrr = new Uri.Builder();
				rrr.authority(DatabaseContract.PROVIDER_NAME);
				rrr.path("Booking");
				rrr.scheme("content");
				Uri ur = rrr.build();

				ContentValues values = new ContentValues();
				Random rand = new Random();

				ArrayList<ContentProviderOperation> proba = new ArrayList<ContentProviderOperation>();

				for (int i = 0; i < 1010; i++) {
					values.put("ProjectID", rand.nextInt(15000));
					values.put("Week", rand.nextInt(15000));
					values.put("Ratio", (float) rand.nextInt(15000));

					// cr.update(rrr.build(), values,
					// "ResourceID="+Integer.toString(i), null);

					proba.add(ContentProviderOperation
							.newUpdate(ur)
							.withSelection("ResourceID=" + Integer.toString(i),
									null).withValues(values).build());

					values.clear();
				}

				Log.i(tag, "Batch elott");

				try {
					cr.applyBatch(DatabaseContract.AUTHORITY, proba);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (OperationApplicationException e) {
					e.printStackTrace();
				}

				Log.i(tag, "Frissites vege");
			}
		});

		gomb = (Button) findViewById(R.id.button3);
		gomb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				/*
				 * Log.i(tag, "query start");
				 * 
				 * ContentResolver cr=getContentResolver(); Uri.Builder rrr =
				 * new Uri.Builder();
				 * rrr.authority(DatabaseContract.PROVIDER_NAME);
				 * rrr.path("Booking"); rrr.scheme("content");
				 * 
				 * cr.query(rrr.build(), null, null, null, null);
				 * 
				 * Log.i(tag, "query end");
				 */

				// Intent proba=new inte
			}
		});

		gomb = (Button) findViewById(R.id.login);
		gomb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent intent=new Intent(getApplicationContext(),
				// TestActivity2.class);

				// SharedPreferences beall = getPreferences(MODE_PRIVATE);
				/*
				 * SharedPreferences beall=getSharedPreferences("sajat",
				 * MODE_PRIVATE); Editor editor = beall.edit();
				 * editor.putInt("alparka", 20); editor.commit();
				 * 
				 * Intent intent = new Intent(getApplicationContext(),
				 * TestActivity2.class); startActivity(intent);
				 * 
				 * Log.i(tag, "Sharedprefbol " +
				 * getPreferences(MODE_PRIVATE).getInt("alparka", 0));
				 */

				Intent intent = new Intent(getApplicationContext(),
						MyService.class);
				intent.putExtra("ACTION", "LOGIN");
				intent.putExtra("username", "rakosi");
				intent.putExtra("password", "jelszo");
				startService(intent);
				Log.i(tag, "Thread " + Thread.currentThread().getId());
			}
		});

		gomb = (Button) findViewById(R.id.login);
		gomb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Uri.Builder uri = new Uri.Builder();

				Intent intent = new Intent(getApplicationContext(),
						MyService.class);
				intent.putExtra("ACTION", "QUERY");
				intent.setData(uri.build());
				startService(intent);
				Log.i(tag, "Thread " + Thread.currentThread().getId());
			}
		});

		gomb = (Button) findViewById(R.id.projects);
		gomb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Uri.Builder uri = new Uri.Builder();
				uri.authority(DatabaseContract.PROVIDER_NAME);
				uri.path("1");
				uri.appendPath("Projects");
				uri.scheme("content");

				Intent intent = new Intent(getApplicationContext(),
						MyService.class);
				intent.putExtra("ACTION", "QUERY");
				intent.setData(uri.build());
				startService(intent);
			}
		});

		gomb = (Button) findViewById(R.id.projectsquery);
		gomb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Uri.Builder uri = new Uri.Builder();
				uri = new Uri.Builder();
				uri.authority(DatabaseContract.PROVIDER_NAME);
				uri.path("Projects");
				uri.scheme("content");

				ContentResolver cr = getContentResolver();
				Cursor c = cr.query(uri.build(), null, null, null, null);

				TextView txt = (TextView) findViewById(R.id.textView1);

				while (c.moveToNext()) {
					txt.append(c.getString(0) + " ");
					txt.append(c.getString(1) + " ");
					txt.append(c.getString(2) + " ");
					txt.append(c.getString(3) + " ");
					txt.append(c.getString(4) + " ");
					txt.append(c.getString(5) + " ");
					txt.append("\r\n");
				}
			}
		});

		ContentResolver cr = getContentResolver();

		Uri.Builder rrr = new Uri.Builder();
		rrr.authority(DatabaseContract.PROVIDER_NAME);
		rrr.path("Proba");
		rrr.scheme("content");

		ContentValues values = new ContentValues();
		values.put("nev", "Alpar");
		values.put("szuletes", "1991");
		values.put("eletkor", 20);
		Log.i(tag, "Insert URI " + cr.insert(rrr.build(), values).toString());

		values.clear();
		values.put("nev", "Arnold");
		values.put("szuletes", "1985");
		values.put("eletkor", 26);
		Log.i(tag, "Insert URI " + cr.insert(rrr.build(), values).toString());

		Log.i(tag, rrr.toString());
		// rrr.appendPath("0");
		/*
		 * Cursor results = cr.query(rrr.build(), null, null, null, null);
		 * 
		 * while (results.moveToNext()) { Log.i(tag, "_ID " +
		 * results.getInt(results.getColumnIndex("_id"))); Log.i(tag, "nev " +
		 * results.getString(results.getColumnIndex("nev"))); Log.i(tag,
		 * "szuletes " + results.getString(results
		 * .getColumnIndex("szuletes"))); Log.i(tag, "eletkor " +
		 * results.getInt(results.getColumnIndex("eletkor"))); }
		 */

	}

	public void onClick(View v) {
		/*
		 * Intent intent=new Intent(getApplicationContext(),
		 * TestActivity2.class); startActivity(intent);
		 */

		ContentResolver cr = getContentResolver();
		Uri.Builder rrr = new Uri.Builder();
		rrr.authority(DatabaseContract.PROVIDER_NAME);
		rrr.path("Booking");
		rrr.scheme("content");

		Log.i(tag, rrr.build().toString());
		/*
		 * ContentValues values=new ContentValues(); Random rand=new Random();
		 * 
		 * for(int i=0;i<1000;i++){ values.put("ResourceID", i);
		 * values.put("ProjectID", rand.nextInt(15000)); values.put("Week",
		 * rand.nextInt(15000)); values.put("Ratio",
		 * (float)rand.nextInt(15000));
		 * 
		 * cr.insert(rrr.build(), values);
		 * 
		 * 
		 * 
		 * values.clear(); }
		 */

		ContentValues[] values = new ContentValues[1000];
		Random rand = new Random();

		for (int i = 0; i < 1000; i++) {
			values[i] = new ContentValues();
			// values[i].put("ResourceID", i);
			values[i].put("ProjectID", rand.nextInt(15000));
			values[i].put("Week", rand.nextInt(15000));
			values[i].put("Ratio", (float) rand.nextInt(15000));
		}

		Log.i(tag, "Generalas eleje");
		cr.bulkInsert(rrr.build(), values);

		Log.i(tag, "VEGE VAAAAAAAAAAAAAAAN");
	}
}
