package comp3111h.anytaxi.driver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
    GoogleApiManager googleApiManager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
        googleApiManager = new GoogleApiManager(this);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();

        googleApiManager.connect();
	}
	
	@Override
	protected void onStop() {
	    super.onStop();

        if (googleApiManager.isConnected()) {
            googleApiManager.disconnect();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setting_Login(View view) {
	    Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
	}
}
