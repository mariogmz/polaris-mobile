package com.evologics.polaris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.evologics.polaris.controller.UserStore;
import com.evologics.polaris.controller.UserStoreImpl;

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
    	Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
    	startActivity(intent);
    	finish();
    }
    
    public void doLogin(View view){
    	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    	startActivity(intent);
    	finish();
    }
}
