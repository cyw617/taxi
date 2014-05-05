package comp3111h.anytaxi.customer;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationService extends Service implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private LocationRequest mLocationRequest;
	LocationClient mLocationClient;
	/*
	 * Constants for location update parameters
	 */
	// Milliseconds per second
	public static final int MILLISECONDS_PER_SECOND = 1000;
	// The update interval
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// A fast interval ceiling
	public static final int FAST_CEILING_IN_SECONDS = 1;
	// Update interval in milliseconds
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// A fast ceiling of update intervals, used when the app is visible
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;

	@Override
	public void onCreate() {
		super.onCreate();

		// Create a new global location parameters object
		mLocationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		mLocationRequest
				.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		mLocationClient = new LocationClient(this, this, this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		// Don't automatically restart this Service if it is killed
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {

		// If the client is connected
		if (mLocationClient.isConnected()) {
			stopPeriodicUpdates();
		}

		// After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		ConnectionUtils.connectionResultHandler(connectionResult, this);

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();

	}

	/**
	 * Report location updates to the UI.
	 * 
	 * @param location
	 *            The updated location.
	 */
	@Override
	public void onLocationChanged(Location location) {

		// Report to the UI that the location was updated

		// Changes only need to be done in getLocation to cosntantly update the
		// location info

	}

	@SuppressWarnings("unused")
	private void startPeriodicUpdates() {
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
	}

}
