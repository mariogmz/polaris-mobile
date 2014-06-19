package com.evologics.polaris;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

public class UpdateActivity extends Activity {
	
	private EditText contact;
	private Spinner category;
	private EditText description;
	public static TextView startDate;
	public static TextView endDate;
	private CheckBox locationShare;
	
	double latitude = 0;
	double longitude = 0;
	
	AppLocationService appLocationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		appLocationService = new AppLocationService(
		        UpdateActivity.this);
		
		contact = (EditText) findViewById(R.id.contactFieldU);
		category = (Spinner) findViewById(R.id.categorySpinnerU);
		description = (EditText) findViewById(R.id.descriptionFieldU);
		startDate = (TextView) findViewById(R.id.startDateU);
		endDate = (TextView) findViewById(R.id.endDateU);
		locationShare = (CheckBox) findViewById(R.id.locationShareU);
		
		DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		DateFormat finalDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.categories_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		category.setAdapter(adapter);
		
		
		//Poopulating
		contact.setText( PolarisActivity.loan.getLoanOwner() );
		String c = PolarisActivity.loan.getObjectName();

		if(c.equals("Dinero")){
			category.setSelection(0);
		}
		if(c.equals("Libro")){
			category.setSelection(1);
		}
		if(c.equals("Pelicula")){
			category.setSelection(2);
		}
		if(c.equals("CD")){
			category.setSelection(3);
		}
		if(c.equals("Otros")){
			category.setSelection(4);
		}
		
		description.setText(PolarisActivity.loan.getNote());
		
		Date start_date;
		try {
			start_date = dateFormat.parse(PolarisActivity.loan.getDateStart().toString());
			startDate.setText( finalDateFormat.format(start_date) );
			if( PolarisActivity.loan.getDateEnd() != null ){
				start_date = dateFormat.parse(PolarisActivity.loan.getDateEnd().toString());
				endDate.setText(finalDateFormat.format(start_date));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public void onLoanSave(View view){	
		
		
		
		if( locationShare.isChecked() ){
			try{
				latitude = getLatitude();
				longitude = getLongitude();
			}catch(Exception e){
				Log.d("Location ->", "Could not retreive");
			}
			
		}
		
		Thread thread = new Thread(new Runnable(){
			
			@Override
			public void run(){
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String fecha_prestamo = startDate.getText().toString();
				String fecha_entrega = endDate.getText().toString();
				
				if( !fecha_entrega.equals("dd-mm-yyyy") || fecha_entrega == null ){
					try {
						Date start_date = dateFormat.parse(fecha_prestamo);
						Date end_date = dateFormat.parse(fecha_entrega);
						
						if(end_date.compareTo(start_date) < 0){
							runOnUiThread(new Runnable(){
								public void run(){
									Toast.makeText(getApplicationContext(), "Fecha de entrega invalida!", Toast.LENGTH_LONG ).show();
								}
							});
							return;
						}
						
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				String contacto = contact.getText().toString();
				String concepto = category.getSelectedItem().toString();
				String detalle = description.getText().toString();
				boolean regresado = false;
				
				
				
				
				JSONObject addReminderJSON = PolarisUtil.generateAddReminderJSON(fecha_prestamo, fecha_entrega, 
						contacto, concepto, detalle, regresado, latitude, longitude );
				
				String remindersModuleUrl = "https://polaris-app.herokuapp.com/api/reminders/"+PolarisActivity.loan.getLoan_id();
				String responseBody = PolarisUtil.serverRequest(addReminderJSON, RequestMethod.PUT, remindersModuleUrl);
				
				Log.d("Response body ->", responseBody);
				
				if (!responseBody.equals(null)){
					try {
						JSONObject response = new JSONObject(responseBody);
						boolean success = response.getBoolean("success");
						
						if(success){
							runOnUiThread(new Runnable(){
								public void run(){
									Toast.makeText(getApplicationContext(), "Modificado Exitosamente!", Toast.LENGTH_LONG).show();
									finish();
								}
							});
						}else{
							runOnUiThread(new Runnable(){
								public void run(){
									Toast.makeText(getApplicationContext(), "La modificacion falló!", Toast.LENGTH_LONG).show();
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
