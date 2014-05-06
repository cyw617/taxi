package comp3111h.anytaxi.driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.GeoPt;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

public class LocationBroadcastService extends Service implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
	LocationListener {
	
	IBinder mBinder = new LocalBinder();
  
  private LocationClient mLocationClient;
  private LocationRequest mLocationRequest;
  // Flag that indicates if a request is underway.
  private boolean mInProgress;
  
  private Boolean servicesAvailable = false;
  
  private AnyTaxi endpoint;
  
  private String driverEmail;
  private GeoPt driverLoc;
  
  public class LocalBinder extends Binder {
  	public LocationBroadcastService getServerInstance() {
  		return LocationBroadcastService.this;
  	}
  }
  
  @Override
	public void onCreate() {
      super.onCreate();
      

      mInProgress = false;
      // Create the LocationRequest object
      mLocationRequest = LocationRequest.create();
      // Use high accuracy
      mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
      // Set the update interval to 5 seconds
      mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
      // Set the fastest update interval to 1 second
      mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL);
      
      servicesAvailable = servicesConnected();
      
      /*
       * Create a new location client, using the enclosing class to
       * handle callbacks.
       */
      mLocationClient = new LocationClient(this, this, this);
      
      
  }
  
  private boolean servicesConnected() {
  	
      // Check that Google Play services is available
      int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
      
      // If Google Play services is available
      if (ConnectionResult.SUCCESS == resultCode) {

          return true;
      } else {

          return false;
      }
  }
  
  public int onStartCommand (Intent intent, int flags, int startId)
  {
      super.onStartCommand(intent, flags, startId);
      
      if(!servicesAvailable || mLocationClient.isConnected() || mInProgress)
      	return START_STICKY;
      
      setUpLocationClientIfNeeded();
      if(!mLocationClient.isConnected() || !mLocationClient.isConnecting() && !mInProgress)
      {
      	mInProgress = true;
      	mLocationClient.connect();
      }
      if(endpoint ==null)
      {
    	  endpoint = CloudEndpointUtils.updateBuilder(
  				new AnyTaxi.Builder(AndroidHttp
  						.newCompatibleTransport(),
  						new JacksonFactory(), null)).build();
      }
      
      driverEmail = Utils.getPreference(this, Utils.PREFS_ACCOUNT_KEY, null);
      String DriverLat = Utils.getPreference(this, Utils.DRIVER_LAT, null);
      String DriverLng = Utils.getPreference(this, Utils.DRIVER_LNG, null);
      
      driverLoc = new GeoPt();
      driverLoc.setLatitude(Float.valueOf(DriverLat));
      driverLoc.setLongitude(Float.valueOf(DriverLng));
		
      
      
      return START_STICKY;
  }

	/*
   * Create a new location client, using the enclosing class to
   * handle callbacks.
   */
  private void setUpLocationClientIfNeeded()
  {
  	if(mLocationClient == null) 
          mLocationClient = new LocationClient(this, this, this);
  }
  
  // Define the callback method that receives location updates
  @Override
  public void onLocationChanged(Location location) {
      // Report to the UI that the location was updated
      String msg = Double.toString(location.getLatitude()) + "," +
              Double.toString(location.getLongitude());
      Log.d("debug", msg);
      // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
      appendLog(msg, Constants.LOCATION_FILE);
      if(endpoint == null)
      {
    	  Log.e("debug","endpoint is null on location changed");
      }
      
    try {
		endpoint.updateDriverLocation(driverEmail, driverLoc);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
      
  }

  @Override
  public IBinder onBind(Intent intent) {
  	return mBinder;
  }

  public String getTime() {
  	SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  	return mDateFormat.format(new Date());
  }
  
  public void appendLog(String text, String filename)
  {       
     File logFile = new File(filename);
     if (!logFile.exists())
     {
        try
        {
           logFile.createNewFile();
        } 
        catch (IOException e)
        {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }
     }
     try
     {
        //BufferedWriter for performance, true to set append to file flag
        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
        buf.append(text);
        buf.newLine();
        buf.close();
     }
     catch (IOException e)
     {
        // TODO Auto-generated catch block
        e.printStackTrace();
     }
  }
  
  @Override
  public void onDestroy(){
      // Turn off the request flag
      mInProgress = false;
      if(servicesAvailable && mLocationClient != null) {
	        mLocationClient.removeLocationUpdates(this);
	        // Destroy the current location client
	        mLocationClient = null;
      }
      // Display the connection status
     
      super.onDestroy();  
  }
  
  /*
   * Called by Location Services when the request to connect the
   * client finishes successfully. At this point, you can
   * request the current location or start periodic updates
   */
  @Override
  public void onConnected(Bundle bundle) {
  	
      // Request location updates using static settings
      mLocationClient.requestLocationUpdates(mLocationRequest, this);
   

  }

  /*
   * Called by Location Services if the connection to the
   * location client drops because of an error.
   */
  @Override
  public void onDisconnected() {
      // Turn off the request flag
      mInProgress = false;
      // Destroy the current location client
      mLocationClient = null;
      // Display the connection status
  }

  /*
   * Called by Location Services if the attempt to
   * Location Services fails.
   */
  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
  	mInProgress = false;
  	
      /*
       * Google Play services can resolve some errors it detects.
       * If the error has a resolution, try sending an Intent to
       * start a Google Play services activity that can resolve
       * error.
       */
      if (connectionResult.hasResolution()) {

      // If no resolution is available, display an error dialog
      } else {

      }
  }
  
  
}
