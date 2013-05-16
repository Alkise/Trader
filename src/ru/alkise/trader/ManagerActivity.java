package ru.alkise.trader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.db.mssql.SQLConnectionFactory;
import ru.alkise.trader.model.Manager;
import ru.alkise.trader.model.Order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ManagerActivity extends Activity {
	/*private Activity activity;
	private Connection connection;
	private Intent data;
	private int managerCode;
	private ProgressDialog progressDialog;
	private ListView managersList;
	private ArrayAdapter<Manager> managersAdapter;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_layout);

		/*activity = this;
		data = new Intent();

		managerCode = getIntent().getIntExtra(Order.MANAGER_CODE, -1);

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.searching));

		managersList = (ListView) findViewById(R.id.listManagersManagerLayout);*/
	}
/*
	private void returnResult() {
		setResult(RESULT_OK, data);
		finish();
	}
	
	@Override
	protected void onStart() {
		new ManagerSearchTask().execute();
		super.onStart();
	}*/

	/*private class ManagerSearchTask extends AsyncTask<Object, Object, Object> {
		private List<Manager> managers;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = SQLConnectionFactory.createTrade2000Connection();

				String query = "SELECT CODE, DESCR FROM SC377 WHERE"
						+ (managerCode != -1 ? " WHERE CODE = ?" : "")
						+ " SP1860 = 1 ORDER BY DESCR";

				PreparedStatement pstmt = connection.prepareStatement(query);
				if (managerCode != -1) {
					pstmt.setInt(1, managerCode);
				}

				ResultSet rs = pstmt.executeQuery();

				if (rs.getFetchSize() > 0) {
					
					managers = new ArrayList<Manager>();

					while (rs.next()) {
						managers.add(new Manager(rs.getInt(1), rs.getString(2)));
					}
					
					if (managers.size() == 1) {
						data.putExtra("manager", managers.get(0));
						returnResult();
					}
					
					managersAdapter = new ArrayAdapter<Manager>(activity, android.R.layout.simple_list_item_1, managers);
				}
			} catch (Exception e) {
				Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT)
						.show();
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (Exception e) {
						Log.e("ManagersActivity.RemainsSearchTask",
								e.getMessage());
					}
				}
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}
		
		@Override
		protected void onPostExecute(Object result) {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
					Log.i("ManagerActivity", "Connection closed.");
				}
			} catch (Exception e) {
				Log.e("ManagerActivity", e.getMessage());
			}
			
			if (managersAdapter != null) {
				managersList.setAdapter(managersAdapter);
			}

			progressDialog.dismiss();
			super.onPostExecute(result);
		}
	}*/
}
