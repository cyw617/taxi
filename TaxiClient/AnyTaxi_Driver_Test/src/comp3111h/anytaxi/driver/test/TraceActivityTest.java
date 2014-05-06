package comp3111h.anytaxi.driver.test;

import comp3111h.anytaxi.driver.R;
import comp3111h.anytaxi.driver.TraceActivity;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;

public class TraceActivityTest extends ActivityUnitTestCase<TraceActivity>{
	
	private Context targetContext;
    
	public TraceActivityTest(){
		super(TraceActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();
		
		setActivityContext(new ContextThemeWrapper(targetContext, R.style.AppTheme));
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				TraceActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
	
	@SmallTest
	public void testCustomerListActivityStart(){ // checks if the activity is created
		
		assertNotNull(getActivity());
	}
	
	
}
