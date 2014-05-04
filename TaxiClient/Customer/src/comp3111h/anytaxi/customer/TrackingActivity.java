package comp3111h.anytaxi.customer;

import java.io.IOException;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.appspot.hk_taxi.anyTaxi.model.GeoPt;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import comp3111h.anytaxi.customer.RequestToTrackingActivity.LoadingDriverTask;




public class TrackingActivity extends ActionBarActivity{
	
	private static final String TAG = "TrackingActivity";
	final int driverUpDelay = 500;
	public static GoogleMap mMap;
	private static Marker marker;
	
	
	Driver myDriver;
	private static Bundle driverInfo;
	private static String driverEmail;
	private static AnyTaxi endpoint; 
	private static GeoPt driverLoc;
	private static LatLng myDriverLoc;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        endpoint = CloudEndpointUtils.updateBuilder(
				new AnyTaxi.Builder(
						AndroidHttp.newCompatibleTransport(),
						new JacksonFactory(),
						null)).build();
        
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        //We get the previous RequestToTrackingActivity as an Intent
        Intent prevIntent = getIntent();
        driverInfo = prevIntent.getExtras();
        driverEmail = driverInfo.getString("Email");
        
        try {
			myDriver = endpoint.getDriver(driverEmail).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        driverLoc = myDriver.getLoc();
        myDriverLoc = new LatLng(driverLoc.getLatitude(),driverLoc.getLongitude());
        CameraUpdate cameraup=CameraUpdateFactory.newLatLngZoom(myDriverLoc,15);
        mMap.animateCamera(cameraup);
        
                
        new TrackingDriverTask().execute();   
		
	}
	
	public void goBack(View view){
		finish();
	}
	

	class TrackingDriverTask extends AsyncTask<Void, LatLng, Void>{

		@Override
		protected void onPreExecute() {
		}

		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			marker = mMap.addMarker(new MarkerOptions().position(myDriverLoc));
			
			while(true)
			{
				GeoPt newLoc = myDriver.getLoc();
				myDriverLoc = new LatLng(newLoc.getLatitude(),newLoc.getLongitude());
				publishProgress(myDriverLoc);
				sleep();
			}
		}
		
	}
	
	protected void onProgressUpdate(LatLng location) {
		marker.setPosition(location);
	}
	
	private void sleep() {
		try {
			Thread.sleep(driverUpDelay);
		} catch (InterruptedException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	
}
