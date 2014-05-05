package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;

import comp3111h.anytaxi.customer.MainActivity;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.Utils;

/**
 * @author Yi.Bairen
 *
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
	
	private Context targetContext;
	
	public MainActivityTest() {		
		super(MainActivity.class);
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
		        MainActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@MediumTest
	public void testLoginActivityStart() {
		
	    Utils.updateCustomer(targetContext, null);
	    assertNull("customer wasn't null", Utils.getCustomer(getActivity()));
	    
	    getActivity().onStart();
	    
	    final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals(launchIntent.getComponent().getShortClassName(), ".LoginActivity");
	}
	
	@MediumTest
	public void testRequestActivityStart() throws InterruptedException {
		
	    Utils.updateCustomer(targetContext, TestUtils.getCustomer());
	    assertNotNull("customer was null", Utils.getCustomer(getActivity()));
	    
	    getActivity().onResume();
	    assertTrue("Unable to connect Internet", Utils.isOnline(getActivity()));
	    
	    // waiting for network response
	    Thread.sleep(5000);
	    
	    final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals(launchIntent.getComponent().getShortClassName(), ".RequestActivity");
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}
