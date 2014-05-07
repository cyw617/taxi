package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;

import com.appspot.hk_taxi.anyTaxi.model.Transaction;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RequestToTrackingActivity;
import comp3111h.anytaxi.customer.Utils;

public class RequestToTrackingActivityTest 
	extends ActivityUnitTestCase<RequestToTrackingActivity> {

	private Context targetContext;

	public RequestToTrackingActivityTest() {
		super(RequestToTrackingActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();
		
		// set theme for AppCompat support
		ContextThemeWrapper context =
				new ContextThemeWrapper(targetContext, R.style.AppTheme);
		setActivityContext(context);
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				RequestToTrackingActivity.class);
		
		intent.putExtra("LAT", 22);
		intent.putExtra("LON", 107);
		
		intent.putExtra("EMAIL", "yibairen1994@gmail.com");
		intent.putExtra("CURADD", "HKUST");
		intent.putExtra("DEST", "HKU");
		
	    Utils.updateCustomer(targetContext, TestUtils.getCustomer());
		
		this.startActivity(intent, null, null);
	}

	@MediumTest
	public void test() throws InterruptedException {
		Thread.sleep(10000);
		getActivity().returnedTrans = new Transaction();
		getActivity().returnedTrans.setDriverEmail("kim.sung.iloveyou@gmail.com");
		Thread.sleep(10000);
		
	    final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals(launchIntent.getComponent().getShortClassName(), ".TrackingActivity");		
	}

}
