package ru.alkise.trader.task;

import java.sql.Connection;

import ru.alkise.trader.sql.DBConnection;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionTask extends AsyncTask<Object, Object, Object> {
	private Connection connection;
	private ProgressDialog loadingDialog;

	@Override
	protected Connection doInBackground(Object... args) {
		loadingDialog = (ProgressDialog) args[5];

		try {
			DBConnection.INSTANCE.createConnection(String.valueOf(args[0]),
					String.valueOf(args[1]), String.valueOf(args[2]),
					String.valueOf(args[3]), String.valueOf(args[4]));
			connection = DBConnection.INSTANCE.getConnection();
		} catch (Exception e) {
			Log.e("Connection task", e.getMessage());
		}
		return connection;
	}

	@Override
	protected void onPostExecute(Object result) {
		loadingDialog.dismiss();
		super.onPostExecute(result);
	}
}
