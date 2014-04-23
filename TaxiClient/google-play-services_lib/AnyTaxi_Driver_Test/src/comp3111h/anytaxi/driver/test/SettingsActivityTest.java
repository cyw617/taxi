package comp3111h.anytaxi.driver.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import comp3111h.anytaxi.driver.SettingsActivity;

public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity>{
	
	private SettingsActivity mActivity;
	private static final double DELTA = 1e-2;
	
	public SettingsActivityTest(){
		super(SettingsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		mActivity = (SettingsActivity) getActivity();
	}
	
	@Override
	protected void tearDown() throws Exception{
		//this method is called every time after any test execution
		
		super.tearDown();
	}
	
	@SmallTest
	public void testView(){ // checks if the activity is created
		assertNotNull(getActivity());
	}
}

