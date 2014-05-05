package comp3111h.anytaxi.driver.test;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import comp3111h.anytaxi.driver.LoginActivity;
import comp3111h.anytaxi.driver.R;
import comp3111h.anytaxi.driver.Utils;

public class LoginActivityTest extends ActivityUnitTestCase<LoginActivity> {
	
	private Context targetContext;
	private Intent onActivityResultIntent;
	
	public LoginActivityTest() {
		super(LoginActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();
		
		// set theme for AppCompat support
		ContextThemeWrapper context = 
				new ContextThemeWrapper(targetContext, R.style.AppTheme);
		setActivityContext(context);
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				LoginActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@MediumTest
	public void testAccountPicker() {
		this.getActivity().onStart();
		assertEquals("wrong request for start account picker activity",
				this.getStartedActivityRequest(), 
				Utils.REQUEST_ACCOUNT_PICKER);
	    
	    final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals("intent was passed with wrong action",
	    		launchIntent.getAction(),
	    		"com.google.android.gms.common.account.CHOOSE_ACCOUNT");
	}
	
	@MediumTest
	public void testOnAcitivityResult() throws InterruptedException {
		onActivityResultIntent = new Intent();
		onActivityResultIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME,
				TestUtils.getDriver().getEmail());
		
		this.getActivity().onActivityResult(Utils.REQUEST_ACCOUNT_PICKER,
				Activity.RESULT_OK, onActivityResultIntent);
		assertEquals("account name mismatch",
				Utils.getPreference(getActivity(), Utils.PREFS_ACCOUNT_KEY, null),
				TestUtils.getDriver().getEmail());

	    assertTrue("Unable to connect Internet", Utils.isOnline(getActivity()));
		Thread.sleep(5000);
	    
	    final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals(launchIntent.getComponent().getShortClassName(), ".RequestActivity");		
	}
	
	@MediumTest
	public void testRegisterActivityStart() throws InterruptedException {		
		this.getActivity().onActivityResult(Utils.REQUEST_ACCOUNT_PICKER,
				Activity.RESULT_OK, onActivityResultIntent);
		assertNull("account name mismatch",
				Utils.getPreference(getActivity(), Utils.PREFS_ACCOUNT_KEY, null));

	    assertTrue("Unable to connect Internet", Utils.isOnline(getActivity()));
		Thread.sleep(5000);
	    
	    final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals(launchIntent.getComponent().getShortClassName(), ".RegisterActivity");	
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}
