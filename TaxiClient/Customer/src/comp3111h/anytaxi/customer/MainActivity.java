package comp3111h.anytaxi.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    
	    // TODO: Validate login status from server
	    boolean isLogin = false;
	    
	    Intent intent = (isLogin)?
	            new Intent(this, RequestActivity.class):
	            new Intent(this, LoginActivity.class);
        startActivity(intent);
	}
	
	@Override
	protected void onStop() {
	    super.onStop();
	}
}
