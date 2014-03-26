package com.example.customer_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class RequestActivity extends Activity{
	
	PopupWindow popup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
	}
	
	private void initPopWindow() {
		 if (popup == null) {
			 popup = new PopupWindow(getLayoutInflater().inflate(R.layout.pop, null),
					 LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 }
		 if (popup.isShowing()) {
			 popup.dismiss();
		 }
	}
	public void MoreOption(View view) {
		Intent intent = new Intent(this, MoreOption.class);
		startActivity(intent);
	}
	
	public void Request(View view){
		initPopWindow();
		popup.showAtLocation(view,Gravity.CENTER, 0, 0);
	}
	
	public void Dismiss(View view){
		popup.dismiss();
	}
}
