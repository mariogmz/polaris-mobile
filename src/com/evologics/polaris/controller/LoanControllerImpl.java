package com.evologics.polaris.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.evologics.polaris.ListActivity;
import com.evologics.polaris.model.Loan;
import com.evologics.polaris.util.Config;

public class LoanControllerImpl implements LoanController {
	
	private ArrayList<Loan> loanList;
	private ArrayList<HashMap<String,String>> listViewItems;
	
	//URL for GET
	private String url = "https://polaris-app.herokuapp.com/api/reminders";
	
	public LoanControllerImpl(){
		loanList = new ArrayList<Loan>();
		listViewItems = new ArrayList<HashMap<String,String>>();
	}
	
	public void populateLoanList(){
		new HttpAsyncTask().execute(url);
	}
	
	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try{
			//Create Httpclient
			HttpClient httpClient = new DefaultHttpClient();
			
			//Make GET request to the given URL
			HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
			
			//Receive response as InputStream
			inputStream = httpResponse.getEntity().getContent();
			
			//Convert inputstream into string
			if(inputStream != null){
				result = convertInputStreamToString(inputStream);
			}else{
				result = "GET JSON Failed";
			}
		}catch(Exception ex){
			Log.d("InputStream",ex.getMessage());
		}
		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		
		while((line = bufferedReader.readLine()) != null)
			result += line;
		
		inputStream.close();
		return result;
	}
	
	public boolean isConnected(){
		ConnectivityManager connMgr = (ConnectivityManager)Config.context.getSystemService(ListActivity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		if(networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String ... urls){
			return GET(urls[0]);
		}
		
		// onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Config.context.getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //etResponse.setText(result);
       }
	}
}