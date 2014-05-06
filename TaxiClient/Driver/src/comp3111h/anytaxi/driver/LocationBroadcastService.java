package comp3111h.anytaxi.driver;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

//Service

public class LocationBroadcastService extends Service {
	private final IBinder binder = new MyBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public void doServiceStuff() {
		task.execute();
	}

	// create an inner Binder class
	public class MyBinder extends Binder {
		public LocationBroadcastService getService() {
			return LocationBroadcastService.this;
		}
	}

	AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

		@Override
		protected Void doInBackground(Void... params) {
			Log.d("yourTag", "long running service task");
			return null;
		}
	};
}