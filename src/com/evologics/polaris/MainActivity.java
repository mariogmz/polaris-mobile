package com.evologics.polaris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void doCreateAccount(View view){
    	//TODO: Create account implementation
    	Toast.makeText(getApplicationContext(), "Create function called", Toast.LENGTH_LONG).show();
    }
    
    public void doLogin(View view){
    	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    	startActivity(intent);
    }
}
