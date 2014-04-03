package comp3111h.anytaxi.customer;

import comp3111h.anytaxi.customer.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MoreOption extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_option);
	}
	
	public void goBack(View view){
		finish();
	}
}
