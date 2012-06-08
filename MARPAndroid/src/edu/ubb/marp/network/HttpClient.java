package edu.ubb.marp.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

/**
 * AsyncTask class, which sends and receives the messages between the client and
 * the server
 * 
 * @author Rakosi Alpar, Vizer Arnold
 * 
 */
public class HttpClient extends AsyncTask<JSONArray, Integer, JSONArray> {
	/**
	 * Static variable, which contains the servers URL
	 */
	private static final String URL = "http://188.24.87.112:8080/MARP/AndroidServlet";
	/**
	 * Timeout constant
	 */
	private static final int TIMEOUT = 6000;
	/**
	 * Socket timeout constant
	 */
	private static final int SOTIMEOUT = 8000;

	/**
	 * The HttpClient object
	 */
	private static DefaultHttpClient httpclient;

	/**
	 * The service object
	 */
	private MyService service;
	/**
	 * It contains the JSONArray, which will be sent to the server
	 */
	private JSONArray requestJSON;
	/**
	 * Requestid, which identifies the request
	 */
	private long requestid;

	/**
	 * Public constructor, which initializes the variables
	 * 
	 * @param service
	 *            The service object
	 * @param requestid
	 *            Requestid, which identifies the request
	 */
	public HttpClient(MyService service, long requestid) {
		this.service = service;
		this.requestid = requestid;
		if (httpclient == null) {
			httpclient = new DefaultHttpClient();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected JSONArray doInBackground(JSONArray... jsonObjSend) {
		requestJSON = jsonObjSend[0];
		try {
			HttpPost httpPostRequest = new HttpPost(URL);

			StringEntity se;
			se = new StringEntity(requestJSON.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			httpPostRequest.setHeader("user-agent", "Android");

			HttpParams parameters = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parameters, TIMEOUT);
			HttpConnectionParams.setSoTimeout(parameters, SOTIMEOUT);

			httpclient.setParams(parameters);

			HttpResponse response = httpclient.execute(httpPostRequest);

			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				// convert content stream to a String
				String resultString = convertStreamToString(instream);
				instream.close();

				// Transform the String into a JSONObject
				JSONArray jsonArrayRecv = new JSONArray(resultString);

				return jsonArrayRecv;
			}

		} catch (Exception e) {
			e.printStackTrace();

			try {
				JSONArray array = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("error", 0);
				array.put(obj);
				return array;
			} catch (JSONException jsone) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Converts a stream to a String object
	 * 
	 * @param is
	 *            The input stream
	 * @return The String object
	 */
	private String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 * 
		 * (c) public domain:
		 * http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/
		 * 11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	protected void onPostExecute(JSONArray result) {
		service.setResults(requestJSON, result, requestid);
	}

}
