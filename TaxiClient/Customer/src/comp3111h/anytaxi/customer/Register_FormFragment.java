package comp3111h.anytaxi.customer;

import java.util.ArrayList;

import com.appspot.hk_taxi.anyTaxi.model.Customer;
import com.appspot.hk_taxi.anyTaxi.model.PhoneNumber;

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

public class Register_FormFragment extends Fragment {
    
    public static String TAG = "Register_FormFragment";
    
    private final static int EMAIL = 0;
    private final static int FIRSTNAME = 1;
    private final static int LASTNAME = 2;
    private final static int PHONE = 3;
    private final static int NUM_OF_FIELD = 4;
    
    private ArrayList<EditText> rField;
    private Button rConfirm_Btn;
    
    private ArrayList<String> rInput;
    
    private Customer rCustomer;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rCustomer = new Customer();
        
        View view = inflater.inflate(R.layout.fragment_register_form, container, false);

        rField = new ArrayList<EditText>(NUM_OF_FIELD);
        rField.add(EMAIL, (EditText) view.findViewById(R.id.register_email));
        rField.add(FIRSTNAME, (EditText) view.findViewById(R.id.register_first_name));
        rField.add(LASTNAME, (EditText) view.findViewById(R.id.register_last_name));
        rField.add(PHONE, (EditText) view.findViewById(R.id.register_phone));
        rConfirm_Btn = (Button) view.findViewById(R.id.register_confirm_btn);

        rInput = new ArrayList<String>(NUM_OF_FIELD);
        rInput.add(EMAIL, Utils.getPreference(getActivity().getApplicationContext(), Utils.PREFS_ACCOUNT_KEY, null));
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
    public void onResume() {
        super.onResume();

        Log.i(TAG, "User is filling the registration form.");
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
            Log.i(TAG, "Registration has not been accepted, due to invalid inputs.");
            
            return;
        }
        
        // TODO: Signing-up message
        
        // Create a customer model
        // TODO: rCustomer.setDeviceRegistrationID(deviceRegistrationID);
        rCustomer.setEmail(rInput.get(EMAIL));
        rCustomer.setName(rInput.get(FIRSTNAME) + ", " + rInput.get(LASTNAME));
        rCustomer.setPhoneNumber(new PhoneNumber().setNumber(rInput.get(PHONE)));
        // TODO: rCustomer.setRegDate(regDate);
        
        // Store the user information into sharedPreference
        Log.i(TAG, "Registration information is saving into sharedPreference.");
        
        Utils.updateCustomer(getActivity(), rCustomer);
        
        // TODO: Register the user to the server
    }
    
    private boolean isValid(final int field) {
        switch (field) {
        case EMAIL:
            return true;
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
    
    private boolean isValidName(final int field) {
        EditText rName_Field;
        String rName_Input;
        
        switch (field) {
        case FIRSTNAME:
        case LASTNAME:
            rName_Field = rField.get(field);
            rInput.set(field, rName_Field.getText().toString());
            rName_Input = rInput.get(field);
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
        
        // TODO: Make the name more appealing
        // remove last "space"
        // capitalize first letter
        
        return true;
    }
    
    private boolean isValidPhone() {
        EditText rPhone_Field;
        String rPhone_Input;
        
        rPhone_Field = rField.get(PHONE);
        rInput.set(PHONE, rPhone_Field.getText().toString());
        rPhone_Input = rInput.get(PHONE);
        
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
        
        return true;
    }
}
