package comp3111h.anytaxi.driver.test;

import android.content.Intent;
import android.test.ServiceTestCase;
import android.test.mock.MockApplication;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import comp3111h.anytaxi.driver.CustomerListActivity;
import comp3111h.anytaxi.driver.GCMIntentService;
import comp3111h.anytaxi.driver.R;
import comp3111h.anytaxi.driver.TraceActivity;


public class GCMIntentServiceTest extends ServiceTestCase<GCMIntentService> {

	private GCMIntentService service;
	private MockApplication application;

	public GCMIntentServiceTest() {
		super(GCMIntentService.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		service = new GCMIntentService();
		ContextThemeWrapper context = 
				new ContextThemeWrapper(getSystemContext(), R.style.AppTheme);
		this.setContext(context);
		application = new MockApplication();
		this.setApplication(application);
	}

	@MediumTest
	public void testRegister() throws InterruptedException
	{
		GCMIntentService.register(getSystemContext());
		GCMIntentService.unregister(getSystemContext());
		service.onError(getSystemContext(), "0");
	}
	
	@MediumTest
	public void testMessageFail() {
		Intent intent = new Intent();
		intent.putExtra("fail", "true");
		service.onMessage(getSystemContext(), intent);
	}
	
	@MediumTest
	public void testMessage() {
		Intent intent = new Intent();
		intent.putExtra("fail", "false");
		service.onMessage(getSystemContext(), intent);
	}
}
