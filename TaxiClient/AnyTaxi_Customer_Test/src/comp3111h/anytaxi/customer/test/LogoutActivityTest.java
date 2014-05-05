package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;

import comp3111h.anytaxi.customer.LogoutActivity;
import comp3111h.anytaxi.customer.R;

public class LogoutActivityTest extends ActivityUnitTestCase<LogoutActivity> {
private Context targetContext;
	
	public LogoutActivityTest() {		
		super(LogoutActivity.class);
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
		        LogoutActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@MediumTest
	public void testLoginActivityStart() throws InterruptedException {
		getActivity().onStart();
		final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals(launchIntent.getComponent().getShortClassName(), ".LoginActivity");
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}

