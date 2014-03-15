/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import javax.security.auth.PrivateCredentialPermission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class MainActivity extends Activity
{
	private EditText editText1;
	private EditText editText2;
	private Button openMapButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		openMapButton=(Button)findViewById(R.id.openMap);
		openMapButton.setOnClickListener(openMapButtonClicked);
		
		
	}

	OnClickListener openMapButtonClicked = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(MainActivity.this, EventsDemoActivity.class);

			editText1 = (EditText) findViewById(R.id.edit_message);
			editText2 = (EditText) findViewById(R.id.edit_message2);
			String message1 = editText1.getText().toString();
			String message2 = editText2.getText().toString();

			String message = message1 + " " + message2;

			intent.putExtra("EXTRA_MESSAGE", message);
			startActivity(intent);
		}
	};

}
