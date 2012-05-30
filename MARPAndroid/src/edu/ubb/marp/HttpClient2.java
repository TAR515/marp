package edu.ubb.marp;

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
import org.json.JSONObject;

public class HttpClient2 {
	public JSONObject SendHttpPost(String URL, JSONObject jsonObjSend) {

		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPostRequest = new HttpPost(URL);

			StringEntity se;
			se = new StringEntity(jsonObjSend.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			httpPostRequest.setHeader("user-agent", "Android");
			// httpPostRequest.setHeader("Accept-Encoding", "gzip");

			// long t = System.currentTimeMillis();
			HttpResponse response = httpclient.execute(httpPostRequest);

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

				// Transform the String into a JSONObject
				JSONObject jsonObjRecv = new JSONObject(resultString);

				return jsonObjRecv;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
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
	
	public boolean login(){
		return false;
	}
}
