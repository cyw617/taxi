/*
 * This is a wrapper for GoogleApiClient object.
 * 
 * For every activity using a GoogleApiClient, please overload
 * the following functions and implement the codes:
 * onCreate ==> googleApiManager = new GoogleApiManager(this);
 * onStart  ==> googleApiManager.connect();
 * onStop   ==> if (googleApiManager.isConnected()) {
 *                  googleApiManager.disconnect();
 *              }
 */

package comp3111h.anytaxi.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class GoogleApiManager implements ConnectionCallbacks, OnConnectionFailedListener {
    
    static final String TAG = "GoogleApiManager";
    
    Context context;
    
    GoogleApiClient googleApiClient;
    
    GoogleApiManager(Context context) {
        this.context = context;
        this.googleApiClient = new GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API, null)
            .addScope(Plus.SCOPE_PLUS_LOGIN)
            .build();
    }

    // If the connection is failed, the user should be redirected to login.
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed");
        
        // alert the user to login
        Toast.makeText(this.context, "Please login to enjoy AnyTaxi :)", Toast.LENGTH_SHORT).show();
        
        // redirect the user to login
        Intent login = new Intent(this.context, LoginActivity.class);
        this.context.startActivity(login);
    }

    // If a connection is made successfully, nothing happens.
    @Override
    public void onConnected(Bundle connectHint) {
        Log.i(TAG, "onConnected");
    }

    // If a connection is suspended, reconnection will be done.
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "onConnectionSuspended");
        
        this.googleApiClient.connect();
    }
    
    // attempt to make connection
    public void connect() {
        this.googleApiClient.connect();
    }
    
    // disconnect the connection
    public void disconnect() {
        this.googleApiClient.disconnect();
    }
    
    // return the user from Google+
    public Person get_User() {
        return Plus.PeopleApi.getCurrentPerson(this.googleApiClient);
    }
    
    // check whether a connection has been made
    public boolean isConnected() {
        return this.googleApiClient.isConnected();
    }
}
