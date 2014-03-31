package com.example.driver_ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TrackingActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
	}
	
	public void finishActivity(View view){
		finish();
	}
}
