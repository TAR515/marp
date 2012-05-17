package edu.ubb.arp.logic.commands;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public interface BaseCommandOperationsInterface {

	public JSONArray setError(int errorCode);

	public JSONArray addString(String key, String element, JSONArray response);

	public JSONArray addInt(String key, int element, JSONArray response);

	public JSONArray addBool(String key, boolean element, JSONArray response);

	public String getString(int index, String key, JSONArray request) throws JSONException;

	public int getInt(int index, String key, JSONArray request) throws JSONException;

	public boolean getBool(int index, String key, JSONArray request) throws JSONException;

	public boolean errorCheck(JSONArray response);

	public JSONArray getJSONArray(int index, JSONArray request) throws JSONException;

	public JSONObject getJSONObject(int index, JSONArray request) throws JSONException;

	public JSONArray makeCheckUserRequest(JSONArray request) throws JSONException;

	public boolean checkResponseIfLoginSuccessfull(JSONArray response);
	
	public JSONArray changeInt(int index, String key, int value, JSONArray request) throws JSONException;

}
