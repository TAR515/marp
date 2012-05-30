package edu.ubb.marp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class TestActivity2 extends Activity {

	private static final String tag = "TestActivity2";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * DatabaseHelper adat = new DatabaseHelper(getApplicationContext(),
		 * "Valami nev", null, 1); SQLiteDatabase mdb =
		 * adat.getWritableDatabase(); mdb.close();
		 */

		/*ContentResolver cr = getContentResolver();

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
		Cursor results = cr.query(rrr.build(), null, null, null, null);

		while (results.moveToNext()) {
			Log.i(tag, "_ID " + results.getInt(results.getColumnIndex("_id")));
			Log.i(tag,
					"nev " + results.getString(results.getColumnIndex("nev")));
			Log.i(tag,
					"szuletes "
							+ results.getString(results
									.getColumnIndex("szuletes")));
			Log.i(tag,
					"eletkor "
							+ results.getInt(results.getColumnIndex("eletkor")));
		}*/

		Log.i(tag, "beallitbol "+getSharedPreferences("sajat", MODE_PRIVATE).getInt("alparka", 0));
	}
}
