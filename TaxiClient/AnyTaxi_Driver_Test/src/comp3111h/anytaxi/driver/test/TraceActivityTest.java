package comp3111h.anytaxi.driver.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import comp3111h.anytaxi.driver.TraceActivity;

public class TraceActivityTest extends ActivityInstrumentationTestCase2<TraceActivity>{
	
	private TraceActivity mActivity;
	private static final double DELTA = 1e-2;
	
	public TraceActivityTest(){
		super(TraceActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		mActivity = (TraceActivity) getActivity();
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
