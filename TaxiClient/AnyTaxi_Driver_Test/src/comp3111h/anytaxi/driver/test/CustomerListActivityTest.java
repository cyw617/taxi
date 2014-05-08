package comp3111h.anytaxi.driver.test;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import comp3111h.anytaxi.driver.CustomerListActivity;
import comp3111h.anytaxi.driver.GCMIntentService;
import comp3111h.anytaxi.driver.R;

public class CustomerListActivityTest extends ActivityUnitTestCase<CustomerListActivity>{
	
	private Context targetContext;
    
	public CustomerListActivityTest(){
		super(CustomerListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		targetContext = getInstrumentation().getTargetContext();
		
		setActivityContext(new ContextThemeWrapper(targetContext, R.style.AppTheme));
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(),
				CustomerListActivity.class);
		
		this.startActivity(intent, null, null);
	}
	
	@SmallTest
	public void testCustomerListActivityStart(){ // checks if the activity is created
		
		assertNotNull(getActivity());
	}
	
	@MediumTest
	public void testListEmpty(){ // checks if clear list works
		getActivity().clearItems();
		assertEquals("List should be clean after clear",getActivity().numItemsInStrArr(), 0);
	}
	
	@MediumTest
	public void testRemoveItemInList(){ // checks if removing items in list works
		int size = getActivity().numItemsInStrArr();
		for(int i = 0; i<size ; i++){
			getActivity().removeItem();
		}
		assertEquals("List shoud be clean after removing items", getActivity().numItemsInStrArr(), 0);
	}
	
	@MediumTest
	public void testRemoveItemInListUsingIndex(){ // checks if removing items in list using works
		int size = getActivity().numItemsInStrArr();
		for(int i = 0; i<size ; i++){
			CustomerListActivity.removeItemInList(0);
		}
		assertEquals("List shoud be clean after removing items", getActivity().numItemsInStrArr(), 0);
	}
	
	@MediumTest
	public void testTouchOnItem(){

		Intent intent = new Intent();
		intent.putExtra(GCMIntentService.GCM_INTENT, "GCM");
		intent.putExtra(GCMIntentService.TRANSACTION_ID, "1");
		intent.putExtra(GCMIntentService.LATITUDE, "1");
		intent.putExtra(GCMIntentService.LONGITUDE, "1");
		getActivity().onNewIntent(intent);
		getActivity();
		getActivity().listView.performItemClick(CustomerListActivity.arrAdapter.getView(0, null, null),
				0, CustomerListActivity.arrAdapter.getItemId(0));

	    final Intent launchIntent = this.getStartedActivityIntent();
	    assertNotNull("Intent was null", launchIntent);
	    assertEquals(launchIntent.getComponent().getShortClassName(), ".TraceActivity");
	}
}
