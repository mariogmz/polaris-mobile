package com.evologics.polaris;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.evologics.polaris.controller.UserStoreImpl;
import com.evologics.polaris.model.Loan;
import com.evologics.polaris.util.Communicator;
import com.evologics.polaris.util.LoanAdapter;
import com.evologics.polaris.util.PolarisUtil;
import com.evologics.polaris.util.SwipeDismissListViewTouchListener;

public class PolarisActivity extends Activity {
	
	private ListView loanView;
	private ArrayList<Loan> loanList;
	LoanAdapter adapter;
	public static Loan loan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_polaris);
		
		//Initializing objects
		initializeObjects();
		
	}

	private void initializeObjects() {

		//Initializing array
		loanList = new ArrayList<Loan>();
		
		
		//Get JSON and populate the viewList
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						getJSON("https://polaris-app.herokuapp.com/api/reminders");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.e("ERROR", e.getMessage());
					}
				} catch (Exception e) {
					Log.e("ERROR", e.getMessage());
					e.printStackTrace();
				}
			}
		});
		thread.start();
		try {
			thread.join(2000);
			   
	     	// 1. pass context and data to the custom adapter
			adapter = new LoanAdapter(this, loanList);
	 
	        // 2. Get ListView from activity_main.xml
	        ListView listView = (ListView) findViewById(R.id.loanListView);
	 
	        // 3. setListAdapter
	        listView.setAdapter(adapter);
	        
	        //Adding onClickListener to listView
	        listView.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	        	  public void onItemClick(AdapterView<?> parent, View view,
	        	    int position, long id) {
	        		loan = loanList.get(position);
	        	    Toast.makeText(getApplicationContext(),
	        	      "Click ListItem Number " + loanList.get(position).getLoan_id(), Toast.LENGTH_LONG)
	        	      .show();
	        	  }
			});
	        
	        SwipeDismissListViewTouchListener touchListener =
	                new SwipeDismissListViewTouchListener(
	                        listView,
	                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
	                            @Override
	                            public boolean canDismiss(int position) {
	                                return true;
	                            }

	                            @Override
	                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
	                                    //Recovering the loan id to remove it from the database
	                                    final int loanId = adapter.getItem(reverseSortedPositions[0]).getLoan_id();
	                                    String name = adapter.getItem(reverseSortedPositions[0]).getObjectName();
	                                    
	                                    adapter.remove(adapter.getItem(reverseSortedPositions[0]));
	                                    
	                                    
	                                    try {
											
											Thread thread = new Thread(new Runnable() {
												
												
												@Override
												public void run() {
													try {
														try {
															//Here we make the DELETE request
						                                    JSONObject json = new JSONObject();
															json.put("auth_token",UserStoreImpl.getInstance().getUserAuthToken());
															PolarisUtil.serverRequest(json,PolarisUtil.RequestMethod.DELETE,"https://polaris-app.herokuapp.com/api/reminders/" + loanId);
														} catch (Exception e) {
															// TODO Auto-generated catch block
															Log.e("ERROR", e.getMessage());
														}
													} catch (Exception e) {
														Log.e("ERROR", e.getMessage());
														e.printStackTrace();
													}
												}
											});
											//Starting thread for delete
											thread.start();
											//Waiting for thread to finish
											thread.join(2000);
											
										} catch (Exception e){
											Log.d("ERROR","EX:" + e.getMessage());
										}
	                                    
	                                    Toast.makeText(getBaseContext(),name + " ha sido removido!", Toast.LENGTH_SHORT).show();
	                                    adapter.notifyDataSetChanged();
	                                
	                            }
	                        });
	        listView.setOnTouchListener(touchListener);
	        // Setting this scroll listener is required to ensure that during ListView scrolling,
	        // we don't look for swipes.
	        listView.setOnScrollListener(touchListener.makeScrollListener());
		        
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public void doNew(View view){
		Intent intent = new Intent(getApplicationContext(), NewLoanActivity.class);
    	startActivity(intent);
    	
	}
	
	public void logout(View view){
		// TODO: Call logout activity to display the process.
		// finish();
		Intent intent = new Intent(getApplicationContext(),
				LogoutActivity.class);
		startActivity(intent);
		finish();

		// intent = new Intent(getApplicationContext(), MainActivity.class);
		// startActivity(intent);
		// finish();
	}
	
	

	private void getJSON(String url) {
		String page;
		try {
			page = new Communicator().executeHttpGet(url);

			JSONArray jsonArray = new JSONArray(page);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject entry = (JSONObject) jsonArray.get(i);
				loanList.add(new Loan(entry));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		initializeObjects();
		Log.d("OnRestart","PolarisActivity_Restart");
	}
}