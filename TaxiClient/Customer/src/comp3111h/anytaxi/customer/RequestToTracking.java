package comp3111h.anytaxi.customer;

import java.io.IOException;

import android.support.v4.app.FragmentActivity;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.appspot.hk_taxi.anyTaxi.model.Customer;
import com.appspot.hk_taxi.anyTaxi.model.Transaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class RequestToTracking extends ActionBarActivity {

	private final static String TAG = "LoadingDriverAsyncTask";
	
	//Tracking the progress of finding near drivers
	private ProgressBar mProgressBar;
	//Delay before sending next inquiry to server
	private int mDelay = 300;
	Customer customer;
	static AnyTaxi endpoint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_to_tracking);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		mProgressBar = (ProgressBar) findViewById(R.id.loadingdriverprogress2);
		customer = Utils.customer;
		
		new LoadingDriverTask().execute();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.request_to_tracking, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_request_to_tracking, container, false);
			return rootView;
		}
	}
	
	class LoadingDriverTask extends AsyncTask<Void, Integer, String> {


		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected String doInBackground(Void... none) {
			if(LoginActivity.endpoint!=null)
			{
				endpoint=LoginActivity.endpoint;
			}
			else
			{
				if(MainActivity.endpoint!=null)
				{
					endpoint=MainActivity.endpoint;
				}
				else
				{
					ConnectionUtils.showError(RequestToTracking.this,"endpoint in LoginActivity is null");
				}
			}
			
			if(endpoint==null)
			{
				ConnectionUtils.showError(RequestToTracking.this,"endpoint is null!");
			}
			else
			{
				ConnectionUtils.showError(RequestToTracking.this,"endpoint is not null");
			}
			
			try {
				endpoint.addTransaction(Utils.customer.getEmail(), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// simulating long-running operation 	mer
			Transaction myTrans = new Transaction();
			myTrans.setDriverEmail(null);
			Integer i=0;
			do
			{
				sleep();
				i++;
				try {
					myTrans = endpoint.getTransaction(Utils.customer.getEmail(), null).execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i = (i>=9)?9:i;
				publishProgress(i * 10);
			}
			while(myTrans==null||myTrans.getDriverEmail()==null);
			
			publishProgress(Integer.valueOf(100));
			return myTrans.getDriverEmail();
			
			
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String driverEmail) {
			mProgressBar.setVisibility(ProgressBar.INVISIBLE);
			
			Bundle driverInfo = new Bundle();
			driverInfo.putString("Email",driverEmail);
			
	    	Intent intent = new Intent(RequestToTracking.this, TrackingActivity.class);
	    	intent.putExtras(driverInfo);
			startActivity(intent);
			finish();
		}

		private void sleep() {
			try {
				Thread.sleep(mDelay);
			} catch (InterruptedException e) {
				Log.e(TAG, e.toString());
			}
		}
	}
	


}
