package ru.alkise.trader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.db.mssql.SQLConnectionFactory;
import ru.alkise.trader.model.OrderIntf;
import ru.alkise.trader.model.OrganizationIntf;
import ru.alkise.trader.model.factory.OrganizationFactory;
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

public class OrganizationsActivity extends Activity {
	private Activity activity;
	private Connection connection;
	private Intent data;
	private int organizationCode;
	private ProgressDialog progressDialog;
	private ListView organizationsList;
	private TextView headingLabel;
	private ArrayAdapter<OrganizationIntf> organizationsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection_layout);

		activity = this;
		data = new Intent();

		organizationCode = getIntent().getIntExtra(OrderIntf.ORDER_ORGANIZATION, -1);

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.searching));

		headingLabel = (TextView) findViewById(R.id.lblHeadingSelectionLayout);
		headingLabel.setText(getString(R.string.choose_organization));

		organizationsList = (ListView) findViewById(R.id.listManagersSelectionLayout);
		organizationsList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapter, View arg1,
							int pos, long arg3) {
						data.putExtra(OrganizationIntf.TABLE_NAME,
								(OrganizationIntf) adapter.getItemAtPosition(pos));
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
		new OrganizationsSearchTask().execute();
		super.onStart();
	}

	private class OrganizationsSearchTask extends
			AsyncTask<Object, Object, Object> {
		private List<OrganizationIntf> organizations;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = SQLConnectionFactory.createTrade2000Connection();

				String query = "SELECT CODE, DESCR FROM SC308"
						+ (organizationCode != -1 ? " WHERE CODE = ?" : "");

				PreparedStatement pstmt = connection.prepareStatement(query);
				if (organizationCode != -1) {
					pstmt.setInt(1, organizationCode);
				}

				ResultSet rs = pstmt.executeQuery();

				if (rs.getFetchSize() > 0) {

					organizations = new ArrayList<OrganizationIntf>();

					while (rs.next()) {
						organizations.add(OrganizationFactory.createOrganization(rs.getInt(1), rs
								.getString(2)));
					}

					if (organizations.size() == 1) {
						data.putExtra(OrganizationIntf.TABLE_NAME,
								organizations.get(0));
						returnResult(data);
					}

					organizationsAdapter = new ArrayAdapter<OrganizationIntf>(
							activity, android.R.layout.simple_list_item_1,
							organizations);
				}
			} catch (Exception e) {
				Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT)
						.show();
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (Exception e) {
						Log.e("OrganizationsActivity.RemainsSearchTask",
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
					Log.i("OrganizationsActivity", "Connection closed.");
				}
			} catch (Exception e) {
				Log.e("ManagerActivity", e.getMessage());
			}

			if (organizationsAdapter != null) {
				organizationsList.setAdapter(organizationsAdapter);
			}

			progressDialog.dismiss();
			super.onPostExecute(result);
		}

	}
}
