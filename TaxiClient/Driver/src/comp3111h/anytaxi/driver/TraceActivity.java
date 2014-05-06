package comp3111h.anytaxi.driver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
public class TraceActivity extends FragmentActivity implements
		LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener{
	

    

	private TextView mAddress;
	private ProgressBar mActivityIndicator;

    
	//The lat and lng passed by CusTomerListActivity
	private double latDouble;
	private double lntDouble;
	
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
		String latString = intent.getStringExtra("Latitude");
		String lntString = intent.getStringExtra("Longitude");
		index = intent.getIntExtra("customer_list_id", 0);
		//test cases needed
		
		if(latString!=null&&lntString!=null)
		{
			latDouble = Double.parseDouble(latString);
			lntDouble = Double.parseDouble(lntString);
		}
		else
		{
			showError(this,"FuckTheCode!latlntParseFailed");
		}
		
		


        // Open Shared Preferences
        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

        

	}
	

	
	public void accept(View view){
		// block cloud message
		Intent intent = new Intent(TraceActivity.this, TrackingActivity.class);
		startActivity(intent);
		CustomerListActivity.removeItemInList(index); //test cases needed
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
    
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();


        mMap = mMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        
        LatLng locationNew = new LatLng(latDouble,lntDouble);
        CameraUpdate cameraup=CameraUpdateFactory.newLatLngZoom(locationNew,15);
        mMap.animateCamera(cameraup);
        final Marker marker = mMap.addMarker(new MarkerOptions().position(locationNew));
    }
    
	public static void showError(final Activity activity, String message) {
		  final String errorMessage = message == null ? "Error" : "[Error ] "
		      + message;
		  activity.runOnUiThread(new Runnable() {
		    public void run() {
		      Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
		          .show();
		    }
		  });
		}
 
}
