package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.MainActivity;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.DeleteOrder;
import comp3111h.anytaxi.customer.Utils;
import comp3111h.anytaxi.customer.DeleteOrder.deleteTask;

// ActivityInstrumentationTestCase2 provides functional testing of a single activity 
public class DeleteOrderTest extends ActivityUnitTestCase<DeleteOrder>
{
	private Context targetContext;

	private DeleteOrder mActivity;

	private Button refreshButton;
	private Button showlist;

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

		mActivity.new deleteTask().execute();

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

	// @SmallTest
	// // SmallTest: this test doesn't interact with any file system or network.
	// public void testView()
	// { // checks if the activity is created
	// TouchUtils.clickView(this, refreshButton);
	// // TouchUtils.clickView(this, showlist);
	// // mActivity.new joinTask().execute();
	//
	// }

}
