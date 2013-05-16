package ru.alkise.trader.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ru.alkise.trader.db.mssql.SQLConnectionFactory;
import ru.alkise.trader.model.Client;
import ru.alkise.trader.model.ClientType;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class SearchClientsTask extends AsyncTask<Object, List<Client>, Object> {
	private Connection connection;
	private String searchingString;
	private ProgressDialog searchingDialog;
	private List<Client> clients;
	private static String[] parents = { "   2MA   ", "   2M8   ", "   5EY   ",
			"   2MC   " };

	@Override
	protected Object doInBackground(Object... params) {
		try {
			connection = SQLConnectionFactory.createTrade2000Connection();
			searchingString = String.valueOf(params[0]).trim();

			searchingDialog = (ProgressDialog) params[1];

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
			}
		} catch (Exception e) {

		}
		return clients;
	}

	@Override
	protected void onPostExecute(Object result) {
		searchingDialog.dismiss();
		super.onPostExecute(result);
	}

}
