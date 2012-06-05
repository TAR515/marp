package edu.ubb.marp.network;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubb.marp.Constants;
import edu.ubb.marp.Constants.ACTIONS;
import edu.ubb.marp.database.DatabaseContract;
import edu.ubb.marp.database.DatabaseContract.*;
import edu.ubb.marp.database.RefreshData;
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

			incRequests();
			String action = intent.getStringExtra("ACTION");

			switch (ACTIONS.valueOf(action)) {
			case LOGIN:
				login(intent);
				break;

			case QUERY:
				query(intent);
				break;

			case QUERYWITHOUTSTORING:
				queryWithoutStoring(intent);
				break;

			case NEWPROJECT:
				newProject(intent);
				break;

			case CHANGEPROJECT:
				changeProject(intent);
				break;
				
			case ADDRESOURCETOPROJECT:
				addResourceToProject(intent);
				break;

			case RESOURCERESERVATIONMODIFICATION:
				resourceReservationModification(intent);
				break;

			case RESOURCEMODIFICATIONS:
				resourceModifications(intent);
				break;

			case REQUESTS:
				requests(intent);
				
			case CHANGEUSERNAME:
				changeUserName(intent);
				break;
				
			case CHANGEUSERRESOURCENAME:
				changeUserResourceName(intent);
				break;
				
			case CHANGEUSERPHONENUMBER:
				changeUserPhoneNumber(intent);
				break;
				
			case CHANGEUSEREMAIL:
				changeUserEmail(intent);
				break;
				
			case CHANGEUSERPASSWORD:
				changeUserPassword(intent);
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
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
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

			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	private void query(Intent intent) {
		Log.i(tag, "query");
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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
			case Constants.QUERYUSER:
				if (intent.getBooleanExtra("self", true))
					json.put("targetusername", pref.getString("username", ""));
				else
					json.put("targetusername", intent.getStringExtra("targetusername"));
				break;
			case Constants.LOADASSIGNMENTSCMD:
				json.put("currentweek", intent.getIntExtra("currentweek", 0));
				break;
			}

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			// json.put("uri", uri);
			JSONArray array = new JSONArray();
			array.put(json);

			Log.i(tag, "httpclient elott " + json.toString());
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	private void queryWithoutStoring(Intent intent) {
		Log.i(tag, "querywithoutstoring");
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.QUERYAVAILABLERESOURCESCODE:
				json.put("resourceid", intent.getIntExtra("resourceid", 0));
				json.put("startweek", intent.getIntExtra("startweek", 0));
				json.put("endweek", intent.getIntExtra("endweek", 0));
				json.put("projectname", intent.getStringExtra("projectname"));
				json.put("action", intent.getStringExtra("action"));
				break;
			}

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			JSONArray array = new JSONArray();
			array.put(json);

			Log.i(tag, "httpclient elott " + json.toString());
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}

	}

	private void newProject(Intent intent) {
		Log.i(tag, "newProject");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.NEWPROJECT);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			/*intent.putExtra("resourceid", bundle.getInt("resourceid"));
					intent.putExtra("projectname", bundle.getString("projectname"));
					intent.putExtra("openedstatus", bundle.getBoolean("openedstatus"));
					intent.putExtra("startweek", bundle.getInt("startweek"));
					intent.putExtra("endweek", bundle.getInt("endweek"));
					intent.putExtra("deadline", bundle.getInt("deadline"));
					intent.putExtra("nextrelease", bundle.getString("nextrelease"));
					intent.putExtra("statusname", bundle.getString("statusname"));*/
			
			json.put("resourceid", intent.getIntExtra("resourceid", 0));
			json.put("projectname", intent.getStringExtra("projectname"));
			json.put("openedstatus", intent.getBooleanExtra("openedstatus", false));
			json.put("startweek", intent.getIntExtra("startweek", 0));
			json.put("endweek", intent.getIntExtra("endweek", 0));
			json.put("deadline", intent.getIntExtra("deadline", 0));
			json.put("nextrelease", intent.getStringExtra("nextrelease"));
			json.put("statusname", intent.getStringExtra("statusname"));

			JSONArray elements = new JSONArray();
			int[] intUpdateRatios = intent.getIntArrayExtra("updateratios");
			int[] intRequestRatios = intent.getIntArrayExtra("requestratios");
			for (int i = 0; i < intUpdateRatios.length; i++) {
				JSONObject obj = new JSONObject();
				obj.put("insertratio", intUpdateRatios[i]);
				obj.put("requestratio", intRequestRatios[i]);
				elements.put(obj);
			}

			JSONArray array = new JSONArray();
			array.put(json);
			array.put(elements);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	private void changeProject(Intent intent) {
		Log.i(tag, "changeProject");
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.CHANGEPROJECTOPENEDSTATUS:
				// json.put("projectid", uri.getPathSegments().get(1));
				json.put("openedstatus", intent.getBooleanExtra("openedstatus", false));
				break;
			case Constants.CHANGEPROJECTNAME:
				json.put("newprojectname", intent.getStringExtra("newprojectname"));
				break;
			case Constants.CHANGEPROJECTDEADLINE:
				json.put("newdeadline", intent.getStringExtra("newdeadline"));
				break;
			case Constants.CHANGEPROJECTNEXTRELEASE:
				json.put("newnextrelease", intent.getStringExtra("newnextrelease"));
				break;
			case Constants.CHANGEPROJECTCURRENTSTATUS:
				json.put("newcurrentstatus", intent.getStringExtra("newcurrentstatus"));
				break;
			}
			// TODO Ezek utan kell varni valaszt?

			json.put("projectid", intent.getIntExtra("projectid", -1));

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			// json.put("uri", uri);
			JSONArray array = new JSONArray();
			array.put(json);

			Log.i(tag, "httpclient elott " + json.toString());
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}
	
	private void addResourceToProject(Intent intent){
		Log.i(tag, "addResourceToProject");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.ADDRESOURCETOPROJECTCMD);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			json.put("projectid", intent.getIntExtra("projectid", 0));
			json.put("startweek", intent.getIntExtra("startweek", 0));
			json.put("endweek", intent.getIntExtra("endweek", 0));
			json.put("targetresourceid", intent.getIntExtra("targetresourceid", 0));
			json.put("senderresourceid", intent.getIntExtra("senderresourceid", 0));
			json.put("isleader", intent.getBooleanExtra("isleader", false));

			JSONArray elements = new JSONArray();
			int[] intUpdateRatios = intent.getIntArrayExtra("updateratios");
			int[] intRequestRatios = intent.getIntArrayExtra("requestratios");
			for (int i = 0; i < intUpdateRatios.length; i++) {
				JSONObject obj = new JSONObject();
				obj.put("insertratio", intUpdateRatios[i]);
				obj.put("requestratio", intRequestRatios[i]);
				elements.put(obj);
			}

			JSONArray array = new JSONArray();
			array.put(json);
			array.put(elements);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	private void resourceReservationModification(Intent intent) {
		Log.i(tag, "resourceReservationModification");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.UPDATERESOURCERESERVATION);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			json.put("projectid", intent.getIntExtra("projectid", 0));
			json.put("startweek", intent.getIntExtra("startweek", 0));
			json.put("endweek", intent.getIntExtra("endweek", 0));
			json.put("targetresourceid", intent.getIntExtra("targetresourceid", 0));
			json.put("senderresourceid", intent.getIntExtra("senderresourceid", 0));

			JSONArray elements = new JSONArray();
			int[] intUpdateRatios = intent.getIntArrayExtra("updateratios");
			int[] intRequestRatios = intent.getIntArrayExtra("requestratios");
			for (int i = 0; i < intUpdateRatios.length; i++) {
				JSONObject obj = new JSONObject();
				obj.put("updateratio", intUpdateRatios[i]);
				obj.put("requestratio", intRequestRatios[i]);
				elements.put(obj);
			}

			JSONArray array = new JSONArray();
			array.put(json);
			array.put(elements);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	private void resourceModifications(Intent intent) {
		Log.i(tag, "resourceModifications");
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.INSERTNEWRESOURCE:
				// json.put("projectid", uri.getPathSegments().get(1));
				json.put("resourcename", intent.getStringExtra("resourcename"));
				json.put("active", intent.getBooleanExtra("active", false));
				json.put("resourcetypename", intent.getStringExtra("resourcetypename"));
				json.put("resourcegroupname", intent.getStringExtra("resourcegroupname"));
				break;
			case Constants.ADDRESOURCETOGROUP:
				json.put("resourcename", intent.getStringExtra("resourcename"));
				json.put("resourcename", intent.getStringExtra("resourcename"));
				json.put("groupname", intent.getStringExtra("groupname"));
				break;
			case Constants.REMOVERESOURCEFROMGROUP:
				json.put("resourcename", intent.getStringExtra("resourcename"));
				json.put("groupname", intent.getStringExtra("groupname"));
				break;
			case Constants.UPDATERESOURCE:
				json.put("oldresourcename", intent.getStringExtra("oldresourcename"));
				json.put("oldactive", intent.getBooleanExtra("oldactive", false));
				json.put("oldresourcetypename", intent.getStringExtra("oldresourcetypename"));
				json.put("oldresourcegroupname", intent.getStringExtra("oldresourcegroupname"));
				json.put("newresourcename", intent.getStringExtra("newresourcename"));
				json.put("newactive", intent.getBooleanExtra("newactive", false));
				json.put("newresourcetypename", intent.getStringExtra("newresourcetypename"));
				json.put("newresourcegroupname", intent.getStringExtra("newresourcegroupname"));
				break;
			case Constants.BUYSELLRESOURCE:
				json.put("resourcename", intent.getStringExtra("resourcename"));
				json.put("active", intent.getBooleanExtra("active", false));
				break;
			}
			// TODO Ezek utan kell varni valaszt?

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			// json.put("uri", uri);
			JSONArray array = new JSONArray();
			array.put(json);

			Log.i(tag, "httpclient elott " + json.toString());
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}
	
	private void changeUserName(Intent intent){
		Log.i(tag, "changeUserName");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.CHANGEUSERNAMECMD);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			json.put("newusername", intent.getStringExtra("newusername"));

			JSONArray array = new JSONArray();
			array.put(json);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}
	
	private void changeUserResourceName(Intent intent){
		Log.i(tag, "changeUserResourceName");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.CHANGEUSERRESOURCENAMECMD);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			json.put("newresourcename", intent.getStringExtra("newresourcename"));

			JSONArray array = new JSONArray();
			array.put(json);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}
	
	private void changeUserPhoneNumber(Intent intent){
		Log.i(tag, "changeUserPhoneNumber");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.CHANGEUSERPHONENUMBERCMD);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			json.put("newphonenumber", intent.getStringExtra("newphonenumber"));

			JSONArray array = new JSONArray();
			array.put(json);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}
	
	private void changeUserEmail(Intent intent){
		Log.i(tag, "changeUserEmail");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.CHANGEUSEREMAILCMD);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			json.put("newemail", intent.getStringExtra("newemail"));

			JSONArray array = new JSONArray();
			array.put(json);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}
	
	private void changeUserPassword(Intent intent){
		Log.i(tag, "changeUserPassword");

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.CHANGEUSERPASSWORDCMD);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			json.put("newpassword", intent.getStringExtra("newpassword"));

			JSONArray array = new JSONArray();
			array.put(json);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	private void requests(Intent intent) {
		Log.i(tag, "resourceCommands");
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.CREATENEWREQUESTFORUSER:
				break;
			case Constants.CREATENEWREQUESTFORRESOURCE:
				break;
			case Constants.REMOVEREQUESTFROMSOMEBODY:
				json.put("resourceid", intent.getStringExtra("resourceid"));
				json.put("requestid", intent.getStringExtra("requestid"));
				json.put("projectid", intent.getStringExtra("projectid"));
				break;
			case Constants.REMOVEEXPIREDREQUESTS:
				json.put("currentweek", intent.getStringExtra("currentweek"));
				break;
			case Constants.UPDATEREQUESTRATIOOFUSER:
				break;
			case Constants.UPDATEREQUESTRATIOOFRATIO:
				break;
			}
			// TODO Ezek utan kell varni valaszt?

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			// json.put("uri", uri);
			JSONArray array = new JSONArray();
			array.put(json);

			Log.i(tag, "httpclient elott " + json.toString());
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	public synchronized void setResults(JSONArray req, JSONArray r, long requestid) {
		decRequests();
		Uri.Builder uri;
		JSONObject result;

		try {
			JSONObject request = req.getJSONObject(0);
			String mainColumns[];
			switch (request.getInt("command")) {
			case 0:// Login
				result = r.getJSONObject(0);
				try {
					if (result.getInt("existsuser") == 1) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorLogin) {
					try {
						if (result.getInt("error") <= 0) {
							Intent intent = new Intent(Constants.BROADCAST_ACTION);
							intent.putExtra("originalReqeustid", requestid);
							intent.putExtra("Successful", false);
							intent.putExtra("error", result.getInt("error"));
							afterRefresh(intent);
						}
					} catch (JSONException errorRead) {
						errorRead.printStackTrace();
					}
				}
				break;

			case 1:// Query - projects
					// Uri uri = (Uri) request.get("uri");
				result = r.getJSONObject(0);
				try {
					if (result.getInt("error") <= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", false);
						intent.putExtra("error", result.getInt("error"));
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(DatabaseContract.TABLE_PROJECTS);
					uri.scheme("content");

					for (int i = 0; i < r.length(); i++) {
						JSONObject obj = r.getJSONObject(i);
						if (obj.getBoolean("isleader") == true)
							obj.put("isleader", "Leader");
						else
							obj.put("isleader", "User");
						r.put(i, obj);
					}

					mainColumns = new String[1];
					mainColumns[0] = TABLE_PROJECTS.PROJECTID;
					new RefreshData(uri.build(), getContentResolver(), mainColumns, requestid, this, false, false).execute(r);
				}

				break;

			case 2:// Query - booking + resources
				try {
					result = r.getJSONObject(0);
					if (result.getInt("error") <= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", false);
						intent.putExtra("error", result.getInt("error"));
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					incRequests();
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(DatabaseContract.TABLE_BOOKING);
					uri.scheme("content");
					mainColumns = new String[3];
					mainColumns[0] = TABLE_BOOKING.PROJECTID;
					mainColumns[1] = TABLE_BOOKING.RESOURCEID;
					mainColumns[2] = TABLE_BOOKING.WEEK;
					new RefreshData(uri.build(), getContentResolver(), mainColumns, requestid, this, true, false)
							.execute(r.getJSONArray(0));

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
					new RefreshData(uri.build(), getContentResolver(), mainColumns, requestid, this, false, true)
							.execute(r.getJSONArray(1));
				}
				break;
				
			case Constants.LOADASSIGNMENTSCMD:
				try {
					result = r.getJSONObject(0);
					if (result.getInt("error") <= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", false);
						intent.putExtra("error", result.getInt("error"));
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					incRequests();
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(DatabaseContract.TABLE_BOOKING);
					uri.scheme("content");
					mainColumns = new String[3];
					mainColumns[0] = TABLE_BOOKING.PROJECTID;
					mainColumns[1] = TABLE_BOOKING.RESOURCEID;
					mainColumns[2] = TABLE_BOOKING.WEEK;
					new RefreshData(uri.build(), getContentResolver(), mainColumns, requestid, this, true, false)
							.execute(r.getJSONArray(0));
				}
				break;

			/*case 201: // New project
				try {
					result = r.getJSONObject(0);
					if (result.getInt("error") <= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", false);
						intent.putExtra("error", result.getInt("error"));
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", true);
					afterRefresh(intent);
				}
				break;*/

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
				result = r.getJSONObject(0);
				try {
					if (result.getInt("error") <= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", false);
						intent.putExtra("error", result.getInt("error"));
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					uri = new Uri.Builder();
					uri.authority(DatabaseContract.PROVIDER_NAME);
					uri.path(DatabaseContract.TABLE_USERS);
					uri.scheme("content");
					mainColumns = new String[1];
					mainColumns[0] = TABLE_USERS.USERID;
					new RefreshData(uri.build(), getContentResolver(), mainColumns, requestid, this, false, false).execute(r);
				}
				break;

			case Constants.QUERYAVAILABLERESOURCESCODE:
				try {
					result = r.getJSONObject(0);
					if (result.getInt("error") <= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", false);
						intent.putExtra("error", result.getInt("error"));
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", true);

					try {
						//JSONArray array = r.getJSONArray(0);

						int results[] = new int[r.length()];
						for (int i = 0; i < r.length(); i++)
							results[i] = r.getJSONObject(i).getInt("ratiototal");

						intent.putExtra("results", results);

						afterRefresh(intent);
					} catch (JSONException errorReal) {
						errorReal.printStackTrace();
					}
				}
				break;
				
			case Constants.NEWPROJECT:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("projectcreated") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;
				
			case Constants.ADDRESOURCETOPROJECTCMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("resourceaddedtoproject") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;

			case Constants.UPDATERESOURCERESERVATION:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("resourceratioupdated") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;
				
			case Constants.CHANGEUSERNAMECMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("usernamechanged") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						intent.putExtra("change", true);
						intent.putExtra("changeUsername", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;
				
			case Constants.CHANGEUSERRESOURCENAMECMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("userresourcenamechanged") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						intent.putExtra("change", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;
				
			case Constants.CHANGEUSEREMAILCMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("useremailchanged") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						intent.putExtra("change", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;
				
			case Constants.CHANGEUSERPHONENUMBERCMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("userphonenumberchanged") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						intent.putExtra("change", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;
				
			case Constants.CHANGEUSERPASSWORDCMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("userpasswordchanged") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						intent.putExtra("changePassword", true);
						intent.putExtra("change", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					int errorCode = -1;
					if (result != null)
						try {
							errorCode = result.getInt("error");
						} catch (JSONException errorCodeException) {
							errorCode = -1;
						}
					intent.putExtra("error", errorCode);
					afterRefresh(intent);
				}
				break;
				
			case Constants.LOADRESOURCES:
				try {
					result = r.getJSONObject(0);
					if (result.getInt("error") <= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", false);
						intent.putExtra("Resources", true);
						intent.putExtra("error", result.getInt("error"));
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", true);
					intent.putExtra("Resources", true);

					try {
						//JSONArray array = r.getJSONArray(0);

						int resourceids[] = new int[r.length()];
						String resourcenames[] = new String[r.length()];
						for (int i = 0; i < r.length(); i++){
							JSONObject obj=r.getJSONObject(i);
							resourceids[i] = obj.getInt("resourceid");
							resourcenames[i] = obj.getString("resourcename");
						}

						intent.putExtra("resourceid", resourceids);
						intent.putExtra("resourcename", resourcenames);

						afterRefresh(intent);
					} catch (JSONException errorReal) {
						errorReal.printStackTrace();
					}
				}
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
}
