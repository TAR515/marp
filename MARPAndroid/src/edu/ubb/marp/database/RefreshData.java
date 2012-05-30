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
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

public class RefreshData extends AsyncTask<JSONArray, Integer, Integer> {

	private static final String tag = "RefreshData";
	private Uri uri;
	private ContentResolver cr;
	private String[] mainColumns;
	// private Intent originalIntent;
	private Long requestid;
	private MyService service;
	private Boolean complex;
	private boolean leaveOld;

	public RefreshData(Uri uri, ContentResolver cr, String[] mainColumns,
			Long requestid, MyService service, Boolean complex, boolean leaveOld) {
		this.uri = uri;
		this.cr = cr;
		this.mainColumns = mainColumns;
		// this.originalIntent = originalIntent;
		this.requestid = requestid;
		this.service = service;
		this.complex = complex;
		this.leaveOld = leaveOld;
	}

	@Override
	protected Integer doInBackground(JSONArray... params) {
		Log.i(tag, "refreshData");

		JSONArray results = params[0];

		String sortOrder = new String();
		sortOrder = sortOrder.concat(mainColumns[0]);
		for (int i = 1; i < mainColumns.length; i++)
			sortOrder = sortOrder.concat(", " + mainColumns[i]);

		Cursor c = null;
		try {
			if (complex) {
				c = cr.query(
						uri,
						null,
						mainColumns[0]
								+ "="
								+ Integer.toString(results.getJSONObject(0)
										.getInt(mainColumns[0])), null,
						sortOrder);
			} else {
				c = cr.query(uri, null, null, null, sortOrder);
			}
		} catch (JSONException e) {
		}
		c.moveToFirst();

		int numberOfColumns = c.getColumnCount();
		String[] columns = new String[numberOfColumns];

		// int cursorRows = c.getCount();
		// boolean[] ok = new boolean[cursorRows];

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
			// c.moveToFirst();
			temp = true;

			if (c.moveToFirst()) {
				for (i = 0; i < length; i++) {
					obj = results.getJSONObject(i);

					for (int k = 0; k < mainColumns.length; k++) {
						while ((temp)
								&& (c.getInt(c.getColumnIndex(mainColumns[k])) < obj
										.getInt(mainColumns[k]))) {
							if (!leaveOld) {
								Integer[] forDelete = new Integer[mainColumns.length];
								for (int l = 0; l < mainColumns.length; l++)
									forDelete[l] = c.getInt(c
											.getColumnIndex(mainColumns[l]));
								delete.add(forDelete);
							}
							temp = c.moveToNext();
						}
					}

					int k = 0;
					while ((!c.isAfterLast())
							&& (k < mainColumns.length)
							&& (c.getInt(c.getColumnIndex(mainColumns[k])) <= obj
									.getInt(mainColumns[k])))
						k++;

					if ((c.isAfterLast()) || (k < mainColumns.length)) {
						currentOperation = ContentProviderOperation
								.newInsert(newUri);

						for (j = 0; j < numberOfColumns; j++)
							currentOperation = currentOperation.withValue(
									columns[j], obj.get(columns[j]));

						operations.add(currentOperation.build());
					} else {
						j = 0;
						/*
						 * while ((j < notPrimaryColumns.size()) &&
						 * (c.getString(j).equals(obj
						 * .getString(columns[notPrimaryColumns.get(j)])))) {
						 */
						String s1 = c.getString(notPrimaryColumns.get(j));
						String s2 = obj.getString(columns[notPrimaryColumns
								.get(j)]);
						if (s2.equals("true")) {
							s2 = "1";
						} else if (s2.equals("false")) {
							s2 = "0";
						}
						// Log.i(tag, "s1 = "+s1);
						// Log.i(tag, "s2 = "+s2);
						/*
						 * while ((j < notPrimaryColumns.size()) &&
						 * (c.getString(notPrimaryColumns.get(j)).equals(obj
						 * .getString(columns[notPrimaryColumns.get(j)])))) {
						 */
						while ((j < notPrimaryColumns.size())
								&& (s1.equals(s2))) {
							s1 = c.getString(notPrimaryColumns.get(j));
							s2 = obj.getString(columns[notPrimaryColumns.get(j)]);
							if (s2.equals("true")) {
								s2 = "1";
							} else if (s2.equals("false")) {
								s2 = "0";
							}
							// Log.i(tag, "s1 = "+s1);
							// Log.i(tag, "s2 = "+s2);
							j++;
						}

						// if (j < numberOfColumns) {
						if (j < notPrimaryColumns.size()) {
							currentOperation = ContentProviderOperation
									.newUpdate(newUri);
							String selection = new String();
							selection = selection.concat(mainColumns[0]
									+ "="
									+ c.getString(c
											.getColumnIndex(mainColumns[0])));
							for (int l = 1; l < mainColumns.length; l++)
								selection = selection
										.concat(" AND "
												+ mainColumns[l]
												+ "="
												+ c.getString(c
														.getColumnIndex(mainColumns[l])));
							// currentOperation =
							// currentOperation.withSelection(mainColumn + "=" +
							// c.getString(0), null);
							currentOperation = currentOperation.withSelection(
									selection, null);

							for (j = 0; j < notPrimaryColumns.size(); j++)
								currentOperation = currentOperation.withValue(
										columns[notPrimaryColumns.get(j)], obj
												.get(columns[notPrimaryColumns
														.get(j)]));

							// Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
							// currentOperation.build().toString());
							operations.add(currentOperation.build());
						}

						temp = c.moveToNext();
					}
				}

				if (!c.isAfterLast() && (!leaveOld)) {
					Integer[] forDelete = new Integer[mainColumns.length];
					for (int l = 0; l < mainColumns.length; l++)
						forDelete[l] = c.getInt(c
								.getColumnIndex(mainColumns[l]));
					// delete.add(c.getInt(0));
					delete.add(forDelete);
					while (c.moveToNext()) {
						forDelete = new Integer[mainColumns.length];
						for (int l = 0; l < mainColumns.length; l++)
							forDelete[l] = c.getInt(c
									.getColumnIndex(mainColumns[l]));
						delete.add(forDelete);
					}
				}
				/*
				 * Integer[] forDelete=new Integer[mainColumns.length]; for(int
				 * l=0;l<mainColumns.length;l++)
				 * forDelete[l]=c.getInt(c.getColumnIndex(mainColumns[l]));
				 * delete.add(forDelete);
				 */

				if (!leaveOld) {
					for (i = 0; i < delete.size(); i++) {
						builder.path(uri.getPathSegments().get(0));
						// operations.add( ContentProviderOperation.newDelete(
						// builder.appendPath(delete.get(i).toString()) .build()
						// ).build());
						String selection = new String();
						selection = selection.concat(mainColumns[0] + "="
								+ Integer.toString(delete.get(i)[0]));
						for (j = 1; j < mainColumns.length; j++)
							selection = selection.concat(" AND "
									+ mainColumns[j] + "="
									+ Integer.toString(delete.get(i)[j]));
						operations.add(ContentProviderOperation
								.newDelete(builder.build())
								.withSelection(selection, null).build());
					}
				}

				cr.applyBatch(DatabaseContract.AUTHORITY, operations);
			} else {
				Log.i(tag, "else ag");
				Log.i(tag, Integer.toString(length));
				for (i = 0; i < length; i++) {
					obj = results.getJSONObject(i);
					Log.i(tag, obj.toString());

					currentOperation = ContentProviderOperation
							.newInsert(newUri);

					for (j = 0; j < numberOfColumns; j++)
						currentOperation = currentOperation.withValue(
								columns[j], obj.get(columns[j]));

					operations.add(currentOperation.build());
				}

				cr.applyBatch(DatabaseContract.AUTHORITY, operations);
			}
		} /*
		 * catch (JSONException e) { } catch (OperationApplicationException e) {
		 * } catch (RemoteException e) { }
		 */catch (Exception e) {
			return -1;
		}

		return 1;
	}

	protected void onPostExecute(Integer result) {
		Log.i(tag, "onPostExecute");

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
