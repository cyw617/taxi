package comp3111h.anytaxi.customer;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class RequestActivity extends ActionBarActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener{
	
	//Temporary Variable
	//Inspecting the connection status
	static TextView mConnectionState;
	static TextView mConnectionStatus;
	static TextView mAddress;
	static TextView mLatLng;
	static ProgressBar mActivityIndicator;
	
	private Button moreButoon=(Button) findViewById(R.id.more);
	
	
	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		
		//Identity user's identity, used to communicate with the server
		/*
		CustomerAccountInfo.credential = GoogleAccountCredential.usingAudience(this,
				"server:client_id:" + CustomerAccountInfo.WEB_CLIENT_ID);
				*/
		
		mLocationClient = new LocationClient(this, this, this);
		mConnectionState = (TextView) findViewById(R.id.text_connection_state);
		mConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
		mAddress = (TextView) findViewById(R.id.address);
		mLatLng = (TextView) findViewById(R.id.lat_lng);
		mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
		
		
		LocationUtils.mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		LocationUtils.mMap.setMyLocationEnabled(true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		/*
		 * Connect the client. Don't re-start any requests here;
		 * instead, wait for onResume()
		 */
		mLocationClient.connect();
	}
	
	/*
	 * Called when the Activity is no longer visible at all.
	 * Stop updates and disconnect.
	 */
	@Override
	public void onStop() {
		// After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();
		super.onStop();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		super.onActivityResult(requestCode, resultCode, intent);
		// Choose what to do based on the request code
		ConnectionUtils.requestCodeHandler(requestCode, resultCode, intent, this);
	} 
	
	/*
	 * Called by Location Services when the request to connect the
	 * client finishes successfully. At this point, you can
	 * request the current location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle bundle) {
		mConnectionStatus.setText(R.string.connected);

		Location currentLoc = mLocationClient.getLastLocation();
		while(currentLoc==null)
		{
			currentLoc = mLocationClient.getLastLocation();
		}
		LatLng locationNew = new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude());
		CameraUpdate cameraup=CameraUpdateFactory.newLatLngZoom(locationNew,15);
		LocationUtils.mMap.animateCamera(cameraup);
		
		
		LocationUtils.getAddress(this,currentLoc);
		
	}
	
	@Override
	public void onDisconnected() {
	       Toast.makeText(this, "Disconnected. Please re-connect.",
	       Toast.LENGTH_SHORT).show();
	}
	
	/*
	 * Called by Location Services if the attempt to
	 * Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects.
		 * If the error has a resolution, try sending an Intent to
		 * start a Google Play services activity that can resolve
		 * error.
		 */
		ConnectionUtils.connectionResultHandler(connectionResult,this);
	}
}