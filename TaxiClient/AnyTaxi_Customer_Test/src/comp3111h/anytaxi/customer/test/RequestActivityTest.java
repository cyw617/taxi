package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RequestActivity;
import comp3111h.anytaxi.customer.Utils;

public class RequestActivityTest extends ActivityUnitTestCase<RequestActivity> {

	private Context rContext;

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

		mockLocation = new Location(PROVIDER);
		mockLocation.setLatitude(LAT);
		mockLocation.setLongitude(LNG);
		mockLocation.setAccuracy(ACCURACY);

		// set theme for AppCompat support
		setActivityContext(new ContextThemeWrapper(rContext, R.style.AppTheme));

		// onCreate() will be called for the activity
		Intent intent = new Intent(rContext, RequestActivity.class);
		startActivity(intent, null, null);
	}

	@MediumTest
	public void testOnRequestListener() throws Exception {
	    Utils.updateCustomer(rContext, TestUtils.getCustomer());
		getActivity().curLocGlobal = mockLocation;

		Button requestButton = (Button) getActivity().findViewById(R.id.request_btn);

		assertNotNull("Request button is not found.", requestButton);

		requestButton.performClick();

		final Intent redirect = getStartedActivityIntent();
		assertNotNull("No activity is started.", redirect);
		assertEquals(redirect.getComponent().getShortClassName(), ".RequestToTrackingActivity");
	}
	
	@MediumTest
	public void testOnRequestListenerWithoutCustomer() throws Exception {
	    Utils.updateCustomer(rContext, null);
		getActivity().curLocGlobal = mockLocation;

		Button requestButton = (Button) getActivity().findViewById(R.id.request_btn);

		assertNotNull("Request button is not found.", requestButton);

		requestButton.performClick();

		final Intent redirect = getStartedActivityIntent();
		assertNull("No activity is started.", redirect);
	}
	
	@MediumTest
	public void testOnRequestListenerWithoutLocation() throws Exception {
	    Utils.updateCustomer(rContext, TestUtils.getCustomer());
		getActivity().curLocGlobal = null;

		Button requestButton = (Button) getActivity().findViewById(R.id.request_btn);

		assertNotNull("Request button is not found.", requestButton);

		requestButton.performClick();

		final Intent redirect = getStartedActivityIntent();
		assertNull("No activity is started.", redirect);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}
