package com.evologics.polaris.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.evologics.polaris.R;
import com.evologics.polaris.controller.SessionStore;
import com.evologics.polaris.controller.UserStoreImpl;

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
	
	public static JSONObject generateLogoutJSON(String email) {
		JSONObject user_login = new JSONObject();
		try {
			user_login.put("user_login", email);
			Log.d("Logout JSON -> ", user_login.toString());
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
	
	public static JSONObject generateAddReminderJSON(String startDate, String endDate, String contact, 
			String concept, String desc, boolean status, double latitude, double longitude) {
		JSONObject reminder = new JSONObject();
		JSONObject recordatorio = new JSONObject();
		try {
			recordatorio.put("long", longitude);
			recordatorio.put("lat", latitude);
			recordatorio.put("regresado", status);
			recordatorio.put("detalle", desc);
			recordatorio.put("concepto", concept);
			recordatorio.put("contacto", contact);
			recordatorio.put("fecha_entrega", endDate);
			recordatorio.put("fecha_prestamo", startDate);
			reminder.put("recordatorio", recordatorio);
			reminder.put("auth_token", UserStoreImpl.getInstance().getUserAuthToken());
			Log.d("Login JSON -> ", reminder.toString());
			return reminder;
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
	
	public enum DateType { STARTDATE, ENDDATE }
	
	public static String serverRequest(JSONObject jsonObject, RequestMethod method, String url ){
		
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
					HttpResponse response = SessionStore.getInstance().getClient().execute(request);
					return getResponseBody(response);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				//End case POST
			case DELETE:
				HttpDeleteWithBody deleteRequest = new HttpDeleteWithBody(url);

				StringEntity se;
				try {
					se = new StringEntity(jsonObject.toString(),HTTP.UTF_8);
					se.setContentType("application/json");
					deleteRequest.setEntity(se);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
				deleteRequest.setHeader("Content-type", "application/json");
							
				try {
					HttpResponse response = SessionStore.getInstance().getClient().execute(deleteRequest);
					return getResponseBody(response);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
				
			//TODO: Implementation for a all the other request methods
			case GET:
			case PUT:
				HttpPut putRequest = new HttpPut(url);
				try {
					putRequest.setEntity( new StringEntity( jsonObject.toString() ) );
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				putRequest.setHeader("Accept", "application/json");
				putRequest.setHeader("Content-type", "application/json");
							
				try {
					HttpResponse response = SessionStore.getInstance().getClient().execute(putRequest);
					return getResponseBody(response);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			
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
	
	/**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     * */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? android.R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
	
}
