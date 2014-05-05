package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.CreateActivity;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.Utils;

public class CreateActivityTest extends ActivityUnitTestCase<CreateActivity> {
	
	private Context targetContext;
	private Button submitButton;
	
	public CreateActivityTest() {		
		super(CreateActivity.class);
	}

	@MediumTest
	public void testsubmitButton() {
		submitButton = (Button) getActivity().findViewById(R.id.submitbutton);
		assertNotNull("Button not allowed to be null", submitButton);
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
		        CreateActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}

