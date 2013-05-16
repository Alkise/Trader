package ru.alkise.trader.db.mssql;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnectionFactory {
	public static Connection createMSSQLConnection(String host, String port, String dbname,
			String user, String password) throws Exception  {
		Connection conn;
		StringBuilder connAdr = new StringBuilder("jdbc:jtds:sqlserver://");
		connAdr.append(host);
		connAdr.append(':');
		connAdr.append(port);
		connAdr.append('/');
		connAdr.append(dbname);
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		conn = DriverManager.getConnection(connAdr.toString(), user,
				password);
		return conn;
	}
	
	public static Connection createTrade2000Connection() throws Exception {
		Connection conn;
		conn = createMSSQLConnection("192.168.0.202", "1433", "trade2005", "test", "test");
		return conn;
	}
}
