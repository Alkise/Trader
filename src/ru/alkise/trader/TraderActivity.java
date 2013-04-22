package ru.alkise.trader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.model.Manager;
import ru.alkise.trader.model.Organization;
import ru.alkise.trader.sql.SQLConnection;
import ru.alkise.trader.task.SearchClientsTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ru.alkise.trader.model.*;
import android.widget.*;
import android.view.*;
import android.text.*;
import android.app.*;
import android.content.*;
import ru.alkise.trader.task.ConnectionTask;

public class TraderActivity extends Activity {
	private ConnectionTask connectionTask;
	private DataLoaderTask dataLoaderTask;
	private SearchClientsTask searchClientTask;
	private ProgressDialog loadingDialog;
	private ProgressDialog searchingDialog;
	private Spinner organizationSpinner;
	private Spinner managerSpinner;
	private Button findClientsBtn;
	private Button addPositionBtn;
	private Button uploadTo1CBtn;
	private EditText clientField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trader);

		organizationSpinner = (Spinner) findViewById(R.id.organizationSpinner);
		managerSpinner = (Spinner) findViewById(R.id.managerSpinner);

		loadingDialog = new ProgressDialog(this);
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(true);
		loadingDialog.setMessage(getString(R.string.connecting));

		searchingDialog = new ProgressDialog(this);
		searchingDialog.setIndeterminate(true);
		searchingDialog.setCancelable(true);
		searchingDialog.setMessage(getString(R.string.searching));

		connectionTask = new ConnectionTask();
		dataLoaderTask = new DataLoaderTask();

		findClientsBtn = (Button) findViewById(R.id.findClientsBtn);

		addPositionBtn = (Button) findViewById(R.id.btnAddPosition);
		uploadTo1CBtn = (Button) findViewById(R.id.btnUpload);

		clientField = (EditText) findViewById(R.id.clientField);
		clientField.addTextChangedListener(new TextWatcher(){
			private int currentLength;
			
			public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				currentLength = p1.length();
			}

			public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
			{
				if ((p1.length() == 3) && (currentLength == 2)) {
					findClientsBtn.setEnabled(true);
				} else if ((p1.length() == 2) && (currentLength == 3)) {
					findClientsBtn.setEnabled(false);
				}
			}

			public void afterTextChanged(Editable p1)
			{
				// TODO: Implement this method
			}			
		});

		uploadTo1CBtn.setEnabled(false);
		loadingDialog.show();
		connectionTask.execute("192.168.0.202", "1433", "trade2005", "test",
				"test", this, loadingDialog);
		dataLoaderTask.execute();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onStop() {
		try {
			SQLConnection.INSTANCE.closeConnection();
		} catch (Exception e) {

		}
		super.onStop();
	}

//	protected class ConnectionTask extends AsyncTask<String, Object, Object> {
//		private Connection connection;
//
//		@Override
//		protected Connection doInBackground(String... args) {
//			try {
//				SQLConnection.INSTANCE.createConnection(args[0], args[1],
//						args[2], args[3], args[4]);
//				connection = SQLConnection.INSTANCE.getConnection();
//
//			} catch (Exception e) {
//				Log.e("Connection Task", e.getMessage());
//			}
//			return connection;
//		}
//
//		@Override
//		protected void onPostExecute(Object result) {
//			loadingDialog.setMessage(getString(R.string.dataloading));
//
//			super.onPostExecute(result);
//		}
//
//		@Override
//		protected void onPreExecute() {
//			loadingDialog.show();
//		}
//	}
//
	protected class DataLoaderTask extends AsyncTask<Object, Object, Object> {
		private List<Organization> organizations;
		private ArrayAdapter<Organization> organizationAdapter;

		private List<Manager> activeManagers;
		private ArrayAdapter<Manager> managerAdapter;

		private Connection connection;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				// Loading organizations
				connection = (Connection) connectionTask.get();
				Statement stmt = connection.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT ID, CODE, DESCR FROM SC308");

				organizations = new ArrayList<Organization>();
				while (rs.next()) {
					organizations.add(new Organization(rs.getString(1), rs
							.getString(2), rs.getString(3)));
				}
				organizationAdapter = new ArrayAdapter<Organization>(
						getApplication(), android.R.layout.simple_list_item_1,
						organizations) {

					@Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						View view = super.getDropDownView(position,
								convertView, parent);
						TextView textView = (TextView) view
								.findViewById(android.R.id.text1);
						textView.setTextColor(Color.DKGRAY);
						return view;
					}

					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
						View view = super
								.getView(position, convertView, parent);
						TextView textView = (TextView) view
								.findViewById(android.R.id.text1);
						textView.setTextColor(Color.BLACK);
						return view;
					}

				};

				// Loading managers
				PreparedStatement pstmt = connection
						.prepareStatement("SELECT ID, CODE, DESCR FROM SC377 WHERE SP1860 = ? ORDER BY DESCR");
				pstmt.setInt(1, 1);

				rs = pstmt.executeQuery();

				activeManagers = new ArrayList<Manager>();
				while (rs.next()) {
					activeManagers.add(new Manager(rs.getString(1), rs
							.getString(2), rs.getString(3)));
				}
				managerAdapter = new ArrayAdapter<Manager>(getApplication(),
						android.R.layout.simple_list_item_1, activeManagers) {

					@Override
					public View getDropDownView(int position, View convertView,
							ViewGroup parent) {
						View view = super.getDropDownView(position,
								convertView, parent);
						TextView textView = (TextView) view
								.findViewById(android.R.id.text1);
						textView.setTextColor(Color.DKGRAY);
						return view;
					}

					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
						View view = super
								.getView(position, convertView, parent);
						TextView textView = (TextView) view
								.findViewById(android.R.id.text1);
						textView.setTextColor(Color.BLACK);
						return view;
					}

				};
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			organizationSpinner.setAdapter(organizationAdapter);
			organizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View arg1,
											   int selectedPosition, long arg3) {
					Order.INSTANCE.setOrganization((Organization)adapter.getSelectedItem());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			
			managerSpinner.setAdapter(managerAdapter);
			managerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View arg1,
						int selectedPosition, long arg3) {
					Order.INSTANCE.setManager((Manager)adapter.getSelectedItem());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			loadingDialog.dismiss();
			super.onPostExecute(result);
		}
	}
	
	public void showOrder(View view) {
		StringBuilder sb = new StringBuilder();
		sb.append(Order.INSTANCE.getOrganization().toString());
		sb.append('\n');
		sb.append(Order.INSTANCE.getManager().toString());
		sb.append('\n');
		sb.append(Order.INSTANCE.getClient().toString());
		
		Toast.makeText(getApplication(), sb.toString(), Toast.LENGTH_LONG).show();
	}
	
	public void searchRemains(View view) {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View remainsView = layoutInflater.inflate(R.layout.remains, null);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(remainsView);
		alertDialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
				}

			
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface p1, int p2)
				{
					p1.cancel();
				}

		}).setCancelable(false);
		
		AlertDialog remainsDialog = alertDialogBuilder.create();
		
		remainsDialog.show();
	}

	public void findClients(View v) {
		searchClientTask = new SearchClientsTask();
		searchingDialog.show();
		try {
			searchClientTask.execute(connectionTask.get(),
					clientField.getText(), searchingDialog, this);
		} catch (Exception e) {
			Log.e("findClients", e.getMessage());
		}
	}
}
