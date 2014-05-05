package comp3111h.anytaxi.customer;

import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DeleteOrder extends ActionBarActivity {
	private final static String TAG = "DeleteOrder";

	private final static String HOST = "143.89.168.85";
	private final static int PORT = 4578;

	private Button deleteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);

		deleteButton = (Button) findViewById(R.id.delete);
		deleteButton.setOnClickListener(onDeleteListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	private OnClickListener onDeleteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new DeleteTask().execute();
		}
	};

	/**
	 * This will send a "delete" token to the server, which will react to delete
	 * the order.
	 */
	public class DeleteTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Socket clientSocket = new Socket(HOST, PORT);

				PrintWriter networkOutput = new PrintWriter(
						clientSocket.getOutputStream(), true);

				networkOutput.println("delete");

				clientSocket.close();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "An order is deleted.");

			Toast.makeText(deleteButton.getContext(),
					"The order is deleted succesfully.", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
