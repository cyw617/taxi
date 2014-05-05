package comp3111h.anytaxi.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.appspot.hk_taxi.anyTaxi.model.Customer;
import com.appspot.hk_taxi.anyTaxi.model.PhoneNumber;
import com.google.api.client.util.DateTime;

/**
 * @author Yi.Bairen
 * @version 0.9
 * 
 */
public class Utils {

	public static Customer customer;

	public static final String PREFS_ACCOUNT_KEY = "__EMAIL__";
	public static final String PREFS_DEVICE_INFO_KEY = "__DEVICE__";
	public static final String PREFS_NAME_KEY = "__NAME__";
	public static final String PREFS_TEL_KEY = "__TEL__";
	public static final String PREFS_LOC_KEY = "__LOC__";
	public static final String PREFS_REGDATE_KEY = "__REGDATE__";

	public static final String SUCCESS_SERVICE_KEY = "__SUCCESS__";

	static final String WEB_CLIENT_ID = "1072316261853-u0gafkut9f919bau91gh9bgjb9555hh2."
			+ "apps.googleusercontent.com";
	static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;
	public static final int REQUEST_ACCOUNT_PICKER = 1;

	/**
	 * Called to save supplied value in shared preferences against given key.
	 * 
	 * @param context
	 *            Context of caller activity
	 * @param key
	 *            Key of value to save against
	 * @param value
	 *            Value to save
	 */
	public static void putPreference(Context context, String key, String value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		final SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Called to retrieve required value from shared preferences, identified by
	 * given key. Default value will be returned of no value found or error
	 * occurred.
	 * 
	 * @param context
	 *            Context of caller activity
	 * @param key
	 *            Key to find value against
	 * @param defaultValue
	 *            Value to return if no data found against given key
	 * @return Return the value found against given key, default if not found or
	 *         any error occurs
	 */
	public static String getPreference(Context context, String key,
			String defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		try {
			return sp.getString(key, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static void updateCustomer(Context context) {
		updateCustomer(context, Utils.customer);
	}

	/**
	 * Called to store the customer information to shared preferences. Won't
	 * store customer if its email is null
	 * 
	 * @param context
	 *            Context of caller activity
	 * @param cutomer
	 *            customer information to store in shared preferences
	 */
	public static void updateCustomer(Context context, Customer customer) {
		if (customer == null || customer.getEmail() == null) {
			Utils.customer = null;
			customer = new Customer();
		} else {
			Utils.customer = customer;
		}
		Utils.putPreference(context, Utils.PREFS_ACCOUNT_KEY,
				customer.getEmail());
		Utils.putPreference(context, Utils.PREFS_NAME_KEY, customer.getName());

		if (customer.getPhoneNumber() != null) {
			Utils.putPreference(context, Utils.PREFS_TEL_KEY, customer
					.getPhoneNumber().getNumber());
		} else {
			Utils.putPreference(context, Utils.PREFS_TEL_KEY, "");
		}

		if (customer.getRegDate() != null) {
			Utils.putPreference(context, Utils.PREFS_REGDATE_KEY, customer
					.getRegDate().toString());
		} else {
			Utils.putPreference(context, Utils.PREFS_REGDATE_KEY, null);
		}
	}

	/**
	 * Called to get the customer information to shared preferences. Default
	 * value will be returned of no value found or error occurred.
	 * 
	 * @param context
	 *            Context of caller activity
	 * @return Customer return null if no account is found
	 */
	public static Customer getCustomer(Context context) {
		Customer customer = new Customer();

		customer.setEmail(Utils.getPreference(context, Utils.PREFS_ACCOUNT_KEY,
				null));
		customer.setName(Utils.getPreference(context, Utils.PREFS_NAME_KEY,
				null));
		customer.setDeviceRegistrationID(Utils.getPreference(context,
				Utils.PREFS_DEVICE_INFO_KEY, null));

		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setNumber(Utils.getPreference(context,
				Utils.PREFS_DEVICE_INFO_KEY, null));
		customer.setPhoneNumber(phoneNumber);

		String dateTimeString = Utils.getPreference(context,
				Utils.PREFS_REGDATE_KEY, null);
		if (dateTimeString != null) {
			DateTime dateTime = new DateTime(dateTimeString);
			customer.setRegDate(dateTime);
		}

		if (customer.getEmail() != null) {
			Utils.customer = customer;
			return customer;
		} else {
			return null;
		}
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
