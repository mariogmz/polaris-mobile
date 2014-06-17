package com.evologics.polaris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;

import com.evologics.polaris.controller.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class ListActivity extends Activity {
	
	//Object linked to out list
	ListView listView;
	
	//LoanController
	LoanController loanController = new LoanControllerImpl();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		//populateList();
		test();
	}

	private void test() {
		Button buttonTest = (Button) findViewById(R.id.button_NewItem);
		buttonTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Thread thread = new Thread(new Runnable(){
					@Override
					public void run(){
						try{
							//Code Goes Here
							String json = getJSON("https://polaris-app.herokuapp.com/api/reminders");
							//Toast.makeText(getBaseContext(),json, Toast.LENGTH_LONG).show();
						}catch(Exception e){
							Log.e("ERROR",e.getMessage());
							e.printStackTrace();
						}
					}
				});
				thread.start();
			}
		});
		
	}

	private void populateList() {
		
		//Get listView
		listView = (ListView) findViewById(R.id.listView_Item);
		
		// Defined Array values to show in ListView
        String[] values = new String[] { "Android List View", 
                                         "Adapter implementation",
                                         "Simple List View In Android",
                                         "Create List View Android", 
                                         "Android Example", 
                                         "List View Source Code", 
                                         "List View Array Adapter", 
                                         "Android Example List View" 
                                        };
        
     // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = 
        		new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);
        
        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) {
                
               // ListView Clicked item index
               int itemPosition     = position;
               
               // ListView Clicked item value
               String  itemValue    = (String) listView.getItemAtPosition(position);
                  
                // Show Alert 
                Toast.makeText(getApplicationContext(),
                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                  .show();
             
              }

         }); 
		
	}
	
	 public String getJSON(String address){
	    	StringBuilder builder = new StringBuilder();
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpGet = new HttpGet(address);
	    	try{
	    		HttpResponse response = client.execute(httpGet);
	    		StatusLine statusLine = response.getStatusLine();
	    		int statusCode = statusLine.getStatusCode();
	    		
	    		//THIS IS WHERE statusCode returns 401 instead of 200 for success
	    		
	    		if(statusCode == 200){
	    			HttpEntity entity = response.getEntity();
	    			InputStream content = entity.getContent();
	    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	    			String line;
	    			while((line = reader.readLine()) != null){
	    				builder.append(line);
	    			}
	    		} else {
	    			Log.e(MainActivity.class.toString(),"Failed to get JSON object");
	    		}
	    	}catch(Exception e){
	    		Log.e("ERROR","ERROR IN CODE: " +  e.toString());
	    		
	    		e.printStackTrace();
	    	}
	    	return builder.toString();
	    }
}
