package ru.alkise.trader.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Goods;
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

public class SearchGoodsTask extends AsyncTask<Object, Object, Object> {
	private Connection connection;
	private String searchingString;
	private ProgressDialog searchingDialog;
	private List<Goods> goods;
	private Activity activity;
	private static ArrayAdapter<Goods> goodsAdapter;

	@Override
	protected Object doInBackground(Object... params) {
		connection = (Connection) params[0];
		searchingString = String.valueOf(params[1]).trim();

		searchingDialog = (ProgressDialog) params[2];
		searchingDialog.dismiss();

		activity = (Activity) params[3];

		if (searchingString.length() >= 3) {
			goods = new ArrayList<Goods>();

			try {
				PreparedStatement pstmt = connection
						.prepareStatement("SELECT SC14.ID, SC14.CODE, SC14.DESCR, SUM(RG46.SP49) FROM SC14, RG46 WHERE RG46.PERIOD = (SELECT MAX(PERIOD) FROM RG46) AND SC14.ID = RG46.SP48 AND DESCR LIKE ? GROUP BY SC14.ID, SC14.CODE,SC14.CODE, SC14.DESCR ORDER BY DESCR");
				pstmt.setString(1, "%" + searchingString + "%");

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					if (rs.getDouble(4) > 0) {
						goods.add(new Goods(rs.getString(1), rs.getString(2),
								rs.getString(3)));
					}
				}

				goodsAdapter = new ArrayAdapter<Goods>(activity,
						android.R.layout.simple_expandable_list_item_1, goods) {

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
				Log.e("SearchGoodsTask", e.getMessage());
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				activity);
		alertDialogBuilder.setAdapter(goodsAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						searchingDialog.show();
						
						SearchRemainsTask searchRemainsTask = new SearchRemainsTask();
						searchRemainsTask.execute(connection, goods.get(which)
								.getCode(), searchingDialog, activity);
					}
				});
		alertDialogBuilder.setTitle(activity
				.getString(R.string.selectNomeclature));
		alertDialogBuilder.setNegativeButton(
				activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		
		super.onPostExecute(result);
	}

}
