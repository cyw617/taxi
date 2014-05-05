package comp3111h.anytaxi.customer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class LogoutActivity extends ActionBarActivity {
	private final static String TAG = "LogoutActivity";

	@Override
	public void onStart() {
		super.onStart();

		Log.i(TAG, "User is logging out.");

		clearCache();

		// redirect the user to login
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * This methods clear all the user information in the shared preference.
	 */
	private void clearCache() {
		Utils.putPreference(this, Utils.PREFS_ACCOUNT_KEY, null);
		Utils.putPreference(this, Utils.PREFS_NAME_KEY, null);
		Utils.putPreference(this, Utils.PREFS_TEL_KEY, null);
		Utils.putPreference(this, Utils.PREFS_REGDATE_KEY, null);
		Utils.putPreference(this, Utils.PREFS_LOC_KEY, null);
	}
}
