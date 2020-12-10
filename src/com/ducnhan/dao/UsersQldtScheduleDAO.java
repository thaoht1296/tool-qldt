package com.ducnhan.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.ducnhan.dbutils.Icommon;
import com.ducnhan.model.UsersQldtSchedule;

public class UsersQldtScheduleDAO extends Icommon<UsersQldtSchedule>{

	public UsersQldtScheduleDAO(){
		super();
	}

	@Override
	public List<UsersQldtSchedule> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsersQldtSchedule> getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(UsersQldtSchedule t) {
		String query = "INSERT INTO `users_qldt_schedules`(`username_qldt`, `schedule_id`, `created`)"
				+ " VALUES (?, ?, ?)";
		try {
			PreparedStatement preStmt = this.conn.prepareStatement(query);
			preStmt.setString(1, t.getUsernameQldt());
			preStmt.setString(2, t.getScheduleId());
			preStmt.setTimestamp(3, t.getCreated());
			preStmt.executeUpdate();
			this.conn.commit();
			System.out.println("Inserted into users_qldt_schedule successfully!");
		} catch (SQLException e) {
			String errMess = e.getMessage();
			if (errMess.contains("Duplicate") && errMess.contains("'PRIMARY'")) {
				System.out.println("users_qldt_schedule already exists!");
			} else {
				try {
					this.conn.rollback();
				} catch (SQLException sqlE) {
					sqlE.printStackTrace();
				}
				e.printStackTrace();
			}
		} finally {
			try {
				this.conn.close();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
		}
		
	}

	@Override
	public void update(UsersQldtSchedule t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UsersQldtSchedule t) {
		// TODO Auto-generated method stub
		
	}

}
