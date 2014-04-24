package comp3111h.anytaxi.customer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class RegisterActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.register_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState == null) {
                // Create a new Fragment to be placed in the activity layout
                Register_AgreementFragment agreementFragment = new Register_AgreementFragment();
                
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                agreementFragment.setArguments(getIntent().getExtras());
                
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.register_fragment_container, agreementFragment).commit();
            }
        }
    }
}
