package edu.ubb.arp.logic.commands;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class BaseCommandOperations {
	private static final Logger logger = Logger.getLogger(BaseCommandOperations.class);
	// Setter methods
	
	public JSONArray setError(int errorCode, JSONArray response) {
		JSONArray result = response;
		
		if (result.size() > 0) { // The response is not empty.
			while(result.size() > 0) {
				result.remove(0);
			}
		}
		
		JSONObject errmsg = new JSONObject();
		errmsg.put("error", new Integer(errorCode));
		result.add(errmsg);
		
		return result;
	}
	
	/**
	 * Appends response with the parameter element, if the response were null create a new response with the parameter element.
	 * @param key
	 * @param element
	 * @param response
	 * @return
	 */
	public JSONArray addInt(String key, int element, JSONArray response) {
		JSONArray result = response;
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		obj.put(key, new Integer(element));
		result.add(obj);
		
		return result;
	}
	
	
	
	// Getter methods
	
	public String getString(int index, String key, JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		String result = null;
	
		if (index <= request.size()) {
			JSONObject obj = request.getJSONObject(index);
			try {
				result = obj.getString(key);
			} catch (JSONException e) {
				logger.error(getClass().getName() + methodName + e);
				result = null;
			}
		}
		
		return result;
	}
	
	// Others
	
    /**
	 * 
	 * @param response
	 * @return true - if the response is an error message, false if not.
	 */
	public boolean errorCheck(JSONArray response) {
		if (response.size() == 1) {
			JSONObject obj = response.getJSONObject(0);
			try {
				obj.getInt("error");
				return true;
			} catch(JSONException e) {
				return false;
			}
		} 
		
		return false;
	}
	
}
