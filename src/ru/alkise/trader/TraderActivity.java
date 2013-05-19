package ru.alkise.trader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import ru.alkise.trader.adapter.NewPositionAdater;
import ru.alkise.trader.db.mssql.SQLConnectionFactory;
import ru.alkise.trader.model.ClientIntf;
import ru.alkise.trader.model.ClientType;
import ru.alkise.trader.model.DocumentType;
import ru.alkise.trader.model.GoodsIntf;
import ru.alkise.trader.model.ManagerIntf;
import ru.alkise.trader.model.OrderIntf;
import ru.alkise.trader.model.OrganizationIntf;
import ru.alkise.trader.model.PositionIntf;
import ru.alkise.trader.model.WarehouseIntf;
import ru.alkise.trader.model.Warehouses;
import ru.alkise.trader.model.factory.ClientFactory;
import ru.alkise.trader.model.factory.OrderFactory;
import ru.alkise.trader.model.factory.WarehouseFactory;
import ru.alkise.trader.model.store.DataSaverIntf;
import ru.alkise.trader.model.store.SharedPreferencesDataSaver;
import ru.alkise.trader.task.SearchClientsTask;
import ru.alkise.trader.xml.XmlOrderGenerator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

//Calling GoodsActivity
//sending parameters
//parameter: OrderIntf.ORDER_TYPE value: order.getOrderDocumentType())
//parameter: GoodsIntf.GOODS_NAME value: searchingText
//received parameters
//PositionIntf.TABLE_NAME value: PositionIntf
public class TraderActivity extends Activity {

	private static final String DATABASE_NAME = "trader_db";
	public static final int REMAINS_OK = 1;
	public static final int FIND_GOODS_OK = 2;
	public static final int POSITION_EDIT_OK = 3;
	public static final int MANAGERS_OK = 4;
	public static final int ORGANIZATIONS_OK = 5;
	public static final int UPLOAD_OK = 0;

	private OrderIntf order;
	private DataLoaderTask dataLoaderTask;
	private SearchClientsTask searchClientTask;
	private ProgressDialog loadingDialog;
	private ProgressDialog searchingDialog;
	private Spinner orderTypeSpinner;
	private ListView positionsList;
	private ImageButton findClientsBtn;
	private Button btnManagers;
	private Button btnOrganizations;
	private EditText clientField;
	private Activity activity;
	private ArrayAdapter<PositionIntf> positionsAdapter;
	private ArrayAdapter<ClientIntf> clientAdapter;
	private ArrayAdapter<ClientType> clientTypeAdapter;
	private ArrayAdapter<DocumentType> orderTypeAdapter;
	private LayoutInflater inflater;
	private DataSaverIntf dataSaver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trader);

		order = OrderFactory.createOrder();

		activity = this;

		inflater = LayoutInflater.from(this);

		dataSaver = new SharedPreferencesDataSaver(DATABASE_NAME, activity);

		orderTypeSpinner = (Spinner) findViewById(R.id.spinnerOrderType);

		positionsAdapter = new NewPositionAdater(activity,
				R.layout.new_pos_layout, order.getOrderPositions());

		positionsList = (ListView) findViewById(R.id.positionList);
		positionsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		positionsList.setAdapter(positionsAdapter);

		positionsList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapter, View arg1,
							int pos, long arg3) {
						Intent editIntent = new Intent(
								"ru.alkise.trader.PositionEditActivity");
						editIntent.putExtra(OrderIntf.ORDER_TYPE,
								order.getOrderDocumentType());
						editIntent.putExtra(PositionIntf.POSITION_CODE, pos);
						editIntent.putExtra(PositionIntf.TABLE_NAME,
								positionsAdapter.getItem(pos));
						startActivityForResult(editIntent, POSITION_EDIT_OK);
					}
				});

		registerForContextMenu(positionsList);

		orderTypeAdapter = new ArrayAdapter<DocumentType>(activity,
				android.R.layout.simple_spinner_dropdown_item,
				DocumentType.values());
		orderTypeSpinner.setAdapter(orderTypeAdapter);
		orderTypeSpinner.setSelection(orderTypeAdapter
				.getPosition(DocumentType.CONSIGNMENT_NOTE));

		orderTypeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View arg1, int pos, long arg3) {
						order.setOrderDocumentType((DocumentType) adapter
								.getItemAtPosition(pos));
						if ((positionsAdapter != null)
								&& (!positionsAdapter.isEmpty())) {
							positionsAdapter.clear();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		loadingDialog = new ProgressDialog(this);
		loadingDialog.setIndeterminate(false);
		loadingDialog.setCancelable(false);
		loadingDialog.setMessage(getString(R.string.connecting));

		searchingDialog = new ProgressDialog(this);
		searchingDialog.setIndeterminate(false);
		searchingDialog.setCancelable(false);
		searchingDialog.setMessage(getString(R.string.searching));

		dataLoaderTask = new DataLoaderTask();

		findClientsBtn = (ImageButton) findViewById(R.id.findClientsBtn);

		btnManagers = (Button) findViewById(R.id.btnManagers);
		btnOrganizations = (Button) findViewById(R.id.btnOrganizations);

		clientField = (EditText) findViewById(R.id.clientField);
		clientField.selectAll();

		clientField.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence p1, int p2, int p3,
					int p4) {
			}

			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {

			}

			public void afterTextChanged(Editable p1) {
				if (p1.length() < 3 && findClientsBtn.isEnabled()) {
					findClientsBtn.setEnabled(false);
				} else {
					if (p1.length() >= 3 && !findClientsBtn.isEnabled()) {
						findClientsBtn.setEnabled(true);
					}
				}

			}
		});

		clientTypeAdapter = new ArrayAdapter<ClientType>(this,
				android.R.layout.simple_spinner_dropdown_item,
				ClientType.values());

		loadingDialog.show();
		dataLoaderTask.execute();
		order = dataSaver.load();

		orderTypeSpinner.setSelection(orderTypeAdapter.getPosition(order
				.getOrderDocumentType()));

		if (order.getOrderOrganization() != null) {
			btnOrganizations.setText(order.getOrderOrganization()
					.getOrganizationName());
		}

		if (order.getOrderManager() != null) {
			btnManagers.setText(order.getOrderManager().getManagerName());
		}

		if (order.getOrderClient() != null) {
			clientField.setText(order.getOrderClient().getClientFullName());
			clientField.selectAll();
		}
	}

	@Override
	protected void onDestroy() {
		dataSaver.store(order);

		if (positionsAdapter != null) {
			positionsAdapter.clear();
		}

		super.onDestroy();
	}

	@Override
	public void onCreateContextMenu(android.view.ContextMenu menu, View v,
			android.view.ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.positionList) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(positionsAdapter.getItem(info.position)
					.getPositionGoods().getGoodsName());
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		switch (menuItemIndex) {
		case 0:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					activity);
			alertDialogBuilder.setMessage(activity
					.getString(R.string.delete_position)
					+ " "
					+ ((PositionIntf) positionsAdapter.getItem(info.position))
							.getPositionGoods().getGoodsName() + " ?");
			alertDialogBuilder.setPositiveButton(
					activity.getString(R.string.delete),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							System.out.println(which);
							positionsAdapter.remove(positionsAdapter
									.getItem(info.position));
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

			AlertDialog confirmationDialog = alertDialogBuilder.create();
			confirmationDialog.show();
			break;
		case 1:
			Intent remainsIntent = new Intent(
					"ru.alkise.trader.RemainsActivity");
			remainsIntent.putExtra(OrderIntf.ORDER_TYPE,
					DocumentType.CONSIGNMENT_NOTE);
			remainsIntent.putExtra(GoodsIntf.GOODS_CODE,
					((PositionIntf) positionsAdapter.getItem(info.position))
							.getPositionGoods().getGoodsCode());
			startActivityForResult(remainsIntent, REMAINS_OK);
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			PositionIntf receivedPosition = (PositionIntf) data
					.getSerializableExtra(PositionIntf.TABLE_NAME);
			switch (requestCode) {
			case ORGANIZATIONS_OK:
				OrganizationIntf organization = (OrganizationIntf) data
						.getSerializableExtra(OrganizationIntf.TABLE_NAME);
				btnOrganizations.setText(organization.getOrganizationName());
				order.setOrderOrganization(organization);
				break;
			case MANAGERS_OK:
				ManagerIntf manager = (ManagerIntf) data
						.getSerializableExtra(ManagerIntf.TABLE_NAME);
				btnManagers.setText(manager.getManagerName());
				order.setOrderManager(manager);
				break;
			case REMAINS_OK:
			case FIND_GOODS_OK:
				if (order.checkNewPosition(receivedPosition)) {
					positionsAdapter.add(receivedPosition);
				} else {
					Toast.makeText(activity, getString(R.string.dublicating_position), Toast.LENGTH_SHORT).show();
				}
				break;
			case POSITION_EDIT_OK:
				PositionIntf positionToModify = positionsAdapter.getItem(data
						.getIntExtra(PositionIntf.POSITION_CODE, 0));
				positionToModify.setPositionCount(data.getDoubleExtra(
						PositionIntf.POSITION_COUNT, 1.0));
				positionToModify
						.setPositionToWarehouse((WarehouseIntf) data
								.getSerializableExtra(PositionIntf.POSITION_TO_WAREHOUSE));
				positionsAdapter.notifyDataSetChanged();
				break;
			}
		}
	}

	protected class DataLoaderTask extends AsyncTask<Object, Object, Object> {
		private ProgressDialog dataLoadingDialog;

		private Connection connection;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				// Loading warehouses
				connection = SQLConnectionFactory.createTrade2000Connection();
				PreparedStatement pstmt = connection
						.prepareStatement("SELECT CODE, DESCR FROM SC12 WHERE SP2505 = ? AND ISFOLDER = ? ORDER BY DESCR ");
				pstmt.setInt(1, 0);
				pstmt.setInt(2, 2);

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					Warehouses.INSTANCE.addWarehouse(WarehouseFactory
							.createWarehouse(rs.getInt(1), rs.getString(2)));
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
			loadingDialog.dismiss();
			dataLoadingDialog.dismiss();
			super.onPostExecute(result);
		}
	}

	//Manager Button
	public void onManagersButtonClick(View view) {
		Intent managersIntent = new Intent("ru.alkise.trader.ManagersActivity");
		startActivityForResult(managersIntent, MANAGERS_OK);
	}
	
	//Organization button
	public void onOrganizationsButtonClick(View view) {
		Intent organizationsIntent = new Intent(
				"ru.alkise.trader.OrganizationsActivity");
		startActivityForResult(organizationsIntent, ORGANIZATIONS_OK);
	}

	public void showOrder(View view) {
		StringBuilder builder = new StringBuilder();
		// Map<String, String> params = Reflector.reflect(order);
		// for (String string : params.keySet()) {
		// builder.append(string + " : " + params.get(string));
		// builder.append('\n');
		// }
		builder.append(order.displayOrder());
		Toast.makeText(getApplication(), builder.toString(), Toast.LENGTH_LONG)
				.show();
	}

	// Add position button click
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
									remainsIntent.putExtra(
											OrderIntf.ORDER_TYPE,
											order.getOrderDocumentType());
									remainsIntent.putExtra(
											GoodsIntf.GOODS_CODE, code);
									startActivityForResult(remainsIntent,
											REMAINS_OK);
								} catch (Exception e) {
									Log.e("TraderActivity", e.getMessage());
								}

							} else {
								Intent goodsIntent = new Intent(
										"ru.alkise.trader.GoodsActivity");

								goodsIntent.putExtra(OrderIntf.ORDER_TYPE,
										order.getOrderDocumentType());
								goodsIntent.putExtra(GoodsIntf.GOODS_NAME,
										searchingText);
								startActivityForResult(goodsIntent,
										FIND_GOODS_OK);
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
	@SuppressWarnings("unchecked")
	public void findClients(View v) {
		searchClientTask = new SearchClientsTask();
		searchingDialog.show();
		try {
			searchClientTask.execute(clientField.getText(), searchingDialog);
			clientAdapter = new ArrayAdapter<ClientIntf>(activity,
					android.R.layout.simple_spinner_dropdown_item,
					(List<ClientIntf>) searchClientTask.get());
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					activity);
			alertDialogBuilder.setAdapter(clientAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							ClientIntf client = clientAdapter.getItem(which);
							order.setOrderClient(client);
							((EditText) activity.findViewById(R.id.clientField))
									.setText(client.toString());
						}
					});
			alertDialogBuilder.setTitle(activity
					.getString(R.string.selectClient));
			alertDialogBuilder.setNegativeButton(
					activity.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
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
						})
				.setMessage(activity.getString(R.string.delete_all_positions));

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
						order.setOrderClient(ClientFactory.createClient(-1,
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
	public void uploadTo1C(View view) {
		if (order.checkOrder()) {
			FileOutputStream os = null;
			File file = null;
			try {
				File sdCard = Environment.getExternalStorageDirectory();
				File directory = new File(sdCard.getAbsolutePath() + "/Orders");
				directory.mkdirs();
				String fileName = System.currentTimeMillis() + ".xml";
				file = new File(directory, fileName);
				os = new FileOutputStream(file);
				String xmlText = XmlOrderGenerator.getXmlText(order);
				os.write(xmlText.getBytes());
				os.flush();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				Uri smbUri = Uri.parse("smb://192.168.0.202");
				intent.setDataAndType(smbUri,
						"vnd.android.cursor.dir/lysesoft.andsmb.uri");
				intent.putExtra("command_type", "upload");
				intent.putExtra("smb_username", "Order");
				intent.putExtra("smb_password", "dk2013order");
				intent.putExtra("smb_domain", "SHOP.DOMKAFEL.RU");
				intent.putExtra("local_file1", file.getAbsolutePath());
				intent.putExtra("remote_folder", "/Orders");
				startActivityForResult(intent, UPLOAD_OK);
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
		} else {
			System.out.println("Error order");
		}
	}

	// Clear client button
	public void clearClient(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				activity);
		alertDialogBuilder.setMessage(getString(R.string.clear_client_field));
		alertDialogBuilder.setPositiveButton(getString(R.string.clear),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						order.setOrderClient(null);
						clientField.setText(activity
								.getString(R.string.client_not_selected));
						clientField.selectAll();
					}
				});
		alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog clearClientConfirmationDialog = alertDialogBuilder.create();
		clearClientConfirmationDialog.show();
	}
}
