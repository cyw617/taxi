package comp3111h.anytaxi.customer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity implements
		Register_AgreementFragment.OnAgreementCheckedListener {

	public final static String TAG = "RegisterActivity";

	private boolean agreement_isChecked = false;

	private Register_AgreementFragment agreementFragment = null;
	private Register_FormFragment formFragment = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// Check that the activity is using the layout version with
		// the register_fragment_container FrameLayout
		if (findViewById(R.id.register_fragment_container) != null) {

			// If the activity is restored from a previous state,
			// then nothing should be done or else
			// there will be overlapping fragments.
			if (savedInstanceState == null) {
				agreementFragment = new Register_AgreementFragment();

				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.register_fragment_container,
								agreementFragment).commit();
				getSupportFragmentManager().executePendingTransactions();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.i(TAG, "Registration process begins.");

		Toast.makeText(this, "Please sign up to enjoy AnyTaxi :)",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAgreementChecked(boolean isChecked) {
		Log.i(TAG, "Agreement is " + ((isChecked) ? "checked" : "unchecked")
				+ ".");

		agreement_isChecked = isChecked;

		if (agreement_isChecked) {
			if (formFragment == null)
				formFragment = new Register_FormFragment();

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.register_fragment_container, formFragment)
					.addToBackStack(null).commit();
			getSupportFragmentManager().executePendingTransactions();
		}
	}
}
