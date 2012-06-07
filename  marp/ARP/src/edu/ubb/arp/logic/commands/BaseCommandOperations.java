package edu.ubb.arp.logic.commands;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


public class BaseCommandOperations implements BaseCommandOperationsInterface {
	private static final Logger logger = Logger.getLogger(BaseCommandOperations.class);
	// Setter methods
	
	/**
	 * Creates an error JSONArray.
	 * @param errorCode -inf - 0
	 * @return Returns an error message in JSONArray if the errorCode <= 0 else returns unknown error.
	 */
	public JSONArray setError(int errorCode) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = new JSONArray();
		JSONObject errmsg = new JSONObject();
		
		if (errorCode <= 0) { // everithing is okey
			errmsg.put("error", new Integer(errorCode));
		} else { 
			logger.error(getClass().getName() + methodName + "errorCode > 0");
			errmsg.put("error", new Integer(-1));
		}
		
		result.add(errmsg);
		
		return result;
	}
	/**
	 * adds a string to response JSONArray
	 * @param key is the JSONobject's name 
	 * @param element is the JSONobject's value
	 * @return returns a JSONArray containing the key and element pair
	 */
	public JSONArray addString(String key, String element, JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (key == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		obj.put(key, element);
		result.add(obj);
		
		return result;
	}
	
	/**
	 * Appends response with the parameter element, if the response were null create a new response with the parameter element.
	 * @param key is not null.
	 * @param element
	 * @param response 
	 * @return unknown error if the parameter: key is null, else response appended with the new element.
	 */
	public JSONArray addInt(String key, int element, JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (key == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		obj.put(key, new Integer(element));
		result.add(obj);

		return result;
	}
	/**
	 * Appends response with the parameter element, if the response were null create a new response with the parameter element.
	 * @param key is not null.
	 * @param element
	 * @param response 
	 * @return unknown error if the parameter: key is null, else response appended with the new element.
	 */
	public JSONArray addBool(String key, boolean element, JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (key == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		obj.put(key, new Boolean(element));
		result.add(obj);
		
		return result;
	}
	
	/**
	 * Adds more key-value pairs into a JSONObject and appends "response" with it!
	 * @param keys is an array strings containing keys which will be the JSONObjects names
	 * @param elements is an array of int's containing values which will be the JSONObjects values
	 * @param response is a JSONArray to which the keys and elements will be appended
	 * @return returns a JSONArray containing the appended key and element pairs 
	 */
	public JSONArray addMoreInt(String keys[], int elements[], JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (keys == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		for(int i = 0; i < keys.length; i++) {
			obj.put(keys[i], new Integer(elements[i]));
		}
		result.add(obj);

		return result;
	}
	/**
	 * Adds more key-value pairs into a JSONObject and appends "response" with it!
	 * @param keys is an array strings containing keys which will be the JSONObjects names
	 * @param elements is an array of strings containing values which will be the JSONObjects values
	 * @param response is a JSONArray to which the keys and elements will be appended
	 * @return returns a JSONArray containing the appended key and element pairs 
	 */
	public JSONArray addMoreStrings(String keys[], String elements[], JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (keys == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		for(int i = 0; i < keys.length; i++) {
			obj.put(keys[i], new String(elements[i]));
		}
		result.add(obj);

		return result;
	}
	/**
	 * Adds more key-value pairs into a JSONObject and appends "response" with them!
	 * @param keys is an array strings containing keys which will be the JSONObjects names
	 * @param elements is an array of int's containing values which will be the JSONObjects values
	 * @param element is a value which will be appended to the JSONArray
	 * @param response is a JSONArray to which the keys and elements will be appended
	 * @return returns a JSONArray containing the appended key and element pairs 
	 */
	public JSONArray addMoreIntAndOneBool(String keys[], int elements[],Boolean element, JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (keys == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		for(int i = 0; i < keys.length-1; i++) {
			obj.put(keys[i], new Integer(elements[i]));
		}
		obj.put(keys[keys.length-1],new Boolean(element));
		result.add(obj);

		return result;
	}
	
	/**
	 *Adds more key-value pairs into a JSONObject and appends "response" with them!
	 * @param keys is an array strings containing keys which will be the JSONObjects names
	 * @param intElement is a value which will be appended to the JSONArray
	 * @param stringElement is a value which will be appended to the JSONArray
	 * @param response is a JSONArray to which the keys and elements will be appended
	 * @return returns a JSONArray containing the appended key and element pairs
	 */
	public JSONArray addIntAndString(String keys[], int intElement,String stringElement, JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (keys == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		
		JSONObject obj = new JSONObject();
		obj.put(keys[0], new Integer(intElement));
		obj.put(keys[keys.length-1],new String(stringElement));
		result.add(obj);

		return result;
	}
	/**
	 * Adds more key-value pairs into a JSONObject and appends "response" with them!
	 * @param keys is an array strings containing keys which will be the JSONObjects names
	 * @param intElements is an array of int's containing values which will be the JSONObjects values
	 * @param stringElement is an array of string's containing values which will be the JSONObjects values
	 * @param response is a JSONArray to which the keys and elements will be appended
	 * @return returns a JSONArray containing the appended key and element pairs
	 */
	public JSONArray addMoreIntAndString(String keys[], int intElements[],String stringElement[], JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(response);
		
		if (keys == null) {
			logger.error(getClass().getName() + methodName + "parameter: key is null");
			return setError(-1);
		}
		
		if (result == null) {
			result = new JSONArray();
		}
		int elementsLength = intElements.length;
		int stringsLength = stringElement.length;
		
		JSONObject obj = new JSONObject();
		for(int i = 0; i < elementsLength; i++) {
			obj.put(keys[i], new Integer(intElements[i]));
		}
		int j = 0;
		for(int i = elementsLength; i < stringsLength + elementsLength; i++) {
			obj.put(keys[i], stringElement[j++]);
		}
		result.add(obj);

		return result;
	}
	
	// Getter methods
	
	/**
	 * 
	 * @param index is the index of the name/value pair which i need
	 * @param key is the name from the JSONArray i need 
	 * @param request is the JSONArray from where i need the value 
	 * @return returns a string which contains the desired value 
	 * @throws JSONException if the request do not contains the given element.
	 */
	public String getString(int index, String key, JSONArray request) throws JSONException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		String result = null;
		
		try {
			JSONObject obj = request.getJSONObject(index);
			result = obj.getString(key);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error(getClass().getName() + methodName + e);
			throw new JSONException(e);
		} catch (JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw e;
		}	
		return result;
	}
	/**
	 * 
	 * @param index is the index of the name/value pair which i need
	 * @param key is the name from the JSONArray i need 
	 * @param request is the JSONArray from where i need the value 
	 * @return returns an int which contains the desired value 
	 * @throws JSONException if the request do not contains the given element.
	 */
	public int getInt(int index, String key, JSONArray request) throws JSONException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int result = 0;
		
		try {
			JSONObject obj = request.getJSONObject(index);
			result = obj.getInt(key);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error(getClass().getName() + methodName + e);
			throw new JSONException(e);
		} catch (JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw e;
		}	
		return result;
	}
	/**
	 * 
	 * @param index is the index of the name/value pair which i need
	 * @param key is the value from the JSONArray i need 
	 * @param request is the JSONArray from where i need the value 
	 * @return returns a boolean value which contains the desired value 
	 * @throws JSONException if the request do not contains the given element.
	 */
	public boolean getBool(int index, String key, JSONArray request) throws JSONException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		boolean result = false;
		
		try {
			JSONObject obj = request.getJSONObject(index);
			result = obj.getBoolean(key);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error(getClass().getName() + methodName + e);
			throw new JSONException(e);
		} catch (JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw e;
		}	
		return result;
	}
	
	
	// Others
	
    /**
	 * 
	 * @param response is a JSONArray which is checked if contains errors 
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
	/**
	 * @param index is the index of the required JSONArray
	 * @param request is the JSONArray from which i need the JSONArray 
	 * @return returns the desired JSONArray
	 */
	public JSONArray getJSONArray(int index , JSONArray request ) throws JSONException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray array = new JSONArray();
		
		try {
			array = request.getJSONArray(index);
		} catch (IndexOutOfBoundsException e) {
			logger.error(getClass().getName() + methodName + e);
			throw new JSONException(e);
		} catch (JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw e;
		}
		return array;
	}
	/**
	 * @param index is the index of the required JSONObject
	 * @param request is the JSONArray from which i need the JSONObject 
	 * @return returns the desired JSONObject
	 */
	public JSONObject getJSONObject(int index , JSONArray request) throws JSONException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONObject object = new JSONObject();
		
		try {
			object = request.getJSONObject(index);
		} catch (IndexOutOfBoundsException e) {
			logger.error(getClass().getName() + methodName + e);
			throw new JSONException(e);
		} catch (JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw e;
		}
		return object;
	}
	
	/**
	 * @param
	 * @return
	 */
	public JSONArray makeCheckUserRequest(JSONArray request) throws JSONException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray response = new JSONArray();
		JSONObject o = new JSONObject();
		
		try {
			o.put("username", getString(0, "username", request));
			o.put("password", getString(0, "password", request));
			o.put("command", getInt(0, "command", request));

			response.add(o);
		} catch(JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw e;
		}
		return response;
	}
	/**
	 * @return returns true if user exists
	 */
	public boolean checkResponseIfLoginSuccessfull(JSONArray response) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			if(errorCheck(response)) {
				if (getInt(0,"error",response) == -8) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} catch(JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw e;
		}
	}
	/**
	 * @param index is the index of the value i want to change
	 * @param key is the name from the name/value pair which i want to change
	 * @param value is the new value of the name/value pair
	 * @param request is a JSONArray in from which i want to change the value of a name/value pair
	 * @return  returns a JSONArray containing the changed value
	 */
	public JSONArray changeInt(int index, String key, int value, JSONArray request) throws JSONException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		JSONArray result = JSONArray.fromObject(request);
		
		try {
			result.getJSONObject(index).put("command", value);
		} catch(ArrayIndexOutOfBoundsException|JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			throw new JSONException(e);
		}
		return result;
	}
	
	
	
}
