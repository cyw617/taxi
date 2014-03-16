package comp3111h.anytaxi.customer;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "comp3111h.anytaxi.customer.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		
		EditText editText1 = (EditText) findViewById(R.id.edit_message);
		EditText editText2 = (EditText) findViewById(R.id.edit_message2);
		String message1 = editText1.getText().toString();
		String message2 = editText2.getText().toString();
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Runnable worker = new WorkerThread("http://byronyi-diet.appspot.com/sign",
 				"POST", "content=test+from+Android+by+Tony&guestbookName=default");
 		executor.execute(worker);
 		executor.shutdown();
 		while (!executor.isTerminated())
 			/* busy waiting */;
 		Log.d("DEBUG","Finished all threads");
		
		String message = message1+" "+message2;
		
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
}
