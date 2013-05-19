package ru.alkise.trader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.adapter.RemainsAdapter;
import ru.alkise.trader.db.mssql.SQLConnectionFactory;
import ru.alkise.trader.model.DocumentType;
import ru.alkise.trader.model.GoodsIntf;
import ru.alkise.trader.model.OrderIntf;
import ru.alkise.trader.model.PositionIntf;
import ru.alkise.trader.model.Warehouses;
import ru.alkise.trader.model.factory.GoodsFactory;
import ru.alkise.trader.model.factory.PositionFactory;
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

public class RemainsActivity extends Activity {
	private TextView codeLabel;
	private TextView descrLabel;
	private ListView remainsList;
	private GoodsIntf goods;
	private int code;
	private int whCode;
	private ArrayAdapter<PositionIntf> remainsAdapter;
	private Connection connection;
	private Activity activity;
	private ProgressDialog progressDialog;
	private Intent data;
	private DocumentType docType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remains_layout);

		activity = this;
		data = new Intent();
		docType = (DocumentType) getIntent().getSerializableExtra(OrderIntf.ORDER_TYPE);
		code = getIntent().getIntExtra(GoodsIntf.GOODS_CODE, -1);
		whCode = getIntent().getIntExtra(PositionIntf.POSITION_FROM_WAREHOUSE, -1);

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
						data.putExtra(PositionIntf.TABLE_NAME,
								(PositionIntf) adapter.getItemAtPosition(pos));
						returnResult();
					}
				});

		codeLabel.setText(String.valueOf(code));
	}

	private void returnResult() {
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		new RemainsSearchingTask().execute();
	}

	private class RemainsSearchingTask extends
			AsyncTask<Object, Object, Object> {
		private List<PositionIntf> positions;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = SQLConnectionFactory.createTrade2000Connection();

				String query = "SELECT SC14.CODE, SC14.DESCR, RG46.SP49, SC12.CODE "
						+ "FROM RG46 "
						+ "LEFT JOIN SC14 ON (SC14.ID = RG46.SP48) "
						+ "LEFT JOIN SC12 ON (SC12.ID = RG46.SP47) "
						+ "WHERE SC14.CODE = ? "
						+ (whCode != -1 ? "AND SC12.CODE = ?" : "")
						+ "AND RG46.PERIOD = (SELECT MAX(PERIOD) FROM RG46) "
						+ "ORDER BY SC14.DESCR";

				String demandQuery = "SELECT SC14.CODE, SC14.DESCR, 1, SC12.CODE "
						+ "FROM SC14 "
						+ "LEFT JOIN SC12 ON (SC12.ID = SC14.SP1225) "
						+ "WHERE SC14.CODE = ? "
						+ (whCode != -1 ? "AND SC12.CODE = ?" : "")
						+ "ORDER BY SC14.DESCR ";

				PreparedStatement pstmt = connection
						.prepareStatement(docType == DocumentType.DEMAND ? demandQuery
								: query);
				pstmt.setInt(1, code);
				if (whCode != -1) {
					pstmt.setInt(2, whCode);
				}


				ResultSet rs = pstmt.executeQuery();
				
				if (rs.getFetchSize() > 0) {
					
					positions = new ArrayList<PositionIntf>();
					
					while (rs.next()) {
						
						if (rs.isFirst()) {
							goods = GoodsFactory.createGoods(rs.getInt(1), rs.getString(2),
									rs.getDouble(3));
						}
						
						positions.add(PositionFactory.createPosition(goods, rs.getDouble(3),
								Warehouses.INSTANCE.getWarehouseByCode(rs
										.getInt(4)), Warehouses.INSTANCE
										.getWarehouseByCode(rs.getInt(4))));
					}

					if (positions.size() == 1) {
						data.putExtra(PositionIntf.TABLE_NAME, positions.get(0));
						returnResult();
					}
					
					remainsAdapter = new RemainsAdapter(activity,
							R.layout.one_remain, positions);
				}
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
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
					Log.i("RemainsActivity", "Connection closed.");
				}
			} catch (Exception e) {
				Log.e("RemainsActivity", e.getMessage());
			}
			
			if (goods != null) {
				descrLabel.setText(goods.getGoodsName().trim());
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
		}
	}
}
