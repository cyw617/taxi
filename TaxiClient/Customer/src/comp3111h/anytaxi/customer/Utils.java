package comp3111h.anytaxi.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Yi.Bairen
 * @version 0.9
 * 
 */
public class Utils {
	
    public static final String PREFS_ACCOUNT_KEY = "__EMAIL__" ;
    public static final String PREFS_DEVICE_INFO_KEY = "__DEVICE__";
    public static final String PREFS_NAME_KEY = "__NAME__";
    public static final String PREFS_TEL_KEY = "__TEL__";
    public static final String PREFS_LOC_KEY = "__LOC__";
    public static final String PREFS_LICENCE_KEY = "__LICENSE__";
	
    public static final String SUCCESS_SERVICE_KEY = "__SUCCESS__";   


    static final String WEB_CLIENT_ID = "1072316261853-u0gafkut9f919bau91gh9bgjb9555hh2."
			+ "apps.googleusercontent.com";
    static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID; 
    static final int REQUEST_ACCOUNT_PICKER = 1;
        
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
}
