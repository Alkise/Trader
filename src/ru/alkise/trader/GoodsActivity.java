package ru.alkise.trader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.adapter.FindingGoodsAdapter;
import ru.alkise.trader.db.mssql.SQLConnectionFactory;
import ru.alkise.trader.model.DocumentType;
import ru.alkise.trader.model.GoodsIntf;
import ru.alkise.trader.model.OrderIntf;
import ru.alkise.trader.model.factory.GoodsFactory;
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

public class GoodsActivity extends Activity {
	private DocumentType docType;
	private TextView lblPositionName;
	private String searchingString;
	private ListView goodsList;
	private ArrayAdapter<GoodsIntf> goodsAdapter;
	private ProgressDialog progressDialog;
	private Activity activity;
	private Connection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_layout);

		activity = this;

		docType = (DocumentType) getIntent().getSerializableExtra(OrderIntf.ORDER_TYPE);
		searchingString = getIntent().getStringExtra(GoodsIntf.GOODS_NAME);

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.searching));

		lblPositionName = (TextView) findViewById(R.id.lblPositionName);
		lblPositionName.setText(searchingString);

		goodsList = (ListView) findViewById(R.id.listPositionNames);
		goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int pos,
					long arg3) {
				Intent intent = new Intent("ru.alkise.trader.RemainsActivity");
				int code = ((GoodsIntf) adapter.getItemAtPosition(pos))
						.getGoodsCode();
				intent.putExtra(OrderIntf.ORDER_TYPE, docType);
				intent.putExtra(GoodsIntf.GOODS_CODE, code);
				startActivityForResult(intent, 1);
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		new GoodsSearchTask().execute();
	}

	@Override
	protected void onStop() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				Log.i("GoodsActivity", "Connection closed");
			}
		} catch (Exception e) {
			Log.e("GoodsActivity", e.getMessage());
		}
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK, data);
				finish();
			}
		}
	}

	private class GoodsSearchTask extends AsyncTask<Object, Object, Object> {

		private List<GoodsIntf> goods;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				connection = SQLConnectionFactory.createTrade2000Connection();

				String query = "SELECT SC14.CODE, SC14.DESCR, SUM(RG46.SP49) AS 'COUNT' "
						+ "FROM SC14 LEFT JOIN RG46 ON (RG46.SP48 = SC14.ID) "
						+ "WHERE SC14.SP135 = 1" 
						+ "AND SC14.DESCR LIKE ? "
						+ "AND RG46.PERIOD = (SELECT MAX(PERIOD) FROM RG46) "
						+ "GROUP BY SC14.ID, SC14.CODE,SC14.CODE, SC14.DESCR "
						+ "ORDER BY SC14.DESCR";

				String demandQuery = "SELECT SC14.CODE, SC14.DESCR, 1 FROM SC14 WHERE SC14.SP135 = 1 AND SC14.DESCR LIKE ?";

				PreparedStatement pstmt = connection
						.prepareStatement(docType == DocumentType.DEMAND ? demandQuery
								: query);
				pstmt.setString(1, "%" + searchingString + "%");

				ResultSet rs = pstmt.executeQuery();

				goods = new ArrayList<GoodsIntf>();

				while (rs.next()) {
					goods.add(GoodsFactory.createGoods(rs.getInt(1),
							rs.getString(2), rs.getDouble(3)));
				}

				goodsAdapter = new FindingGoodsAdapter(activity,
						R.layout.goods_remain_layout, goods);

			} catch (Exception e) {
				Log.e("GoogsActivity.GoodsSearchTask", e.getMessage());
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (Exception e) {
						Log.e("GoogsActivity.GoodsSearchTask", e.getMessage());
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (goodsAdapter != null) {
				goodsList.setAdapter(goodsAdapter);
			}
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			goods = new ArrayList<GoodsIntf>();
		}

	}
}
