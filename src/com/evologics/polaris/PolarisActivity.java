package com.evologics.polaris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		
		Button buttonList = (Button) findViewById(R.id.button_goToList);
		
		buttonList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),ListActivity.class);
				startActivity(intent);
			}
		});
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
}
