package com.evologics.polaris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.evologics.polaris.model.Loan;
import com.evologics.polaris.util.Communicator;

public class PolarisActivity extends Activity {

	private TextView userStatus;
	private ArrayList<Loan> loanList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_polaris);
		
		initializeObjects();

		
	}

	private void initializeObjects() {
		/*
		userStatus = (TextView) findViewById(R.id.user_status);
		userStatus.setText("User email: "
				+ UserStoreImpl.getInstance().getUserEmail()
				+ ", User authToken: "
				+ UserStoreImpl.getInstance().getUserAuthToken());
		 */
		
		//Initializing array
		loanList = new ArrayList<Loan>();
		
		// Setting new loan action
		Button buttonNewLoan = (Button) findViewById(R.id.button_newLoan);
		buttonNewLoan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							try {
								getJSON("https://polaris-app.herokuapp.com/api/reminders");
								//String responseText = getResponseText("https://polaris-app.herokuapp.com/api/reminders?auth_token="+UserStoreImpl.getInstance().getUserAuthToken().toString());
								//JSONObject mainResponseObject = new JSONObject(responseText);
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

			}
		});
		
	}

	public void logout(View view) {
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
}
