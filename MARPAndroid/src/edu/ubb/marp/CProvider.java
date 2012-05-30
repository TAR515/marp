package edu.ubb.marp;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import edu.ubb.marp.DatabaseContract;

public class CProvider extends ContentProvider {

	private static final String tag = "CProvider";

	private DatabaseHelper mOpenHelper;
	private SQLiteDatabase mdb;

	@Override
	public boolean onCreate() {
		Log.i(tag, "onCreate");

		mOpenHelper = new DatabaseHelper(getContext(),
				DatabaseContract.DATABASE_NAME, null,
				DatabaseContract.DATABASE_VERSION);

		mdb = mOpenHelper.getWritableDatabase();

		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (DatabaseContract.sUriMatcher.match(uri)) {
		case DatabaseContract.MULTIPLE:
			return "vnd.android.cursor.dir/" + DatabaseContract.AUTHORITY;
		case DatabaseContract.ROW:
			return "vnd.android.cursor.item/" + DatabaseContract.AUTHORITY;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.i(tag, "query");

		/*switch (DatabaseContract.sUriMatcher.match(uri)) {
		case DatabaseContract.MULTIPLE:
			if (TextUtils.isEmpty(sortOrder))
				sortOrder = "_ID ASC";
			break;

		case DatabaseContract.ROW:
			selection = "_ID = " + uri.getLastPathSegment();
			break;
		}*/

		// mdb = mOpenHelper.getWritableDatabase(); Log.i(tag,
		// "querying table: " + uri.getPathSegments().get(0));
		Cursor c = mdb.query(uri.getPathSegments().get(0), projection,
				selection, selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		Log.i(tag, "query end");
		return c;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// mdb = mOpenHelper.getWritableDatabase();
		long rowID = mdb.insert(uri.getPathSegments().get(0), null, values);
		Uri _uri = Uri.parse("content://" + DatabaseContract.AUTHORITY + "/"
				+ uri.getPathSegments().get(0) + "/" + rowID);
		getContext().getContentResolver().notifyChange(_uri, null);
		return _uri;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		Log.i(tag, "bulkInsert start");

		mdb.beginTransaction();

		for (int i = 0; i < values.length; i++) {
			mdb.insert(uri.getPathSegments().get(0), null, values[i]);
			// mdb.update(uri.getPathSegments().get(0), values[i],
			// "ResourceID="+ Integer.toString(i), null);
		}

		mdb.endTransaction();

		Log.i(tag, "bulkInsert end");
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		// mdb = mOpenHelper.getWritableDatabase();
		/*switch (DatabaseContract.sUriMatcher.match(uri)) {
		case DatabaseContract.MULTIPLE:
			count = mdb.update(uri.getPathSegments().get(0), values, selection,
					selectionArgs);
			break;

		case DatabaseContract.ROW:
			count = mdb.update(uri.getPathSegments().get(0), values, "_id = "
					+ uri.getLastPathSegment()
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}*/
		count = mdb.update(uri.getPathSegments().get(0), values, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		/*switch (DatabaseContract.sUriMatcher.match(uri)) {
		case DatabaseContract.MULTIPLE:
			count = mdb.delete(uri.getPathSegments().get(0), selection,
					selectionArgs);
			break;

		case DatabaseContract.ROW:
			count = mdb.delete(
					uri.getPathSegments().get(0),
					"_id = "
							+ uri.getLastPathSegment()
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}*/
		count = mdb.delete(uri.getPathSegments().get(0), selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public ContentProviderResult[] applyBatch(
			ArrayList<ContentProviderOperation> operations) {
		Log.i(tag, "applyBatch start");
		int i = 0;
		ContentProviderResult[] results = new ContentProviderResult[operations
				.size()];

		Iterator<ContentProviderOperation> it = operations.iterator();
		try {
			mdb.beginTransaction();

			ContentProviderOperation op;
			while (it.hasNext()) {
				// it.next().apply(this, results, i++);
				op = it.next();
				Log.i(tag, op.toString());
				op.apply(this, results, i++);
			}

			mdb.setTransactionSuccessful();
			mdb.endTransaction();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}

		Log.i(tag, "applyBatch end");
		return results;
	}
}
