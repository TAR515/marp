package edu.ubb.arp.logic.commands;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public interface BaseCommandOperationsInterface {
	
	/**
	 * Creates an error JSONArray.
	 * @param errorCode -inf - 0
	 * @return Returns an error message in JSONArray if the errorCode <= 0 else returns unknown error.
	 */
	public JSONArray setError(int errorCode);
	
	/**
	 * adds a string to response JSONArray
	 * @param key is the JSONobject's name 
	 * @param element is the JSONobject's value
	 * @return returns a JSONArray containing the key and element pair
	 */
	public JSONArray addString(String key, String element, JSONArray response);
	
	/**
	 * Appends response with the parameter element, if the response were null create a new response with the parameter element.
	 * @param key is not null.
	 * @param element
	 * @param response 
	 * @return unknown error if the parameter: key is null, else response appended with the new element.
	 */
	public JSONArray addInt(String key, int element, JSONArray response);
	
	/**
	 * Appends response with the parameter element, if the response were null create a new response with the parameter element.
	 * @param key is not null.
	 * @param element
	 * @param response 
	 * @return unknown error if the parameter: key is null, else response appended with the new element.
	 */
	public JSONArray addBool(String key, boolean element, JSONArray response);
	
	/**
	 * 
	 * @param index is the index of the name/value pair which i need
	 * @param key is the name from the JSONArray i need 
	 * @param request is the JSONArray from where i need the value 
	 * @return returns a string which contains the desired value 
	 * @throws JSONException if the request do not contains the given element.
	 */
	public String getString(int index, String key, JSONArray request) throws JSONException;
	
	/**
	 * 
	 * @param index is the index of the name/value pair which i need
	 * @param key is the name from the JSONArray i need 
	 * @param request is the JSONArray from where i need the value 
	 * @return returns an int which contains the desired value 
	 * @throws JSONException if the request do not contains the given element.
	 */
	public int getInt(int index, String key, JSONArray request) throws JSONException;
	
	/**
	 * 
	 * @param index is the index of the name/value pair which i need
	 * @param key is the value from the JSONArray i need 
	 * @param request is the JSONArray from where i need the value 
	 * @return returns a boolean value which contains the desired value 
	 * @throws JSONException if the request do not contains the given element.
	 */
	public boolean getBool(int index, String key, JSONArray request) throws JSONException;
	

    /**
	 * 
	 * @param response is a JSONArray which is checked if contains errors 
	 * @return true - if the response is an error message, false if not.
	 */
	public boolean errorCheck(JSONArray response);
	
	/**
	 * @param index is the index of the required JSONArray
	 * @param request is the JSONArray from which i need the JSONArray 
	 * @return returns the desired JSONArray
	 */
	public JSONArray getJSONArray(int index, JSONArray request) throws JSONException;
	
	/**
	 * @param index is the index of the required JSONObject
	 * @param request is the JSONArray from which i need the JSONObject 
	 * @return returns the desired JSONObject
	 */
	public JSONObject getJSONObject(int index, JSONArray request) throws JSONException;
	
	/**
	 * @param
	 * @return
	 */
	public JSONArray makeCheckUserRequest(JSONArray request) throws JSONException;
	
	/**
	 * @return returns true if user exists
	 */
	public boolean checkResponseIfLoginSuccessfull(JSONArray response);
	
	/**
	 * @param index is the index of the value i want to change
	 * @param key is the name from the name/value pair which i want to change
	 * @param value is the new value of the name/value pair
	 * @param request is a JSONArray in from which i want to change the value of a name/value pair
	 * @return  returns a JSONArray containing the changed value
	 */
	public JSONArray changeInt(int index, String key, int value, JSONArray request) throws JSONException;

}
