package comp3111h.anytaxi.customer.test;

import com.appspot.hk_taxi.anyTaxi.model.Driver;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.TrackingActivity;
import comp3111h.anytaxi.customer.Utils;

public class TrackingActivityTest extends ActivityUnitTestCase<TrackingActivity> {

	private Context targetContext;
	private Driver driver;

	public TrackingActivityTest() {
		super(TrackingActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();
	    Utils.updateCustomer(targetContext, TestUtils.getCustomer());

		// set theme for AppCompat support
		ContextThemeWrapper context = 
				new ContextThemeWrapper(targetContext, R.style.AppTheme);
		setActivityContext(context);
		driver = new Driver();
		driver.setEmail("kim.sung.iloveyou@gmail.com");
		driver.setLicense("HKUST");
	}

	@MediumTest
	public void test() throws InterruptedException {		
		final Intent intent = new Intent(getInstrumentation().getTargetContext(),
				TrackingActivity.class);
		intent.putExtra("Email", "kim.sung.iloveyou@gmail.com");
		startActivity(intent, null, null);
		Thread.sleep(15000);
		assertEquals("Driver not equal", driver.getEmail(), getActivity().myDriver.getEmail());
	}

}
