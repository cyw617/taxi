package comp3111h.anytaxi.customer;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

public class LoginActivity extends ActionBarActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final String SAVED_PROGRESS = "login_InProgress";

    private GoogleApiClient googleApiClient;

    // Use login_Progress to track whether user has clicked sign in.
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
    private int login_Progress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent login_Intent;

    private LinearLayout agreement_Layout;
    private TextView agreement_Text;
    private CheckBox agreement_Chkbox;
    private SignInButton login_Btn_In;
    private Button login_Btn_Out;
    private Button login_Btn_Revoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        agreement_Layout = (LinearLayout) findViewById(R.id.login_Agreement_Layout);
        agreement_Text = (TextView) findViewById(R.id.login_Agreement_Text);
        agreement_Chkbox = (CheckBox) findViewById(R.id.login_Agreement_Chkbox);
        login_Btn_In = (SignInButton) findViewById(R.id.login_Btn_In);
        login_Btn_Out = (Button) findViewById(R.id.login_Btn_Out);
        login_Btn_Revoke = (Button) findViewById(R.id.login_Btn_Revoke);

        agreement_Text.setText(Html.fromHtml(getString(R.string.login_Agreement_Text)));

        login_Btn_In.setOnClickListener(this);
        login_Btn_Out.setOnClickListener(this);
        login_Btn_Revoke.setOnClickListener(this);

        if (savedInstanceState != null) {
            login_Progress = savedInstanceState.getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        googleApiClient = build_GoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, login_Progress);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            if (googleApiClient.isConnected())
                NavUtils.navigateUpFromSameTask(this);
            else {
                exit();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        if (googleApiClient.isConnected())
            super.onBackPressed();
        else {
            exit();
        }
    }

    @Override
    public void onClick(View v) {
        if (!googleApiClient.isConnecting()) {
            // Button clicks are only process when GoogleApiClient is not
            // transitioning between connected and not connected.
            switch (v.getId()) {
            case R.id.login_Btn_In:
                if (agreement_Chkbox.isChecked())
                    resolveSignInError();
                else
                    Toast.makeText(this, "Please agree with our terms and conditions.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_Btn_Out:
                // The default account is cleared on sign out so that Google
                // Play services will not return an onConnected callback
                // without user interaction.
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
                googleApiClient.connect();
                break;
            case R.id.login_Btn_Revoke:
                // After revoking permissions for the user with a
                // GoogleApiClient instance, a new instance should be created.
                // A callback on revokeAccessAndDisconnect should be
                // registered to delete user data so as to comply with Google
                // developer policies.
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
                googleApiClient = build_GoogleApiClient();
                googleApiClient.connect();
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
        login_Btn_In.setEnabled(false);
        login_Btn_Out.setEnabled(true);
        login_Btn_Revoke.setEnabled(true);
        agreement_Layout.setVisibility(View.GONE);
        login_Btn_In.setVisibility(View.GONE);
        login_Btn_Out.setVisibility(View.VISIBLE);
        login_Btn_Revoke.setVisibility(View.VISIBLE);

        // Indicate that the sign in process is complete.
        login_Progress = STATE_DEFAULT;
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

        if (login_Progress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the
            // latest error resolution intent for use when the sign in button
            // is clicked.
            login_Intent = result.getResolution();

            if (login_Progress == STATE_SIGN_IN) {
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
        if (login_Intent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error. For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback. This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                login_Progress = STATE_IN_PROGRESS;
                startIntentSenderForResult(
                    login_Intent.getIntentSender(),
                    RC_SIGN_IN, null, 0, 0, 0
                );
            } catch (SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: " + e.getLocalizedMessage());
                // The intent was canceled before it was sent. Attempt to
                // connect to get an updated ConnectionResult.
                login_Progress = STATE_SIGN_IN;
                googleApiClient.connect();
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
                login_Progress = STATE_SIGN_IN;
            } else {
                // If the error resolution was not successful or the user
                // canceled, we should stop processing errors.
                login_Progress = STATE_DEFAULT;
            }

            if (!googleApiClient.isConnecting()) {
                // If Google Play services resolved the issue with a dialog then
                // onStart is not called so we need to re-attempt connection
                // here.
                googleApiClient.connect();
            }
            break;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        googleApiClient.connect();
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
        agreement_Chkbox.setChecked(false);
        login_Btn_In.setEnabled(true);
        login_Btn_Out.setEnabled(false);
        login_Btn_Revoke.setEnabled(false);
        agreement_Layout.setVisibility(View.VISIBLE);
        login_Btn_In.setVisibility(View.VISIBLE);
        login_Btn_Out.setVisibility(View.GONE);
        login_Btn_Revoke.setVisibility(View.GONE);
    }
    
    public GoogleApiClient get_GoogleApiClient() {
        return googleApiClient;
    }
    
    private void exit() {
        new AlertDialog.Builder(this)
        .setMessage(getString(R.string.quit_Message))
        .setPositiveButton(getString(R.string.quit_Positive),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    finish();
                }
            })
        .setNegativeButton(getString(R.string.quit_Negative), null)
        .show();
    }
}
