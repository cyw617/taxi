package comp3111h.anytaxi.driver.test;

import comp3111h.anytaxi.driver.CustomerListActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.ListView;

public class CustomerListActivityTest extends ActivityInstrumentationTestCase2<CustomerListActivity>{

    private CustomerListActivity mActivity;
    Button Add, Remove, Clear;
    ListView dynamicList;
    
    private static final double DELTA = 1e-2;
    
	public CustomerListActivityTest(){
		super(CustomerListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		//this method is called every time before any test execution
		super.setUp();
		
		mActivity = (CustomerListActivity) getActivity();
		
		Add = (Button) mActivity.findViewById(comp3111h.anytaxi.driver.R.id.button1);
		Remove = (Button) mActivity.findViewById(comp3111h.anytaxi.driver.R.id.button2);
		Clear = (Button) mActivity.findViewById(comp3111h.anytaxi.driver.R.id.button3);
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
	
	@SmallTest
	public void testAddItem(){ // checks if items are added on dynamic list
		TouchUtils.clickView(this, Add);
		int size = mActivity.numItemsInStrArr();
		assertEquals("1 item should be added on list", 1, size, DELTA);
	}
	
	@SmallTest
	public void testRemoveitem(){ //checks if items are removed from list
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Remove);
		int size = mActivity.numItemsInStrArr();
		assertEquals("1 item should be removed from list", 0, size, DELTA);
	}
	
	@SmallTest
	public void testMultipleAddItems(){ // checks if multiple items are added on dynamic list
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		int size = mActivity.numItemsInStrArr();
		assertEquals("10 items should be added on list", 5, size, DELTA);
	}
	
	@SmallTest
	public void testMultipleRemoveItems(){ // checks if multiple items are removed on dynamic list
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Remove);
		TouchUtils.clickView(this, Remove);
		int size = mActivity.numItemsInStrArr();
		assertEquals("4 items should be removed from list", 3, size, DELTA);
	}
	
	@SmallTest
	public void testClearItems(){ // checks if multiple items are added on dynamic list

		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Add);
		TouchUtils.clickView(this, Clear);
		int size = mActivity.numItemsInStrArr();
		assertEquals("6 items should be removed from list", 0, size, DELTA);
	}
	
	
}
