package comp3111h.anytaxi.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.appspot.hk_taxi.anyTaxi.model.Transaction;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

public class CustomerListActivity extends ActionBarActivity{
	
	protected static final String TAG = "CustomerListActivity";
	private ListView listView;
	private static ArrayList<String> strArr;
	private static ArrayAdapter<String> arrAdapter;
	
	private AnyTaxi endpoint;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_list);		

		listView = (ListView) findViewById(R.id.listView1);
		strArr = new ArrayList<String>();
		//arrAdapter = new ArrayAdapter<String>(getApplicationContext(), 
			//	android.R.layout.simple_list_item_1, strArr);
		arrAdapter = new ArrayAdapter<String>(getApplicationContext(), 
				R.layout.customer_list_view_layout, strArr);
		
		listView.setEmptyView(findViewById(android.R.id.empty));
		listView.setAdapter(arrAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Intent intent = new Intent(CustomerListActivity.this, TraceActivity.class);
				String latlnt=(String)parent.getItemAtPosition(position);
				/*
				 * Set up the string delimiter as "\t"
				 * The lat and lnt are separated by a "\t"
				 */
				String delims = "[\t]+";
				String[] tokens = latlnt.split(delims);
				String latString=tokens[1];
				String lntString=tokens[2];				
				
				intent.putExtra("Latitude", latString);
				intent.putExtra("Longitude", lntString);
				startActivity(intent);
			}
		});
		
		
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
	
    
    /*
     * Add Item to the textList. First Latitude then Longitude. 
     * They will be separated by tab
     */
	public void addItem(View view){
		
		latText = (EditText) findViewById(R.id.lat);
		lntText = (EditText) findViewById(R.id.lnt);
		Geocoder geocoder;
		List<Address> addresses;
		String address;
		geocoder = new Geocoder(this, Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(Double.parseDouble(latText.getText().toString()), Double.parseDouble(lntText.getText().toString()), 1);
			 // If the reverse geocode returned an address
	        if (addresses != null && addresses.size() > 0) {

	            // Get the first address
	        	address = addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1);
				
				strArr.add(address+"\t"+latText.getText().toString()+"\t"+lntText.getText().toString());

				arrAdapter.notifyDataSetChanged();

	        // If there aren't any addresses, post a message
	        } else {
	          //return getString(R.string.no_address_found);
	        }
			
		} catch (IOException exception1) {

            // Log an error and return an error message
            Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

            // print the stack trace
            exception1.printStackTrace();


        // Catch incorrect latitude or longitude values
        } catch (IllegalArgumentException exception2) {

            // Construct a message containing the invalid arguments
            String errorString = getString(
                    R.string.illegal_argument_exception,
                    latText.getText().toString(),
                    lntText.getText().toString()
            );
            // Log the error and print the stack trace
            Log.e(LocationUtils.APPTAG, errorString);
            exception2.printStackTrace();

        }
	}
	
	public void removeItem(View view){
		if(strArr.isEmpty()!=true){
			strArr.remove(0);
			arrAdapter.notifyDataSetChanged();
		}
	}
	
	public void clearItems(View view){
		strArr.clear();
		arrAdapter.notifyDataSetChanged();
	}
	
	public static void removeItemInList(int index){
		if(index>=0 && index<strArr.size() )
		strArr.remove(index);
		arrAdapter.notifyDataSetChanged();
	}
    
	public int numItemsInStrArr(){ // for test
		return strArr.size();
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
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	Long transactionId = Long.parseLong(intent.getStringExtra(GCMIntentService.TRANSACTION_ID));
    	final Context context = this.getApplicationContext();
    	if (transactionId != null) {
    		new AsyncTask<Long, Void, Transaction>() {
    			private Exception exceptionThrown;
    			@Override
				protected Transaction doInBackground(Long...params) {
    				endpoint = CloudEndpointUtils.updateBuilder(
    						new AnyTaxi.Builder(
    								AndroidHttp.newCompatibleTransport(),
    								new JacksonFactory(),
    								null)).build();
    				Driver d = Utils.getDriver(context);
    				if (d != null) {
    					Transaction t;
    					try {
							t = endpoint.acceptTransaction(d.getEmail(), params[0]).execute();
						} catch (IOException e) {
							exceptionThrown = e;
							return null;
						}
    					return t;
    				}
    				return null;
				}
    			
    			@Override
    			protected void onPostExecute(Transaction t) {
    				if (t == null) {
    					Toast.makeText(context, "It's too late! The order has been accepted by someone else.",
    							Toast.LENGTH_LONG).show();
    				} else {
    					// TODO: add something meaningful!
    					t.getCustomerLocStr();
    					t.getDestLocStr();
    				}
    				if (exceptionThrown != null) {
    					Log.e(TAG, "Exception", exceptionThrown);
    				}
    			}
    		}.execute(transactionId);
    	}
    }
}
