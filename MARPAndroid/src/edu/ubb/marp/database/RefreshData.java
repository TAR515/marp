package edu.ubb.marp.database;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubb.marp.Constants;
import edu.ubb.marp.network.MyService;

import android.content.ContentProviderOperation.Builder;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * AsyncTask class, which makes the database refreshing
 * 
 * @author Rakosi Alpar, Vizer Arnold
 */
public class RefreshData extends AsyncTask<JSONArray, Integer, Integer> {
	/**
	 * Uri object, which identifies the table, which has to be updated
	 */
	private Uri uri;
	/**
	 * ContentResolver object, through this we can access the Content provider
	 */
	private ContentResolver cr;
	/**
	 * String array, which contains the names of the primary key columns of the
	 * table
	 */
	private String[] mainColumns;
	/**
	 * Requestid, which identifies the request
	 */
	private Long requestid;
	/**
	 * The service object
	 */
	private MyService service;
	/**
	 * This variable is true, if the table has more than one primary key column
	 */
	private Boolean complex;
	/**
	 * If this variable is true, old values are leaved in the database
	 */
	private boolean leaveOld;

	/**
	 * Public constructor, which initializes the variables
	 * 
	 * @param uri
	 *            Uri object, which identifies the table, which has to be
	 *            updated
	 * @param cr
	 *            ContentResolver object, through this we can access the Content
	 *            provider
	 * @param mainColumns
	 *            String array, which contains the names of the primary key
	 *            columns of the table
	 * @param requestid
	 *            Requestid, which identifies the request
	 * @param service
	 *            The service object
	 * @param complex
	 *            This variable is true, if the table has more than one primary
	 *            key column
	 * @param leaveOld
	 *            If this variable is true, old values are leaved in the
	 *            database
	 */
	public RefreshData(Uri uri, ContentResolver cr, String[] mainColumns, Long requestid, MyService service, Boolean complex,
			boolean leaveOld) {
		this.uri = uri;
		this.cr = cr;
		this.mainColumns = mainColumns;
		this.requestid = requestid;
		this.service = service;
		this.complex = complex;
		this.leaveOld = leaveOld;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Integer doInBackground(JSONArray... params) {
		JSONArray results = params[0];

		String sortOrder = new String();
		sortOrder = sortOrder.concat(mainColumns[0]);
		for (int i = 1; i < mainColumns.length; i++)
			sortOrder = sortOrder.concat(", " + mainColumns[i]);

		Cursor c = null;
		try {
			if (complex) {
				c = cr.query(uri, null, mainColumns[0] + "=" + Integer.toString(results.getJSONObject(0).getInt(mainColumns[0])), null,
						sortOrder);
			} else {
				c = cr.query(uri, null, null, null, sortOrder);
			}
		} catch (JSONException e) {
		}
		c.moveToFirst();

		int numberOfColumns = c.getColumnCount();
		String[] columns = new String[numberOfColumns];

		ArrayList<Integer[]> delete = new ArrayList<Integer[]>();
		ArrayList<Integer> notPrimaryColumns = new ArrayList<Integer>();
		boolean temp;

		int i, j;

		for (i = 0; i < numberOfColumns; i++) {
			columns[i] = c.getColumnName(i);
			temp = true;
			for (j = 0; j < mainColumns.length; j++)
				if (columns[i].equals(mainColumns[j]))
					temp = false;
			if (temp)
				notPrimaryColumns.add(i);
		}

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
			temp = true;

			if (c.moveToFirst()) {
				for (i = 0; i < length; i++) {
					obj = results.getJSONObject(i);

					int[] prevValues = new int[mainColumns.length];
					int k = 0;
					boolean end = false;
					while ((k < mainColumns.length) && (!end)) {
						while ((temp) && (c.getInt(c.getColumnIndex(mainColumns[k])) < obj.getInt(mainColumns[k]))) {
							if (!leaveOld) {
								Integer[] forDelete = new Integer[mainColumns.length];
								for (int l = 0; l < mainColumns.length; l++)
									forDelete[l] = c.getInt(c.getColumnIndex(mainColumns[l]));
								delete.add(forDelete);
							}
							for (int l = 0; l < k; l++)
								if (c.getInt(c.getColumnIndex(mainColumns[l])) != prevValues[l])
									end = true;
							if (!end)
								temp = c.moveToNext();
							else
								temp = !end;
						}
						prevValues[k] = obj.getInt(mainColumns[k]);
						k++;
					}

					k = 0;
					while ((!c.isAfterLast()) && (k < mainColumns.length)
							&& (c.getInt(c.getColumnIndex(mainColumns[k])) <= obj.getInt(mainColumns[k])))
						k++;

					if ((c.isAfterLast()) || (k < mainColumns.length)) {
						currentOperation = ContentProviderOperation.newInsert(newUri);

						for (j = 0; j < numberOfColumns; j++)
							currentOperation = currentOperation.withValue(columns[j], obj.get(columns[j]));

						operations.add(currentOperation.build());
					} else {
						j = 0;
						String s1 = c.getString(notPrimaryColumns.get(j));
						String s2 = obj.getString(columns[notPrimaryColumns.get(j)]);
						if (s2.equals("true")) {
							s2 = "1";
						} else if (s2.equals("false")) {
							s2 = "0";
						}
						while ((j < notPrimaryColumns.size()) && (s1.equals(s2))) {
							j++;
							try {
								s1 = c.getString(notPrimaryColumns.get(j));
								s2 = obj.getString(columns[notPrimaryColumns.get(j)]);
								if (s2.equals("true")) {
									s2 = "1";
								} else if (s2.equals("false")) {
									s2 = "0";
								}
							} catch (Exception se) {
							}
						}

						if (j < notPrimaryColumns.size()) {
							currentOperation = ContentProviderOperation.newUpdate(newUri);
							String selection = new String();
							selection = selection.concat(mainColumns[0] + "=" + c.getString(c.getColumnIndex(mainColumns[0])));
							for (int l = 1; l < mainColumns.length; l++)
								selection = selection
										.concat(" AND " + mainColumns[l] + "=" + c.getString(c.getColumnIndex(mainColumns[l])));
							currentOperation = currentOperation.withSelection(selection, null);

							for (j = 0; j < notPrimaryColumns.size(); j++)
								currentOperation = currentOperation.withValue(columns[notPrimaryColumns.get(j)],
										obj.get(columns[notPrimaryColumns.get(j)]));

							operations.add(currentOperation.build());
						}

						temp = c.moveToNext();
					}
				}

				if (!c.isAfterLast() && (!leaveOld)) {
					Integer[] forDelete = new Integer[mainColumns.length];
					for (int l = 0; l < mainColumns.length; l++)
						forDelete[l] = c.getInt(c.getColumnIndex(mainColumns[l]));
					delete.add(forDelete);
					while (c.moveToNext()) {
						forDelete = new Integer[mainColumns.length];
						for (int l = 0; l < mainColumns.length; l++)
							forDelete[l] = c.getInt(c.getColumnIndex(mainColumns[l]));
						delete.add(forDelete);
					}
				}

				if (!leaveOld) {
					for (i = 0; i < delete.size(); i++) {
						builder.path(uri.getPathSegments().get(0));
						String selection = new String();
						selection = selection.concat(mainColumns[0] + "=" + Integer.toString(delete.get(i)[0]));
						for (j = 1; j < mainColumns.length; j++)
							selection = selection.concat(" AND " + mainColumns[j] + "=" + Integer.toString(delete.get(i)[j]));
						operations.add(ContentProviderOperation.newDelete(builder.build()).withSelection(selection, null).build());
					}
				}

				cr.applyBatch(DatabaseContract.AUTHORITY, operations);
			} else {
				for (i = 0; i < length; i++) {
					obj = results.getJSONObject(i);

					currentOperation = ContentProviderOperation.newInsert(newUri);

					for (j = 0; j < numberOfColumns; j++)
						currentOperation = currentOperation.withValue(columns[j], obj.get(columns[j]));

					operations.add(currentOperation.build());
				}

				cr.applyBatch(DatabaseContract.AUTHORITY, operations);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	protected void onPostExecute(Integer result) {
		if (requestid != null) {
			Intent intent = new Intent(Constants.BROADCAST_ACTION);
			intent.putExtra("originalReqeustid", requestid);

			if (result > 0)
				intent.putExtra("Successful", true);
			else
				intent.putExtra("Error", result);

			service.afterRefresh(intent);
		}
	}
}
