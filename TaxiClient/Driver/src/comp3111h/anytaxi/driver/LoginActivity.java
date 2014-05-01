package comp3111h.anytaxi.driver;

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
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
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
		// TODO: Design a new login layout for new procedure
		// setContentView(R.layout.activity_login);        
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
			if (intent != null && resultCode == RESULT_OK) {
				String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				Utils.putPreference(this, Utils.PREFS_ACCOUNT_KEY, accountName);   
			} else {
				Utils.putPreference(this, Utils.PREFS_ACCOUNT_KEY, null);  
			} 
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

	 private class CheckLoginTask extends AsyncTask<Void, Void, Driver> {

		 Context context;
		 Exception exceptionThrown;

		 public CheckLoginTask(Context context) {
			 this.context = context.getApplicationContext();
		 }
		 @Override
		 protected Driver doInBackground(Void... params) {
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
				 Driver result = endpoint.getDriver(accountName).execute();
				 return result;
			 } catch (IOException e) {
				 exceptionThrown = e;
				 return null;
			 }			
		 }

		 @Override
		 protected void onPostExecute(Driver result) {
			 super.onPostExecute(result);
			 if (exceptionThrown != null) {
				 CloudEndpointUtils.logAndShow(LoginActivity.this, TAG, exceptionThrown);
				 return;
			 }
			 if (result == null) {
				 Intent intent = new Intent(this.context, RegisterActivity.class);
				 startActivityViaIntent(intent);
			 } else {
				Utils.updateDriver(context, result);
				
				// TODO: remove this in the final version
				Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
				
				 Intent intent = new Intent(this.context, CustomerListActivity.class);
				 // TODO: since RequestActivity currently has a bug, we display the message 
				 // from server instead.
				 startActivityViaIntent(intent);
				 CloudEndpointUtils.logAndShow(LoginActivity.this, TAG, "Successfully logged in!");
			 }
		 }

	 }
}
