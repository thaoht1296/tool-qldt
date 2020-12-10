package com.ducnhan.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {

	public static String strHost = "localhost";
	public static String port = "3306";
	public static String strDbName = "jpn_crawl_qldt";
	public static String strUserName = "root";
	public static String strPassword = "";
	public static String strDbClass = "com.mysql.cj.jdbc.Driver";

	public DbUtils() {
		// do nothing
	}

	public static Connection openConnection() throws ClassNotFoundException, SQLException {
		Class.forName(strDbClass);
		String mysqlURL = "jdbc:mysql://" + strHost + ":" + port + "/" + strDbName
				+ "?useTimezone=true&useJDBCCompliantTimezoneShift=true&serverTimezone=Asia/Ho_Chi_Minh";
		Connection conn = DriverManager.getConnection(mysqlURL, strUserName, strPassword);
		return conn;
	}

	public static void close(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

}
