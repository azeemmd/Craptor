package craptor.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryHelper {
	private Connection connection;

	private Statement stmt;

	public void init(IEnvConnection conn) throws Exception {
		connection = conn.getConnection(); 
		connection.setAutoCommit(false);
		stmt = connection.createStatement();
	}

	public static String testConnection(EnvConnection conn){

		try {
			conn.loadDriver();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Connection connection = conn.getConnection();
			connection.close();
			return "success";
		} catch (Exception e) {
			return "Failed : " + e.getMessage();
		}
	}

	public void close() throws SQLException {
		stmt.close();
		connection.close();
	}

	public ResultSet getResultSet(String query) throws SQLException {
		return stmt.executeQuery(query);
	}

	public int executeQuery(String query) throws SQLException{
		return stmt.executeUpdate(query);
	}

	public void executeCommit() throws SQLException
	{
		connection.commit();
	}

	public String[][] descTable(String table) throws SQLException {
		String query = "select * from " + table + " where 1 = 2";
		ResultSet rs = getResultSet(query);
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		String[][] columnTypes = new String[columnCount][2];
		for (int i = 0; i < columnCount; ++i) {
			columnTypes[i][0] = metaData.getColumnName(i+1);
			columnTypes[i][1] = metaData.getColumnTypeName(i+1);
		}
		return columnTypes;
	}

	public QueryHelper(IEnvConnection conn) throws Exception {
		init(conn);
	}
}
