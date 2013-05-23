package ru.alkise.trader;

import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.model.store.SettingsSaverIntf;
import ru.alkise.trader.model.store.SharedPreferencesSettingsSaver;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private SettingsSaverIntf settingsSaver;

	private EditText allowedGroupEdit;
	private ListView allowedClientGroupsView;
	private ArrayAdapter<String> allowedClientGroupsAdapter;
	private List<EditText> editStringList;
	private List<EditText> editIntList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);

		allowedGroupEdit = (EditText) findViewById(R.id.editAllowedGroupsTabClients);
		allowedClientGroupsView = (ListView) findViewById(R.id.listAllowedGroupsListTabClients);
		allowedClientGroupsView
				.setTag(SettingsSaverIntf.PARAM_CLIENT_ALLOWED_GROUPS);
		allowedClientGroupsView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
							View arg1, int pos, long arg3) {
						allowedClientGroupsAdapter
								.remove(allowedClientGroupsAdapter.getItem(pos));
						return false;
					}
				});
		allowedClientGroupsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>());
		allowedClientGroupsView.setAdapter(allowedClientGroupsAdapter);

		settingsSaver = new SharedPreferencesSettingsSaver(
				getApplicationContext());

		editStringList = new ArrayList<EditText>();
		editIntList = new ArrayList<EditText>();

		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		// System settings tab
		TabHost.TabSpec spec = tabHost.newTabSpec("tagSystem");
		spec.setContent(R.id.systemSettings);
		spec.setIndicator(getString(R.string.system));
		tabHost.addTab(spec);

		// 1c server settings tab
		spec = tabHost.newTabSpec("tagDB");
		spec.setContent(R.id.tableDB);
		spec.setIndicator(getString(R.string.settings_1c));
		tabHost.addTab(spec);

		// Upload server tab
		spec = tabHost.newTabSpec("tagUpload");
		spec.setContent(R.id.tableUpload);
		spec.setIndicator(getString(R.string.uploads));
		tabHost.addTab(spec);

		// Organizations tab
		spec = tabHost.newTabSpec("tagOrganizations");
		spec.setContent(R.id.tableOrganizations);
		spec.setIndicator(getString(R.string.organizations));
		tabHost.addTab(spec);

		// Managers tab
		spec = tabHost.newTabSpec("tagManagers");
		spec.setContent(R.id.tableManagers);
		spec.setIndicator(getString(R.string.manangers));
		tabHost.addTab(spec);

		// Warehouses tab
		spec = tabHost.newTabSpec("tagWarehouses");
		spec.setContent(R.id.tableWarehouses);
		spec.setIndicator(getString(R.string.warehouses));
		tabHost.addTab(spec);

		// Clients tab
		spec = tabHost.newTabSpec("tagClients");
		spec.setContent(R.id.tableClients);
		spec.setIndicator(getString(R.string.clients));
		tabHost.addTab(spec);

		// Nomenclature tab
		spec = tabHost.newTabSpec("tagNomenclature");
		spec.setContent(R.id.tableNomenclature);
		spec.setIndicator(getString(R.string.nomenclature));
		tabHost.addTab(spec);

		// Remains tab
		spec = tabHost.newTabSpec("tagRemains");
		spec.setContent(R.id.tableRemains);
		spec.setIndicator(getString(R.string.remains));
		tabHost.addTab(spec);

		// SQL
		bindStringParam(R.id.editHostTabDB, SettingsSaverIntf.PARAM_SQL_HOST);
		bindIntParam(R.id.editPortTabDB, SettingsSaverIntf.PARAM_SQL_PORT);
		bindStringParam(R.id.editDatabaseTabDB,
				SettingsSaverIntf.PARAM_SQL_DATABASE);
		bindStringParam(R.id.editUsernameTabDB,
				SettingsSaverIntf.PARAM_SQL_USER);
		bindStringParam(R.id.editPasswordTabDB,
				SettingsSaverIntf.PARAM_SQL_PASSWORD);

		// Upload
		bindStringParam(R.id.editHostTabUpload,
				SettingsSaverIntf.PARAM_UPLOAD_HOST);
		bindStringParam(R.id.editUsernameTabUpload,
				SettingsSaverIntf.PARAM_UPLOAD_USER);
		bindStringParam(R.id.editPasswordTabUpload,
				SettingsSaverIntf.PARAM_UPLOAD_PASSWORD);
		bindStringParam(R.id.editDomainTabUpload,
				SettingsSaverIntf.PARAM_UPLOAD_DOMAIN);
		bindStringParam(R.id.editCatalogTabUpload,
				SettingsSaverIntf.PARAM_UPLOAD_CATALOG);

		// Organizations
		bindStringParam(R.id.editTableNameTabOrganizations,
				SettingsSaverIntf.PARAM_ORGANIZATION_TABLE_NAME);
		bindStringParam(R.id.editIDTabOrganizations,
				SettingsSaverIntf.PARAM_ORGANIZATION_ID);
		bindStringParam(R.id.editCodeTabOrganizations,
				SettingsSaverIntf.PARAM_ORGANIZATION_CODE);
		bindStringParam(R.id.editDescrTabOrganizations,
				SettingsSaverIntf.PARAM_ORGANIZATION_DESCR);

		// Managers
		bindStringParam(R.id.editTableNameTabManager,
				SettingsSaverIntf.PARAM_MANAGER_TABLE_NAME);
		bindStringParam(R.id.editIDTabManager,
				SettingsSaverIntf.PARAM_MANAGER_ID);
		bindStringParam(R.id.editCodeTabManager,
				SettingsSaverIntf.PARAM_MANAGER_CODE);
		bindStringParam(R.id.editDescrTabManager,
				SettingsSaverIntf.PARAM_MANAGER_DESCR);
		bindStringParam(R.id.editActivityTabManager,
				SettingsSaverIntf.PARAM_MANAGER_IS_ACTIVITY);

		// Warehouses
		bindStringParam(R.id.editTableNameTabWarehouses,
				SettingsSaverIntf.PARAM_WAREHOUSE_TABLE_NAME);
		bindStringParam(R.id.editIDTabWarehouses,
				SettingsSaverIntf.PARAM_WAREHOUSE_ID);
		bindStringParam(R.id.editCodeTabWarehouses,
				SettingsSaverIntf.PARAM_WAREHOUSE_CODE);
		bindStringParam(R.id.editDescrTabWarehouses,
				SettingsSaverIntf.PARAM_WAREHOUSE_DESCR);
		bindStringParam(R.id.editFolderTabWarehouses,
				SettingsSaverIntf.PARAM_WAREHOUSE_IS_FOLDER);
		bindStringParam(R.id.editOldTabWarehouses,
				SettingsSaverIntf.PARAM_WAREHOUSE_OLD);
		bindStringParam(R.id.editParentIDTabWarehouses,
				SettingsSaverIntf.PARAM_CLIENT_PARENT_ID);

		// Clients
		bindStringParam(R.id.editTableNameTabClients,
				SettingsSaverIntf.PARAM_CLIENT_TABLE_NAME);
		bindStringParam(R.id.editIDTabClients,
				SettingsSaverIntf.PARAM_CLIENT_ID);
		bindStringParam(R.id.editCodeTabClients,
				SettingsSaverIntf.PARAM_CLIENT_CODE);
		bindStringParam(R.id.editDescrTabClients,
				SettingsSaverIntf.PARAM_CLIENT_DESCR);
		bindStringParam(R.id.editFullNameTabClients,
				SettingsSaverIntf.PARAM_CLIENT_FULL_NAME);
		bindStringParam(R.id.editTypeTabClients,
				SettingsSaverIntf.PARAM_CLIENT_TYPE);
		bindStringParam(R.id.editActivityTabClients,
				SettingsSaverIntf.PARAM_CLIENT_IS_ACTIVITY);
		bindStringParam(R.id.editParentIDTabClients,
				SettingsSaverIntf.PARAM_CLIENT_PARENT_ID);
		settingsSaver.loadStingSetParams(allowedClientGroupsView);

		// Nomenclature
		bindStringParam(R.id.editTableNameTabNomenclature,
				SettingsSaverIntf.PARAM_NOMENCLATURE_TABLE_NAME);
		bindStringParam(R.id.editIDTabNomenclature,
				SettingsSaverIntf.PARAM_NOMENCLATURE_ID);
		bindStringParam(R.id.editCodeTabNomenclature,
				SettingsSaverIntf.PARAM_NOMENCLATURE_CODE);
		bindStringParam(R.id.editDescrTabNomenclature,
				SettingsSaverIntf.PARAM_NOMENCLATURE_DESCR);
		bindStringParam(R.id.editWarehouseTabNomenclature,
				SettingsSaverIntf.PARAM_NOMENCLATURE_WAREHOUSE);
		bindStringParam(R.id.editActivityTabNomenclature,
				SettingsSaverIntf.PARAM_NOMENCLATURE_IS_ACTIVITY);

		// Remains
		bindStringParam(R.id.editTableNameTabRemains,
				SettingsSaverIntf.PARAM_REMAINS_TABLE_NAME);
		bindStringParam(R.id.editNomenclatureTabRemains,
				SettingsSaverIntf.PARAM_REMAINS_NOMENCLATURE);
		bindStringParam(R.id.editWarehouseTabRemains,
				SettingsSaverIntf.PARAM_REMAINS_WAREHOUSE);
		bindStringParam(R.id.editPeriodTabRemains,
				SettingsSaverIntf.PARAM_REMAINS_PERIOD);
		bindStringParam(R.id.editCountTabRemains,
				SettingsSaverIntf.PARAM_REMAINS_COUNT);

		settingsSaver.loadIntParams(editIntList);
		settingsSaver.loadStringParams(editStringList);
	}

	private EditText bindStringParam(int id, String parameterName) {
		return bindEditText(id, parameterName, editStringList);
	}

	private EditText bindIntParam(int id, String parameterName) {
		return bindEditText(id, parameterName, editIntList);
	}

	private EditText bindEditText(int id, String parameterName,
			List<EditText> list) {
		EditText newEditText = (EditText) findViewById(id);
		newEditText.setTag(parameterName);
		list.add(newEditText);
		return newEditText;
	}

	private void closeActivity() {
		super.onBackPressed();
	}

	public void addAllowedGroup(View view) {
		String newGroup = (String.valueOf(allowedGroupEdit.getText())).trim();
		if (newGroup.length() > 0) {
			allowedClientGroupsAdapter.add("   " + newGroup + "   ");
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(getString(R.string.saving_question));
		alertDialogBuilder.setTitle(getString(R.string.settings));
		alertDialogBuilder.setPositiveButton(getString(R.string.save),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						settingsSaver.saveIntParams(editIntList);
						settingsSaver.saveStringParams(editStringList);
						settingsSaver
								.saveStringSetParams(allowedClientGroupsView);

						settingsSaver.commit();
						dialog.cancel();
						closeActivity();
					}
				});

		alertDialogBuilder.setNegativeButton(getString(R.string.dont_save),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						closeActivity();
					}
				});
		alertDialogBuilder.setNeutralButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog settingsDialog = alertDialogBuilder.create();

		settingsDialog.show();
	}

	public void applyNewSettingsPassword(View view) {
		EditText newPasswordEdit = (EditText) findViewById(R.id.editNewPasswordTabSystem);
		EditText confirmNewPasswordEdit = (EditText) findViewById(R.id.editConfirmPasswordTabSystem);
		String newPassword = String.valueOf(newPasswordEdit.getText()).trim();
		String confirmNewPassword = String.valueOf(
				confirmNewPasswordEdit.getText()).trim();
		if (newPassword.equals(confirmNewPassword)) {
			bindStringParam(R.id.editConfirmPasswordTabSystem,
					SettingsSaverIntf.PARAM_SETTINGS_PASSWORD);
			Toast.makeText(this, "New password saved",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "New password validation error",
					Toast.LENGTH_SHORT).show();
		}
	}
}
