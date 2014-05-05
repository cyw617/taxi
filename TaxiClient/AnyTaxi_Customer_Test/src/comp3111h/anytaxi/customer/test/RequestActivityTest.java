package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RequestActivity;

public class RequestActivityTest extends ActivityUnitTestCase<RequestActivity> implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private Context rContext;
    
    private LocationClient rLocationClient;
    
    private Location mockLocation;
    
    private static final String PROVIDER = "flp";
    private static final double LAT = 37.377166;
    private static final double LNG = -122.086966;
    private static final float ACCURACY = 3.0f;
    
    public RequestActivityTest() {
        super(RequestActivity.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        rContext = getInstrumentation().getTargetContext();
        
        rLocationClient = new LocationClient(rContext, this, this);
        
        mockLocation = new Location(PROVIDER);
        mockLocation.setLatitude(LAT);
        mockLocation.setLongitude(LNG);
        mockLocation.setAccuracy(ACCURACY);
        
        // set theme for AppCompat support
        setActivityContext(new ContextThemeWrapper(rContext, R.style.AppTheme));
        
        // onCreate() will be called for the activity
        Intent intent = new Intent(rContext, RequestActivity.class);
        startActivity(intent, null, null);
        
        // getActivity().onCreateOptionsMenu(new MenuBuilder(rContext));
    }
    
/*    @SmallTest
    public void testOnRequestListener() {
        // getActivity().onStart();
        
        Button requestButton = (Button) getActivity().findViewById(R.id.request_btn);
        
        assertNotNull("Request button is not found.", requestButton);
        
        requestButton.performClick();
        
        final Intent redirect = getStartedActivityIntent();
        assertNotNull("No activity is started.", redirect);
        assertEquals(redirect.getComponent().getShortClassName(), ".RequestToTrackingActivity");
    }*/
    
    @SmallTest
    public void testCreateButtonListener() {
        // getActivity().onStart();
        
        Button moreButton = (Button) getActivity().findViewById(R.id.more);
        
        assertNotNull("More button is not found.", moreButton);
        
        moreButton.performClick();
        
        Button requestButton = (Button) getActivity().findViewById(R.id.request_btn);
        
        assertNotNull("Request button is not found.", requestButton);
        
        requestButton.performClick();
        
        
        final Intent redirect = getStartedActivityIntent();
        assertNotNull("No activity is started.", redirect);
        assertEquals(redirect.getComponent().getShortClassName(), ".IndexActivity");
    }
    
/*    @MediumTest
    public void testOnConnected() throws Exception {
        // prepare a mock location for the location service
        rLocationClient.connect();
        Thread.sleep(5000);
        rLocationClient.setMockLocation(mockLocation);
        
        // connect the activity to GooglePlayService
        getActivity().onStart();
        Thread.sleep(5000);
        
        // get the location
        Location myLocation = null; 
        while (myLocation == null)
            myLocation = getActivity().getLocationClient().getLastLocation();
        
        // the location client from the activity is expected to
        // retrieve the location as set for the location service
        assertEquals(mockLocation, myLocation);
        
        rLocationClient.disconnect();
    }*/

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

    @Override
    public void onConnected(Bundle bundle) {
        rLocationClient.setMockMode(true);
    }

    @Override
    public void onDisconnected() {
        rLocationClient.setMockMode(false);
    }
}
