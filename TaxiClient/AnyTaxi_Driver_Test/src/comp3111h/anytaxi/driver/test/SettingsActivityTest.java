package comp3111h.anytaxi.driver.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;

import comp3111h.anytaxi.driver.SettingsActivity;
import comp3111h.anytaxi.driver.R;

public class SettingsActivityTest extends ActivityUnitTestCase<SettingsActivity> {
	
	private Context targetContext;
	
	public SettingsActivityTest() {		
		super(SettingsActivity.class);
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
		        SettingsActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@MediumTest
	public void testNoError() {
		assertTrue("Error!", true);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}
