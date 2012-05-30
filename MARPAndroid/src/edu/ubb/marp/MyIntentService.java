package edu.ubb.marp;


import edu.ubb.marp.Constants.ACTIONS;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {

	// private HttpClient client;
	private static final String tag = "MyService";

	public MyIntentService() {
		super("MARPService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(tag, "onHandleIntent " + intent.toString());

		String action = intent.getStringExtra("ACTION");

		switch (ACTIONS.valueOf(action)) {
		case LOGIN:
			login(intent);
			break;

		default:
			break;
		}
	}

	private void login() {

	}

	private void login(Intent intent) {
		Log.i(tag, "login");
		String username = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");

		// client.login(username, password);

		/*try {
			new HttpClient().execute(new JSONObject().put("valami", "ertek"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
