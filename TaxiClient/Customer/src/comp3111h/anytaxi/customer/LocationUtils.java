package comp3111h.anytaxi.customer;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.AnyTaxi.GetDriver;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

public final class LocationUtils {

    // Debugging tag for the application
    public static final String APPTAG = "AnyTaxiCustomer";

    // Name of shared preferences repository that stores persistent state
    public static final String SHARED_PREFERENCES =
            "comp3111h.anytaxi.customer.SHARED_PREFERENCES";
	
    // Key for storing the "updates requested" flag in shared preferences
    public static final String KEY_UPDATES_REQUESTED =
            "comp3111h.anytaxi.customer.KEY_UPDATES_REQUESTED";
    
    // Create an empty string for initializing strings
    public static final String EMPTY_STRING = new String();
    
	public static GoogleMap mMap;
    
    /**
     * Get the latitude and longitude from the Location object returned by
     * Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return The latitude and longitude of the current location, or null if no
     * location is available.
     */
    public static String getLatLng(Context context, Location currentLocation) {
        // If the location is valid
        if (currentLocation != null) {

            // Return the latitude and longitude as strings
            return context.getString(
                    R.string.latitude_longitude,
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
        } else {

            // Otherwise, return the empty string
            return EMPTY_STRING;
        }
    }
    
	@SuppressLint("NewApi")
	public static void getAddress(Context myActivity, Location currentLocation) {

		// In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
			// No geocoder is present. Issue an error message
			Toast.makeText(myActivity, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
			return;
		}

		if (ConnectionUtils.servicesConnected(myActivity)) {

			// Turn the indefinite activity indicator on
			RequestActivity.mActivityIndicator.setVisibility(View.VISIBLE);

			// Start the background task
			LocationUtils myUtils = new LocationUtils();
			(myUtils.new GetAddressTask(myActivity)).execute(currentLocation);
		}
	}
	/**
	 * An AsyncTask that calls getFromLocation() in the background.
	 * The class uses the following generic types:
	 * Location - A {@link android.location.Location} object containing the current location,
	 *            passed as the input parameter to doInBackground()
	 * Void     - indicates that progress units are not used by this subclass
	 * String   - An address passed to onPostExecute()
	 */
	protected class GetAddressTask extends AsyncTask<Location, Void, String> {

		// Store the context passed to the AsyncTask when the system instantiates it.
		Context localContext;

		// Constructor called by the system to instantiate the task
		public GetAddressTask(Context context) {

			// Required by the semantics of AsyncTask
			super();

			// Set a Context for the background task
			localContext = context;
		}

		/**
		 * Get a geocoding service instance, pass latitude and longitude to it, format the returned
		 * address, and return the address to the UI thread.
		 */
		@Override
		protected String doInBackground(Location... params) {
			/*
			 * Get a new geocoding service instance, set for localized addresses. This example uses
			 * android.location.Geocoder, but other geocoders that conform to address standards
			 * can also be used.
			 */
			Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

			
			
			
			// Get the current location from the input parameter list
			Location location = params[0];
			
			
			if(location==null)
			{
				Log.e(LocationUtils.APPTAG, "Location inside ASYNCTASK is null");
			}
			

			// Create a list to contain the result address
			List <Address> addresses = null;

			// Try to get an address for the current location. Catch IO or network problems.
			try {

				/*
				 * Call the synchronous getFromLocation() method with the latitude and
				 * longitude of the current location. Return at most 1 address.
				 */
				addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1
						);

				// Catch network or other I/O problems.
			} catch (IOException exception1) {

				// Log an error and return an error message
				Log.e(LocationUtils.APPTAG, localContext.getString(R.string.IO_Exception_getFromLocation));

				// print the stack trace
				exception1.printStackTrace();

				// Return an error message
				return (localContext.getString(R.string.IO_Exception_getFromLocation));

				// Catch incorrect latitude or longitude values
			} catch (IllegalArgumentException exception2) {

				// Construct a message containing the invalid arguments
				String errorString = localContext.getString(
						R.string.illegal_argument_exception,
						location.getLatitude(),
						location.getLongitude()
						);
				// Log the error and print the stack trace
				Log.e(LocationUtils.APPTAG, errorString);
				exception2.printStackTrace();

				//
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {

				// Get the first address
				Address address = addresses.get(0);

				// Format the first line of address
				String addressText = localContext.getString(R.string.address_output_string,

						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ?
								address.getAddressLine(0) : "NoStreet",
								
								// Locality is usually a city
								address.getAddressLine(1),

								// The country of the address
								address.getAddressLine(2)
						);

				// Return the text
				return addressText;

				// If there aren't any addresses, post a message
			} else {
				return localContext.getString(R.string.no_address_found);
			}
		}

		/**
		 * A method that's called once doInBackground() completes. Set the text of the
		 * UI element that displays the address. This method runs on the UI thread.
		 */
		@Override
		protected void onPostExecute(String address) {

			// Turn off the progress bar
			RequestActivity.mActivityIndicator.setVisibility(View.GONE);

			// Set the address in the UI
			RequestActivity.mAddress.setText(address);
		}
	}




 
}
