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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This shows how to listen to some {@link GoogleMap} events.
 */
// public class EventsDemoActivity extends FragmentActivity
// implements OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener
// {

public class EventsDemoActivity extends FragmentActivity implements
		OnMapClickListener
{

	private GoogleMap mMap;
	// private TextView mTapTextView;
	// private TextView mCameraTextView;
	private TextView showMessageOnGmapEditText_1;
	private TextView showMessageOnGmapEditText_2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_demo);

		Intent intent = getIntent();
		String message = intent.getStringExtra("EXTRA_MESSAGE");

		// mTapTextView = (TextView) findViewById(R.id.tap_text);
		// mCameraTextView = (TextView) findViewById(R.id.camera_text);
		showMessageOnGmapEditText_1 = (TextView) findViewById(R.id.showMessage1);
		showMessageOnGmapEditText_2 = (TextView) findViewById(R.id.showMessage2);
		// showMessageOnGmapEditText.setText(message);

		setUpMapIfNeeded();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded()
	{
		if (mMap == null)
		{
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null)
			{
				setUpMap();
			}
		}
	}

	private void setUpMap()
	{
		mMap.setOnMapClickListener(this);
		// mMap.setOnMapLongClickListener(this);
		// mMap.setOnCameraChangeListener(this);
	}

	@Override
	public void onMapClick(LatLng point)
	{
		// mTapTextView.setText("tapped, point=" + point);
		showMessageOnGmapEditText_1.setText("latitude:\n" + point.latitude);
		showMessageOnGmapEditText_2.setText("longitude:\n" + point.longitude);
	}

	// @Override
	// public void onMapLongClick(LatLng point) {
	// mTapTextView.setText("long pressed, point=" + point);
	// }

	// @Override
	// public void onCameraChange(final CameraPosition position) {
	// mCameraTextView.setText(position.toString());
	// }
}
