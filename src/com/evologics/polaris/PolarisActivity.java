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

import com.evologics.polaris.controller.Communicator;
import com.evologics.polaris.controller.UserStoreImpl;
import com.evologics.polaris.model.Loan;

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
								String responseText = getResponseText("https://polaris-app.herokuapp.com/api/reminders?auth_token="+UserStoreImpl.getInstance().getUserAuthToken().toString());
								JSONObject mainResponseObject = new JSONObject(responseText);
							} catch (IOException e) {
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

	private String getResponseText(String stringUrl) throws IOException {
		StringBuilder response = new StringBuilder();

		URL url = new URL(stringUrl);
		HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
		if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					httpconn.getInputStream()), 8192);
			String strLine = null;
			while ((strLine = input.readLine()) != null) {
				response.append(strLine);
			}
			input.close();
		}
		return response.toString();
	}

	private void getJSON(String url) {
		String page;
		try {
			page = new Communicator().executeHttpGet(url);

			JSONArray jsonArray = new JSONArray(page);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject entry = (JSONObject) jsonArray.get(i);
				loanList.add(parseJSONLoan(entry));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * This function "maps" from JSON to java object in a hard-coding way
	 */
	@SuppressWarnings("finally")
	private Loan parseJSONLoan(JSONObject entry) {
		DateFormat formatter = new SimpleDateFormat("dd-MM-yy");
		Loan loan = null;
		try {
			loan = new Loan(entry.getString("concepto"),entry.getString("contacto"),
					formatter.parse(entry.getString("fecha_prestamo")),formatter.parse(entry.getString("fecha_entrega")),
					entry.getString("detalle")," ",(entry.getBoolean("regresado") == true ? Loan.Loan_State.Refunded : Loan.Loan_State.On_Time));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return loan;
		}
	}
}
