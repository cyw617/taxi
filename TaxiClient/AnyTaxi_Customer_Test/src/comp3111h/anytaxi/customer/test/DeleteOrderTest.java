package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;

import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.DeleteOrder;

import comp3111h.anytaxi.customer.DeleteOrder.deleteTask;

// ActivityInstrumentationTestCase2 provides functional testing of a single activity 
public class DeleteOrderTest extends ActivityUnitTestCase<DeleteOrder>
{
	private Context targetContext;

	private DeleteOrder mActivity;

//	private Button refreshButton;
//	private Button showlist;

	public DeleteOrderTest()
	{
		super(DeleteOrder.class);

	}

	@Override
	protected void setUp() throws Exception
	{
		// this method is called every time before any test execution
		super.setUp();

		targetContext = getInstrumentation().getTargetContext();

		// set theme for AppCompat support
		ContextThemeWrapper context = new ContextThemeWrapper(targetContext,
				R.style.AppTheme);
		setActivityContext(context);

		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				DeleteOrder.class);

		this.startActivity(intent, null, null);

		// mActivity = (DeleteOrder) getActivity(); // get current activity
		//
		// // link the objects with the activity objects
		//
		// refreshButton = (Button) mActivity.findViewById(R.id.delete);

		// showlist = (Button) mActivity
		// .findViewById(com.example.driver.R.id.showlist);

	}

	@Override
	protected void tearDown() throws Exception
	{
		// this method is called every time after any test execution
		// we want to clean the texts
		// textKilos.clearComposingText();
		// textPounds.clearComposingText();
		super.tearDown();
	}

	@MediumTest
	// SmallTest: this test doesn't interact with any file system or network.
	public void testView()
	{
		//mActivity.new deleteTask().execute();

	}

}
