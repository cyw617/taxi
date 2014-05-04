package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RequestActivity;

public class RequestActivityTest extends ActivityUnitTestCase<RequestActivity>{

    private Context rContext; 
    
    public RequestActivityTest() {
        super(RequestActivity.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rContext = getInstrumentation().getTargetContext();
        
        // set theme for AppCompat support
        setActivityContext(new ContextThemeWrapper(rContext, R.style.AppTheme));
        
        // onCreate() will be called for the activity
        Intent intent = new Intent(rContext, RequestActivity.class);
        startActivity(intent, null, null);
    }
    
    @SmallTest
    public void testOnRequestListener() {
        getActivity().onStart();
        
        Button requestButton = (Button) getActivity().findViewById(R.id.request_btn);
        
        assertNotNull("Request button is not found.", requestButton);
        
        requestButton.performClick();
        
        final Intent redirect = getStartedActivityIntent();
        assertNotNull("No activity is started.", redirect);
        assertEquals(redirect.getComponent().getShortClassName(), ".TrackingActivity");
    }
}
