package com.example.infoshare;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Test extends Activity
{
	int num=10;
	Button[] btnWord = new Button[num];
	 LinearLayout linear;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_test);
	  test();
	 }
	 private void test() {  
	  linear = (LinearLayout) findViewById(R.id.LinearLayout1);
	  for (int i = 0; i < btnWord.length; i++) {
	   btnWord[i] = new Button(this);
	   btnWord[i].setHeight(50);
	   btnWord[i].setWidth(50);
	   btnWord[i].setTag(i);
	   btnWord[i].setOnClickListener(btnClicked);
	   linear.addView(btnWord[i]);
	  }
	 }
	 OnClickListener btnClicked = new OnClickListener() {
		 
	  @Override
	  public void onClick(View v) {
	   Object tag = v.getTag();
	   Toast.makeText(getApplicationContext(), "clicked button", Toast.LENGTH_SHORT).show();
	  }

	
	 };

}
