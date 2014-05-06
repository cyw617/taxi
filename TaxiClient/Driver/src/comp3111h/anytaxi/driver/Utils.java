package comp3111h.anytaxi.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.appspot.hk_taxi.anyTaxi.model.PhoneNumber;
import com.google.api.client.util.DateTime;

/**
 * @author Yi.Bairen
 * @version 0.9
 * 
 */
public class Utils {
	
	public static Driver driver;
	
    public static final String PREFS_ACCOUNT_KEY = "__EMAIL__" ;
    public static final String PREFS_DEVICE_INFO_KEY = "__DEVICE__";
    public static final String PREFS_NAME_KEY = "__NAME__";
    public static final String PREFS_TEL_KEY = "__TEL__";
    public static final String PREFS_LICENSE_KEY = "__LICENSE__";
    public static final String PREFS_LOC_KEY = "__LOC__";
    public static final String PREFS_REGDATE_KEY = "__REGDATE__";
	
    public static final String SUCCESS_SERVICE_KEY = "__SUCCESS__";  
    public static final String CUSTOMER_LOC = "__CUSTOMER_LOCATION__";
    public static final String CUSTOMER_DES = "__CUSTOMER_DESTINATION__";
    public static final String CUSTOMER_LATLNG = "__CUSTOMER_LATLNG__";
    public static final String CUSTOMER_LIST_ID = "__CUSTOMER_LIST_ID__";
    public static final String TRASACTION_ID = "__TRANSACTION_ID__";
    
    public static final String DRIVER_LAT = "__DRIVER_LATITUDE__";
    public static final String DRIVER_LNG = "__DRIVER_LONGITUDE__";


    static final String WEB_CLIENT_ID = "1072316261853-u0gafkut9f919bau91gh9bgjb9555hh2."
			+ "apps.googleusercontent.com";
    static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID; 
    public static final int REQUEST_ACCOUNT_PICKER = 1;
        
    /**
     * Called to save supplied value in shared preferences against given key.
     * @param context Context of caller activity
     * @param key Key of value to save against
     * @param value Value to save
     */
    public static void putPreference(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    
    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @param key Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getPreference(Context context, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sp.getString(key, defaultValue);
        } catch (Exception e) {
             e.printStackTrace();
             return defaultValue;
        }
    }
    
    public static void updateDriver(Context context) {
    	updateDriver(context, Utils.driver);
    }
    
    /**
     * Called to store the driver information to shared preferences.
     * Won't store driver if its email is null
     * @param context Context of caller activity
     * @param driver driver information to store in shared preferences
     */
    public static void updateDriver(Context context, Driver driver) {		
    	if (driver == null || driver.getEmail() == null) {
    		Utils.driver = null;
    		driver = new Driver();
    	} else {
    	   	Utils.driver = driver;
    	}
    	Utils.putPreference(context, Utils.PREFS_ACCOUNT_KEY, driver.getEmail());
		Utils.putPreference(context, Utils.PREFS_NAME_KEY, driver.getName());    
		
    	if (driver.getPhoneNumber() != null) {
    		Utils.putPreference(context, Utils.PREFS_TEL_KEY, driver.getPhoneNumber().getNumber());
    	} else {
    		Utils.putPreference(context, Utils.PREFS_TEL_KEY, "");
    	}
        
        if (driver.getLicense() != null) {
            Utils.putPreference(context, Utils.PREFS_LICENSE_KEY, driver.getLicense());
        } else {
            Utils.putPreference(context, Utils.PREFS_LICENSE_KEY, "");
        }
    	
    	if (driver.getRegDate() != null) {
    		Utils.putPreference(context, Utils.PREFS_REGDATE_KEY, driver.getRegDate().toString());
    	} else {
    		Utils.putPreference(context, Utils.PREFS_REGDATE_KEY, null);
    	}
    }
    
    /**
     * Called to get the driver information from shared preferences.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @return Driver return null if no account is found
     */
    public static Driver getDriver(Context context) {
    	Driver driver = new Driver();		
    	
    	driver.setEmail(Utils.getPreference(context, Utils.PREFS_ACCOUNT_KEY, null));
    	driver.setName(Utils.getPreference(context, Utils.PREFS_NAME_KEY, null));
        driver.setLicense(Utils.getPreference(context, Utils.PREFS_LICENSE_KEY, null));
    	driver.setDeviceRegistrationID(Utils.getPreference(context, Utils.PREFS_DEVICE_INFO_KEY, null));
    	
    	PhoneNumber phoneNumber = new PhoneNumber();
    	phoneNumber.setNumber(Utils.getPreference(context, Utils.PREFS_DEVICE_INFO_KEY, null));
    	driver.setPhoneNumber(phoneNumber);
    	
    	String dateTimeString = Utils.getPreference(context, Utils.PREFS_REGDATE_KEY, null);
    	if (dateTimeString != null) {
    		DateTime dateTime = new DateTime(dateTimeString);
    		driver.setRegDate(dateTime);
    	}

    	if (driver.getEmail() != null) {
    		Utils.driver = driver;
    		return driver; 
    	} else {
    		return null;
    	}
    }
	
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
