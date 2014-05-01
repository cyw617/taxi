package comp3111h.anytaxi.customer.test;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import comp3111h.anytaxi.customer.R;
import comp3111h.anytaxi.customer.RegisterActivity;
import comp3111h.anytaxi.customer.Register_AgreementFragment;
import comp3111h.anytaxi.customer.Register_FormFragment;
import comp3111h.anytaxi.customer.Utils;

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
        Fragment currentFragment;
        
        // ensure the current fragment is the Agreement
        currentFragment = rFragmentManager.findFragmentById(R.id.register_fragment_container);
        
        displayFragment(currentFragment, new Register_AgreementFragment());
        
        // simulate a user checking the agreement
        ((CheckBox) getActivity().findViewById(R.id.register_Agreement_Chkbox))
                .performClick();
        
        // the agreement should be replaced by a registration form
        currentFragment = rFragmentManager.findFragmentById(R.id.register_fragment_container);
        
        assertTrue("Failed to display the registration form.", currentFragment instanceof Register_FormFragment);
    }
    
    /**
     * This test simulates a user submitting an empty form
     * during registration.
     */
    @MediumTest
    public void testAttemptRegister_Fail() {
        Fragment currentFragment;
        
        // create a fake email in the sharedPreference, which
        // should be created in LoginActivity
        Utils.putPreference(getActivity(), Utils.PREFS_ACCOUNT_KEY, randomString(10) + "@anytaxi.hk");
        
        // create a new registration form
        currentFragment = rFragmentManager.findFragmentById(R.id.register_fragment_container);
        
        displayFragment(currentFragment, new Register_FormFragment(), true);
        
        EditText rField_FirstName = (EditText) getActivity().findViewById(R.id.register_first_name);
        EditText rField_LastName = (EditText) getActivity().findViewById(R.id.register_last_name);
        EditText rField_Phone = (EditText) getActivity().findViewById(R.id.register_phone);
        
        Button rConfirmBtn = (Button) getActivity().findViewById(R.id.register_confirm_btn);
        
        // simulate a user submitting the form with empty inputs
        rConfirmBtn.performClick();
        
        // errors should have been set for all fields
        assertNotNull("No error is shown in First Name.", rField_FirstName.getError());
        assertNotNull("No error is shown in Last Name.", rField_LastName.getError());
        assertNotNull("No error is shown in Phone.", rField_Phone.getError());
        
        // no other activity will start
        assertNull("A unintended activity has been started.", getStartedActivityIntent());
    }
    
    /**
     * This test simulates a user submitting a valid form
     * during registration.
     */
    @MediumTest
    public void testAttemptRegister_Succeed() throws Exception {
        Fragment currentFragment;
        
        // create a fake email in the sharedPreference, which
        // should be created in LoginActivity
        Utils.putPreference(getActivity(), Utils.PREFS_ACCOUNT_KEY, randomString(10) + "@anytaxi.hk");
        
        // create a new registration form
        currentFragment = rFragmentManager.findFragmentById(R.id.register_fragment_container);
        
        displayFragment(currentFragment, new Register_FormFragment(), true);
        
        EditText rField_FirstName = (EditText) getActivity().findViewById(R.id.register_first_name);
        EditText rField_LastName = (EditText) getActivity().findViewById(R.id.register_last_name);
        EditText rField_Phone = (EditText) getActivity().findViewById(R.id.register_phone);
        
        Button rConfirmBtn = (Button) getActivity().findViewById(R.id.register_confirm_btn);
        
        // create valid inputs for the fields
        rField_FirstName.setText("Valid");
        rField_LastName.setText("Test");
        rField_Phone.setText("69999999");
        
        // simulate a user submitting the form with empty inputs
        rConfirmBtn.performClick();
        
        // errors should have been set for all fields
        assertNull("Unexpected error is shown in First Name.", rField_FirstName.getError());
        assertNull("Unexpected error is shown in Last Name.", rField_LastName.getError());
        assertNull("Unexpected error is shown in Phone.", rField_Phone.getError());
        
        // ensure to have Internet connection
        assertTrue("No internet connection.", Utils.isOnline(getActivity()));
        Thread.sleep(5000);
        
        // RequestActivity should be started
        final Intent redirect = getStartedActivityIntent();
        assertNotNull("No activity is started.", redirect);
        assertEquals(redirect.getComponent().getShortClassName(), ".RequestActivity");
    }
    
    private void displayFragment(Fragment currentFragment, Fragment targetFragment) {
        displayFragment(currentFragment, targetFragment, false);
    }
    
    /**
     * This method determines whether the current displaying fragment is
     * the target expected. If not, the current fragment will be
     * replaced by the target fragment.
     * 
     * @param currentFragment   the displaying fragment of the activity
     * @param targetFragment    the target fragment to be displayed
     * @param isNewFragment     state whether a new fragment should be displayed
     */
    private void displayFragment(Fragment currentFragment, Fragment targetFragment, boolean isNewFragment) {
        if (currentFragment == null) {
            rFragmentManager.beginTransaction()
                    .add(R.id.register_fragment_container, targetFragment)
                    .commit();
            rFragmentManager.executePendingTransactions();
        }
        else if (!(currentFragment.getClass().equals(targetFragment.getClass()))
                || isNewFragment) {
            rFragmentManager.beginTransaction()
                    .replace(R.id.register_fragment_container, targetFragment)
                    .commit();
            rFragmentManager.executePendingTransactions();
        }
    }
    
    /**
     * This method generates a random string.
     * 
     * @param length    the length of the output string
     * @return          a random string with given length
     */
    private String randomString(int length) {
        final char[] source = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        
        StringBuilder output = new StringBuilder();
        
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            char c = source[random.nextInt(source.length)];
            output.append(c);
        }
        
        return output.toString();
    }
}
