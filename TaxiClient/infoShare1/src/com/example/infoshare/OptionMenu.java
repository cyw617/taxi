package com.example.infoshare;



import android.util.Log;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OptionMenu extends Activity
{
	private String TAG = "OptionMenu";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option_menu);
		
		Log.i(TAG, "Activity: onCreate");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_menu, menu);
		return true;
	}

}
