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

import ru.alkise.trader.adapter.NewPositionAdater;
import ru.alkise.trader.model.Client;
import ru.alkise.trader.model.ClientType;
import ru.alkise.trader.model.Manager;
import ru.alkise.trader.model.Order;
import ru.alkise.trader.model.OrderType;
import ru.alkise.trader.model.Organization;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.Warehouse;
import ru.alkise.trader.model.Warehouses;
import ru.alkise.trader.sql.DBConnection;
import ru.alkise.trader.sql.SQLConnectionFactory;
import ru.alkise.trader.task.SearchClientsTask;
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
	private Order order;

	private DataLoaderTask dataLoaderTask;
	private SearchClientsTask searchClientTask;
	private ProgressDialog loadingDialog;
	private ProgressDialog searchingDialog;
	private ProgressDialog searchingRemainsDialog;
	private Spinner organizationSpinner;
	private Spinner managerSpinner;
	private Spinner orderTypeSpinner;
	private ListView positionsList;
	private ImageButton findClientsBtn;
	private EditText clientField;
	private Activity activity;
	private NewPositionAdater positionsAdapter;
	private ArrayAdapter<Client> clientAdapter;
	private ArrayAdapter<ClientType> clientTypeAdapter;
	private ArrayAdapter<OrderType> orderTypeAdapter;
	private LayoutInflater inflater;
	public static final int REMAINS_OK = 1;
	public static final int FIND_GOODS_OK = 2;
	public static final int POSITION_EDIT_OK = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trader);

		order = new Order();

		activity = this;

		inflater = LayoutInflater.from(this);

		organizationSpinner = (Spinner) findViewById(R.id.organizationSpinner);
		managerSpinner = (Spinner) findViewById(R.id.managerSpinner);
		orderTypeSpinner = (Spinner) findViewById(R.id.spinnerOrderType);

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
						editIntent.putExtra("docType", order.getOrderType());
						editIntent.putExtra("pos", pos);
						editIntent.putExtra("position",
								positionsAdapter.getItem(pos));
						startActivityForResult(editIntent, POSITION_EDIT_OK);
					}
				});

		orderTypeAdapter = new ArrayAdapter<OrderType>(activity,
				android.R.layout.simple_spinner_dropdown_item,
				OrderType.values());
		orderTypeSpinner.setAdapter(orderTypeAdapter);
		orderTypeSpinner.setSelection(orderTypeAdapter
				.getPosition(OrderType.CONSIGNMENT_NOTE));

		orderTypeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View arg1, int pos, long arg3) {
						order.setOrderType((OrderType) adapter
								.getItemAtPosition(pos));
						if ((positionsAdapter != null)
								&& (!positionsAdapter.isEmpty())) {
							positionsAdapter.clear();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

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
				positionToModify.setCount(data.getDoubleExtra("count", 1.0));
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
						.executeQuery("SELECT CODE, DESCR FROM SC308");

				organizations = new ArrayList<Organization>();
				while (rs.next()) {
					organizations.add(new Organization(rs.getInt(1), rs
							.getString(2)));
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
						.prepareStatement("SELECT CODE, DESCR FROM SC377 WHERE SP1860 = ? ORDER BY DESCR");
				pstmt.setInt(1, 1);

				rs = pstmt.executeQuery();

				activeManagers = new ArrayList<Manager>();
				while (rs.next()) {
					activeManagers.add(new Manager(rs.getInt(1), rs
							.getString(2)));
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
						.prepareStatement("SELECT CODE, DESCR FROM SC12 WHERE SP2505 = ? AND ISFOLDER = ? ORDER BY DESCR ");
				pstmt.setInt(1, 0);
				pstmt.setInt(2, 2);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					Warehouses.INSTANCE.addWarehouse(new Warehouse(
							rs.getInt(1), rs.getString(2)));
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
								order.setOrganization((Organization) adapter
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
								order.setManager((Manager) adapter
										.getSelectedItem());
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

				positionsAdapter = new NewPositionAdater(activity,
						R.layout.new_pos_layout, order.getPositions());
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
		Toast.makeText(getApplication(), order.displayOrder(),
				Toast.LENGTH_SHORT).show();
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
									remainsIntent.putExtra("docType",
											order.getOrderType());
									remainsIntent.putExtra("code", code);
									startActivityForResult(remainsIntent,
											REMAINS_OK);
								} catch (Exception e) {
									Log.e("TraderActivity", e.getMessage());
								}
								searchingRemainsDialog.dismiss();

							} else {
								Intent goodsIntent = new Intent(
										"ru.alkise.trader.GoodsActivity");

								goodsIntent.putExtra("docType",
										order.getOrderType());
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
	@SuppressWarnings("unchecked")
	public void findClients(View v) {
		searchClientTask = new SearchClientsTask();
		searchingDialog.show();
		try {
			searchClientTask.execute(clientField.getText(), searchingDialog,
					activity);
			clientAdapter = new ArrayAdapter<Client>(activity,
					android.R.layout.simple_spinner_dropdown_item,
					(List<Client>) searchClientTask.get());
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					activity);
			alertDialogBuilder.setAdapter(clientAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Client client = clientAdapter.getItem(which);
							order.setClient(client);
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
						order.setClient(new Client(-1, (clientShortNameEdit
								.getText()).toString(), (clientFullNameEdit
								.getText()).toString(),
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
				String xmlText = order.getXmlText();
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
						order.setClient(null);
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
