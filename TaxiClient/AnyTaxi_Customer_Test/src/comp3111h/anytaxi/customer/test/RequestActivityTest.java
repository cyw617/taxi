package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RegisterActivity;
import comp3111h.anytaxi.customer.RequestActivity;

public class RequestActivityTest extends ActivityUnitTestCase<RequestActivity>
{

	private Context rContext;

	Button requestButton;

	Button moreButton;

	public RequestActivityTest(Class<RequestActivity> activityClass)
	{
		super(activityClass);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		rContext = getInstrumentation().getTargetContext();

		// set theme for AppCompat support
		setActivityContext(new ContextThemeWrapper(rContext, R.style.AppTheme));

		// onCreate() will be called for the activity
		Intent intent = new Intent(rContext, RegisterActivity.class);
		startActivity(intent, null, null);
	}

	@MediumTest
	public void testsubmitButton()
	{
		requestButton = (Button) getActivity().findViewById(R.id.request_btn);
		requestButton.performClick();
		moreButton = (Button) getActivity().findViewById(R.id.more);
		moreButton.performClick();

		assertNotNull("Button not allowed to be null", moreButton);
		assertNotNull("Button not allowed to be null", requestButton);
	}

}
