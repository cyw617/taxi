package comp3111h.anytaxi.customer;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.appspot.hk_taxi.anyTaxi.model.GeoPt;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

public class TrackingActivity extends ActionBarActivity {

	private static final String TAG = "TrackingActivity";
	final int driverUpDelay = 500;
	private static Marker marker;

	Driver myDriver;
	private SupportMapFragment mMapFragment;
	private static Bundle driverInfo;
	private static String driverEmail;
	private static AnyTaxi endpoint;
	private static GeoPt driverLoc;
	private static LatLng myDriverLoc;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
        
        mMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map, mMapFragment)
                .commit(); 
        getSupportFragmentManager().executePendingTransactions();

		// We get the previous RequestToTrackingActivity as an Intent
		Intent prevIntent = getIntent();
		driverInfo = prevIntent.getExtras();
		driverEmail = driverInfo.getString("Email");

		new AsyncTask<String, Void, Driver>() {
			Exception exception;
			
			@Override
			protected Driver doInBackground(String...params) {
				try {
					endpoint = CloudEndpointUtils.updateBuilder(
							new AnyTaxi.Builder(AndroidHttp.newCompatibleTransport(),
									new JacksonFactory(), null)).build();
					return endpoint.getDriver(params[0]).execute();
				} catch (IOException e) {
					exception = e;
					return null;
				}
			}
			protected void onPostExecute(Driver d) {
				if (exception != null) {
					Toast.makeText(TrackingActivity.this, "Unable to track your driver!",
							Toast.LENGTH_LONG).show();
					Log.e(TAG, "Can't get driver", exception);
				} else {
					myDriver = d;
					driverLoc = myDriver.getLoc();
					myDriverLoc = new LatLng(driverLoc.getLatitude(),
							driverLoc.getLongitude());
					
			        LocationUtils.mMap = mMapFragment.getMap();
			        LocationUtils.mMap.setMyLocationEnabled(true);
			        CameraUpdate cameraup = CameraUpdateFactory.newLatLngZoom(myDriverLoc,
							15);
			        LocationUtils.mMap.animateCamera(cameraup);
					
					new TrackingDriverTask().execute();
				}
			}
		}.execute(driverEmail);
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onResume(){
		super.onResume();
        
	}

	public void goBack(View view) {
		finish();
	}

	class TrackingDriverTask extends AsyncTask<Void, LatLng, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			//Auto-generated method stub
			marker = LocationUtils.mMap.addMarker(new MarkerOptions().position(myDriverLoc)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.taxipointer)));

			while (true) {
				
				if(myDriver!=null)
				{
					GeoPt newLoc = myDriver.getLoc();
					myDriverLoc = new LatLng(newLoc.getLatitude(),
							newLoc.getLongitude());
					publishProgress(myDriverLoc);
					sleep();
				}
				else
				{
					
				}
				
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
