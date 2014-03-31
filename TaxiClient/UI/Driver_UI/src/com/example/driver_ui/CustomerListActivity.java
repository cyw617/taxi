package com.example.driver_ui;

import java.util.ArrayList;
import java.util.Map;

import com.example.driver_ui.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class CustomerListActivity extends Activity{
	
	private EditText editText;
	private ListView listView;
	private static ArrayList<String> strArr;
	private static ArrayAdapter<String> arrAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_list);
		
		editText = (EditText) findViewById(R.id.editText1);
		listView = (ListView) findViewById(R.id.listView1);
		strArr = new ArrayList<String>();
		//arrAdapter = new ArrayAdapter<String>(getApplicationContext(), 
			//	android.R.layout.simple_list_item_1, strArr);
		arrAdapter = new ArrayAdapter<String>(getApplicationContext(), 
				R.layout.customer_list_view_layout, strArr);
		
		listView.setAdapter(arrAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				
				Intent intent = new Intent(CustomerListActivity.this, TraceActivity.class);
				String msg=(String)parent.getItemAtPosition(position);
				intent.putExtra("customer_list_info", msg);
				intent.putExtra("customer_list_id", position);
				startActivity(intent);
			}
		});
	}
	
	public void addItem(View view){
		strArr.add(editText.getText().toString());
		arrAdapter.notifyDataSetChanged();
	}
	
	public void removeItem(View view){
		if(strArr.isEmpty()!=true){
			strArr.remove(0);
			arrAdapter.notifyDataSetChanged();
		}
	}
	
	public void clearItems(View view){
		strArr.clear();
		arrAdapter.notifyDataSetChanged();
	}
	
	public void logout(View view){
		finish();
	}
	
	public static void removeItemInList(int index){
		if(index>=0 && index<strArr.size() )
		strArr.remove(index);
		arrAdapter.notifyDataSetChanged();
	}
}
