package comp3111h.anytaxi.driver;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.appspot.hk_taxi.anyTaxi.model.GeoPt;
import com.appspot.hk_taxi.anyTaxi.model.Transaction;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
public class TraceActivity extends FragmentActivity{

	// service
	private LocationBroadcastService myService;
	private Intent serviceIntent;
	
	private String TAG = "TraceActivity";

	private TextView mAddress;
	private ProgressBar mActivityIndicator;

	//The lat and lng passed by CusTomerListActivity
	private float latFloat;
	private float lntFloat;
	
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

	// Handle to a SharedPreferences editor
	SharedPreferences.Editor mEditor;

	/*
	 * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
	 * method handleRequestSuccess of LocationUpdateReceiver.
	 *
	 */    

	/*CURRENT LOCATION INFO VAR END*/

	public static GoogleMap mMap;
	private SupportMapFragment mMapFragment;

	int index; // index of item onclick in customer list
	String customerLoc;
	String customerDes;
	float[] myLatLng;
	Long transactionId;

	private AnyTaxi endpoint;    

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);

		// Get handles to the UI view objects
		mAddress = (TextView) findViewById(R.id.address);
		mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);

		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction =
				getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map, mMapFragment);
		fragmentTransaction.commit();
		getSupportFragmentManager().executePendingTransactions();

		Intent intent = getIntent();
		customerLoc = intent.getStringExtra(Utils.CUSTOMER_LOC);
		customerDes = intent.getStringExtra(Utils.CUSTOMER_DES);
		myLatLng = intent.getFloatArrayExtra(Utils.CUSTOMER_LATLNG);
		
		transactionId = intent.getLongExtra(Utils.TRASACTION_ID, 0);
		transactionId = (transactionId == 0)? null : transactionId;
		
		index = intent.getIntExtra("customer_list_id", 0);
		//test cases needed
		
		if(myLatLng!=null)
		{
			latFloat = myLatLng[0];
			lntFloat = myLatLng[1];
		}
		else
		{
			showError(this,"FuckTheCode!latlntParseFailed");
		}
		
		Utils.putPreference(this, Utils.DRIVER_LAT, Float.valueOf(latFloat).toString());
		Utils.putPreference(this, Utils.DRIVER_LNG, Float.valueOf(lntFloat).toString());
		

		// Open Shared Preferences
		mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

		// Get an editor
		mEditor = mPrefs.edit();
		
		serviceIntent = new Intent(this, LocationBroadcastService.class);
	}
	




	public void accept(View view){

		new AsyncTask<Long, Void, Transaction>() {
			private Exception exceptionThrown;

			@Override
			protected Transaction doInBackground(Long... params) {
				endpoint = CloudEndpointUtils.updateBuilder(
						new AnyTaxi.Builder(AndroidHttp
								.newCompatibleTransport(),
								new JacksonFactory(), null)).build();
				Driver d = Utils.getDriver(TraceActivity.this);
				if (d != null) {
					Transaction t;
					try {
						t = endpoint.acceptTransaction(d.getEmail(),
								params[0]).execute();
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
					Toast.makeText(
							TraceActivity.this,
							"It's too late! The order has been accepted by someone else.",
							Toast.LENGTH_LONG).show();
				} else {
					// TODO: add truely meaningful accept
				}
				if (exceptionThrown != null) {
					Log.e(TAG, "Exception", exceptionThrown);
				}
			}
		}.execute(transactionId);

		// block cloud message
		CustomerListActivity.removeItemInList(index); //test cases needed
		startService(new Intent(this, LocationBroadcastService.class));
		
		finish();		
	}

	public void decline(View view){
		CustomerListActivity.removeItemInList(index); //test cases needed
		finish();
	}

	/*
	 * Called when the Activity is restarted, even before it becomes visible.
	 */
	@Override
	public void onStart() {

		super.onStart();
	}

	/*
	 * Called when the Activity is no longer visible at all.
	 * Stop updates and disconnect.
	 */
	@Override
	public void onStop() {
		super.onStop();
	}
	/*
	 * Called when the Activity is going into the background.
	 * Parts of the UI may be visible, but the Activity is inactive.
	 */
	@Override
	public void onPause() {
		super.onPause();
	}
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        
        LatLng locationNew = new LatLng(latFloat,lntFloat);
        CameraUpdate cameraup=CameraUpdateFactory.newLatLngZoom(locationNew,15);
        mMap.animateCamera(cameraup);
        final Marker marker = mMap.addMarker(new MarkerOptions().position(locationNew));
    }

	public static void showError(final Activity activity, String message) {
		final String errorMessage = (message == null) ? "Error" : "[Error ] "
				+ message;
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
				.show();
			}
		});
	}
}
