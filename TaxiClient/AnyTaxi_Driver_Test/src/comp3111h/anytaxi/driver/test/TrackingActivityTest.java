package comp3111h.anytaxi.driver.test;

import comp3111h.anytaxi.driver.R;
import comp3111h.anytaxi.driver.TrackingActivity;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;

public class TrackingActivityTest extends ActivityUnitTestCase<TrackingActivity>{
	
	private Context targetContext;
    
	public TrackingActivityTest(){
		super(TrackingActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();
		
		setActivityContext(new ContextThemeWrapper(targetContext, R.style.AppTheme));
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				TrackingActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@Override
	protected void tearDown() throws Exception{
		//this method is called every time after any test execution
		super.tearDown();
	}
	
	@SmallTest
	public void testCustomerListActivityStart(){ // checks if the activity is created
		
		assertNotNull(getActivity());
	}
	
	
}
