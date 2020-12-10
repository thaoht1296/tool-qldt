package com.ducnhan.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class Icommon<T> {

	protected Connection conn;

	public Icommon() {
		try {
			conn = DbUtils.openConnection();
			conn.setAutoCommit(false);
			System.out.println("Connected successfully!");
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println(e.getClass().getName() + " : " + e.getMessage() );
		}
	}

	public abstract List<T> getAll();

	public abstract List<T> getById(String id);

	public abstract void insert(T t) throws SQLException;

	public abstract void update(T t);

	public abstract void delete(T t);

}
