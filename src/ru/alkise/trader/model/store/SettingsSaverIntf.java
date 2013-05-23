package ru.alkise.trader.model.store;

import java.util.List;

import android.widget.EditText;
import android.widget.ListView;

public interface SettingsSaverIntf {
	public static final String SETTINGS_FILE_NAME = "settings_db";

	// system params
	public static final String PARAM_SETTINGS_PASSWORD = "param_settings_password";

	// SQL params
	public static final String PARAM_SQL_HOST = "param_sql_host";
	public static final String PARAM_SQL_PORT = "param_host_port";
	public static final String PARAM_SQL_DATABASE = "param_sql_database";
	public static final String PARAM_SQL_USER = "param_sql_user";
	public static final String PARAM_SQL_PASSWORD = "param_sql_password";

	// Upload params
	public static final String PARAM_UPLOAD_HOST = "param_upload_host";
	public static final String PARAM_UPLOAD_USER = "param_upload_user";
	public static final String PARAM_UPLOAD_PASSWORD = "param_upload_password";
	public static final String PARAM_UPLOAD_DOMAIN = "param_upload_domain";
	public static final String PARAM_UPLOAD_CATALOG = "param_upload_catalog";

	// Tables
	// Managers
	public static final String PARAM_MANAGER_TABLE_NAME = "param_manager_table_name";
	public static final String PARAM_MANAGER_ID = "param_manager_id";
	public static final String PARAM_MANAGER_CODE = "param_manager_code";
	public static final String PARAM_MANAGER_DESCR = "param_manager_descr";
	public static final String PARAM_MANAGER_IS_ACTIVITY = "param_manager_is_activity";

	// Clients
	public static final String PARAM_CLIENT_TABLE_NAME = "param_client_table_name";
	public static final String PARAM_CLIENT_ID = "param_client_id";
	public static final String PARAM_CLIENT_CODE = "param_client_code";
	public static final String PARAM_CLIENT_DESCR = "param_client_descr";
	public static final String PARAM_CLIENT_FULL_NAME = "param_client_full_name";
	public static final String PARAM_CLIENT_TYPE = "param_client_type";
	public static final String PARAM_CLIENT_PARENT_ID = "param_client_parent_id";
	public static final String PARAM_CLIENT_IS_ACTIVITY = "param_client_is_activity";
	public static final String PARAM_CLIENT_ALLOWED_GROUPS = "param_client_allowed_groups";

	// Warehouses
	public static final String PARAM_WAREHOUSE_TABLE_NAME = "param_warehouse_table_name";
	public static final String PARAM_WAREHOUSE_ID = "param_warehouse_id";
	public static final String PARAM_WAREHOUSE_CODE = "param_warehouse_code";
	public static final String PARAM_WAREHOUSE_DESCR = "param_warehouse_descr";
	public static final String PARAM_WAREHOUSE_OLD = "param_warehouse_old";
	public static final String PARAM_WAREHOUSE_IS_FOLDER = "param_warehouse_is_folder";
	public static final String PARAM_WAREHOUSE_PARENT_ID = "param_warehouse_parent_id";

	// Organizations
	public static final String PARAM_ORGANIZATION_TABLE_NAME = "param_organization_table_name";
	public static final String PARAM_ORGANIZATION_ID = "param_organization_id";
	public static final String PARAM_ORGANIZATION_CODE = "param_organization_code";
	public static final String PARAM_ORGANIZATION_DESCR = "param_organization_descr";

	// Remains
	public static final String PARAM_REMAINS_TABLE_NAME = "param_remains_table_name";
	public static final String PARAM_REMAINS_NOMENCLATURE = "param_remains_nomenclature";
	public static final String PARAM_REMAINS_WAREHOUSE = "param_remains_warehouse";
	public static final String PARAM_REMAINS_PERIOD = "param_remains_period";
	public static final String PARAM_REMAINS_COUNT = "param_remains_count";

	// Nomenclature
	public static final String PARAM_NOMENCLATURE_TABLE_NAME = "param_nomenclature_table_name";
	public static final String PARAM_NOMENCLATURE_ID = "param_nomenclature_id";
	public static final String PARAM_NOMENCLATURE_CODE = "param_nomenclature_code";
	public static final String PARAM_NOMENCLATURE_DESCR = "param_nomenclature_descr";
	public static final String PARAM_NOMENCLATURE_WAREHOUSE = "param_nomenclature_warehouse";
	public static final String PARAM_NOMENCLATURE_IS_ACTIVITY = "param_nomenclature_is_activity";
	
	boolean commit();
	
	void saveStringParam(EditText editText);
	
	void saveIntParam(EditText editText);
	
	void loadStringParam(EditText editText);
	
	void loadIntParam(EditText editText);
	
	void saveStringParams(List<EditText> editTextList);
	
	void saveIntParams(List<EditText> editTextList);
	
	void loadStringParams(List<EditText> editTextList);
	
	void loadIntParams(List<EditText> editTextList);

	void saveStringSetParams(ListView listView);

	void loadStingSetParams(ListView listView);
	
}
