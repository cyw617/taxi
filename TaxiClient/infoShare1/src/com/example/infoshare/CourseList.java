package com.example.infoshare;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CourseList extends Activity
{
	private int num;
	Button[] btnWord; 
	LinearLayout linear;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_list);

		num = 30;
		btnWord = new Button[num];
		
		linear = (LinearLayout) findViewById(R.id.LinearLayout1);
		for (int i = 0; i < btnWord.length; i++)
		{
			btnWord[i] = new Button(this);
			btnWord[i].setHeight(50);
			btnWord[i].setWidth(50);
			btnWord[i].setTag(i);
			btnWord[i].setOnClickListener(btnClicked);
			btnWord[i].setText(Integer.toString(i));
			linear.addView(btnWord[i]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course_list, menu);
		return true;
	}

	OnClickListener btnClicked = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Object tag = v.getTag();
			Toast.makeText(getApplicationContext(), "clicked button",
					Toast.LENGTH_SHORT).show();
		}

	};

}
