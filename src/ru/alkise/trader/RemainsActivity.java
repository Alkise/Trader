package ru.alkise.trader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.adapter.RemainsAdapter;
import ru.alkise.trader.model.Goods;
import ru.alkise.trader.model.OrderType;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.Warehouses;
import ru.alkise.trader.sql.SQLConnectionFactory;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class RemainsActivity extends Activity {
	private TextView codeLabel;
	private TextView descrLabel;
	private ListView remainsList;
	private Goods goods;
	private int code;
	private RemainsAdapter remainsAdapter;
	private Connection connection;
	private Activity activity;
	private ProgressDialog progressDialog;
	private Intent data;
	private OrderType docType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remains_layout);

		activity = this;
		data = new Intent();
		docType = (OrderType) getIntent().getSerializableExtra("docType");
		code = getIntent().getIntExtra("code", 0);

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.searching));

		codeLabel = (TextView) findViewById(R.id.lblGoodsCode);
		descrLabel = (TextView) findViewById(R.id.lblGoodsDescr);
		remainsList = (ListView) findViewById(R.id.listRemains);
		remainsList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapter, View arg1,
							int pos, long arg3) {
						data.putExtra("position",
								(Position) adapter.getItemAtPosition(pos));
						setResult(RESULT_OK, data);
						finish();
					}
				});

		codeLabel.setText(String.valueOf(code));
	}

	@Override
	protected void onStart() {
		super.onStart();
		new RemainsSearchingTask().execute();
	}

	@Override
	protected void onStop() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				Log.i("RemainsActivity", "Connection closed.");
			}
		} catch (Exception e) {
			Log.e("RemainsActivity", e.getMessage());
		}
		super.onStop();
	}

	private class RemainsSearchingTask extends
			AsyncTask<Object, Object, Object> {
		private List<Position> positions;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = SQLConnectionFactory.createTrade2000Connection();

				String query = "SELECT SC14.CODE, SC14.DESCR, RG46.SP49, SC12.CODE "
						+ "FROM RG46 "
						+ "LEFT JOIN SC14 ON (SC14.ID = RG46.SP48) "
						+ "LEFT JOIN SC12 ON (SC12.ID = RG46.SP47) "
						+ "WHERE SC14.CODE LIKE ? "
						+ "AND RG46.PERIOD = (SELECT MAX(PERIOD) FROM RG46) "
						+ "ORDER BY SC14.DESCR";

				String demandQuery = "SELECT SC14.CODE, SC14.DESCR, 1, SC12.CODE "
						+ "FROM SC14 "
						+ "LEFT JOIN SC12 ON (SC12.ID = SC14.SP1225) "
						+ "WHERE SC14.CODE = ? " + "ORDER BY SC14.DESCR ";

				PreparedStatement pstmt = connection
						.prepareStatement(docType == OrderType.DEMAND ? demandQuery
								: query);
				pstmt.setInt(1, code);

				positions = new ArrayList<Position>();

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					if (rs.isFirst()) {
						goods = new Goods(rs.getInt(1), rs.getString(2));
					}
					positions
							.add(new Position(goods, rs.getDouble(3),
									Warehouses.INSTANCE.getWarehouseByCode(rs
											.getInt(4)), Warehouses.INSTANCE
											.getWarehouseByCode(rs.getInt(4))));
				}

				remainsAdapter = new RemainsAdapter(activity,
						R.layout.one_remain, positions);
			} catch (Exception e) {
				Log.e("RemainsActivity.RemainsSearchTask", e.getMessage());
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (Exception e) {
						Log.e("RemainsActivity.RemainsSearchTask",
								e.getMessage());
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (goods != null) {
				descrLabel.setText(goods.getDescr().trim());
			}

			if (remainsAdapter != null) {
				remainsList.setAdapter(remainsAdapter);
			}

			progressDialog.dismiss();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			positions = new ArrayList<Position>();
		}
	}
}
