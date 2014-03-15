package comp3111h.anytaxi.customer;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		
		String serverCon = excutePost("http://byronyi-diet.appspot.com/sign" , "content=test+from+Android&guestbookName=default");
		String message = message1+" "+message2+" "+serverCon;
		
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	public static String excutePost(String targetURL, String urlParameters){
		try {
	        URL url = new URL(targetURL); 
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
	        connection.setDoOutput(true);
	        connection.setDoInput(true);
	        connection.setInstanceFollowRedirects(false); 
	        connection.setRequestMethod("POST"); 
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
	        connection.setRequestProperty("charset", "utf-8");
	        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	        connection.setUseCaches (false);

	        connection.connect();

	        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
	        wr.writeBytes(urlParameters);
	        wr.flush();
	        wr.close();
	        
	        connection.disconnect();
	        return "success"; 
	    } catch (Exception e) {
	      System.out.println("Exception");
	      e.printStackTrace();
	      return "failed";
	    }
	}
}
