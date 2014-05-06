package comp3111h.anytaxi.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.appspot.hk_taxi.anyTaxi.model.PhoneNumber;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Register_FormFragment extends Fragment {
    
    public static String TAG = "Register_FormFragment";
    
    private static GoogleAccountCredential credential;
    private static AnyTaxi endpoint;
    
    private final static int EMAIL = 0;
    private final static int LICENSE = 1;
    private final static int FIRSTNAME = 2;
    private final static int LASTNAME = 3;
    private final static int PHONE = 4;
    private final static int NUM_OF_FIELD = 5;
    
    private ArrayList<EditText> rField;
    private Button rConfirm_Btn;
    
    private ArrayList<String> rInput;
    
    private Driver rDriver;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rDriver = new Driver();
        
        View view = inflater.inflate(R.layout.fragment_register_form, container, false);

        rField = new ArrayList<EditText>(NUM_OF_FIELD);
        rField.add(EMAIL, (EditText) view.findViewById(R.id.register_email));
        rField.add(LICENSE, (EditText) view.findViewById(R.id.register_license));
        rField.add(FIRSTNAME, (EditText) view.findViewById(R.id.register_first_name));
        rField.add(LASTNAME, (EditText) view.findViewById(R.id.register_last_name));
        rField.add(PHONE, (EditText) view.findViewById(R.id.register_phone));
        rConfirm_Btn = (Button) view.findViewById(R.id.register_confirm_btn);

        rInput = new ArrayList<String>(NUM_OF_FIELD);
        rInput.add(EMAIL, Utils.getPreference(getActivity().getApplicationContext(), Utils.PREFS_ACCOUNT_KEY, null));
        rInput.add(LICENSE, null);
        rInput.add(FIRSTNAME, null);
        rInput.add(LASTNAME, null);
        rInput.add(PHONE, null);
        
        if (rInput.get(EMAIL) != null) {
            rField.get(EMAIL).setText(rInput.get(EMAIL));
            rField.get(EMAIL).setEnabled(false);
        }
        else {
            Log.e(TAG, "User email cannot be found in sharedPreference.");
            
            // Return to LoginActivity for picking an account,
            // by finishing the current activity.
            getActivity().finish();
        }
        
        rConfirm_Btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
        
        return view;
    }
    
    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "User is filling the registration form.");
        
        credential = GoogleAccountCredential.usingAudience(getActivity(), Utils.AUDIENCE);
    }
    
    private void attemptRegister() {
        Log.i(TAG, "User attempts to register.");
        
        boolean isCancelled = false;
        boolean isRequestFocus = false;
        
        // Validate the inputs
        for (int i = 0; i < NUM_OF_FIELD; i++) {
            if (!isValid(i)) {
                isCancelled = true;
                if (!isRequestFocus) {
                    rField.get(i).requestFocus();
                    isRequestFocus = true;
                }
            }
        }
        
        // Cancel the registration attempt
        // if any input is invalid.
        if (isCancelled) {
            Log.i(TAG, "Registration has not been accepted due to invalid inputs.");
            
            return;
        }
        
        // Show a message to tell user that sign-up is under progress
        Toast.makeText(getActivity(), getString(R.string.register_signing_up), Toast.LENGTH_LONG).show();
        
        // Create a driver model
        // TODO: rDriver.setDeviceRegistrationID(deviceRegistrationID);
        rDriver.setEmail(rInput.get(EMAIL));
        rDriver.setLicense(rInput.get(LICENSE));
        rDriver.setName(rInput.get(FIRSTNAME) + " " + rInput.get(LASTNAME));
        rDriver.setPhoneNumber(new PhoneNumber().setNumber(rInput.get(PHONE)));
        
        // Store the user information into sharedPreference
        Log.i(TAG, "Registration information is saving into sharedPreference.");
        
        Utils.updateDriver(getActivity(), rDriver);
        
        // Register the user to the server
        Log.i(TAG, "Registration information is sending to server.");
        
        new RegistrationTask(getActivity()).execute();
    }
    
    private boolean isValid(final int field) {
        switch (field) {
        case EMAIL:
            return true;
        case LICENSE:
            return isValidLicense();
        case FIRSTNAME:
        case LASTNAME:
            return isValidName(field);
        case PHONE:
            return isValidPhone();
        default:
            Log.e(TAG, "Invalid Field: " + field);
            return false;
        }
    }

    private boolean isValidLicense() {
        EditText rLicense_Field;
        String rLicense_Input;
        
        rLicense_Field = rField.get(LICENSE);
        rLicense_Input = licenseBeautifier(rLicense_Field.getText().toString());
        rInput.set(LICENSE, rLicense_Input);
        
        // Reset the error message
        rLicense_Field.setError(null);
        
        // Check for invalidity
        if (TextUtils.isEmpty(rLicense_Input)) {
            rLicense_Field.setError(getString(R.string.register_error_required_field));
            return false;
        }
        if (!rLicense_Input.matches("[ A-Z0-9]+")) {
            rLicense_Field.setError(getString(R.string.register_error_only_alphanumeric));
            return false;
        }
        if (rLicense_Input.length() < 4) {
            rLicense_Field.setError(getString(R.string.register_error_unreal_license));
            return false;
        }
        
        return true;
    }
    
    private boolean isValidName(final int field) {
        EditText rName_Field;
        String rName_Input;
        
        switch (field) {
        case FIRSTNAME:
        case LASTNAME:
            rName_Field = rField.get(field);
            rName_Input = nameBeautifier(rName_Field.getText().toString());
            rInput.set(field, rName_Input);
            break;
        default:
            Log.e(TAG, "Invalid Name Field: " + field);
            return false;
        }
        
        // Reset the error message
        rName_Field.setError(null);
        
        // Check for invalidity
        if (TextUtils.isEmpty(rName_Input)) {
            rName_Field.setError(getString(R.string.register_error_required_field));
            return false;
        }
        if (!rName_Input.matches("[ A-Za-z]+")) {
            rName_Field.setError(getString(R.string.register_error_only_alphabet));
            return false;
        }
        if (rName_Input.length() < 2) {
            rName_Field.setError(getString(R.string.register_error_unreal_name));
            return false;
        }
        
        return true;
    }

    private boolean isValidPhone() {
        EditText rPhone_Field;
        String rPhone_Input;
        
        rPhone_Field = rField.get(PHONE);
        rPhone_Input = rPhone_Field.getText().toString();
        rInput.set(PHONE, rPhone_Input);
        
        // Reset the error message
        rPhone_Field.setError(null);
        
        // Check for invalidity
        if (TextUtils.isEmpty(rPhone_Input)) {
            rPhone_Field.setError(getString(R.string.register_error_required_field));
            return false;
        }
        if (!TextUtils.isDigitsOnly(rPhone_Input)) {
            rPhone_Field.setError(getString(R.string.register_error_only_number));
            return false;
        }
        if (rPhone_Input.length() < 8) {
            rPhone_Field.setError(getString(R.string.register_error_unreal_phone));
            return false;
        }
        if (rPhone_Input.charAt(0) != '5'
                && rPhone_Input.charAt(0) != '6'
                && rPhone_Input.charAt(0) != '9') {
            rPhone_Field.setError(getString(R.string.register_error_mobile_phone));
            return false;
        }
        
        return true;
    }
    
    // Beautify a license by
    // trimming the white spaces at the front and back,
    // and capitalizing the each character
    private String licenseBeautifier(String license) {
        String[] partialLicense = TextUtils.split(license.trim().toUpperCase(Locale.getDefault()), " +");
        
        license = TextUtils.join(" ", partialLicense);
        
        return license;
    }
    
    // Beautify a name by
    // trimming the white spaces at the front and back,
    // and capitalizing the first letter of each word
    private String nameBeautifier(String name) {
        String[] partialNames = TextUtils.split(name.trim().toUpperCase(Locale.getDefault()), " +");
        for (int i = 0; i < partialNames.length; i++)
            partialNames[i] = partialNames[i].charAt(0)
                    + partialNames[i].substring(1).toLowerCase(Locale.getDefault());
        
        name = TextUtils.join(" ", partialNames);
        
        return name;
    }
    
    private class RegistrationTask extends AsyncTask<Void, Void, Void> {
        
        private Context context;
        private Exception exception;
        
        protected RegistrationTask(Context context) {
            this.context = context;
        }
        
        @Override
        protected Void doInBackground(Void... params) {
            credential.setSelectedAccountName(rDriver.getEmail());
            endpoint = CloudEndpointUtils.updateBuilder(
                    new AnyTaxi.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            new JacksonFactory(),
                            null))
                            .build();
            
            try {
                endpoint.addDriver(rDriver).execute();
            } catch (IOException e) {
                exception = e;
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (exception != null) {
                CloudEndpointUtils.logAndShow(getActivity(), TAG, exception);
                return;
            }
            
            GCMIntentService.register(context);            
            Intent intent = new Intent(this.context, CustomerListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            Toast.makeText(context, "Congrats! You've signed up!", Toast.LENGTH_LONG).show();
            
            Log.i(TAG, "Sign-up progress is completed.");
        }
    }
}
