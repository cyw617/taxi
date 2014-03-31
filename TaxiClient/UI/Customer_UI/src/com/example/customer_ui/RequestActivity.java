package com.example.customer_ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;

public class RequestActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
	}
	
	private void showDialog(String message) {
		  new AlertDialog.Builder(this)
		      .setMessage(message)
		      .setPositiveButton(android.R.string.ok,
		          new DialogInterface.OnClickListener() {
		    	  
		    	    @Override
		            public void onClick(DialogInterface dialog, int id) {
		    	    	Intent intent = new Intent(RequestActivity.this, TrackingActivity.class);
		    			startActivity(intent);
		            }
		          })
		          .setNegativeButton(android.R.string.cancel, 
		        		  new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								
							}
						}).show();
		}
	
	public void MoreOption(View view) {
		Intent intent = new Intent(this, MoreOption.class);
		startActivity(intent);
	}
	
	public void Request(View view){
		String x = ((EditText)findViewById(R.id.editText1)).getText().toString();
		String y = ((EditText)findViewById(R.id.editText2)).getText().toString();
		String des = ((EditText)findViewById(R.id.editText3)).getText().toString();
		showDialog("This will send to sever "+x+" "+y+" "+des);
	}
	
	public void logout(View view){
		finish();
	}
}
