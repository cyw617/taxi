package comp3111h.anytaxi.customer;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import comp3111h.anytaxi.customer.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

class ConnectionUtils {

	/*
	 * Define a request code to send to Google Play services
	 * This code is returned in Activity.onActivityResult
	 */
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	public static class ErrorDialogFragment extends DialogFragment {
	
	    // Global field to contain the error dialog
	    private Dialog mDialog;
	
	    /**
	     * Default constructor. Sets the dialog field to null
	     */
	    public ErrorDialogFragment() {
	        super();
	        mDialog = null;
	    }
	
	    /**
	     * Set the dialog to display
	     *
	     * @param dialog An error dialog
	     */
	    public void setDialog(Dialog dialog) {
	        mDialog = dialog;
	    }
	
	    /*
	     * This method must return a Dialog to the DialogFragment.
	     */
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        return mDialog;
	    }
	}

	public static void showError(final Activity activity, String message) {
	  final String errorMessage = message == null ? "Error" : "[Error ] "
	      + message;
	  activity.runOnUiThread(new Runnable() {
	    public void run() {
	      Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
	          .show();
	    }
	  });
	}

	static void requestCodeHandler(int requestCode, int resultCode, Intent intent, Context myActivity)
	{
		switch (requestCode) {
		case CustomerAccountInfo.REQUEST_ACCOUNT_PICKER:
	         if (intent != null && intent.getExtras() != null) {
	
	        	 CustomerAccountInfo.setAccountInfo(intent);
	          }
			break;
	
			// If the request code matches the code sent in onConnectionFailed
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
	
	
			switch (resultCode) {
			// If Google Play services resolved the problem
			case Activity.RESULT_OK:
				// Log the result
				Log.d(LocationUtils.APPTAG, myActivity.getString(R.string.resolved));
				// Display the result
				RequestActivity.mConnectionState.setText(R.string.connected);
				RequestActivity.mConnectionStatus.setText(R.string.resolved);
				break;
	
				// If any other result was returned by Google Play services
			default:
				// Log the result
				Log.d(LocationUtils.APPTAG, myActivity.getString(R.string.no_resolution));
	
				// Display the result
				RequestActivity.mConnectionState.setText(R.string.disconnected);
				RequestActivity.mConnectionStatus.setText(R.string.no_resolution);
	
				break;
			}
	
			// If any other request code was received
		default:
			// Report that this Activity received an unknown requestCode
			Log.d(LocationUtils.APPTAG,
					myActivity.getString(R.string.unknown_activity_request_code, requestCode));
	
			break;
		}
		
	}
	
	static boolean servicesConnected(Context myActivity) {

		// Check that Google Play services is available
		int resultCode =
				GooglePlayServicesUtil.isGooglePlayServicesAvailable(myActivity);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG, myActivity.getString(R.string.play_services_available));

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) myActivity, 0);
			if (dialog != null) {
				ConnectionUtils.ErrorDialogFragment errorFragment = new ConnectionUtils.ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(((ActionBarActivity) myActivity).getSupportFragmentManager(), LocationUtils.APPTAG);
			}
			return false;
		}
	}   
	
	/**
	 * Show a dialog returned by Google Play services for the
	 * connection error code
	 *
	 * @param errorCode An error code returned from onConnectionFailed
	 */
	static void showErrorDialog(int errorCode, Context myActivity) {

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
				errorCode,
				(Activity) myActivity,
				ConnectionUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(((ActionBarActivity) myActivity).getSupportFragmentManager(), LocationUtils.APPTAG);
		}
	}
	
	static void connectionResultHandler(ConnectionResult connectionResult, Context myActivity) {
		if (connectionResult.hasResolution()) {
			try {

				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						(Activity) myActivity,
						ConnectionUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */

			} catch (IntentSender.SendIntentException e) {

				// Log the error
				e.printStackTrace();
			}
		} else {

			// If no resolution is available, display a dialog to the user with the error.
			ConnectionUtils.showErrorDialog(connectionResult.getErrorCode(),myActivity);
		}
	}

}
