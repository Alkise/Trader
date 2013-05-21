package ru.alkise.trader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class SettingsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		
		//Server settings tab
		TabHost.TabSpec spec = tabHost.newTabSpec("tagDB");
		spec.setContent(R.id.tableDB);
		spec.setIndicator(getString(R.string.settings));
		tabHost.addTab(spec);
		
		//Upload server tab
		spec = tabHost.newTabSpec("tagUpload");
		spec.setContent(R.id.tableUpload);
		spec.setIndicator(getString(R.string.uploads));
		tabHost.addTab(spec);
		
		//Organizations tab
		spec = tabHost.newTabSpec("tagOrganizations");
		spec.setContent(R.id.tableOrganizations);
		spec.setIndicator(getString(R.string.organizations));
		tabHost.addTab(spec);
		
		//Managers tab
		spec = tabHost.newTabSpec("tagManagers");
		spec.setContent(R.id.tableManagers);
		spec.setIndicator(getString(R.string.manangers));
		tabHost.addTab(spec);
		
		//Warehouses tab
		spec = tabHost.newTabSpec("tagWarehouses");
		spec.setContent(R.id.tableWarehouses);
		spec.setIndicator(getString(R.string.warehouses));
		tabHost.addTab(spec);
		
		//Clients tab
		spec = tabHost.newTabSpec("tagClients");
		spec.setContent(R.id.tableClients);
		spec.setIndicator(getString(R.string.clients));
		tabHost.addTab(spec);
		
		//Nomenclature tab
		spec = tabHost.newTabSpec("tagNomenclature");
		spec.setContent(R.id.tableNomenclature);
		spec.setIndicator(getString(R.string.nomenclature));
		tabHost.addTab(spec);
		
		//Remains tab
		spec = tabHost.newTabSpec("tagRemains");
		spec.setContent(R.id.tableRemains);
		spec.setIndicator(getString(R.string.remains));
		tabHost.addTab(spec);
	}
}
