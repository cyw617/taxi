package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.IndexActivity;
import comp3111h.anytaxi.customer.R;

public class IndexActivityTest extends ActivityUnitTestCase<IndexActivity>
{

	private Context targetContext;
	private Button callButton;
	private Button createButton;
	private Button refreshButton;
	private Button showlist;

	public IndexActivityTest()
	{
		super(IndexActivity.class);
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
				IndexActivity.class);

		this.startActivity(intent, null, null);
	}

	@MediumTest
	public void testCreateButton()
	{
		
		createButton = (Button) getActivity().findViewById(R.id.create);
		assertNotNull("Button not allowed to be null", createButton);
		createButton.performClick();
		final Intent launchIntent = this.getStartedActivityIntent();
		assertEquals(launchIntent.getComponent().getShortClassName(),
				".CreateActivity");
		assertEquals(createButton.getText(),"create");
	}

	@MediumTest
	public void testRefreshButton()
	{
		refreshButton = (Button) getActivity().findViewById(R.id.refresh);
		assertNotNull("Button not allowed to be null", refreshButton);
		refreshButton.performClick();
		assertEquals(refreshButton.getText(),"refresh");
	}

	@MediumTest
	public void testCallButton()
	{
		callButton = (Button) getActivity().findViewById(R.id.callButton);
		assertNotNull("Button not allowed to be null", callButton);
		callButton.performClick();
		assertEquals(callButton.getText(),"call");
	}

	@MediumTest
	public void testShowlist()
	{
		showlist = (Button) getActivity().findViewById(R.id.showlist);
		assertNotNull("Button not allowed to be null", showlist);
		showlist.performClick();
		getActivity().new joinTask().execute();
		assertEquals(true,getActivity().ifJoinTask());

	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		setActivity(null);
	}
}
