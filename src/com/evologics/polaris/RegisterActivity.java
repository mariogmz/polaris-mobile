package com.evologics.polaris;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evologics.polaris.controller.UserStore;
import com.evologics.polaris.controller.UserStoreImpl;
import com.evologics.polaris.util.PolarisUtil;
import com.evologics.polaris.util.PolarisUtil.RequestMethod;

/**
 * Activity which displays a registration screen to the user.
 * @author Polaris Dev Team
 * @version 1.0
 * @since 06/12/2014
 */
public class RegisterActivity extends Activity {
	// The default email to populate the email field with.
		public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

		// Keep track of the login task to ensure we can cancel it if requested.
		private UserLoginTask mAuthTask = null;

		// Values for email and password at the time of the login attempt.
		private String mEmail;
		private String mPasswordConfirmation;
		private String mPassword;

		// UI references.
		private EditText mEmailView;
		private EditText mPasswordConfirmationView;
		private EditText mPasswordView;
		private View mLoginFormView;
		private View mLoginStatusView;
		private TextView mLoginStatusMessageView;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_register);
			setupActionBar();
			
			UserStore us = UserStoreImpl.getInstance();
	        
	        if( us.getUserAuthToken() != null ){
	        	Intent intent = new Intent(getApplicationContext(), PolarisActivity.class);
	        	startActivity(intent);
	        	finish();
	        } 

			// Set up the login form.
			mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
			mEmailView = (EditText) findViewById(R.id.email);
			mEmailView.setText(mEmail);
			
			mPasswordConfirmation = getIntent().getStringExtra(EXTRA_EMAIL);
			mPasswordConfirmationView = (EditText) findViewById(R.id.password_re);
			mPasswordConfirmationView.setText(mEmail);

			mPasswordView = (EditText) findViewById(R.id.password);
			mPasswordView
					.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView textView, int id,
								KeyEvent keyEvent) {
							if (id == R.id.login || id == EditorInfo.IME_NULL) {
								attemptRegistration();
								return true;
							}
							return false;
						}
					});

			mLoginFormView = findViewById(R.id.login_form);
			mLoginStatusView = findViewById(R.id.login_status);
			mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

			findViewById(R.id.sign_in_button).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							attemptRegistration();
						}
					});
		}

		// Set up the {@link android.app.ActionBar}, if the API is available.
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void setupActionBar() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				// Show the Up button in the action bar.
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			if (id == android.R.id.home) {
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.htmlup-vs-back
				//
				// TODO: If Settings has multiple levels, Up should navigate up
				// that hierarchy.
				NavUtils.navigateUpFromSameTask(this);
				return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			getMenuInflater().inflate(R.menu.login, menu);
			return true;
		}

		/**
		 * Attempts to sign in or register the account specified by the login form.
		 * If there are form errors (invalid email, missing fields, etc.), the
		 * errors are presented and no actual login attempt is made.
		 */
		public void attemptRegistration() {
			if (mAuthTask != null) {
				return;
			}

			// Reset errors.
			mEmailView.setError(null);
			mPasswordConfirmationView.setError(null);
			mPasswordView.setError(null);

			// Store values at the time of the login attempt.
			mEmail = mEmailView.getText().toString();
			mPasswordConfirmation = mPasswordConfirmationView.getText().toString();
			mPassword = mPasswordView.getText().toString();

			boolean cancel = false;
			View focusView = null;

			// Check for a valid password.
			if (TextUtils.isEmpty(mPassword)) {
				mPasswordView.setError(getString(R.string.error_field_required));
				focusView = mPasswordView;
				cancel = true;
			} else if (mPassword.length() < 8) {
				mPasswordView.setError(getString(R.string.error_invalid_password));
				focusView = mPasswordView;
				cancel = true;
			}

			// Check for a valid email address.
			if (TextUtils.isEmpty(mEmail)) {
				mEmailView.setError(getString(R.string.error_field_required));
				focusView = mEmailView;
				cancel = true;
			} else if (!mEmail.contains("@")) {
				mEmailView.setError(getString(R.string.error_invalid_email));
				focusView = mEmailView;
				cancel = true;
			}
			
			//Check if the confirmation email matches the primary one
			if (TextUtils.isEmpty(mPasswordConfirmation)) {
				mPasswordConfirmationView.setError(getString(R.string.error_field_required));
				focusView = mPasswordConfirmationView;
				cancel = true;
			} else if (mPasswordConfirmation.length() < 8) {
				mPasswordConfirmationView.setError(getString(R.string.error_invalid_password));
				focusView = mPasswordConfirmationView;
				cancel = true;
			} else if ( !mPassword.equals(mPasswordConfirmation) ) {
				mPasswordConfirmationView.setError(getString(R.string.r_error_passwords_dont_match));
				focusView = mPasswordConfirmationView;
				cancel = true;
			}

			if (cancel) {
				// There was an error; don't attempt login and focus the first
				// form field with an error.
				focusView.requestFocus();
			} else {
				// Show a progress spinner, and kick off a background task to
				// perform the user login attempt.
				mLoginStatusMessageView.setText(R.string.r_login_progress_signing_in);
				showProgress(true);
				mAuthTask = new UserLoginTask();
				mAuthTask.execute((Void) null);
			}
		}

		// Shows the progress UI and hides the login form.
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		private void showProgress(final boolean show) {
			// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
			// for very easy animations. If available, use these APIs to fade-in
			// the progress spinner.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				int shortAnimTime = getResources().getInteger(
						android.R.integer.config_shortAnimTime);

				mLoginStatusView.setVisibility(View.VISIBLE);
				mLoginStatusView.animate().setDuration(shortAnimTime)
						.alpha(show ? 1 : 0)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mLoginStatusView.setVisibility(show ? View.VISIBLE
										: View.GONE);
							}
						});

				mLoginFormView.setVisibility(View.VISIBLE);
				mLoginFormView.animate().setDuration(shortAnimTime)
						.alpha(show ? 0 : 1)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mLoginFormView.setVisibility(show ? View.GONE
										: View.VISIBLE);
							}
						});
			} else {
				// The ViewPropertyAnimator APIs are not available, so simply show
				// and hide the relevant UI components.
				mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
				mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			}
		}

		// Represents an asynchronous login/registration task used to authenticate the user.
		public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
			
			@Override
			protected Boolean doInBackground(Void... params) {			
				//Generate LoginJSON
				
				
				JSONObject jsonRegister = PolarisUtil.generateRegistrationJSON(mEmail, mPassword);
				
				
				String loginModuleUrl = "https://polaris-app.herokuapp.com/api/register";
				String responseBody = PolarisUtil.serverRequest(jsonRegister, RequestMethod.POST, loginModuleUrl);
				
				Log.d("Response body ->", responseBody);
				
				
				if (!responseBody.equals(null)){
					try {
						JSONObject response = new JSONObject(responseBody);
						
						if( PolarisUtil.processResponse(response, "authentication_token") ){
							String authTokenResponse = response.getString("authentication_token");
							
							UserStore us = UserStoreImpl.getInstance();
							us.setUserEmail(mEmail);
							us.setUserAuthToken( authTokenResponse);
							
							if( !authTokenResponse.isEmpty() ){
								runOnUiThread(new Runnable(){
									public void run(){
										//Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
										Intent intent = new Intent(getApplicationContext(), PolarisActivity.class);
								    	startActivity(intent);
									}
								});
							}
						} else {
							if ( PolarisUtil.processResponse(response, "success") ){
								boolean success = response.getBoolean("success");
								if( !success ){
									runOnUiThread(new Runnable(){
										public void run(){
											Toast.makeText(getApplicationContext(), "Ya iniciaste sesión" , Toast.LENGTH_LONG).show();
										}
									});
								} 
							} else {
								runOnUiThread(new Runnable(){
									public void run(){
										Toast.makeText(getApplicationContext(), "Correo electronico ya existe" , Toast.LENGTH_LONG).show();
									}
								});
							}
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("exception", e.getMessage());
					}
					
				}
				

				// TODO: register the new account here.
				return true;
			}

			@Override
			protected void onPostExecute(final Boolean success) {
				mAuthTask = null;
				showProgress(false);

				if (success) {
					finish();
				} else {
					mPasswordView
							.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
			}

			@Override
			protected void onCancelled() {
				mAuthTask = null;
				showProgress(false);
			}
		}
}
