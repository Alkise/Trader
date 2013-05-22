package ru.alkise.trader;

import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.model.store.SettingsSaverIntf;
import ru.alkise.trader.model.store.SharedPreferencesSettingsSaver;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TabHost;

public class SettingsActivity extends Activity {
	private SettingsSaverIntf settingsSaver;

	private List<EditText> editStringList;
	private List<EditText> editIntList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);

		settingsSaver = new SharedPreferencesSettingsSaver(
				getApplicationContext());

		editStringList = new ArrayList<EditText>();
		editIntList = new ArrayList<EditText>();

		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		// Server settings tab
		TabHost.TabSpec spec = tabHost.newTabSpec("tagDB");
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
}
