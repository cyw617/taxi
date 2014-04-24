package comp3111h.anytaxi.customer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public class Register_AgreementFragment extends Fragment {
    public static String TAG = "Register_AgreementFragment";
    
    private OnAgreementCheckedListener agreement_Callback;
    private CheckBox agreement_Chkbox;
    
    public interface OnAgreementCheckedListener {
        public void onAgreementChecked(boolean isChecked);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            agreement_Callback = (OnAgreementCheckedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAgreementCheckedListener");
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_agreement, container, false);
        
        ((TextView) view.findViewById(R.id.register_Agreement_Text))
                .setText(Html.fromHtml(getString(R.string.register_Agreement_Text)));
        
        agreement_Chkbox = (CheckBox) view.findViewById(R.id.register_Agreement_Chkbox);
        agreement_Chkbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Agreement checkbox is clicked.");
                
                agreement_Callback.onAgreementChecked(((CheckBox) v).isChecked());
            }
        });
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();

        agreement_Chkbox.setChecked(false);
    }
}
