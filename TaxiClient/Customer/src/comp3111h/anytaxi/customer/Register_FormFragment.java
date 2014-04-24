package comp3111h.anytaxi.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Register_FormFragment extends Fragment {
    private EditText rEmail_Field;
    private EditText rFirstName_Field;
    private EditText rLastName_Field;
    private EditText rPhone_Field;
    
    private String rEmail;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_form, container, false);
        
        rEmail_Field = (EditText) view.findViewById(R.id.register_email);
        rFirstName_Field = (EditText) view.findViewById(R.id.register_first_name);
        rLastName_Field = (EditText) view.findViewById(R.id.register_last_name);
        rPhone_Field = (EditText) view.findViewById(R.id.register_phone);
        
        rEmail = getArguments().getString(Utils.PREFS_ACCOUNT_KEY);
        
        rEmail_Field.setText(rEmail);
        rEmail_Field.setEnabled(false);
        
        return view;
    }
}
