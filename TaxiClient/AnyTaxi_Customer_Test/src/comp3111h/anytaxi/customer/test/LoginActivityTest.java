package comp3111h.anytaxi.customer.test;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.CheckBox;
import comp3111h.anytaxi.customer.LoginActivity;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{
    
    private LoginActivity mActivity;

    public LoginActivityTest(){
        super(LoginActivity.class);
    }
    
    @Override
    protected void setUp() throws Exception{
        //this method is called every time before any test execution
        super.setUp();
        mActivity = (LoginActivity) getActivity();
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
    
    /*
    @SmallTest
    public void testOnClick_In() {
        TouchUtils.tapView(this, agreement_Chkbox);
        TouchUtils.clickView(this, login_Btn_In);
        
        try {
            Thread.sleep(3000);
        }
        catch (Exception e) {}
        
        assertTrue( googleApiClient.isConnected() );
    }
    
    @SmallTest
    public void testOnClick_Out() {
        TouchUtils.clickView(this, login_Btn_Out);
        
        assertFalse( googleApiClient.isConnected() );
    }*/
}
