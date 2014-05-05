package comp3111h.anytaxi.customer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class IndexActivity extends Activity
{
	private final static String HOST = "143.89.168.85";
	private final static int PORT = 4578;

	private Button callButton;
	private Button createButton;
	private Button refreshButton;
	private Button showlist;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		createButton = (Button) findViewById(R.id.create);
		refreshButton = (Button) findViewById(R.id.refresh);
		callButton = (Button) findViewById(R.id.callButton);

		showlist = (Button) findViewById(R.id.showlist);

		createButton.setOnClickListener(createButtonListener);
		refreshButton.setOnClickListener(showClientListListener);
		callButton.setOnClickListener(callListener);
		showlist.setOnClickListener(showlistListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// create a new Intent to launch the AddEditContact Activity
		Intent addNewContact = new Intent(IndexActivity.this, DeleteOrder.class);
		startActivity(addNewContact); // start the AddEditContact Activity
		return super.onOptionsItemSelected(item); // call super's method
	}

	private OnClickListener callListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			new callTask().execute();
		}
	};

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

		AlertDialog.Builder builder = new AlertDialog.Builder(
				IndexActivity.this);
		builder.setTitle("Are you sure???"); // title bar string
		builder.setMessage("do you want to share taxi"); // message to display

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

		try
		{
			builder.show();
		} catch (Exception e)
		{
			;// display the Dialog
		}
	} // end method deleteContact

	private OnClickListener createButtonListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent TestGroupMessageTable = new Intent(
					IndexActivity.this.getApplicationContext(),
					CreateActivity.class);
			startActivity(TestGroupMessageTable);
		}
	};

	private OnClickListener showClientListListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			showClientList show = new showClientList();
			show.execute(); // pass selected Button to submitGuess
		} // end method onClick
	};

	private class callTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			try
			{

				Socket Clientsocket;

				String feedback = "Error";

				Clientsocket = new Socket(HOST, PORT);

				Scanner networkInput = new Scanner(
						Clientsocket.getInputStream());

				PrintWriter networkOutput = new PrintWriter(
						Clientsocket.getOutputStream(), true);

				networkOutput.println("call");

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

			Toast.makeText(showlist.getContext(),
					"call successfully,waiting for driver!", Toast.LENGTH_LONG)
					.show();

		}
	}

	private class joinTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			try
			{

				Socket Clientsocket;

				String feedback = "Error";

				Clientsocket = new Socket(HOST, PORT);

				Scanner networkInput = new Scanner(
						Clientsocket.getInputStream());

				PrintWriter networkOutput = new PrintWriter(
						Clientsocket.getOutputStream(), true);

				networkOutput.println("join");

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

			Toast.makeText(showlist.getContext(), "join successfully",
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

				Clientsocket = new Socket(HOST, PORT);

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

		}
	}

}
