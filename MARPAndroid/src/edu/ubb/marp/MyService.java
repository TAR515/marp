package edu.ubb.marp;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubb.marp.Constants.ACTIONS;
import edu.ubb.marp.DatabaseContract.TABLE_BOOKING;
import edu.ubb.marp.DatabaseContract.TABLE_PROJECTS;
import edu.ubb.marp.DatabaseContract.TABLE_RESOURCEISUSER;
import edu.ubb.marp.DatabaseContract.TABLE_RESOURCES;
import edu.ubb.marp.DatabaseContract.TABLE_USERS;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyService extends Service {
	private static final String tag = "MyService";

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private int pendingRequests;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			Intent intent = (Intent) msg.obj;

			Log.i(tag, "handleMessage " + intent.toString());
			Log.i(tag, "Threadhandle " + Thread.currentThread().getId());

			incRequests();
			String action = intent.getStringExtra("ACTION");

			switch (ACTIONS.valueOf(action)) {
			case LOGIN:
				login(intent);
				break;

			case QUERY:
				query(intent);
				break;

			default:
				break;
			}

			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			/*
			 * if (!isRequestPending()) stopSelf(msg.arg1);
			 */
		}
	}

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		pendingRequests = 0;

		Log.i(tag, "Thread " + Thread.currentThread().getId());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
		Log.i(tag, "Service destroyed");
	}

	private synchronized void incRequests() {
		pendingRequests++;
	}

	private synchronized void decRequests() {
		pendingRequests--;
	}

	private synchronized boolean isRequestPending() {
		return pendingRequests > 0;
	}

	public synchronized void setResults(JSONArray req, JSONArray r,
			long requestid) {
		decRequests();
		Uri.Builder uri;

		try {
			JSONObject request = req.getJSONObject(0);
			String mainColumns[];
			switch (request.getInt("command")) {
			case 0:// Login
				JSONObject result = r.getJSONObject(0);
				try {
					if (result.getInt("existsuser") == 1) {
						SharedPreferences pref = PreferenceManager
								.getDefaultSharedPreferences(getApplicationContext());
						Editor editor = pref.edit();

						editor.putString("username",
								request.getString("username"));
						editor.putString("password",
								request.getString("password"));

						editor.apply();

						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						// sendBroadcast(intent);
						afterRefresh(intent);
					}
				} catch (JSONException e) {
					//TODO error code catching
				}
				break;

			case 1:// Query - projects
					// Uri uri = (Uri) request.get("uri");
				Log.i(tag, "130as case");
				uri = new Uri.Builder();
				uri.authority(DatabaseContract.PROVIDER_NAME);
				uri.path(DatabaseContract.TABLE_PROJECTS);
				uri.scheme("content");

				mainColumns = new String[1];
				mainColumns[0] = TABLE_PROJECTS.PROJECTID;
				new RefreshData(uri.build(), getContentResolver(), mainColumns,
						requestid, this, false).execute(r);
				break;

			case 2:// Query - booking + resources
				Log.i(tag, "1es case");
				incRequests();
				uri = new Uri.Builder();
				uri.authority(DatabaseContract.PROVIDER_NAME);
				uri.path(DatabaseContract.TABLE_BOOKING);
				uri.scheme("content");
				mainColumns = new String[3];
				mainColumns[0] = TABLE_BOOKING.PROJECTID;
				mainColumns[1] = TABLE_BOOKING.RESOURCEID;
				mainColumns[2] = TABLE_BOOKING.WEEK;
				new RefreshData(uri.build(), getContentResolver(), mainColumns,
						requestid, this, true).execute(r.getJSONArray(0));

				uri.path(DatabaseContract.TABLE_RESOURCES);

				JSONArray jsonarray = r.getJSONArray(1);
				JSONObject jsonobj;
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobj = jsonarray.getJSONObject(i);
					try {
						jsonobj.getString("username");
					} catch (JSONException e) {
						jsonobj.put("username", "");
					}
				}

				mainColumns = new String[1];
				mainColumns[0] = TABLE_RESOURCES.RESOURCEID;
				new RefreshData(uri.build(), getContentResolver(), mainColumns,
						requestid, this, false).execute(r.getJSONArray(1));
				break;

			/*
			 * case 3:// Query - resourceisUser + users uri = new Uri.Builder();
			 * uri.authority(DatabaseContract.PROVIDER_NAME);
			 * uri.path(DatabaseContract.TABLE_RESOURCEISUSER);
			 * uri.scheme("content"); new RefreshData(uri.build(),
			 * getContentResolver(), TABLE_RESOURCEISUSER.RESOURCEID, null,
			 * this).execute(r.getJSONArray(0));
			 * 
			 * uri.path(DatabaseContract.TABLE_USERS); new
			 * RefreshData(uri.build(), getContentResolver(),
			 * TABLE_USERS.USERID, originalIntent,
			 * this).execute(r.getJSONArray(1)); break;
			 */

			case 131:// Query - MyAccount
				uri = new Uri.Builder();
				uri.authority(DatabaseContract.PROVIDER_NAME);
				uri.path(DatabaseContract.TABLE_USERS);
				uri.scheme("content");
				mainColumns = new String[1];
				mainColumns[0] = TABLE_USERS.USERID;
				new RefreshData(uri.build(), getContentResolver(), mainColumns,
						requestid, this, false).execute(r);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		/*
		 * if (!isRequestPending()){ stopSelf(); Log.i(tag,
		 * "elvileg leallitja"); }
		 */
	}

	public synchronized void afterRefresh(Intent intent) {
		Log.i(tag, "afterrefresh");
		sendBroadcast(intent);

		if (!isRequestPending()) {
			stopSelf();
			Log.i(tag, "elvileg leallitja");
		}
	}

	/*
	 * private void login() { Log.i(tag, "login()");
	 * 
	 * SharedPreferences pref = PreferenceManager
	 * .getDefaultSharedPreferences(getApplicationContext());
	 * 
	 * try { JSONObject json = new JSONObject(); json.put("command", 0);
	 * json.put("username", pref.getString("username", ""));
	 * json.put("password", pref.getString("password", ""));
	 * 
	 * new HttpClient(this).execute(json); } catch (JSONException e) { } }
	 */

	private void login(Intent intent) {
		Log.i(tag, "login(intent)");
		String username = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");

		try {
			JSONObject json = new JSONObject();
			json.put("command", 0);
			json.put("username", username);
			json.put("password", password);

			JSONArray array = new JSONArray();
			array.put(json);

			new HttpClient(this, intent.getLongExtra("requestid", 0))
					.execute(array);
		} catch (JSONException e) {
		}
	}

	private void query(Intent intent) {
		Log.i(tag, "query");
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case 2:
				json.put("projectid", uri.getPathSegments().get(1));
				break;
			case 3:
				json.put("resourceid", uri.getPathSegments().get(1));
				break;
			}

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			// json.put("uri", uri);
			JSONArray array = new JSONArray();
			array.put(json);

			Log.i(tag, "httpclient elott " + json.toString());
			new HttpClient(this, intent.getLongExtra("requestid", 0))
					.execute(array);
		} catch (JSONException e) {
		}
	}
}
