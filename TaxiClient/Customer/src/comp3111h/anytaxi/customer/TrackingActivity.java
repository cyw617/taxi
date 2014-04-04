package comp3111h.anytaxi.customer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class TrackingActivity extends ActionBarActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void goBack(View view){
		finish();
	}
}
