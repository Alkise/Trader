package ru.alkise.trader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import ru.alkise.trader.adapter.NewPositionAdater;
import ru.alkise.trader.model.Client;
import ru.alkise.trader.model.ClientType;
import ru.alkise.trader.model.Manager;
import ru.alkise.trader.model.Order;
import ru.alkise.trader.model.Organization;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.Warehouse;
import ru.alkise.trader.model.Warehouses;
import ru.alkise.trader.sql.DBConnection;
import ru.alkise.trader.sql.SQLConnectionFactory;
import ru.alkise.trader.task.SearchClientsTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
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
	private DataLoaderTask dataLoaderTask;
	private SearchClientsTask searchClientTask;
	private ProgressDialog loadingDialog;
	private ProgressDialog searchingDialog;
	private ProgressDialog searchingRemainsDialog;
	private Spinner organizationSpinner;
	private Spinner managerSpinner;
	private ListView positionsList;
	private ImageButton findClientsBtn;
	private EditText clientField;
	private Activity activity;
	private NewPositionAdater positionsAdapter;
	private ArrayAdapter<ClientType> clientTypeAdapter;
	private LayoutInflater inflater;
	public static final int REMAINS_OK = 1;
	public static final int FIND_GOODS_OK = 2;
	public static final int POSITION_EDIT_OK = 3;

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

		positionsList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View arg1, final int pos, long arg3) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								activity);
						alertDialogBuilder.setMessage(activity
								.getString(R.string.delete_position)
								+ " "
								+ ((Position) positionsAdapter.getItem(pos))
										.getGoods().getDescr() + " ?");
						alertDialogBuilder.setPositiveButton(
								activity.getString(R.string.delete),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										System.out.println(which);
										positionsAdapter
												.remove(positionsAdapter
														.getItem(pos));
									}
								});
						alertDialogBuilder.setNegativeButton(
								activity.getString(R.string.cancel),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								});

						AlertDialog confirmationDialog = alertDialogBuilder
								.create();
						confirmationDialog.show();
						return false;
					}
				});

		positionsList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapter, View arg1,
							int pos, long arg3) {
						Intent editIntent = new Intent(
								"ru.alkise.trader.PositionEditActivity");
						editIntent.putExtra("pos", pos);
						editIntent.putExtra("position",
								positionsAdapter.getItem(pos));
						startActivityForResult(editIntent, POSITION_EDIT_OK);
					}
				});

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

		dataLoaderTask = new DataLoaderTask();

		findClientsBtn = (ImageButton) findViewById(R.id.findClientsBtn);

		clientField = (EditText) findViewById(R.id.clientField);
		clientField.selectAll();

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

		dataLoaderTask.execute();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		try {
			DBConnection.INSTANCE.closeConnection();
		} catch (Exception e) {
			Log.e("Stop trader", e.getMessage());
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		positionsAdapter.clear();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Position receivedPosition = (Position) data
					.getSerializableExtra("position");
			switch (requestCode) {
			case REMAINS_OK:
			case FIND_GOODS_OK:
				positionsAdapter.add(receivedPosition);
				break;
			case POSITION_EDIT_OK:
				Position positionToModify = positionsAdapter.getItem(data
						.getIntExtra("pos", 0));
				positionToModify.setCount(data.getDoubleExtra("count", 0.0));
				positionToModify.setWhTo((Warehouse) data
						.getSerializableExtra("whTo"));
				positionsAdapter.notifyDataSetChanged();
				break;
			}
		}
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
				connection = SQLConnectionFactory.createTrade2000Connection();
				Statement stmt = connection.createStatement();

				ResultSet rs = stmt
						.executeQuery("SELECT ID, CODE, DESCR FROM SC308");

				organizations = new ArrayList<Organization>();
				while (rs.next()) {
					organizations.add(new Organization(rs.getString(1), rs
							.getInt(2), rs.getString(3)));
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
							.getInt(2), rs.getString(3)));
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

				positionsAdapter = new NewPositionAdater(activity,
						R.layout.new_pos_layout, Order.INSTANCE.getPositions());
				positionsList.setAdapter(positionsAdapter);
			}
			loadingDialog.dismiss();
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
						String searchingText = String.valueOf(
								((EditText) remainsView
										.findViewById(R.id.requiredEdit))
										.getText()).trim();

						try {
							if ((((RadioButton) remainsView
									.findViewById(((RadioGroup) remainsView
											.findViewById(R.id.paramGroup))
											.getCheckedRadioButtonId()))
									.getText().equals(activity
									.getString(R.string.by_code_param)))) {

								try {
									int code = Integer.valueOf(searchingText
											.trim());
									Intent remainsIntent = new Intent(
											"ru.alkise.trader.RemainsActivity");
									remainsIntent.putExtra("code", code);
									startActivityForResult(remainsIntent,
											REMAINS_OK);
									searchingRemainsDialog.dismiss();
								} catch (Exception e) {
									Log.e("TraderActivity", e.getMessage());
								}

							} else {
								Intent goodsIntent = new Intent(
										"ru.alkise.trader.GoodsActivity");

								goodsIntent.putExtra("positionName",
										searchingText);
								startActivityForResult(goodsIntent,
										FIND_GOODS_OK);
								searchingRemainsDialog.dismiss();
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
			searchClientTask.execute(clientField.getText(), searchingDialog,
					activity);
		} catch (Exception e) {
			Log.e("Find clients", e.getMessage());
		}
	}

	// Trash button click
	public void onTrashButtonClick(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface p1, int p2) {
								p1.cancel();
							}

						})
				.setPositiveButton(getString(R.string.delete),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface p1, int p2) {
								positionsAdapter.clear();
							}
						}).setMessage("Delete all positions?");

		AlertDialog deletingDialog = alertDialogBuilder.create();

		deletingDialog.show();
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
						Order.INSTANCE.setClient(new Client("new", -1,
								(clientShortNameEdit.getText()).toString(),
								(clientFullNameEdit.getText()).toString(),
								(ClientType) clientTypeSpinner
										.getSelectedItem()));
						EditText clientEdit = (EditText) activity
								.findViewById(R.id.clientField);
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

	// Upload button
	@SuppressLint("WorldReadableFiles")
	public void uploadTo1C(View view) {
		if (Order.INSTANCE.checkOrder()) {
			XmlSerializer serializer = Xml.newSerializer();
			FileOutputStream os = null;
			File file = null;
			try {
				File sdCard = Environment.getExternalStorageDirectory();
				File directory = new File(sdCard.getAbsolutePath()+"/Orders");
				directory.mkdirs();
				String fileName = System.currentTimeMillis() + ".xml";
				file = new File(directory , fileName);
				os = new FileOutputStream(file);
				serializer.setOutput(os, "UTF-8");
				serializer.startDocument("UTF-8", true);
				//Order
				serializer.startTag(null, "ORDER");

				//Organization
				serializer.startTag(null, "ORGANIZATION");
				//Organization.code
				serializer.startTag(null, "CODE");
				serializer.text(String.valueOf(Order.INSTANCE
						.getOrganization().getCode()));
				serializer.endTag(null, "CODE");
				
				serializer.endTag(null, "ORGANIZATION");
				
				//Manager
				serializer.startTag(null, "MANAGER");
				//Manager.code
				serializer.startTag(null, "CODE");
				serializer.text(String.valueOf(Order.INSTANCE.getManager().getCode()));
				serializer.endTag(null, "CODE");
				
				serializer.endTag(null, "MANAGER");
				
				//Client
				serializer.startTag(null, "CLIENT");
				//Client.code
				serializer.startTag(null, "CODE");
				serializer.text(
						String.valueOf(Order.INSTANCE.getClient().getCode()));
				serializer.endTag(null, "CODE");
				//Client.type
				serializer.startTag(null, "TYPE");
				if (Order.INSTANCE.getClient().getType() == null) {
					serializer.text("");
				} else
				serializer.text(Order.INSTANCE.getClient().getType().getValue());
				serializer.endTag(null, "TYPE");
				//Client.short_name
				serializer.startTag(null, "SHORT_NAME");
				serializer.text(Order.INSTANCE
						.getClient().getDescr());
				serializer.endTag(null, "SHORT_NAME");
				//Client.full_name
				serializer.startTag(null, "FULL_NAME");
				serializer.text(String
						.valueOf(Order.INSTANCE.getClient().getFullName()));
				serializer.endTag(null, "FULL_NAME");
				
				serializer.endTag(null, "CLIENT");
				
				for (Position position : Order.INSTANCE.getPositions()) {
					//Position
					serializer.startTag(null, "POSITION");
					//Position.code
					serializer.startTag(null, "CODE");
					serializer.text(String.valueOf(position.getGoods().getCode()));
					serializer.endTag(null, "CODE");
					//Position.count
					serializer.startTag(null, "COUNT");
					serializer.text(
							String.valueOf(position.getCount()));
					serializer.endTag(null, "COUNT");
					//Position.warehouse_from
					serializer.startTag(null, "WAREHOUSE_FROM");
					serializer.text(
							String.valueOf(position.getWhFrom().getCode()));
					serializer.endTag(null, "WAREHOUSE_FROM");
					//Position.warehouse_to
					serializer.startTag(null, "WAREHOUSE_TO");
					serializer.text(
							String.valueOf(position.getWhTo().getCode()));
					serializer.endTag(null, "WAREHOUSE_TO");
					
					serializer.endTag(null, "POSITION");
				}
				
				serializer.endTag(null, "ORDER");
				serializer.endDocument();

				serializer.flush();
				os.flush();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				Uri smbUri = Uri.parse("smb://192.168.0.202");
				intent.setDataAndType(smbUri, "vnd.android.cursor.dir/lysesoft.andsmb.uri");
				intent.putExtra("command_type", "upload");
				intent.putExtra("smb_username", "Order");
				intent.putExtra("smb_password", "dk2013order");
				intent.putExtra("smb_domain", "SHOP.DOMKAFEL.RU");
				intent.putExtra("local_file1", file.getAbsolutePath());
				intent.putExtra("remote_folder", "/Orders");
				startActivityForResult(intent, 4);
			} catch (Exception e) {
				Log.e("XML Serialization error", e.getMessage());
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e1) {
						Log.e("XML Serialization error", e1.getMessage());
					}
				}
			}
		}  else {
			System.out.println("Error order");
		}
	}

	// Clear client button
	public void clearClient(View view) {
		Order.INSTANCE.setClient(null);
		clientField.setText(activity.getString(R.string.client_not_selected));
	}
}
