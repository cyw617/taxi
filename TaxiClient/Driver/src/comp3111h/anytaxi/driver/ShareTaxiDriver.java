package comp3111h.anytaxi.driver;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.Toast;

public class ShareTaxiDriver extends Activity
{
	static String host = "143.89.168.85";
	static int port = 4578;

	private Button refreshButton;
	private Button showlist;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharetaxidriver);

		refreshButton = (Button) findViewById(R.id.refresh);

		showlist = (Button) findViewById(R.id.showlist);

		refreshButton.setOnClickListener(showClientListListener);
		showlist.setOnClickListener(showlistListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private OnClickListener showlistListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			join();
		}
	};

	private void join()
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(ShareTaxiDriver.this);
		builder.setTitle("Are you sure???"); // title bar string
		builder.setMessage("do you want to pick up this order "); // message to
																	// display

		// provide an OK button that simply dismisses the dialog
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int button)
			{
				new joinTask().execute();
			} // end method onClick
		} // end anonymous inner class
		); // end call to method setPositiveButton

		builder.show(); // display the Dialog
	} // end method deleteContact

	private OnClickListener showClientListListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			showClientList show = new showClientList();
			show.execute(); // pass selected Button to submitGuess
		} // end method onClick
	};

	public class joinTask extends AsyncTask<String, Void, String>
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

				networkOutput.println("pick");

				// feedback = networkInput.nextLine();

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

			Toast.makeText(showlist.getContext(), "pick up order successfully",
					Toast.LENGTH_LONG).show();

		}
	}

	private class showClientList extends AsyncTask<String, Void, String>
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

				networkOutput.println("refresh");

				feedback = networkInput.nextLine();

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
			String[] result = feedback.split("_");
			String p = "";
			for (int i = 0; i < result.length; i++)
			{
				if (i == 5)
					p = p + "\n"
							+ "--------------------------------------------"
							+ "\n";

				if (i == 6)
					p = p + "\n";

				p = p + result[i] + "\n";
			}

			showlist.setText(p);

			// Toast.makeText(showlist.getContext(), feedback,
			// Toast.LENGTH_LONG).show();

		}
	}

}
