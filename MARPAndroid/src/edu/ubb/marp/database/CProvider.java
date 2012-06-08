package edu.ubb.marp.database;

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

/**
 * Content provider for accessing the datas
 * 
 * @author Rakosi Alpar, Vizer Arnold
 */
public class CProvider extends ContentProvider {

	/**
	 * DatabaseHelper object
	 */
	private DatabaseHelper mOpenHelper;
	/**
	 * SQLiteDatabase object for accessing the database
	 */
	private SQLiteDatabase mdb;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {

		mOpenHelper = new DatabaseHelper(getContext(), DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);

		mdb = mOpenHelper.getWritableDatabase();

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor c = mdb.query(uri.getPathSegments().get(0), projection, selection, selectionArgs, null, null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = mdb.insert(uri.getPathSegments().get(0), null, values);
		Uri _uri = Uri.parse("content://" + DatabaseContract.AUTHORITY + "/" + uri.getPathSegments().get(0) + "/" + rowID);
		getContext().getContentResolver().notifyChange(_uri, null);
		return _uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#bulkInsert(android.net.Uri,
	 * android.content.ContentValues[])
	 */
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		mdb.beginTransaction();

		for (int i = 0; i < values.length; i++) {
			mdb.insert(uri.getPathSegments().get(0), null, values[i]);
		}

		mdb.endTransaction();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = mdb.update(uri.getPathSegments().get(0), values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = mdb.delete(uri.getPathSegments().get(0), selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#applyBatch(java.util.ArrayList)
	 */
	@Override
	public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) {
		int i = 0;
		ContentProviderResult[] results = new ContentProviderResult[operations.size()];

		Iterator<ContentProviderOperation> it = operations.iterator();
		try {
			mdb.beginTransaction();

			ContentProviderOperation op;
			while (it.hasNext()) {
				op = it.next();
				op.apply(this, results, i++);
			}

			mdb.setTransactionSuccessful();
			mdb.endTransaction();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
		return results;
	}
}
