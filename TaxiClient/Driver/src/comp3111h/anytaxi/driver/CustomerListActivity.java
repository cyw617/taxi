package comp3111h.anytaxi.driver;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class CustomerListActivity extends ActionBarActivity{
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
		    Intent intent = new Intent(this, SettingsActivity.class);
	        startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    
    @Override
    public void onBackPressed() {
        exit();
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
	
	public static void removeItemInList(int index){
		if(index>=0 && index<strArr.size() )
		strArr.remove(index);
		arrAdapter.notifyDataSetChanged();
	}
    
    private void exit() {
        new AlertDialog.Builder(this)
        .setMessage(getString(R.string.quit_Message))
        .setPositiveButton(getString(R.string.quit_Positive),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    finish();
                }
            })
        .setNegativeButton(getString(R.string.quit_Negative), null)
        .show();
    }
}
