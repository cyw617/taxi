package comp3111h.anytaxi.customer;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatActivity extends Activity
{
	static String host = "143.89.168.85";
	static int port = 4578;

	private Button submitButton;

	private EditText fromText;
	private EditText toText;
	private EditText whereText;
	private EditText whenText;
	private EditText capacityText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);

		fromText = (EditText) findViewById(R.id.fromText);
		toText = (EditText) findViewById(R.id.toText);
		whereText = (EditText) findViewById(R.id.whereText);
		whenText = (EditText) findViewById(R.id.whenText);
		capacityText = (EditText) findViewById(R.id.capacityText);

		submitButton = (Button) findViewById(R.id.submitbutton);

		// submitText = (EditText) findViewById(R.id.createText);

		// showlist = (Button) findViewById(R.id.showlist);

		submitButton.setOnClickListener(submitButtonListener);
		// refreshButton.setOnClickListener(showClientListListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private OnClickListener submitButtonListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			showClientList show = new showClientList();
			show.execute(); // pass selected Button to submitGuess
		} // end method onClick
	};

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

				String f = fromText.getText().toString();
				String t = toText.getText().toString();
				String whereT = whereText.getText().toString();
				String whenT = whenText.getText().toString();
				String cT = capacityText.getText().toString();

				String msg = "From:   " + f + "_" + "To:   " + t + "_"
						+ "Where:   " + whereT + "_" + "When:   " + whenT + "_"
						+ "capacity:   " + cT + "_";

				networkOutput.println("create");
				networkOutput.println(msg);

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
			// showlist.setText(feedback);

			Toast.makeText(submitButton.getContext(), "submit successfully",
					Toast.LENGTH_LONG).show();

		}
	}

}
