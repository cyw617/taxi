package com.example.infoshare;



import android.util.Log;

import android.view.View.OnClickListener;
import android.util.Log;

import android.os.Bundle;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity
{
	
	private String TAG = "MainActivity";
	private Button signUpButton;
	private Button creatButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Log.i(TAG, "Activity:	onCreate()");
		signUpButton = (Button) findViewById(R.id.signup);
		signUpButton.setOnClickListener(signupClickListener);
		creatButton =(Button) findViewById(R.id.login);
		creatButton.setOnClickListener(loginClickListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	OnClickListener signupClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(MainActivity.this, CourseList.class);

			// intent.putExtra("EXTRA_MESSAGE", message);
			startActivity(intent);
		}

	};
	
	OnClickListener loginClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(MainActivity.this, OptionMenu.class);

			// intent.putExtra("EXTRA_MESSAGE", message);
			startActivity(intent);
		}

	};

}
