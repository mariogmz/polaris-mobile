package com.evologics.polaris;

import com.evologics.polaris.R.id;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button test = (Button) findViewById(id.button_test);
        test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),ListaPrestamosActivity.class);
				startActivity(intent);
				
			}
		});
    }
    
    public void doCreateAccount(View view){
    	Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
    	startActivity(intent);
    }
    
    public void doLogin(View view){
    	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    	startActivity(intent);
    }
}
