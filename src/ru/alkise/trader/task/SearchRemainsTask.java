package ru.alkise.trader.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Goods;
import ru.alkise.trader.model.Order;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.SearchResult;
import ru.alkise.trader.model.SearchResults;
import ru.alkise.trader.model.Warehouses;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchRemainsTask extends AsyncTask<Object, Object, Object> {
	private Connection connection;
	private Activity activity;
	private ProgressDialog loadingDialog;
	private List<SearchResult> results;
	private ArrayAdapter<SearchResult> remainsAdapter;
	private String searchingValue;
	public static final boolean SEARCH_BY_CODE = true;
	public static final boolean SEARCH_BY_NAME = false;

	@Override
	protected Object doInBackground(Object... params) {
		connection = (Connection) params[0];
		searchingValue = String.valueOf(params[1]).trim();
		loadingDialog = (ProgressDialog) params[2];
		activity = (Activity) params[3];
		
		if (searchingValue.length() > 0 && connection != null) {
			try {
				String query = "SELECT SC14.ID, SC14.CODE, SC14.DESCR, RG46.SP47, RG46.SP49 FROM SC14, RG46 WHERE SC14.ID = RG46.SP48 AND RG46.PERIOD = (SELECT MAX(PERIOD) FROM RG46) AND SC14.CODE = ? ORDER BY SC14.DESCR";

				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setString(1, searchingValue);

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					if (rs.isFirst()) {
						SearchResults.INSTANCE.clear();
						SearchResults.INSTANCE
								.setGoods(new Goods(rs.getString(1), rs
										.getString(2), rs.getString(3)));
					}
					SearchResults.INSTANCE
							.addRemain(Warehouses.INSTANCE.getWarehouseById(rs
									.getString(4)), rs.getDouble(5));
				}

				results = SearchResults.INSTANCE.getResults();
				remainsAdapter = new ArrayAdapter<SearchResult>(activity,
						android.R.layout.simple_expandable_list_item_1, results) {

					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
						View view = super
								.getView(position, convertView, parent);
						TextView textView = (TextView) view
								.findViewById(android.R.id.text1);
						textView.setTextSize(17);
						textView.setTextColor(Color.BLACK);
						return view;
					}

				};

			} catch (Exception e) {
				Log.e("SearchRemainsTask", e.getMessage());
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (!SearchResults.IS_EMPTY && remainsAdapter != null) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					activity);
			alertDialogBuilder.setAdapter(remainsAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Order.INSTANCE.addPosition(new Position(
									SearchResults.INSTANCE.getGoods(), results
											.get(which).getCount(), results
											.get(which).getWarehouse(), results
											.get(which).getWarehouse()));
							SearchResults.INSTANCE.clear();
							results.clear();
						}

					});
			alertDialogBuilder.setTitle(SearchResults.INSTANCE.getGoods()
					.getDescr() + " : " + results.size());
			alertDialogBuilder.setNegativeButton(
					activity.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface p1, int p2) {
							p1.cancel();
							SearchResults.INSTANCE.clear();
							results.clear();
						}

					});
			
			AlertDialog remainsDialog = alertDialogBuilder.create();
			remainsDialog.show();
		}
		
		loadingDialog.dismiss();
		super.onPostExecute(result);
	}

}
