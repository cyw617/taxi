package comp3111h.anytaxi.customer;

import java.io.IOException;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Customer;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;

public class LoginActivity extends ActionBarActivity {

	private static final String TAG = "LoginActivity";

	protected static GoogleAccountCredential credential;  
	protected static AnyTaxi endpoint;

	@Override
	public void onStart() {
		super.onStart();	

	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    //getSupportActionBar().hide();
		setContentView(R.layout.activity_main);	
		
		credential = GoogleAccountCredential.usingAudience(this, Utils.AUDIENCE);         
		startActivityForResult(credential.newChooseAccountIntent(), Utils.REQUEST_ACCOUNT_PICKER);
	}

	@Override
	public void onBackPressed() {
		exit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case Utils.REQUEST_ACCOUNT_PICKER:        
		    Log.i(TAG, "User is attempting to login.");
		    
		    // save the picked account into sharedPreference
			if (intent != null && resultCode == RESULT_OK) {
				String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				Utils.putPreference(this, Utils.PREFS_ACCOUNT_KEY, accountName);   
			} else {
				Utils.putPreference(this, Utils.PREFS_ACCOUNT_KEY, null);  
			} 
			
			// attempt to login
			new CheckLoginTask(this).execute();
			break;

		default:
			Log.e(TAG, getString(R.string.unknown_activity_request_code, requestCode));
		}
	}
	
	// start Activity from AsnycTask will make test fail
	// So move it here
	public void startActivityViaIntent(Intent intent) {
		startActivity(intent);
	}

	private void exit() {
		showAlertDialog(this,
				getString(R.string.quit_Message),
				getString(R.string.quit_Positive),
				getString(R.string.quit_Negative),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				moveTaskToBack(true);
				finish();
			}
		}, null);
	}

	/**
	 * @param context The current context of UI thread
	 * @param msg Message to show in the body of dialog
	 * @param posButtonText Text of positive button
	 * @param negButtonText Text of negative button
	 * @param posListener Click handler of positive button
	 * @param negListener Click handler of negative button
	 * 
	 */
	 public void showAlertDialog(Context context, String msg, String posButtonText,
			 String negButtonText,  OnClickListener posListener, OnClickListener negListener) {
		 new AlertDialog.Builder(context)
		 .setMessage(msg)
		 .setPositiveButton(posButtonText,
				 posListener)
				 .setNegativeButton(negButtonText, negListener)
				 .show();
	 }

	 private class CheckLoginTask extends AsyncTask<Void, Void, Customer> {

		 Context context;
		 Exception exceptionThrown;

		 public CheckLoginTask(Context context) {
			 this.context = context.getApplicationContext();
		 }
		 @Override
		 protected Customer doInBackground(Void... params) {
			 String accountName = Utils.getPreference(context, Utils.PREFS_ACCOUNT_KEY, null);
			 if (accountName == null) {
				 return null;
			 }				
			 credential.setSelectedAccountName(accountName);
			 endpoint = CloudEndpointUtils.updateBuilder(
					 new AnyTaxi.Builder(
							 AndroidHttp.newCompatibleTransport(),
							 new JacksonFactory(),
							 null)).build();
			 try {
				 Customer result = endpoint.getCustomer(accountName).execute();
				 return result;
			 } catch (IOException e) {
				 exceptionThrown = e;
				 return null;
			 }			
		 }

        @Override
        protected void onPostExecute(Customer result) {
            super.onPostExecute(result);
            if (exceptionThrown != null) {
                CloudEndpointUtils.logAndShow(LoginActivity.this, TAG, exceptionThrown);
                return;
            }
            if (result == null) {
                Log.i(TAG, "No user information in database: redirect to RegisterActivity.");
                
                // redirect user to register
                Intent intent = new Intent(this.context, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityViaIntent(intent);
            } else {
                Log.i(TAG, "User has logged in successfully.");
                
                Toast.makeText(this.context, "Welcome, " + result.getName(), Toast.LENGTH_LONG).show();

                // save user information into sharedPreference
                Utils.updateCustomer(context, result);

                // redirect user to call taxi
                Intent intent = new Intent(this.context, RequestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityViaIntent(intent);
            }
		}

	}
}
