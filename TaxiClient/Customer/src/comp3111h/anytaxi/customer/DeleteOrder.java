package comp3111h.anytaxi.customer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DeleteOrder extends Activity
{
	static String host = "143.89.168.85";
	static int port = 4578;

	private Button deleteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);

		deleteButton = (Button) findViewById(R.id.delete);

		deleteButton.setOnClickListener(deleteListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private OnClickListener deleteListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			deleteTask show = new deleteTask();
			show.execute(); // pass selected Button to submitGuess
		} // end method onClick
	};

	private class deleteTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			try
			{

				Socket Clientsocket;

				String feedback = "Error";

				Clientsocket = new Socket(host, port);

				Scanner networkInput = new Scanner(
						Clientsocket.getInputStream());

				PrintWriter networkOutput = new PrintWriter(
						Clientsocket.getOutputStream(), true);

				networkOutput.println("delete");

				Clientsocket.close();

				return feedback;

			} catch (Exception e)
			{
				return "showClientListError!!!!";
			}

		}

		@Override
		protected void onPostExecute(String feedback)
		{

			Toast.makeText(deleteButton.getContext(),
					"delete successfully,waiting for driver!",
					Toast.LENGTH_LONG).show();

		}
	}

}
