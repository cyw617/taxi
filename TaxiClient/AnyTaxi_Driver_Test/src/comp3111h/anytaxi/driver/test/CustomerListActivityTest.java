package comp3111h.anytaxi.driver.test;

import comp3111h.anytaxi.driver.CustomerListActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CustomerListActivityTest extends ActivityInstrumentationTestCase2<CustomerListActivity>{

    private CustomerListActivity mActivity;
    Button Add, Remove, Clear;
    ListView dynamicList;
    EditText lat, lnt;
    
    private static final double DELTA = 1e-2;
    
	public CustomerListActivityTest(){
		super(CustomerListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		
		mActivity = (CustomerListActivity) getActivity();
		
		dynamicList = (ListView) mActivity.findViewById(comp3111h.anytaxi.driver.R.id.listView1);
	}
	
	@Override
	protected void tearDown() throws Exception{
		//this method is called every time after any test execution
		
		super.tearDown();
	}
	
	@SmallTest
	public void testView(){ // checks if the activity is created
		assertNotNull(getActivity());
	}
	
	
}
