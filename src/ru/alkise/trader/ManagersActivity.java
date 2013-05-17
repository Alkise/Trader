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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagersActivity extends Activity {
	private Activity activity;
	private Connection connection;
	private Intent data;
	private int managerCode;
	private ProgressDialog progressDialog;
	private ListView managersList;
	private TextView headingLabel;
	private ArrayAdapter<Manager> managersAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection_layout);

		activity = this;
		data = new Intent();

		managerCode = getIntent().getIntExtra(Order.MANAGER_CODE, -1);

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.searching));

		headingLabel = (TextView) findViewById(R.id.lblHeadingSelectionLayout);
		headingLabel.setText(getString(R.string.choose_manager));
		
		managersList = (ListView) findViewById(R.id.listManagersSelectionLayout);
		managersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int pos,
					long arg3) {
				data.putExtra(Manager.TABLE_NAME, (Manager) adapter.getItemAtPosition(pos));
				returnResult(data);
			}
		});
	}

	private void returnResult(Intent intent) {
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	protected void onStart() {
		new ManagerSearchTask().execute();
		super.onStart();
	}

	private class ManagerSearchTask extends AsyncTask<Object, Object, Object> {
		private List<Manager> managers;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = SQLConnectionFactory.createTrade2000Connection();

				String query = "SELECT CODE, DESCR FROM SC377 WHERE"
						+ (managerCode != -1 ? " CODE = ? AND" : "")
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
					
					if (managers.size() == 1) 
					{
						data.putExtra(Manager.TABLE_NAME, managers.get(0));
						returnResult(data);
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
	}
}
