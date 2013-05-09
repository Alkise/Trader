package ru.alkise.trader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.adapter.ArrayPositonAdapter;
import ru.alkise.trader.model.Client;
import ru.alkise.trader.model.ClientType;
import ru.alkise.trader.model.Manager;
import ru.alkise.trader.model.Order;
import ru.alkise.trader.model.Organization;
import ru.alkise.trader.model.SearchResults;
import ru.alkise.trader.model.Warehouse;
import ru.alkise.trader.model.Warehouses;
import ru.alkise.trader.sql.SQLConnection;
import ru.alkise.trader.task.ConnectionTask;
import ru.alkise.trader.task.SearchClientsTask;
import ru.alkise.trader.task.SearchGoodsTask;
import ru.alkise.trader.task.SearchRemainsTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TraderActivity extends Activity {
	private ConnectionTask connectionTask;
	private DataLoaderTask dataLoaderTask;
	private SearchClientsTask searchClientTask;
	private SearchRemainsTask searchRemainsTask;
	private SearchGoodsTask searchGoodsTask;
	private ProgressDialog loadingDialog;
	private ProgressDialog searchingDialog;
	private ProgressDialog searchingRemainsDialog;
	private Spinner organizationSpinner;
	private Spinner managerSpinner;
	private ListView positionsList;
	private ImageButton findClientsBtn;
	private ImageButton uploadTo1CBtn;
	private EditText clientField;
	private static Activity activity;
	private ArrayPositonAdapter positionsAdapter;
	private ArrayAdapter<ClientType> clientTypeAdapter;
	private boolean loaded;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trader);
		activity = this;

		inflater = LayoutInflater.from(this);

		organizationSpinner = (Spinner) findViewById(R.id.organizationSpinner);
		managerSpinner = (Spinner) findViewById(R.id.managerSpinner);

		positionsList = (ListView) findViewById(R.id.positionList);
		positionsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setIndeterminate(false);
		loadingDialog.setCancelable(false);
		loadingDialog.setMessage(getString(R.string.connecting));
		loadingDialog.show();

		searchingDialog = new ProgressDialog(this);
		searchingDialog.setIndeterminate(true);
		searchingDialog.setCancelable(true);
		searchingDialog.setMessage(getString(R.string.searching));

		searchingRemainsDialog = new ProgressDialog(this);
		searchingRemainsDialog.setIndeterminate(true);
		searchingRemainsDialog.setCancelable(true);
		searchingRemainsDialog.setMessage(getString(R.string.searching));

		connectionTask = new ConnectionTask();
		dataLoaderTask = new DataLoaderTask();

		findClientsBtn = (ImageButton) findViewById(R.id.findClientsBtn);

		uploadTo1CBtn = (ImageButton) findViewById(R.id.btnUpload);
		uploadTo1CBtn.setEnabled(false);

		clientField = (EditText) findViewById(R.id.clientField);

		clientField.addTextChangedListener(new TextWatcher() {
			private int currentLength;

			public void beforeTextChanged(CharSequence p1, int p2, int p3,
					int p4) {
				currentLength = p1.length();
			}

			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				if ((p1.length() == 3) && (currentLength == 2)) {
					findClientsBtn.setEnabled(true);
				} else if ((p1.length() == 2) && (currentLength == 3)) {
					findClientsBtn.setEnabled(false);
				}
			}

			public void afterTextChanged(Editable p1) {
				// TODO: Implement this method
			}
		});

		clientTypeAdapter = new ArrayAdapter<ClientType>(this,
				android.R.layout.simple_spinner_dropdown_item,
				ClientType.values());

		connectionTask.execute("192.168.0.202", "1433", "trade2005", "test",
				"test", loadingDialog);
		dataLoaderTask.execute();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (loaded) {
			loadingDialog.show();
			connectionTask = new ConnectionTask();
			connectionTask.execute("192.168.0.202", "1433", "trade2005",
					"test", "test", loadingDialog);
		} else {
			loaded = true;
		}
	}

	@Override
	protected void onStop() {
		try {
			SQLConnection.INSTANCE.closeConnection();
		} catch (Exception e) {
			Log.e("Stop trader", e.getMessage());
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		SearchResults.INSTANCE.clear();
		positionsAdapter.clear();
		super.onDestroy();
	}

	protected class DataLoaderTask extends AsyncTask<Object, Object, Object> {
		private List<Organization> organizations;
		private ArrayAdapter<Organization> organizationAdapter;

		private List<Manager> activeManagers;
		private ArrayAdapter<Manager> managerAdapter;

		private ProgressDialog dataLoadingDialog;

		private Connection connection;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				// Loading organizations
				connection = (Connection) connectionTask.get();
				Statement stmt = connection.createStatement();

				ResultSet rs = stmt
						.executeQuery("SELECT ID, CODE, DESCR FROM SC308");

				organizations = new ArrayList<Organization>();
				while (rs.next()) {
					organizations.add(new Organization(rs.getString(1), rs
							.getString(2), rs.getString(3)));
				}
				organizationAdapter = new ArrayAdapter<Organization>(
						getApplication(),
						android.R.layout.simple_spinner_dropdown_item,
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
						android.R.layout.simple_spinner_dropdown_item,
						activeManagers) {

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

				// Loading warehouses
				pstmt = connection
						.prepareStatement("SELECT ID, CODE, DESCR FROM SC12 WHERE SP2505 = ? AND ISFOLDER = ? ORDER BY DESCR ");
				pstmt.setInt(1, 0);
				pstmt.setInt(2, 2);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					Warehouses.INSTANCE.addWarehouse(new Warehouse(rs
							.getString(1), rs.getInt(2), rs.getString(3)));
				}

			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			dataLoadingDialog = new ProgressDialog(activity);
			dataLoadingDialog.setIndeterminate(false);
			dataLoadingDialog.setCancelable(false);
			dataLoadingDialog.setMessage(activity
					.getString(R.string.dataloading));
			dataLoadingDialog.show();
		}

		@Override
		protected void onPostExecute(Object result) {
			if (organizationAdapter != null) {
				organizationSpinner.setAdapter(organizationAdapter);
				organizationSpinner
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> adapter,
									View arg1, int selectedPosition, long arg3) {
								Order.INSTANCE
										.setOrganization((Organization) adapter
												.getSelectedItem());
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

				managerSpinner.setAdapter(managerAdapter);
				managerSpinner
						.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> adapter,
									View arg1, int selectedPosition, long arg3) {
								Order.INSTANCE.setManager((Manager) adapter
										.getSelectedItem());
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

				positionsAdapter = new ArrayPositonAdapter(activity, R.layout.position_layout, Order.INSTANCE.getPositions());
				positionsList.setAdapter(positionsAdapter);
			}
			dataLoadingDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	public void showMessage(String message) {
		Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
	}

	public void showWarehouses(View view) {
		Toast.makeText(getApplication(), Warehouses.INSTANCE.printWarehouses(),
				Toast.LENGTH_LONG).show();
	}

	public void showOrder(View view) {
		Toast.makeText(getApplication(), Order.INSTANCE.displayOrder(),
				Toast.LENGTH_LONG).show();
		positionsAdapter.notifyDataSetChanged();
	}

	// Add postion button click
	public void searchRemains(View view) {
		final View remainsView = inflater.inflate(R.layout.remains, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(remainsView);
		alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface p1, int p2) {
						p1.cancel();
					}

				}).setPositiveButton(getString(R.string.search),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface p1, int p2) {

						searchingRemainsDialog.show();

						try {
							if ((((RadioButton) remainsView
									.findViewById(((RadioGroup) remainsView
											.findViewById(R.id.paramGroup))
											.getCheckedRadioButtonId()))
									.getText().equals(activity
									.getString(R.string.by_code_param)))) {

								searchRemainsTask = new SearchRemainsTask();
								searchRemainsTask.execute(
										connectionTask.get(),
										((EditText) remainsView
												.findViewById(R.id.requiredEdit))
												.getText(),
										searchingRemainsDialog, activity, positionsAdapter);
							} else {
								searchGoodsTask = new SearchGoodsTask();
								searchGoodsTask.execute(
										connectionTask.get(),
										((EditText) remainsView
												.findViewById(R.id.requiredEdit))
												.getText(),
										searchingRemainsDialog, activity, positionsAdapter);
							}
						} catch (Exception e) {
							Log.e("Search Remains", e.getMessage());
						}
					}

				});

		AlertDialog remainsDialog = alertDialogBuilder.create();

		remainsDialog.show();
	}

	// Searching client button
	public void findClients(View v) {
		searchClientTask = new SearchClientsTask();
		searchingDialog.show();
		try {
			searchClientTask.execute(connectionTask.get(),
					clientField.getText(), searchingDialog, activity);
		} catch (Exception e) {
			Log.e("Find clients", e.getMessage());
		}
	}
	
	public void onTrashButtonClick(View view) {
		positionsAdapter.clear();
	}

	// Create new client button click
	public void onCreateNewClientClick(View view) {
		final View newClientView = inflater.inflate(R.layout.new_client_layout,
				null);
		final Spinner clientTypeSpinner = (Spinner) newClientView
				.findViewById(R.id.clientTypeSpinner);

		clientTypeSpinner.setAdapter(clientTypeAdapter);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(newClientView);
		alertDialogBuilder.setPositiveButton(
				activity.getString(R.string.create),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText clientShortNameEdit = (EditText) newClientView
								.findViewById(R.id.clientShortNameEdit);
						EditText clientFullNameEdit = (EditText) newClientView
								.findViewById(R.id.clientFullNameEdit);
						Order.INSTANCE.setClient(new Client("new", "new",
								(clientShortNameEdit.getText()).toString(),
								(clientFullNameEdit.getText()).toString(),
								(ClientType) clientTypeSpinner
										.getSelectedItem()));
						EditText clientEdit = (EditText) activity.findViewById(R.id.clientField);
						clientEdit.setText(clientShortNameEdit.getText());
					}
				});

		alertDialogBuilder.setNegativeButton(
				activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog newClientDialog = alertDialogBuilder.create();

		newClientDialog.show();
	}
}
