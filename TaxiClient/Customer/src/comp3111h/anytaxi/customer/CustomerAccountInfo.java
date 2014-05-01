package comp3111h.anytaxi.customer;

import android.accounts.AccountManager;
import android.content.Intent;

import com.appspot.hk_taxi.anyTaxi.AnyTaxi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;

class CustomerAccountInfo {

	/*Variables for transmitting msg to server*/
	static AnyTaxi endpoint;
	static String accountName;
	static GoogleAccountCredential credential;
	static final String WEB_CLIENT_ID = "1072316261853-u0gafkut9f919bau91gh9bgjb9555hh2.apps.googleusercontent.com";
	static final int REQUEST_ACCOUNT_PICKER = 1; 
	/*Variables for transmitting msg to server*/
	
	 static void setAccountInfo(Intent myIntent)
	 {
		 CustomerAccountInfo.accountName = myIntent.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
         if (CustomerAccountInfo.accountName != null) {
        	 CustomerAccountInfo.credential.setSelectedAccountName(CustomerAccountInfo.accountName);
        	 
        	 AnyTaxi.Builder endpointBuilder = new AnyTaxi.Builder(
                     AndroidHttp.newCompatibleTransport(),
                     new JacksonFactory(),
                     null);
        	 CustomerAccountInfo.endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
         }
	 }

}
