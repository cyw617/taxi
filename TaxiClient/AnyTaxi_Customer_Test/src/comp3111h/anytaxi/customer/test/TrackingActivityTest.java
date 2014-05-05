package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import comp3111h.anytaxi.customer.TrackingActivity;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.Utils;

public class TrackingActivityTest extends ActivityUnitTestCase<TrackingActivity> {
	
	private Context targetContext;
	
	public TrackingActivityTest() {		
		super(TrackingActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();
		
		// set theme for AppCompat support
		ContextThemeWrapper context = 
				new ContextThemeWrapper(targetContext, R.style.AppTheme);
		setActivityContext(context);
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				TrackingActivity.class);
		Bundle driverInfo = new Bundle();
		driverInfo.putString("Email", "ywangbc@gmail.com");
		intent.putExtras(driverInfo);
		
		this.startActivity(intent, null, null);
	}
	
	@MediumTest
	public void testTrackingActivityStart() throws Exception{
		Thread.sleep(5000);
		assertNotNull(getActivity());
	}
}
