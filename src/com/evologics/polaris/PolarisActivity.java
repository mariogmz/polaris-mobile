package com.evologics.polaris;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.evologics.polaris.controller.UserStoreImpl;

public class PolarisActivity extends Activity {
	
	private TextView userStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_polaris);
		
		userStatus = (TextView) findViewById(R.id.user_status);
		userStatus.setText( "User email: " + UserStoreImpl.getInstance().getUserEmail() 
				+ ", User authToken: " + UserStoreImpl.getInstance().getUserAuthToken() );
		
		/*Setting new loan action
		Button buttonNewLoan = (Button) findViewById(R.id.button_newLoan);
		buttonNewLoan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Thread thread = new Thread(new Runnable(){
					@Override
					public void run(){
						try{
							try {
								String responseText = getResponseText("https://polaris-app.herokuapp.com/api/reminders");
								JSONObject mainResponseObject = new JSONObject(responseText);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								Log.e("ERROR",e.getMessage());
							}
						}catch(Exception e){
							Log.e("ERROR",e.getMessage());
							e.printStackTrace();
						}
					}
				});
				thread.start();
				
			}
		});
		*/
	}
	
	public void doNew(View view){
		Intent intent = new Intent(getApplicationContext(), NewLoanActivity.class);
    	startActivity(intent);
	}
	
	public void logout(View view){
		// TODO: Call logout activity to display the process.
		//finish();
		Intent intent = new Intent(getApplicationContext(), LogoutActivity.class);
    	startActivity(intent);
    	finish();
    	
    	
		
		//intent = new Intent(getApplicationContext(), MainActivity.class);
    	//startActivity(intent);
    	//finish();
	}
	
	private String getResponseText(String stringUrl) throws IOException
	{
	    StringBuilder response  = new StringBuilder();

	    URL url = new URL(stringUrl);
	    HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
	    if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
	    {
	        BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
	        String strLine = null;
	        while ((strLine = input.readLine()) != null)
	        {
	            response.append(strLine);
	        }
	        input.close();
	    }
	    return response.toString();
	}
}
