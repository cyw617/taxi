package comp3111h.anytaxi.customer;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Customer;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;

public class MainActivity extends ActionBarActivity {

	public static final String TAG = "MainActivity";

	protected static GoogleAccountCredential credential;
	protected static AnyTaxi endpoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// getSupportActionBar().hide();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		credential = GoogleAccountCredential
				.usingAudience(this, Utils.AUDIENCE);
		Utils.customer = Utils.getCustomer(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (Utils.customer == null) {
			Log.i(TAG,
					"No user information in sharedPreference: redirect to LoginActivity.");

			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!Utils.isOnline(this)) {
			Toast.makeText(this, "Unable to connect to Internet.",
					Toast.LENGTH_LONG).show();
		} else if (Utils.customer != null) {
			new CheckLoginTask(this).execute();
		}
	}

	// start Activity from AsnycTask will make test fail
	// So move it here
	public void startActivityViaIntent(Intent intent) {
		startActivity(intent);
	}

	private class CheckLoginTask extends AsyncTask<Void, Void, Customer> {

		Context context;
		Exception exceptionThrown;

		public CheckLoginTask(Context context) {
			this.context = context.getApplicationContext();
		}

		@Override
		protected Customer doInBackground(Void... params) {
			credential.setSelectedAccountName(Utils.customer.getEmail());
			endpoint = CloudEndpointUtils.updateBuilder(
					new AnyTaxi.Builder(AndroidHttp.newCompatibleTransport(),
							new JacksonFactory(), null)).build();
			try {
				Customer result = endpoint.getCustomer(
						Utils.customer.getEmail()).execute();
				return result;
			} catch (IOException e) {
				exceptionThrown = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Customer result) {
			super.onPostExecute(result);
			if (exceptionThrown != null) {
				CloudEndpointUtils.logAndShow(MainActivity.this, TAG,
						exceptionThrown);
				return;
			}
			if (result == null) {
				Log.i(TAG,
						"No user information in database: redirect to LoginActivity.");

				// redirect user to login
				Intent intent = new Intent(this.context, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivityViaIntent(intent);
			} else {
				Log.i(TAG, "User has logged in successfully.");

				Toast.makeText(context, "Welcome, " + result.getName(),
						Toast.LENGTH_LONG).show();

				// save user information into sharedPreference
				Utils.updateCustomer(context, result);

				// redirect user to call taxi
				Intent intent = new Intent(this.context, RequestActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivityViaIntent(intent);
			}
		}

	}
}
