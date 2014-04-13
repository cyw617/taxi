package comp3111h.anytaxi.customer.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import comp3111h.anytaxi.customer.LoginActivity;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{
	
	private LoginActivity mActivity;
	private static final double DELTA = 1e-2;
	
	public LoginActivityTest(){
		super(LoginActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		mActivity = (LoginActivity) getActivity();
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
