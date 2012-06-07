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
import android.util.Log;

public class HttpClient extends AsyncTask<JSONArray, Integer, JSONArray> {

	private static final String URL = "http://86.122.39.33:8080/MARP/AndroidServlet";
	private static final int TIMEOUT=6000;
	private static final int SOTIMEOUT=8000;
	// private static final String URL =
	// "http://192.168.1.100:8080/MARP/AndroidServlet";
	private static final String tag = "HttpClient";

	private static DefaultHttpClient httpclient;

	private MyService service;
	private JSONArray requestJSON;
	// private Intent originalIntent;
	private long requestid;

	public HttpClient(MyService service, long requestid) {
		this.service = service;
		// this.originalIntent = originalIntent;
		this.requestid = requestid;
		if (httpclient == null) {
			httpclient = new DefaultHttpClient();
		}
	}

	@Override
	protected JSONArray doInBackground(JSONArray... jsonObjSend) {
		requestJSON = jsonObjSend[0];
		try {
			// DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPostRequest = new HttpPost(URL);

			StringEntity se;
			se = new StringEntity(requestJSON.toString());
			// se = new StringEntity(new JSONObject().toString());

			Log.i(tag, requestJSON.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			httpPostRequest.setHeader("user-agent", "Android");
			// httpPostRequest.setHeader("Accept-Encoding", "gzip");

			HttpParams parameters=httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(parameters, TIMEOUT);
			HttpConnectionParams.setSoTimeout(parameters, SOTIMEOUT);
			
			httpclient.setParams(parameters);
			
			HttpResponse response = httpclient.execute(httpPostRequest);
			Log.i(tag, "response jott");

			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				// convert content stream to a String
				String resultString = convertStreamToString(instream);
				instream.close();

				Log.i(tag, resultString);

				// Transform the String into a JSONObject
				// JSONObject jsonObjRecv = new JSONObject(resultString);
				JSONArray jsonArrayRecv = new JSONArray(resultString);

				// return jsonObjRecv;
				return jsonArrayRecv;
			}
			/*JSONArray array1=new JSONArray();
			JSONArray array=new JSONArray();
			array.put(10);
			array.put(20);
			array.put(40);
			array.put(30);
			array1.put(array);
			return array1;*/

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, e.getMessage());

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
		/*
		 * try { JSONArray array=new JSONArray(); JSONObject obj=new
		 * JSONObject();
		 * 
		 * obj.put("ProjectID", 2); obj.put("OpenedStatus", 22);
		 * obj.put("DeadLine", 22); obj.put("ProjectName", "proba22");
		 * obj.put("NextRelease", 33); obj.put("CurrentStatus", 33);
		 * obj.put("Role", "User"); array.put(obj);
		 * 
		 * obj=new JSONObject(); obj.put("ProjectID", 4);
		 * obj.put("OpenedStatus", 44); obj.put("DeadLine", 44);
		 * obj.put("ProjectName", "proba44"); obj.put("NextRelease", 55);
		 * obj.put("CurrentStatus", 55); obj.put("Role", "User");
		 * array.put(obj);
		 * 
		 * obj=new JSONObject(); obj.put("ProjectID", 8);
		 * obj.put("OpenedStatus", 88); obj.put("DeadLine", 88);
		 * obj.put("ProjectName", "proba88"); obj.put("NextRelease", 99);
		 * obj.put("CurrentStatus", 99); obj.put("Role", "User");
		 * array.put(obj);
		 * 
		 * //return new JSONArray().put(new JSONObject().put("eredmeny",
		 * "mukodik")); return array; } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } return null;
		 */
	}

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
		Log.i(tag, "onPostExecute");
		service.setResults(requestJSON, result, requestid);
	}

}
