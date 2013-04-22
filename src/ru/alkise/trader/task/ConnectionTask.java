package ru.alkise.trader.task;
import android.os.*;
import java.sql.*;
import ru.alkise.trader.sql.*;
import android.util.*;
import android.app.*;
import ru.alkise.trader.*;

class ConnectionTask extends AsyncTask<Object, Object, Object> {
	private Connection connection;
	private ProgressDialog loadingDialog;
	private Activity parentActivity;

	@Override
	protected Connection doInBackground(Object... args) {
		try {
			SQLConnection.INSTANCE.createConnection(String.valueOf(args[0]), String.valueOf(args[1]),
													String.valueOf(args[2]), String.valueOf(args[3]),
													String.valueOf(args[4]));
			connection = SQLConnection.INSTANCE.getConnection();
			parentActivity = (Activity) args[5];
			loadingDialog = (ProgressDialog) args[6];
		} catch (Exception e) {
			Log.e("Connection Task", e.getMessage());
		}
		return connection;
	}

	@Override
	protected void onPostExecute(Object result) {
		loadingDialog.setMessage(parentActivity.getString(R.string.dataloading));

		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
	}
}

