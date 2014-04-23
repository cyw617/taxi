package comp3111h.anytaxi.customer.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import comp3111h.anytaxi.customer.RequestActivity;

public class RequestActivityTest extends ActivityInstrumentationTestCase2<RequestActivity>{
	
	private RequestActivity mActivity;
	private static final double DELTA = 1e-2;
	
	public RequestActivityTest(){
		super(RequestActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		mActivity = (RequestActivity) getActivity();
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

