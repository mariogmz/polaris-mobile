package com.evologics.polaris;

import org.json.JSONException;
import org.json.JSONObject;

import com.evologics.polaris.LoginActivity.UserLoginTask;
import com.evologics.polaris.controller.UserStore;
import com.evologics.polaris.controller.UserStoreImpl;
import com.evologics.polaris.util.PolarisUtil;
import com.evologics.polaris.util.PolarisUtil.RequestMethod;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class LogoutActivity extends Activity {
	
	private View mLogoutStatusView;
	private UserLogoutTask mLogoutTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logout);
		
		mLogoutStatusView = findViewById(R.id.logout_status);
		
		showProgress(true);
		
		mLogoutTask = new UserLogoutTask();
		mLogoutTask.execute((Void) null);
	}
	
	// Shows the progress UI and hides the login form.
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		private void showProgress(final boolean show) {
			// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
			// for very easy animations. If available, use these APIs to fade-in
			// the progress spinner.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				int shortAnimTime = 200;

				mLogoutStatusView.setVisibility(View.VISIBLE);
				mLogoutStatusView.animate().setDuration(shortAnimTime)
						.alpha(show ? 1 : 0)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mLogoutStatusView.setVisibility(show ? View.VISIBLE
										: View.GONE);
							}
						});
			} else {
				// The ViewPropertyAnimator APIs are not available, so simply show
				// and hide the relevant UI components.
				mLogoutStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			}
			
		}
		
		
		public class UserLogoutTask extends AsyncTask<Void, Void, Boolean> {
			
			@Override
			protected Boolean doInBackground(Void... params) {			
				//Generate LoginJSON
				JSONObject jsonLogout = PolarisUtil.generateLogoutJSON(UserStoreImpl.getInstance().getUserEmail());
				
				String loginModuleUrl = "https://polaris-app.herokuapp.com/api/logout";
				String responseBody = PolarisUtil.serverRequest(jsonLogout, RequestMethod.DELETE, loginModuleUrl);
				
				Log.d("Response body ->", responseBody);
				
				
				if (!responseBody.equals(null)){
					try {
						JSONObject response = new JSONObject(responseBody);
						boolean success = (boolean) response.getBoolean("success");
						
						UserStoreImpl.getInstance().setUserAuthToken(null);
						UserStoreImpl.getInstance().setUserEmail(null);
						
						if(success){
							runOnUiThread(new Runnable(){
								public void run(){
									Intent intent = new Intent(getApplicationContext(), MainActivity.class);
							    	startActivity(intent);
								}
							});
						}else{
							runOnUiThread(new Runnable(){
								public void run(){
									Toast.makeText(getApplicationContext(), "Not a Successful Logout", Toast.LENGTH_LONG).show();
								}
							});
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("exception", e.getMessage());
					}
					
				}
				return true;
			}
		}
		
}
