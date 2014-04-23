package comp3111h.anytaxi.customer;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.AnyTaxi.GetDriver;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;

public class LoginActivity extends ActionBarActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onStart() {
        super.onStart();
        
        // TODO: Relocate in a more appropriate class
        LocationUtils.credential = GoogleAccountCredential.usingAudience(this,
                "server:client_id:" + LocationUtils.WEB_CLIENT_ID);
        startActivityForResult(LocationUtils.credential.newChooseAccountIntent(), LocationUtils.REQUEST_ACCOUNT_PICKER);
    }
    
    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        switch (requestCode) {
        case LocationUtils.REQUEST_ACCOUNT_PICKER:
            Log.i(TAG, "An account has been picked.");
            
            if (intent != null && intent.getExtras() != null) {
                LocationUtils.accountName = intent.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                
                Log.i(TAG, "Account: " + LocationUtils.accountName);
                
                if (LocationUtils.accountName != null) {
                    // TODO: Validate user from the server
                    // If user is not present, start "RegisterActivity"
                    // If user is present, store the email in sharedPreference and start "RequestActivity"
                }
            }
            break;
        
        default:
            Log.e(TAG, getString(R.string.unknown_activity_request_code, requestCode));
        }
    }
    
    private void exit() {
        showAlertDialog(this,
                getString(R.string.quit_Message),
                getString(R.string.quit_Positive),
                getString(R.string.quit_Negative),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                    }
                }, null);
    }

    /**
     * @param context The current context of UI thread
     * @param msg Message to show in the body of dialog
     * @param posButtonText Text of positive button
     * @param negButtonText Text of negative button
     * @param posListener Click handler of positive button
     * @param negListener Click handler of negative button
     * 
     */
    public void showAlertDialog(Context context, String msg, String posButtonText,
            String negButtonText,  OnClickListener posListener, OnClickListener negListener) {
        new AlertDialog.Builder(context)
        .setMessage(msg)
        .setPositiveButton(posButtonText,
                posListener)
                .setNegativeButton(negButtonText, negListener)
                .show();
    }
}
