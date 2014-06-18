package com.evologics.polaris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.evologics.polaris.controller.UserStore;
import com.evologics.polaris.controller.UserStoreImpl;
import com.evologics.polaris.util.ConnectionDetector;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
        
        UserStore us = UserStoreImpl.getInstance();
        
        if( us.getUserAuthToken() != null ){
        	Intent intent = new Intent(getApplicationContext(), PolarisActivity.class);
        	startActivity(intent);
        	finish();
        }
        
    }
    
    public void doCreateAccount(View view){
    	ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
    	if (cd.isConnectingToInternet()){
	    	Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
	    	startActivity(intent);
	    	finish();
    	}else{
    		Toast.makeText(getApplicationContext(),"Verifique su conexión a internet e intente de nuevo.",Toast.LENGTH_LONG).show();
    	}
    }
    
    public void doLogin(View view){
    	ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
    	if (cd.isConnectingToInternet()){
	    	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	    	startActivity(intent);
	    	finish();
    	}else{
    		Toast.makeText(getApplicationContext(),"Verifique su conexión a internet e intente de nuevo.",Toast.LENGTH_LONG).show();
    	}
    }
}
