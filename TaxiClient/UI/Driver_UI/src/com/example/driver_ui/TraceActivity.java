package com.example.driver_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TraceActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);
		
		Intent intent = getIntent();
		String msg = intent.getStringExtra("customer_list_info");
		
		TextView textView = (TextView)findViewById(R.id.textView1);
		textView.setText(msg);
		
	}
	
	public void decline(View view){
		finish();
	}
}
