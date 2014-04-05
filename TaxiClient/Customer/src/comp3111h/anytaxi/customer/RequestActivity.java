package comp3111h.anytaxi.customer;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.AnyTaxi.AddDriver;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson.JacksonFactory;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RequestActivity extends ActionBarActivity{
	
	private AnyTaxi endpoint;
	private String accountName;
	private GoogleAccountCredential credential;
	static final String WEB_CLIENT_ID = "1072316261853-u0gafkut9f919bau91gh9bgjb9555hh2.apps.googleusercontent.com"; 
	static final int REQUEST_ACCOUNT_PICKER = 1; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		
		credential = GoogleAccountCredential.usingAudience(this, "server:client_id" + WEB_CLIENT_ID);
		startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        exit();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
 	   switch (requestCode) {
 	      case REQUEST_ACCOUNT_PICKER:
 	         if (data != null && data.getExtras() != null) {

 	         accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
 	         if (accountName != null) {
 	            credential.setSelectedAccountName(accountName);

 	    		AnyTaxi.Builder endpointBuilder = new AnyTaxi.Builder(
 	    				AndroidHttp.newCompatibleTransport(),
 	    				new JacksonFactory(),
 	    				credential);
 	    		
 	            endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
 	    		
 	         }
 	      }
 	      break;
 	   }
    }
	
	private void showDialog(String message) {
		  new AlertDialog.Builder(this)
		      .setMessage(message)
		      .setPositiveButton(android.R.string.ok,
		          new DialogInterface.OnClickListener() {
		    	  
		    	    @Override
		            public void onClick(DialogInterface dialog, int id) {
		    	    	
		    	    	Driver d = new Driver();
		 	    		d.setEmail(credential.getSelectedAccountName());
		    	    	new EndpointsTask(RequestActivity.this, endpoint, d).execute();
		    	    	Intent intent = new Intent(RequestActivity.this, TrackingActivity.class);
		    			startActivity(intent);
		            }
		          })
		          .setNegativeButton(android.R.string.cancel, 
		        		  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								
							}
						}).show();
		}
	
	public void MoreOption(View view) {
		Intent intent = new Intent(this, MoreOption.class);
		startActivity(intent);
	}
	
	public void Request(View view){
		String x = ((EditText)findViewById(R.id.editText1)).getText().toString();
		String y = ((EditText)findViewById(R.id.editText2)).getText().toString();
		String des = ((EditText)findViewById(R.id.editText3)).getText().toString();
		showDialog("This will send to sever "+x+" "+y+" "+des);
	}
    
    private void exit() {
        new AlertDialog.Builder(this)
        .setMessage(getString(R.string.quit_Message))
        .setPositiveButton(getString(R.string.quit_Positive),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    finish();
                }
            })
        .setNegativeButton(getString(R.string.quit_Negative), null)
        .show();
    }
    
    private class EndpointsTask extends AsyncTask<Void, Void, Driver> {
		Exception exceptionThrown = null;
		AnyTaxi endpoint;
		Driver d;

		public EndpointsTask(Activity activity, AnyTaxi endpoint, Driver d) {
			this.endpoint = endpoint;
			this.d = d;
		}

		@Override
		protected Driver doInBackground(Void... params) {
			try {
				AddDriver task = endpoint.addDriver(d);
				Driver driver = task.execute();
				return driver;
			} catch (Exception e) {
				exceptionThrown = e;
			}
			return null;
		}

		protected void onPostExecute(Driver driver) {
			// Check if exception was thrown
			if (exceptionThrown != null) {
				Log.e(MainActivity.class.getName(), 
						"Exception when submitting data" 
				+ Log.getStackTraceString(exceptionThrown));
				
				showDialog("Failed to submit the data via " +
						"the endpoint at " + endpoint.getBaseUrl() +
						", check log for details");
			}
			else {
				showDialog("Successfully submitted the data via " +
						"the endpoint at " + endpoint.getBaseUrl());
			}
		}   
	}
}
