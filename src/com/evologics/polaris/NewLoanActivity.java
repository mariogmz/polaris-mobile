package com.evologics.polaris;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.evologics.polaris.LoginActivity.UserLoginTask;
import com.evologics.polaris.controller.UserStore;
import com.evologics.polaris.controller.UserStoreImpl;
import com.evologics.polaris.service.AppLocationService;
import com.evologics.polaris.util.DatePickerFragment;
import com.evologics.polaris.util.PolarisUtil;
import com.evologics.polaris.util.PolarisUtil.DateType;
import com.evologics.polaris.util.PolarisUtil.RequestMethod;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewLoanActivity extends Activity {
	
	private EditText contact;
	private Spinner category;
	private EditText description;
	public static TextView startDate;
	public static TextView endDate;
	private CheckBox locationShare;
	
	AppLocationService appLocationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_loan);
		appLocationService = new AppLocationService(
		        NewLoanActivity.this);
		
		contact = (EditText) findViewById(R.id.contactField);
		category = (Spinner) findViewById(R.id.categorySpinner);
		description = (EditText) findViewById(R.id.descriptionField);
		startDate = (TextView) findViewById(R.id.startDate);
		endDate = (TextView) findViewById(R.id.endDate);
		locationShare = (CheckBox) findViewById(R.id.locationShare);
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		if(startDate != null){
			startDate.setText(dateFormat.format(cal.getTime()));
		}
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.categories_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		category.setAdapter(adapter);
		
	}
	
	public void onLoanSave(View view){		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				String fecha_prestamo = startDate.getText().toString();
				String fecha_entrega = endDate.getText().toString();
				String contacto = contact.getText().toString();
				String concepto = category.getSelectedItem().toString();
				String detalle = description.getText().toString();
				boolean regresado = false;
				double latitude = 0;
				double longitude = 0;
				
				if( locationShare.isChecked() ){
					try{
						latitude = getLatitude();
						longitude = getLongitude();
					}catch(Exception e){
						Log.d("Location ->", "Could not retreive");
					}
					
				}
				
				
				
				JSONObject addReminderJSON = PolarisUtil.generateAddReminderJSON(fecha_prestamo, fecha_entrega, 
						contacto, concepto, detalle, regresado, latitude, longitude );
				
				String remindersModuleUrl = "https://polaris-app.herokuapp.com/api/reminders";
				String responseBody = PolarisUtil.serverRequest(addReminderJSON, RequestMethod.POST, remindersModuleUrl);
				
				Log.d("Response body ->", responseBody);
				
				if (!responseBody.equals(null)){
					try {
						JSONObject response = new JSONObject(responseBody);
						String success = response.getString("user_id");
						
						if(!success.isEmpty()){
							runOnUiThread(new Runnable(){
								public void run(){
									Toast.makeText(getApplicationContext(), "Registro Exitoso!", Toast.LENGTH_LONG).show();
									finish();
								}
							});
						}else{
							runOnUiThread(new Runnable(){
								public void run(){
									Toast.makeText(getApplicationContext(), "El registro falló!", Toast.LENGTH_LONG).show();
								}
							});
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("exception", e.getMessage());
					}
					
				}
				
				
			}
		});
		thread.start();
	}	
	
	private double getLatitude(){
		Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

		if (nwLocation != null) {
			return nwLocation.getLatitude();
		} else {
			Toast.makeText(getApplicationContext(), "There was an error retreiving the location.", Toast.LENGTH_SHORT).show();
		}
		return 0;
	}
	
	private double getLongitude(){
		Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

		if (nwLocation != null) {
			return nwLocation.getLongitude();
		} else {
			Toast.makeText(getApplicationContext(), "There was an error retreiving the location.", Toast.LENGTH_SHORT).show();
		}
		return 0;
	}
	
	public void setStartDate(View view){
		DialogFragment newFragment = new DatePickerFragment();
		DatePickerFragment.type = DateType.STARTDATE;
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void setEndDate(View view){
		DialogFragment newFragment = new DatePickerFragment();
		DatePickerFragment.type = DateType.ENDDATE;
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	
	
}
