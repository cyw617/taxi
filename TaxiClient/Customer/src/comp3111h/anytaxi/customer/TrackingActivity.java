package comp3111h.anytaxi.customer;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrackingActivity extends ActionBarActivity{
	
	
	public static GoogleMap mMap;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        
        Double latDouble = 22.3367;
        Double lngDouble = 114.2639;
        LatLng locationNew = new LatLng(latDouble,lngDouble);
        CameraUpdate cameraup=CameraUpdateFactory.newLatLngZoom(locationNew,15);
        mMap.animateCamera(cameraup);
        
        
        final Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(22.3367, 114.2639)));
        
		final Handler h = new Handler();
		final int delay = 1000; //milliseconds
		
		h.postDelayed(new Runnable(){
	        Double latDouble = 22.3367;
	        Double lngDouble = 114.2639;
		    public void run(){
		    	Random rand=new Random();
		    	double locationVariation1 = rand.nextDouble() * 0.01 - 0.005;
		    	double locationVariation2 = rand.nextDouble() * 0.01 - 0.005;
		        latDouble+=locationVariation1;
		        lngDouble+= locationVariation2;
		       
				LatLng locationNew = new LatLng(latDouble,lngDouble);
		        //CameraUpdate cameraup=CameraUpdateFactory.newLatLngZoom(locationNew,15);
		        //mMap.animateCamera(cameraup);
		        
		        marker.setPosition(locationNew);
		    	
		        h.postDelayed(this, delay);
		    }
		}, delay);
        
        
	}
	
	public void goBack(View view){
		finish();
	}
	
	/*
	
    private class EndpointsTask extends AsyncTask<Void, Void, Void> {
		Exception exceptionThrown = null;
		AnyTaxi endpoint;
		Driver d;

		public EndpointsTask(Activity activity, AnyTaxi endpoint, LatLng startLocation) {
			this.endpoint = endpoint;
			this.d = d;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

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
	*/
}
