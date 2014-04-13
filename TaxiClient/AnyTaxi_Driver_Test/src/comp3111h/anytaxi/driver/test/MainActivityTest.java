package comp3111h.anytaxi.driver.test;

import comp3111h.anytaxi.driver.LoginActivity;
import comp3111h.anytaxi.driver.MainActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
	
	private MainActivity mActivity;
	private static final double DELTA = 1e-2;
	
	public MainActivityTest(){
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		mActivity = (MainActivity) getActivity();
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
