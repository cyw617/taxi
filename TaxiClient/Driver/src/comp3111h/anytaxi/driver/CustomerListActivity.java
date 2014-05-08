package comp3111h.anytaxi.driver;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CustomerListActivity extends ActionBarActivity
{
	protected static final String TAG = "CustomerListActivity";
	public ListView listView;
	public static ArrayList<String> strArr;
	public static ArrayAdapter<String> arrAdapter;
	Bundle customerInfo;
	private Menu mOptionsMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_list);

		customerInfo = new Bundle();
		
		listView = (ListView) findViewById(R.id.listView1);
		strArr = new ArrayList<String>();
		// arrAdapter = new ArrayAdapter<String>(getApplicationContext(),
		// android.R.layout.simple_list_item_1, strArr);
		arrAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.customer_list_view_layout, strArr);

		listView.setEmptyView(findViewById(android.R.id.empty));
		listView.setAdapter(arrAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				Intent intent = new Intent(CustomerListActivity.this,
						TraceActivity.class);
				String LocDes = (String) parent.getItemAtPosition(position);
				/*
				 * Set up the string delimiter as "\t" The lat and lnt are
				 * separated by a "\t"
				 */
				String delims = "[\t]+";
				String[] tokens = LocDes.split(delims);
				String Loc = tokens[0];
				String Des = tokens[1];
				float[] myLatLng = customerInfo.getFloatArray(Utils.CUSTOMER_LATLNG);
				intent.putExtra(Utils.CUSTOMER_LOC, Loc);
				intent.putExtra(Utils.CUSTOMER_DES, Des);
				intent.putExtra(Utils.CUSTOMER_LATLNG, myLatLng);
				intent.putExtra(Utils.CUSTOMER_LIST_ID,id);
				
				intent.putExtra(Utils.TRASACTION_ID, customerInfo.getLong(Utils.TRASACTION_ID));
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}

		if (id == R.id.action_driver)
		{
			Intent intent = new Intent(this, ShareTaxiDriver.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed()
	{
		exit();
	}

	public void removeItem()
	{
		if (strArr.isEmpty() != true)
		{
			strArr.remove(0);
			arrAdapter.notifyDataSetChanged();
		}
	}

	public void clearItems()
	{
		strArr.clear();
		arrAdapter.notifyDataSetChanged();
	}

	public static void removeItemInList(int index)
	{
		if (index >= 0 && index < strArr.size())
			strArr.remove(index);
		arrAdapter.notifyDataSetChanged();
	}

	public int numItemsInStrArr()
	{ // for test
		return strArr.size();
	}

	private void exit()
	{
		new AlertDialog.Builder(this)
				.setMessage(getString(R.string.quit_Message))
				.setPositiveButton(getString(R.string.quit_Positive),
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								moveTaskToBack(true);
								finish();
							}
						})
				.setNegativeButton(getString(R.string.quit_Negative), null)
				.show();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.e("onNewIntent", "Start onNewIntent");
		String gcm = intent.getStringExtra(GCMIntentService.GCM_INTENT);
		if (gcm == null) {
			return;
		}
		Long transactionId = Long.parseLong(intent
				.getStringExtra(GCMIntentService.TRANSACTION_ID));
		Float latitude = Float.parseFloat(intent
				.getStringExtra(GCMIntentService.LATITUDE));
		Float longitude = Float.parseFloat(intent
				.getStringExtra(GCMIntentService.LONGITUDE));
		String customerLoc = intent.getStringExtra(GCMIntentService.CUR_LOC_STR);
		String customerDes = intent.getStringExtra(GCMIntentService.DEST_LOC_STR);

		float[] fa = {latitude, longitude};
		customerInfo.putFloatArray(Utils.CUSTOMER_LATLNG, fa);
		customerInfo.putLong(Utils.TRASACTION_ID, transactionId);
		// Update the customer List
		strArr.add(customerLoc + "\t" + customerDes);
		arrAdapter.notifyDataSetChanged();
	}
}
