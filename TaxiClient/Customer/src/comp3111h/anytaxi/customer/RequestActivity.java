package comp3111h.anytaxi.customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.model.Customer;
import com.appspot.hk_taxi.anyTaxi.model.GeoPt;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class RequestActivity extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    private final static String TAG = "RequestActivity";


	// Temporary Variable
	// Inspecting the connection status

	static TextView mAddress;

	static ProgressBar mActivityIndicator;

    private SupportMapFragment mMapFragment;
    
    private MultiAutoCompleteTextView myDestination_Field;

    // Memorize the current location in this activity
    Location curLocGlobal;
    String myDestination;
    ArrayAdapter<String> adapter;

    private Button requestButton;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        // Identity user's identity, used to communicate with the server
        /*
         * CustomerAccountInfo.credential =
         * GoogleAccountCredential.usingAudience(this, "server:client_id:" +
         * CustomerAccountInfo.WEB_CLIENT_ID);
         */



		mLocationClient = new LocationClient(this, this, this);

		mAddress = (TextView) findViewById(R.id.addressNew);
		mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);


        requestButton = (Button) findViewById(R.id.request_btn);
        requestButton.setOnClickListener(onRequestListener);
		// Set up the autoComplete TextView
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, getResources()
						.getStringArray(R.array.common_locations));
		myDestination_Field = (MultiAutoCompleteTextView) findViewById(R.id.destination);

		myDestination_Field.setAdapter(adapter);
		myDestination_Field
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        
        mMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map, mMapFragment)
                .commit(); 
        getSupportFragmentManager().executePendingTransactions();
    }


    @Override
    public void onStart() {
        super.onStart();

       
    }

    /**
     * 
     */
    @Override
    public void onResume() {
        super.onResume();
        
        /*
         * Connect the client. Don't re-start any requests here; instead, wait
         * for onResume()
         */
       
        mLocationClient.connect();
        
        LocationUtils.mMap = mMapFragment.getMap();
        LocationUtils.mMap.setMyLocationEnabled(true);
    }

		


    /*
     * Called when the Activity is no longer visible at all. Stop updates and
     * disconnect.
     */
    @Override
    public void onStop() {
        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Choose what to do based on the request code
        ConnectionUtils.requestCodeHandler(requestCode, resultCode, intent,
                this);
    }

    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location Client is connected to GooglePlayService.");

        Location currentLoc = mLocationClient.getLastLocation();
        while (currentLoc == null) {
            currentLoc = mLocationClient.getLastLocation();
        }
        curLocGlobal = currentLoc;

        LatLng locationNew = new LatLng(currentLoc.getLatitude(),
                currentLoc.getLongitude());
        CameraUpdate cameraup = CameraUpdateFactory.newLatLngZoom(locationNew,
                15);
        LocationUtils.mMap.animateCamera(cameraup);


        LocationUtils.getAddress(this, currentLoc);
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "Location Client is disconnected from GooglePlayService.");

        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Location Client failed to connect to GooglePlayService.");

        /*
         * Google Play services can resolve some errors it detects. If the error
         * has a resolution, try sending an Intent to start a Google Play
         * services activity that can resolve error.
         */
        ConnectionUtils.connectionResultHandler(connectionResult, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        exit();
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
		
		if(id == R.id.action_carpool) {
			Intent TestGroupMessageTable = new Intent(RequestActivity.this,
					IndexActivity.class);
			startActivity(TestGroupMessageTable);
		}

        return super.onOptionsItemSelected(item);
    }



    // When taxi button is pressed,
    // the user will be redirected to RequestToTrackingActivity
    private OnClickListener onRequestListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "The user is requesting a taxi.");

            // String des =
            // ((EditText)findViewById(R.id.destination)).getText().toString();

            if (curLocGlobal != null) {
                LatLng locationNew = new LatLng(curLocGlobal.getLatitude(),
                        curLocGlobal.getLongitude());

                if (Utils.customer != null) {
                    Customer c = Utils.customer;
                    GeoPt p = new GeoPt();
                    p.setLatitude((float) locationNew.latitude); // @ Ryan
                                                                    // please
                                                                    // get 1.00
                                                                    // from map
                    p.setLongitude((float) locationNew.longitude); // @ Ryan
                                                                    // please
                                                                    // get 2.00
                                                                    // from map
                    c.setLoc(p);


                    String curAddress = String.valueOf( mAddress.getText());

                    myDestination = myDestination_Field.getText().toString();


                    Bundle customerInfo = new Bundle();
                    customerInfo.putString("EMAIL", Utils.customer.getEmail());
                    customerInfo.putString("CURADD", (curAddress == null ? ""
                            : curAddress));
                    customerInfo.putDouble("LAT", locationNew.latitude);
                    customerInfo.putDouble("LON", locationNew.longitude);
                    customerInfo.putString("DEST", myDestination);


                    Intent intent = new Intent(RequestActivity.this,
                            RequestToTrackingActivity.class);
                    intent.putExtras(customerInfo);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "The user doesn't exist.");

                    ConnectionUtils.showError(RequestActivity.this,
                            "The customer doesn't exist");
                }
            }

            else {
                Log.e(TAG, "User location is unavailable.");

                ConnectionUtils.showError(RequestActivity.this,
                        "No connection available!");
            }
        }
    };



    private void exit() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.quit_Message))
                .setPositiveButton(getString(R.string.quit_Positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                moveTaskToBack(true);
                                finish();
                            }
                        })
                .setNegativeButton(getString(R.string.quit_Negative), null)
                .show();
    }
    public SupportMapFragment getMapFragment() {
        return mMapFragment;
    }


    public LocationClient getLocationClient() {
        return mLocationClient;
    }

}
