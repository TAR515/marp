package edu.ubb.marp.network;

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
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * The service object, which coordinates the requests, and the answers for the
 * requests
 * 
 * @author Rakosi Alpar, Vizer Arnold
 */
public class MyService extends Service {
	private static final String tag = "MyService";

	/**
	 * The Looper thread
	 */
	private Looper mServiceLooper;
	/**
	 * Handler that receives messages from the thread
	 */
	private ServiceHandler mServiceHandler;
	/**
	 * The number of the pending requests
	 */
	private int pendingRequests;

	/**
	 * Handler that receives messages from the thread
	 */
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		/*
		 * This method gets the messages serialized, and calls the appropriate
		 * methods
		 */
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

			case USERMODIFICATIONS:
				userModifications(intent);
				break;

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

			case REQUESTOPERATIONS:
				requestOperations(intent);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * Start up the thread running the service. Note that we create a separate
	 * thread because the service normally runs in the process's main thread,
	 * which we don't want to block. We also make it background priority so
	 * CPU-intensive work will not disrupt our UI.
	 */
	@Override
	public void onCreate() {
		//
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		pendingRequests = 0;
	}

	/**
	 * For each start request, send a message to start a job and deliver the
	 * start ID so we know which request we're stopping when we finish the job
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	/**
	 * Don't provide binding, so return null
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.i(tag, "Service destroyed");
	}

	/**
	 * Inrements with 1 the number of the pending requests
	 */
	private synchronized void incRequests() {
		pendingRequests++;
	}

	/**
	 * Decrements with 1 the number of the pending requests
	 */
	private synchronized void decRequests() {
		pendingRequests--;
	}

	/**
	 * @return True, if there are pending requests
	 */
	private synchronized boolean isRequestPending() {
		return pendingRequests > 0;
	}

	/**
	 * Sends a login request
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void login(Intent intent) {
		String username = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.LOGINCMD);
			json.put("username", username);
			json.put("password", password);

			JSONArray array = new JSONArray();
			array.put(json);

			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	/**
	 * Send a query request
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void query(Intent intent) {
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.PROJECTRESOURCESCMD:
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

			JSONArray array = new JSONArray();
			array.put(json);
			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	/**
	 * Send a query reqeust without storing the results in the database
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void queryWithoutStoring(Intent intent) {
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

			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}

	}

	/**
	 * Sends a new project request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void newProject(Intent intent) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", Constants.NEWPROJECT);
			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

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

	/**
	 * Sends a project changing request to the server
	 * 
	 * @param intent
	 */
	private void changeProject(Intent intent) {
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.CHANGEPROJECTOPENEDSTATUS:
				json.put("openedstatus", intent.getBooleanExtra("openedstatus", false));
				break;
			case Constants.CHANGEPROJECTNAME:
				json.put("newprojectname", intent.getStringExtra("newprojectname"));
				break;
			case Constants.CHANGEPROJECTDEADLINE:
				json.put("newdeadline", intent.getIntExtra("newdeadline", 0));
				break;
			case Constants.CHANGEPROJECTNEXTRELEASE:
				json.put("newnextrelease", intent.getStringExtra("newnextrelease"));
				break;
			case Constants.CHANGEPROJECTCURRENTSTATUS:
				json.put("newcurrentstatus", intent.getStringExtra("newcurrentstatus"));
				break;
			}

			json.put("projectid", intent.getIntExtra("projectid", -1));

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			JSONArray array = new JSONArray();
			array.put(json);

			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	/**
	 * Sends an "add resoure to project" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void addResourceToProject(Intent intent) {

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

	/**
	 * Sends a "resource reservation modification" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void resourceReservationModification(Intent intent) {

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

	/**
	 * Sends a "resource modifications" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void resourceModifications(Intent intent) {
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
			case Constants.SETRESOURCEACTIVECMD:
				json.put("resourceid", intent.getIntExtra("resourceid", 0));
				json.put("currentweek", intent.getIntExtra("currentweek", 0));
				json.put("active", intent.getBooleanExtra("active", false));
				break;
			}

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			JSONArray array = new JSONArray();
			array.put(json);

			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	/**
	 * Sends a "user modifications" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void userModifications(Intent intent) {
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.INSERTNEWUSER:
				json.put("targetusername", intent.getStringExtra("targetusername"));
				json.put("targetpassword", intent.getStringExtra("targetpassword"));
				json.put("phonenumber", intent.getStringExtra("phonenumber"));
				json.put("email", intent.getStringExtra("email"));
				json.put("resourcename", intent.getStringExtra("resourcename"));
				json.put("active", intent.getBooleanExtra("resourcetypename", false));
				json.put("resourcegroupname", "Team1");
				break;
			}

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			JSONArray array = new JSONArray();
			array.put(json);

			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	/**
	 * Sends a "change username" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void changeUserName(Intent intent) {
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

	/**
	 * Sends a "change resource name" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void changeUserResourceName(Intent intent) {
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

	/**
	 * Sends a "change the users phonenumber" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void changeUserPhoneNumber(Intent intent) {
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

	/**
	 * Sends a "change the users e-mail address" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void changeUserEmail(Intent intent) {
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

	/**
	 * Sends a "change the users password" request to the server
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void changeUserPassword(Intent intent) {
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

	/**
	 * It contains operations, which could be affected on requests
	 * 
	 * @param intent
	 *            The intent, which received
	 */
	private void requestOperations(Intent intent) {
		Uri uri = intent.getData();
		int cmd = Integer.parseInt(uri.getPathSegments().get(0));

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		try {
			JSONObject json = new JSONObject();
			json.put("command", cmd);

			switch (cmd) {
			case Constants.ACCEPTREQUESTCMD:
				json.put("projectid", intent.getIntExtra("projectid", 0));
				json.put("targetresourceid", intent.getIntExtra("targetresourceid", 0));
				json.put("requestid", intent.getIntExtra("currentrequestid", 0));
				json.put("currentweek", intent.getIntExtra("currentweek", 0));
				json.put("updateratio", intent.getIntExtra("updateratio", 0));
				break;
			case Constants.REJECTREQUESTCMD:
				json.put("projectid", intent.getIntExtra("projectid", 0));
				json.put("resourceid", intent.getIntExtra("resourceid", 0));
				json.put("requestid", intent.getIntExtra("currentrequestid", 0));
				break;
			}

			json.put("username", pref.getString("username", ""));
			json.put("password", pref.getString("password", ""));

			JSONArray array = new JSONArray();
			array.put(json);

			new HttpClient(this, intent.getLongExtra("requestid", 0)).execute(array);
		} catch (JSONException e) {
		}
	}

	/**
	 * It gets the results of a request, and makes the appropriate operations
	 * 
	 * @param req
	 *            The request, which was sent to the server
	 * @param r
	 *            The results of the request
	 * @param requestid
	 *            The request id, which identifes the reqeust
	 */
	public synchronized void setResults(JSONArray req, JSONArray r, long requestid) {
		decRequests();
		Uri.Builder uri;
		JSONObject result;

		try {
			JSONObject request = req.getJSONObject(0);
			String mainColumns[];
			switch (request.getInt("command")) {
			case Constants.LOGINCMD:
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

			case Constants.PROJECTSCMD:
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

			case Constants.ALLPROJECTSCMD:
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

			case Constants.PROJECTRESOURCESCMD:
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
					uri.path(DatabaseContract.TABLE_BOOKINGASSIGNMENTS);
					uri.scheme("content");
					mainColumns = new String[1];
					mainColumns[0] = TABLE_BOOKING.RESOURCEID;
					new RefreshData(uri.build(), getContentResolver(), mainColumns, requestid, this, true, false).execute(r);
				}
				break;

			case Constants.QUERYUSER:
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
						boolean updateCmd = req.getJSONObject(0).getString("action").equals("update");

						int results[] = new int[r.length()];
						int currentProjectResults[] = new int[r.length()];
						for (int i = 0; i < r.length(); i++) {
							JSONObject obj = r.getJSONObject(i);
							results[i] = obj.getInt("ratiototal");
							if (updateCmd)
								currentProjectResults[i] = obj.getInt("ratioincurrentproject");
						}

						intent.putExtra("results", results);
						if (updateCmd)
							intent.putExtra("ratioincurrentproject", currentProjectResults);

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
					intent.putExtra("change", true);
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
					intent.putExtra("change", true);
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
					intent.putExtra("change", true);
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
					intent.putExtra("change", true);
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
					intent.putExtra("change", true);
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
						int resourceids[] = new int[r.length()];
						String resourcenames[] = new String[r.length()];
						for (int i = 0; i < r.length(); i++) {
							JSONObject obj = r.getJSONObject(i);
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

			case Constants.LOADINACTIVERESOURCES:
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
						int resourceids[] = new int[r.length()];
						String resourcenames[] = new String[r.length()];
						for (int i = 0; i < r.length(); i++) {
							JSONObject obj = r.getJSONObject(i);
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

			case Constants.INSERTNEWRESOURCE:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("resourcecreated") >= 0) {
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

			case Constants.INSERTNEWUSER:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("userinserted") >= 0) {
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

			case Constants.LOADREQUESTSCMD:
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
					uri.path(DatabaseContract.TABLE_REQUESTS);
					uri.scheme("content");

					mainColumns = new String[1];
					mainColumns[0] = TABLE_REQUESTS.REQUESTID;
					new RefreshData(uri.build(), getContentResolver(), mainColumns, requestid, this, false, false).execute(r);
				}
				break;

			case Constants.CHANGEPROJECTOPENEDSTATUS:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("projectopenstatuschanged") >= 0) {
						Intent intent = new Intent(Constants.BROADCAST_ACTION);
						intent.putExtra("originalReqeustid", requestid);
						intent.putExtra("Successful", true);
						intent.putExtra("changeProjectOpenedStatus", true);
						afterRefresh(intent);
					}
				} catch (JSONException errorRead) {
					Intent intent = new Intent(Constants.BROADCAST_ACTION);
					intent.putExtra("originalReqeustid", requestid);
					intent.putExtra("Successful", false);
					intent.putExtra("change", true);
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
			case Constants.CHANGEPROJECTNAME:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("projectnamechanged") >= 0) {
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
					intent.putExtra("change", true);
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
			case Constants.CHANGEPROJECTDEADLINE:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("projectdeadlinechanged") >= 0) {
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
					intent.putExtra("change", true);
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
			case Constants.CHANGEPROJECTNEXTRELEASE:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("projectnextreleasechanged") >= 0) {
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
					intent.putExtra("change", true);
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
			case Constants.CHANGEPROJECTCURRENTSTATUS:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("projectcurrentstatuschanged") >= 0) {
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
					intent.putExtra("change", true);
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

			case Constants.SETRESOURCEACTIVECMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("setactive") >= 0) {
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
					intent.putExtra("change", true);
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

			case Constants.ACCEPTREQUESTCMD:
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

			case Constants.REJECTREQUESTCMD:
				result = null;
				try {
					result = r.getJSONObject(0);
					if (result.getInt("requestremoved") >= 0) {
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
					intent.putExtra("change", true);
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
			}
		} catch (JSONException e) {
			Intent intent = new Intent(Constants.BROADCAST_ACTION);
			intent.putExtra("originalReqeustid", requestid);
			intent.putExtra("Successful", false);
			intent.putExtra("error", -40);
			afterRefresh(intent);
		} catch (Exception e) {
		}
	}

	public synchronized void afterRefresh(Intent intent) {
		sendBroadcast(intent);

		if (!isRequestPending()) {
			stopSelf();
		}
	}
}
