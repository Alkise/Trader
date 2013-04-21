package ru.alkise.trader.sql;

import java.sql.Connection;
import java.sql.DriverManager;

public enum SQLConnection {
	INSTANCE;

	private Connection conn;

	public Connection createConnection(String host, String port, String dbname,
			String user, String password) throws Exception {
		if (conn == null) {
			StringBuilder connAdr = new StringBuilder("jdbc:jtds:sqlserver://");
			connAdr.append(host);
			connAdr.append(':');
			connAdr.append(port);
			connAdr.append('/');
			connAdr.append(dbname);
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			conn = DriverManager.getConnection(connAdr.toString(), user,
					password);
		}
		return conn;
	}

	public Connection getConnection() {
		return conn;
	}

	public void closeConnection() throws Exception {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
}
