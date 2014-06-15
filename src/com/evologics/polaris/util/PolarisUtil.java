package com.evologics.polaris.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.evologics.polaris.controller.UserStoreImpl;

import android.util.Log;

/**
 * PolarisUtil contains all the helper methods used in the application.
 * @author Polaris Dev Team
 * @version 1.0
 * @since 06/04/2014
 */

public class PolarisUtil {
	
	/**
	 * @param email User credential
	 * @param password User credential
	 * @return A JSON Object containing the appropriate format for API interaction.
	 */

	public static JSONObject generateLoginJSON(String email, String password) {
		JSONObject user_login = new JSONObject();
		JSONObject credentials = new JSONObject();
		try {

			credentials.put("password", password);
			credentials.put("email", email);
			user_login.put("user_login", credentials);
			Log.d("Login JSON -> ", user_login.toString());
			return user_login;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
/* Registration helper methods -- start */
	
	public static JSONObject generateRegistrationJSON(String email, String password) {
		JSONObject user_login = new JSONObject();
		JSONObject credentials = new JSONObject();
		try {

			credentials.put("password", password);
			credentials.put("email", email);
			user_login.put("user", credentials);
			Log.d("Login JSON -> ", user_login.toString());
			return user_login;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param response is the response JSON, containing the attributes needed for validation
	 * @param element is the attribute that is going to be searched in the response JSON
	 * @return true, if the element is in the response JSON
	 */
	public static boolean processResponse(JSONObject response, String element ){
		try {
			return response.getString(element) != null;
		} catch (JSONException e) {
			Log.d("Error getting element -> ", element);
			return false;
		}
	}
	
/* Registration helper methods -- end */
	
	// TODO: Replace enum with real HTTP one.
	public enum RequestMethod {
		GET, POST, PUT, DELETE, PATCH;
	}
	
	public static String serverRequest(JSONObject jsonObject, RequestMethod method, String url ){
		
		int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpClient client = new DefaultHttpClient(httpParams);
		
		
		switch( method ){
			case POST:
				HttpPost request = new HttpPost(url);
				try {
					request.setEntity( new StringEntity( jsonObject.toString() ) );
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setHeader("Accept", "application/json");
				request.setHeader("Content-type", "application/json");
							
				try {
					HttpResponse response = client.execute(request);
					return getResponseBody(response);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				//End case POST
			//TODO: Implementation for a all the other request methods
			case GET:
			case PUT:
			case DELETE:
			case PATCH:
				break;
		}
		return null;
	}
	
	
	/**
	 * @param response Contains the result of processing an HTTP request.
	 * @return A string, the body of the response.
	 */
	public static String getResponseBody(HttpResponse response) {
		String response_text = null;
		HttpEntity entity = null;

		try {
			entity = response.getEntity();
			response_text = _getResponseBody(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e1) {
					
				}
			}
		}

		return response_text;
	}

	public static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException {
		if (entity == null) {
			throw new IllegalArgumentException(
					"HTTP entity may not be null");
		}
		
		InputStream instream = entity.getContent();
		if (instream == null) {
			return "";
		}
		
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
			"HTTP entity too large to be buffered in memory");
		}
		
		String charset = getContentCharSet(entity);
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}
		
		Reader reader = new InputStreamReader(instream, charset);
		StringBuilder buffer = new StringBuilder();
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		
		return buffer.toString();
	}
	
	public static String getContentCharSet(final HttpEntity entity) throws ParseException {

		if (entity == null) {
			throw new IllegalArgumentException(
					"HTTP entity may not be null");
		}

		String charset = null;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();

			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}

		return charset;
	}
	//End of response translation functions
	
}
