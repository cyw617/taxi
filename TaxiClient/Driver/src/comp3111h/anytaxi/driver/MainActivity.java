package comp3111h.anytaxi.driver;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;

public class MainActivity extends ActionBarActivity {

	public static final String TAG = "MainActivity"; 
	
    protected static GoogleAccountCredential credential;  
    protected static AnyTaxi endpoint;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        credential = GoogleAccountCredential.usingAudience(this, Utils.AUDIENCE);  
        Utils.driver = Utils.getDriver(this);
	}
	
	@Override
	public void onStart() {
	    super.onStart();    
		if (Utils.driver == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (!Utils.isOnline(this)) {
			Toast.makeText(this, "Unable to connect to Internet.", Toast.LENGTH_LONG).show();
		} else if (Utils.driver != null) {			
			new CheckLoginTask(this).execute();
	        Toast.makeText(this, "Checking account status, please wait...", 
	        		Toast.LENGTH_LONG).show();
		}
	}
	
	// start Activity from AsnycTask will make test fail
	// So move it here
	public void startActivityViaIntent(Intent intent) {
		startActivity(intent);
	}
	
	private class CheckLoginTask extends AsyncTask<Void, Void, Driver> {

		Context context;
		Exception exceptionThrown;
		
		public CheckLoginTask(Context context) {
			this.context = context.getApplicationContext();
		}
		@Override
		protected Driver doInBackground(Void... params) {	
			credential.setSelectedAccountName(Utils.driver.getEmail());
			endpoint = CloudEndpointUtils.updateBuilder(
					new AnyTaxi.Builder(
							AndroidHttp.newCompatibleTransport(),
							new JacksonFactory(),
							null)).build();
			try {
				Driver result = endpoint.getDriver(Utils.driver.getEmail()).execute();
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
				CloudEndpointUtils.logAndShow(MainActivity.this, TAG, exceptionThrown);
				return;
			}
			if (result == null) {
				Intent intent = new Intent(this.context, LoginActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivityViaIntent(intent);
			} else {
				Utils.updateDriver(context, result);
				Intent intent = new Intent(this.context, CustomerListActivity.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivityViaIntent(intent);
				CloudEndpointUtils.logAndShow(MainActivity.this, TAG, "Successfully logged in!");
			}
		}
		
	}
}
