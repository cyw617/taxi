package comp3111h.anytaxi.customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RequestActivity extends ActionBarActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
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
