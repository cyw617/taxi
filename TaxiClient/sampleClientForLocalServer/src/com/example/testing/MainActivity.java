package com.example.testing;

import org.json.JSONObject;

import com.google.gson.Gson;

//import org.json.simple.JSONValue;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private Button sendLocButton;
	private Button getCustomerListButton;
	private Button deleteButton;

	private TextView tv;
	private EditText input1;
	private EditText input2;
	private EditText customerId;
	private EditText notes;
	private EditText deleteText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendLocButton = (Button) findViewById(R.id.sendLocation);
		getCustomerListButton = (Button) findViewById(R.id.getCustomer);
		deleteButton = (Button) findViewById(R.id.deleteButton);

		tv = (TextView) findViewById(R.id.textView1);

		input1 = (EditText) findViewById(R.id.lon);
		input2 = (EditText) findViewById(R.id.lat);
		customerId = (EditText) findViewById(R.id.CustomerId);
		notes = (EditText) findViewById(R.id.notes);
		deleteText = (EditText) findViewById(R.id.deleteText);

		sendLocButton.setOnClickListener(sendLocButtonListener);
//		getCustomerListButton.setOnClickListener(showClientListListener);
//		deleteButton.setOnClickListener(deleteButtonListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private OnClickListener sendLocButtonListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			insertTask client = new insertTask();
			client.execute(); // pass selected Button to submitGuess
		} // end method onClick
	};

//	private OnClickListener showClientListListener = new OnClickListener()
//	{
//		@Override
//		public void onClick(View v)
//		{
//			showClientList show = new showClientList();
//			show.execute(); // pass selected Button to submitGuess
//		} // end method onClick
//	};

//	private OnClickListener deleteButtonListener = new OnClickListener()
//	{
//		@Override
//		public void onClick(View v)
//		{
//			deleteTask client = new deleteTask();
//			client.execute(); // pass selected Button to submitGuess
//		} // end method onClick
//	};

	private class insertTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			try
			{
				String host = "143.89.168.85";
				int port = 4568;

				Socket Clientsocket;

				String feedback = "Error!";

				JSONObject obj = new JSONObject();

				String lon = input1.getText().toString();
				String lat = input2.getText().toString();
				String Id = customerId.getText().toString();
				String note = notes.getText().toString();

				obj.put("lat", lat);
				obj.put("lon", lon);
				obj.put("Id", Id);
				obj.put("note", note);

				String str = obj.toString();

				Clientsocket = new Socket(host, port);

				PrintWriter networkOutput = new PrintWriter(
						Clientsocket.getOutputStream(), true);

				Scanner networkInput = new Scanner(
						Clientsocket.getInputStream());

				networkOutput.println("sendLoc");

				// String flag= networkInput.nextLine();

				networkOutput.println(str);

				feedback = networkInput.nextLine();

				Clientsocket.close();

				return feedback;

			} catch (Exception e)
			{
				return "Error!!!";
			}

		}

		@Override
		protected void onPostExecute(String result)
		{
			// tv.setText(result);
			Toast.makeText(sendLocButton.getContext(), result,
					Toast.LENGTH_LONG).show();

		}

	}

//	private class showClientList extends AsyncTask<String, Void, String>
//	{
//		@Override
//		protected String doInBackground(String... arg0)
//		{
//			try
//			{
//				String host = "143.89.168.85";
//				int port = 4568;
//
//				Socket Clientsocket;
//
//				String feedback = "Error";
//
//				Clientsocket = new Socket(host, port);
//
//				Scanner networkInput = new Scanner(
//						Clientsocket.getInputStream());
//
//				PrintWriter networkOutput = new PrintWriter(
//						Clientsocket.getOutputStream(), true);
//
//				networkOutput.println("getCusList");
//
//				feedback = networkInput.nextLine();
//
//				Clientsocket.close();
//
//				Gson gson = new Gson();
//				String[] clientArray = feedback.split(" ");
//
//				String temp = "";
//				for (String o : clientArray)
//				{
//					Client c = gson.fromJson(o, Client.class);
//					temp = temp + c.toString() + "\n";
//				}
//
//				return temp;
//
//			} catch (Exception e)
//			{
//				return "Error!!!!";
//			}
//
//		}
//
//		@Override
//		protected void onPostExecute(String feedback)
//		{
//			tv.setText(feedback);
//
//			// Toast.makeText(send.getContext(), result,
//			// Toast.LENGTH_LONG).show();
//
//		}
//	}
//
//	private class deleteTask extends AsyncTask<String, Void, String>
//	{
//		@Override
//		protected String doInBackground(String... arg0)
//		{
//			try
//			{
//				String host = "143.89.168.85";
//				int port = 4568;
//
//				Socket Clientsocket;
//
//				String feedback = "Error";
//
//				Clientsocket = new Socket(host, port);
//
//				PrintWriter networkOutput = new PrintWriter(
//						Clientsocket.getOutputStream(), true);
//
//				networkOutput.println("delete");
//
//				String deleteID = deleteText.getText().toString();
//
//				networkOutput.println(deleteID);
//
//				Clientsocket.close();
//
//				feedback="delete successfully!";
//				
//				return feedback;
//
//			} catch (Exception e)
//			{
//				return "Error!!!!";
//			}
//
//		}
//
//		@Override
//		protected void onPostExecute(String feedback)
//		{
//			Toast.makeText(deleteButton.getContext(), feedback,
//					Toast.LENGTH_LONG).show();
//			// tv.setText(feedback);
//
//			// Toast.makeText(send.getContext(), result,
//			// Toast.LENGTH_LONG).show();
//		}
//	}

}
