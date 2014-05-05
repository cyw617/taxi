package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.DeleteOrder;
import comp3111h.anytaxi.customer.R;

public class DeleteOrderTest extends ActivityUnitTestCase<DeleteOrder>{
	
	private Context targetContext;
	private Button deleteButton;
	
	public DeleteOrderTest() {		
		super(DeleteOrder.class);
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
		        DeleteOrder.class);
		
		this.startActivity(intent, null, null);
	}
	
	@MediumTest
	public void testsubmitButton() {
		deleteButton = (Button) getActivity().findViewById(R.id.delete);
		assertNotNull("Button not allowed to be null", deleteButton);
		deleteButton.performClick();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}
