package comp3111h.anytaxi.customer.test;

import comp3111h.anytaxi.customer.DeleteOrder;
import comp3111h.anytaxi.customer.R;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;

import comp3111h.anytaxi.customer.CustomerAccountInfo;





public class CustomerAccountInfoTest 
{
	private Context targetContext;
	
	public CustomerAccountInfoTest() {		
		//super(CustomerAccountInfo.class);
	}
	
	
	@Override
	protected void setUp() throws Exception {
		;
	}
	
	@MediumTest
	public void testsubmitButton() {
//		deleteButton = (Button) getActivity().findViewById(R.id.delete);
//		assertNotNull("Button not allowed to be null", deleteButton);
//		deleteButton.performClick();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		setActivity(null);
	}
}

