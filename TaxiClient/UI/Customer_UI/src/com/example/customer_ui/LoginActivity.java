package com.example.customer_ui;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
	}
	
	public void login(View view) {
		Intent intent = new Intent(this, RequestActivity.class);
		startActivity(intent);
		
	}

	

}