package comp3111h.anytaxi.driver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TraceActivity extends Activity{
	
	int index; // index of item onclick in customer list
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);
		
		Intent intent = getIntent();
		String msg = intent.getStringExtra("customer_list_info");
		index = intent.getIntExtra("customer_list_id", 0);
		//test cases needed
		
		
		TextView textView = (TextView)findViewById(R.id.textView1);
		textView.setText(msg);
		
	}
	
	public void accept(View view){
		// block cloud message
		Intent intent = new Intent(TraceActivity.this, TrackingActivity.class);
		startActivity(intent);
		CustomerListActivity.removeItemInList(index); //test cases needed
		finish();
		
	}
	
	public void decline(View view){
		CustomerListActivity.removeItemInList(index); //test cases needed
		finish();
	}
}
