package comp3111h.anytaxi.driver.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.driver.ShareTaxiDriver;
import comp3111h.anytaxi.driver.R;

public class ShareTaxiDriverTest extends ActivityUnitTestCase<ShareTaxiDriver>
{

	private Context targetContext;

	private Button refreshButton;
	private Button showlist;

	public ShareTaxiDriverTest()
	{
		super(ShareTaxiDriver.class);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();

		// set theme for AppCompat support
		ContextThemeWrapper context = new ContextThemeWrapper(targetContext,
				R.style.AppTheme);
		setActivityContext(context);

		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				ShareTaxiDriver.class);

		this.startActivity(intent, null, null);
	}

	@MediumTest
	public void testsubmitButton()
	{

		refreshButton = (Button) getActivity().findViewById(R.id.refresh);
		showlist = (Button) getActivity().findViewById(R.id.showlist);
		getActivity().new joinTask().execute();
		assertNotNull("Button not allowed to be null", refreshButton);
		assertNotNull("Button not allowed to be null", showlist);
		showlist.performClick();
		refreshButton.performClick();

		// showlist.performClick();
	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		setActivity(null);
	}
}
