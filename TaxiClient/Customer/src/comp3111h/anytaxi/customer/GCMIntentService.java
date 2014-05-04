package comp3111h.anytaxi.customer;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Customer;
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
		sendNotificationIntent(context, "true".equals(intent.getStringExtra("fail")),
				intent.getStringExtra("transactionKey"));
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

		Customer customer = Utils.getCustomer(context);
		if (customer != null && customer.getDeviceRegistrationID() == null) {
			customer.setDeviceRegistrationID(deviceRegistrationID);
			try {
				endpoint.updateCustomer(customer).execute();
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
				Customer customer = Utils.getCustomer(context);
				customer.setDeviceRegistrationID(null);
				endpoint.updateCustomer(customer).execute();
			} catch (IOException e) {
				Log.e(GCMIntentService.class.getName(),
						"Exception received when attempting to unregister with server at "
								+ endpoint.getRootUrl(), e);
				return;
			}
		}
	}

	private void sendNotificationIntent(Context context, boolean fail,
			String transaction_id) {
		Intent notificationIntent = new Intent(context, TrackingActivity.class);
		notificationIntent.putExtra(FAIL, fail);
		notificationIntent.putExtra(TRANSACTION_ID, transaction_id);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(notificationIntent);
	}
}
