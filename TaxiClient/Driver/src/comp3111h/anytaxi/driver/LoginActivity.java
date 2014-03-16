package comp3111h.anytaxi.driver;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    private GoogleApiClient mGoogleApiClient;

    // Use mSignInProgress to track whether user has clicked sign in.
    //
    // STATE_DEFAULT:
    //         The default state of the application before the user
    //         has clicked 'sign in', or after they have clicked
    //         'sign out'. In this state we will not attempt to
    //         resolve sign in errors and so will display LoginActivity
    //         in a signed-out state.
    // STATE_SIGN_IN:
    //         This state indicates that the user has clicked 'sign
    //         in', so resolve successive errors preventing sign in
    //         until the user has successfully authorized an account
    //         for AnyTaxi.
    // STATE_IN_PROGRESS:
    //         This state indicates that we have started an intent to
    //         resolve an error, and so we should not start further
    //         intents until the current intent completes.
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) findViewById(R.id.revoke_access_button);
        mStatus = (TextView) findViewById(R.id.sign_in_status);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        mGoogleApiClient = build_GoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            // Button clicks are only process when GoogleApiClient is not
            // transitioning between connected and not connected.
            switch (v.getId()) {
            case R.id.sign_in_button:
                mStatus.setText(R.string.status_signing_in);
                resolveSignInError();
                break;
            case R.id.sign_out_button:
                // The default account is cleared on sign out so that Google
                // Play services will not return an onConnected callback
                // without user interaction.
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                break;
            case R.id.revoke_access_button:
                // After revoking permissions for the user with a
                // GoogleApiClient instance, a new instance should be created.
                // A callback on revokeAccessAndDisconnect should be
                // registered to delete user data so as to comply with Google
                // developer policies.
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                mGoogleApiClient = build_GoogleApiClient();
                mGoogleApiClient.connect();
                break;
            }
        }
    }

    /*
     * onConnected is called when our Activity successfully connects to Google
     * Play services. onConnected indicates that an account was selected on the
     * device, that the selected account has granted any requested permissions
     * to our app and that we were able to establish a service connection to
     * Google Play services.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");

        // Update the user interface to reflect that the user is signed in.
        mSignInButton.setEnabled(false);
        mSignOutButton.setEnabled(true);
        mRevokeButton.setEnabled(true);

        // Retrieve some profile information to personalize our app for the
        // user.
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        mStatus.setText(String.format(
            getResources().getString(R.string.signed_in_as),
            currentUser.getDisplayName()
        ));

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;
        
        Toast.makeText(this, mStatus.getText(), Toast.LENGTH_LONG).show();
    }

    /*
     * onConnectionFailed is called when our Activity could not connect to
     * Google Play services. onConnectionFailed indicates that the user needs to
     * select an account, grant permissions or resolve an error in order to sign
     * in.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes
        // might be returned in onConnectionFailed.
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

        if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the
            // latest error resolution intent for use when the sign in button
            // is clicked.
            mSignInIntent = result.getResolution();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in
                // button so we should continue processing errors until the user
                // is signed in or they click cancel.
                resolveSignInError();
            }
        }

        // Whenever the user does not have a connection to Google Play services,
        // the user is considered signed-out.
        onSignedOut();
    }

    /*
     * Starts an appropriate intent or dialog for user interaction to resolve
     * the current error preventing the user from being signed in. This could be
     * a dialog allowing the user to select an account, an activity allowing the
     * user to consent to the permissions being requested by your app, a setting
     * to enable device networking, etc.
     */
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error. For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback. This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(
                    mSignInIntent.getIntentSender(),
                    RC_SIGN_IN, null, 0, 0, 0
                );
            } catch (SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: " + e.getLocalizedMessage());
                // The intent was canceled before it was sent. Attempt to
                // connect to get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case RC_SIGN_IN:
            if (resultCode == RESULT_OK) {
                // If the error resolution was successful we should continue
                // processing errors.
                mSignInProgress = STATE_SIGN_IN;
            } else {
                // If the error resolution was not successful or the user
                // canceled, we should stop processing errors.
                mSignInProgress = STATE_DEFAULT;
            }

            if (!mGoogleApiClient.isConnecting()) {
                // If Google Play services resolved the issue with a dialog then
                // onStart is not called so we need to re-attempt connection
                // here.
                mGoogleApiClient.connect();
            }
            break;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    private GoogleApiClient build_GoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API, null)
            .addScope(Plus.SCOPE_PLUS_LOGIN)
            .build();
    }

    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);
        mSignOutButton.setEnabled(false);
        mRevokeButton.setEnabled(false);

        mStatus.setText(R.string.status_signed_out);
    }
}
