package comp3111h.anytaxi.driver;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson.JacksonFactory;

public class GCMIntentService extends GCMBaseIntentService {
	private final AnyTaxi endpoint;

	protected static final String PROJECT_NUMBER = "1072316261853";

	final static public String GCM_INTENT = "__GCM_INTENT__";
	final static public String TRANSACTION_ID = "__TRANSACTION_ID__";
	final static public String FAIL = "__FAIL__";
	final static public String CUR_LOC_STR = "__CUR_LOC_STR__";
	final static public String DEST_LOC_STR = "__DEST_LOC_STR__";
	final static public String LATITUDE = "__LATITUDE__";
	final static public String LONGITUDE = "__LONGTITUDE__";

	public GCMIntentService() {
		super(PROJECT_NUMBER);
		AnyTaxi.Builder endpointBuilder = new AnyTaxi.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				null);
		endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
	}

	/**
	 * Register the device for GCM.
	 * 
	 * @param mContext
	 *            the activity's context.
	 */
	public static void register(Context mContext) {
		GCMRegistrar.checkDevice(mContext);
		GCMRegistrar.checkManifest(mContext);
		GCMRegistrar.register(mContext, PROJECT_NUMBER);
	}

	/**
	 * Unregister the device from the GCM service.
	 * 
	 * @param mContext
	 *            the activity's context.
	 */
	public static void unregister(Context mContext) {
		GCMRegistrar.unregister(mContext);
	}

	/**
	 * Called on registration error. This is called in the context of a Service
	 * - no dialog or UI.
	 * 
	 * @param context
	 *            the Context
	 * @param errorId
	 *            an error message
	 */
	@Override
	public void onError(Context context, String errorId) {

		Log.e(GCMIntentService.class.getName(),
				"Registration with Google Cloud Messaging...FAILED!\n\n"
						+ "A Google Cloud Messaging registration error occurred (errorid: "
						+ errorId
						+ "). ");
	}

	/**
	 * Called when a cloud message has been received.
	 */
	@Override
	public void onMessage(Context context, Intent intent) {
		boolean fail = "true".equals(intent.getStringExtra("fail"));
		if (fail) {
			Intent notification = new Intent(context, TraceActivity.class);
			notification.putExtra(FAIL, fail);
			notification.putExtra(TRANSACTION_ID, intent.getStringExtra("transactionKey"));
			notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(notification);
		} else {
			Intent notification = new Intent(context, CustomerListActivity.class);
			notification.putExtra(TRANSACTION_ID, intent.getStringExtra("transactionKey"));
			notification.putExtra(CUR_LOC_STR, intent.getStringExtra("curLocStr"));
			notification.putExtra(DEST_LOC_STR, intent.getStringExtra("destLocStr"));
			
			notification.putExtra(GCM_INTENT, GCM_INTENT);

			notification.putExtra(LATITUDE, intent.getStringExtra("lat"));
			notification.putExtra(LONGITUDE, intent.getStringExtra("long"));

			notification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(notification);
		}
	}

	/**
	 * Called back when a registration token has been received from the Google
	 * Cloud Messaging service.
	 * 
	 * @param context
	 *            the Context
	 */
	@Override
	public void onRegistered(Context context, String deviceRegistrationID) {

		Driver driver = Utils.getDriver(context);
		if (driver != null && driver.getDeviceRegistrationID() == null) {
			driver.setDeviceRegistrationID(deviceRegistrationID);
			try {
				Driver d = endpoint.getDriver(Utils.driver.getEmail()).execute();
				d.setDeviceRegistrationID(deviceRegistrationID);
				endpoint.updateDriver(d).execute();
			} catch (IOException e) {
				Log.e(GCMIntentService.class.getName(),
						"Exception received when attempting to register with server at "
								+ endpoint.getRootUrl(), e);
			}
		}
	}

	/**
	 * Called back when the Google Cloud Messaging service has unregistered the
	 * device.
	 * 
	 * @param context
	 *            the Context
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {

		if (registrationId != null && registrationId.length() > 0) {
			try {
				Driver driver = Utils.getDriver(context);
				driver.setDeviceRegistrationID(registrationId);
				endpoint.updateDriver(driver).execute();
			} catch (IOException e) {
				Log.e(GCMIntentService.class.getName(),
						"Exception received when attempting to unregister with server at "
								+ endpoint.getRootUrl(), e);
				return;
			}
		}
	}
}
