package edu.ubb.marp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.network.MyService;

import android.content.ContentProviderOperation.Builder;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

public class RefreshData2 extends AsyncTask<JSONArray, Integer, Integer> {

	private static final String tag = "RefreshData";
	private Uri uri;
	private ContentResolver cr;
	private String mainColumn;
	private Intent originalIntent;
	private MyService service;

	public RefreshData2(Uri uri, ContentResolver cr, String mainColumn,
			Intent originalIntent, MyService service) {
		this.uri = uri;
		this.cr = cr;
		this.mainColumn = mainColumn;
		this.originalIntent = originalIntent;
		this.service = service;
	}

	@Override
	protected Integer doInBackground(JSONArray... params) {
		Log.i(tag, "refreshData");

		JSONArray results = params[0];

		Cursor c = cr.query(uri, null, null, null, mainColumn);
		c.moveToFirst();

		int numberOfColumns = c.getColumnCount();
		String[] columns = new String[numberOfColumns];

		// int cursorRows = c.getCount();
		// boolean[] ok = new boolean[cursorRows];

		ArrayList<Integer> delete = new ArrayList<Integer>();
		boolean temp;

		int i, j;

		for (i = 0; i < numberOfColumns; i++)
			columns[i] = c.getColumnName(i);

		/*
		 * for (i = 0; i < cursorRows; i++) ok[i] = false;
		 */

		int length = results.length();

		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

		Uri.Builder builder = new Uri.Builder();
		builder.authority(DatabaseContract.PROVIDER_NAME);
		builder.path(uri.getPathSegments().get(0));
		builder.scheme("content");
		Uri newUri = builder.build();

		Builder currentOperation;

		try {
			JSONObject obj;
			c.moveToFirst();
			temp = true;

			if (c.moveToFirst()) {
				for (i = 0; i < length; i++) {
					obj = results.getJSONObject(i);

					while ((temp)&&(c.getInt(0) < obj.getInt(mainColumn))) {
						delete.add(c.getInt(0));
						temp = c.moveToNext();
					}

					if ((c.isAfterLast())
							|| (c.getInt(0) > obj.getInt(mainColumn))) {
						currentOperation = ContentProviderOperation
								.newInsert(newUri);

						for (j = 0; j < numberOfColumns; j++)
							currentOperation = currentOperation.withValue(
									columns[j], obj.get(columns[j]));

						operations.add(currentOperation.build());
					} else {
						j = 1;
						while ((j < numberOfColumns)
								&& (c.getString(j).equals(obj
										.getString(columns[j])))) {
							j++;
						}

						if (j < numberOfColumns) {
							currentOperation = ContentProviderOperation
									.newUpdate(newUri);
							currentOperation = currentOperation.withSelection(
									mainColumn + "=" + c.getString(0), null);

							for (j = 1; j < numberOfColumns; j++)
								currentOperation = currentOperation.withValue(
										columns[j], obj.get(columns[j]));

							Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", currentOperation.build().toString());
							operations.add(currentOperation.build());
						}

						temp = c.moveToNext();
					}
				}

				if (!c.isAfterLast()) {
					delete.add(c.getInt(0));
					while (c.moveToNext())
						delete.add(c.getInt(0));
				}

				for (i = 0; i < delete.size(); i++) {
					builder.path(uri.getPathSegments().get(0));
					operations.add(ContentProviderOperation.newDelete(
							builder.appendPath(delete.get(i).toString())
									.build()).build());
				}

				cr.applyBatch(DatabaseContract.AUTHORITY, operations);
			} else {
				for (i = 0; i < length; i++) {
					obj = results.getJSONObject(i);
					
					currentOperation = ContentProviderOperation
							.newInsert(newUri);

					for (j = 0; j < numberOfColumns; j++)
						currentOperation = currentOperation.withValue(
								columns[j], obj.get(columns[j]));

					operations.add(currentOperation.build());
				}
				
				cr.applyBatch(DatabaseContract.AUTHORITY, operations);
			}
		} catch (JSONException e) {
		} catch (OperationApplicationException e) {
		} catch (RemoteException e) {
		}

		return null;
	}

	protected void onPostExecute(Integer result) {
		Log.i(tag, "onPostExecute");

		Intent intent = new Intent(Constants.BROADCAST_ACTION);
		intent.putExtra("originalIntent", originalIntent);

		service.afterRefresh(intent);
	}

}
