package com.evologics.polaris;

import com.evologics.polaris.controller.UserStoreImpl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PolarisActivity extends Activity {
	
	private TextView userStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_polaris);
		
		userStatus = (TextView) findViewById(R.id.user_status);
		userStatus.setText( "User email: " + UserStoreImpl.getInstance().getUserEmail() 
				+ ", User authToken: " + UserStoreImpl.getInstance().getUserAuthToken() );
		
	}
}
