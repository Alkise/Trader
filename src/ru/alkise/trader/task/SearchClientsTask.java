package ru.alkise.trader.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.R;
import ru.alkise.trader.model.Client;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import ru.alkise.trader.model.*;
import ru.alkise.trader.sql.SQLConnectionFactory;

public class SearchClientsTask extends AsyncTask<Object, List<Client>, Object> {
	private Connection connection;
	private String searchingString;
	private ProgressDialog searchingDialog;
	private List<Client> clients;
	private Activity activity;
	private static ArrayAdapter<Client> clientAdapter;
	private static String[] parents = { "   2MA   ", "   2M8   ", "   5EY   ",
			"   2MC   " };

	@Override
	protected Object doInBackground(Object... params) {
		try {
			connection = SQLConnectionFactory.createTrade2000Connection();
			searchingString = String.valueOf(params[0]).trim();

			searchingDialog = (ProgressDialog) params[1];

			activity = (Activity) params[2];
			clients = new ArrayList<Client>();

			if (searchingString.length() >= 3 && connection != null) {

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT CODE, DESCR, SP237, SP3136 FROM SC16 WHERE DESCR LIKE ? AND (PARENTID = ? OR PARENTID = ? OR PARENTID = ? OR PARENTID = ?) ORDER BY DESCR");

				pstmt.setString(1, "%" + searchingString + "%");
				pstmt.setString(2, parents[0]);
				pstmt.setString(3, parents[1]);
				pstmt.setString(4, parents[2]);
				pstmt.setString(5, parents[3]);

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					clients.add(new Client(rs.getInt(1), rs
							.getString(2), rs.getString(3), ClientType._2F2.getClientTypeById(rs.getString(4))));
				}
				clientAdapter = new ArrayAdapter<Client>(activity,
						android.R.layout.simple_expandable_list_item_1, clients) {

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
			}
		} catch (Exception e) {

		}
		return clients;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (clientAdapter != null) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					activity);
			alertDialogBuilder.setAdapter(clientAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Order.setClient(clients.get(which));
							((EditText) activity.findViewById(R.id.clientField))
									.setText(clients.get(which).toString());
						}
					});
			alertDialogBuilder.setTitle(activity
					.getString(R.string.selectClient));
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
		}
		searchingDialog.dismiss();
		super.onPostExecute(result);
	}

}
