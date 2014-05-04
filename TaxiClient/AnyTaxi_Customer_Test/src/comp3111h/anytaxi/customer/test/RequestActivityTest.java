package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.ContextThemeWrapper;

import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RegisterActivity;
import comp3111h.anytaxi.customer.RequestActivity;

public class RequestActivityTest extends ActivityUnitTestCase<RequestActivity>{

	private Context rContext; 
	
	public RequestActivityTest(Class<RequestActivity> activityClass) {
		super(activityClass);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        rContext = getInstrumentation().getTargetContext();
        
        // set theme for AppCompat support
        setActivityContext(new ContextThemeWrapper(rContext, R.style.AppTheme));
        
        // onCreate() will be called for the activity
        Intent intent = new Intent(rContext, RegisterActivity.class);
        startActivity(intent, null, null);
    }
	
	
}
