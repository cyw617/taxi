package comp3111h.anytaxi.customer.test;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.CheckBox;

import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RegisterActivity;
import comp3111h.anytaxi.customer.Register_AgreementFragment;
import comp3111h.anytaxi.customer.Register_FormFragment;

public class RegisterActivityTest extends ActivityUnitTestCase<RegisterActivity> {
    
    private Context rContext;
    
    private FragmentManager rFragmentManager;

    public RegisterActivityTest() {
        super(RegisterActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rContext = getInstrumentation().getTargetContext();
        
        // set theme for AppCompat support
        setActivityContext(new ContextThemeWrapper(rContext, R.style.AppTheme));
        
        // onCreate() will be called for the activity
        Intent intent = new Intent(rContext, RegisterActivityTest.class);
        startActivity(intent, null, null);
        
        // fetch the fragment manager from the activity
        rFragmentManager = getActivity().getSupportFragmentManager();
        
        // start the activity
        this.getActivity().onStart();
    }
    
    /**
     * This test simulates a user reading and checking the agreement,
     * which is the only logic in Register_AgreementFragment.
     */
    @MediumTest
    public void testOnAgreementChecked() {
        Fragment fragment;
        
        // ensure the current fragment is the Agreement
        fragment = rFragmentManager.findFragmentById(R.id.register_fragment_container);
        
        if (fragment == null) {
            rFragmentManager.beginTransaction()
                    .add(R.id.register_fragment_container, new Register_AgreementFragment())
                    .commit();
            rFragmentManager.executePendingTransactions();
        }
        else if (!(fragment instanceof Register_AgreementFragment)) {
            rFragmentManager.beginTransaction()
                    .replace(R.id.register_fragment_container, new Register_AgreementFragment())
                    .commit();
            rFragmentManager.executePendingTransactions();
        }
        
        // simulate a user checking the agreement
        ((CheckBox) getActivity().findViewById(R.id.register_Agreement_Chkbox))
                .performClick();
        
        // the agreement should be replaced by a registration form
        fragment = rFragmentManager.findFragmentById(R.id.register_fragment_container);
        
        assertTrue("Failed to display the registration form.", fragment instanceof Register_FormFragment);
    }
}
