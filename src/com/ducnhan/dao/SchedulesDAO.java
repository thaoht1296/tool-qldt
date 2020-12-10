package com.ducnhan.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ducnhan.dbutils.Icommon;
import com.ducnhan.model.Schedule;


public class SchedulesDAO extends Icommon<Schedule>{

	public SchedulesDAO() {
		super();
	}

	@Override
	public List<Schedule> getAll() {
		List<Schedule> listSchedules = new ArrayList<Schedule>();
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM `schedules`");
			while(resultSet.next()) {
				Schedule schedule = new Schedule(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getInt(4), resultSet.getString(5), resultSet.getDate(6), resultSet.getInt(7),
						resultSet.getInt(8), resultSet.getTime(9), resultSet.getString(10), resultSet.getString(11),
						resultSet.getString(12), resultSet.getString(13), resultSet.getString(14));
				listSchedules.add(schedule);
				System.out.println(schedule);
			}
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + " : " + e.getMessage() );
		}
		return listSchedules;
	}

	@Override
	public List<Schedule> getById(String id) {
		return null;
	}

	@Override
	public void insert(Schedule t) {
		String query = " INSERT INTO schedules(schedule_id, subject_name, subject_code, num_of_credits, day_of_week, date,"
				+ " lesson_start, num_of_lesson, time_start, teacher, class_name, room, week, semester) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

		try {
			PreparedStatement preStmt = this.conn.prepareStatement(query);
			preStmt.setString(1, t.getId());
			preStmt.setString(2, t.getSubjectName());
			preStmt.setString(3, t.getSubjectCode());
			preStmt.setInt(4, t.getNumOfCredits());
			preStmt.setString(5, t.getDayOfWeek());
			preStmt.setDate(6, new Date(t.getDate().getTime()));
			preStmt.setInt(7, t.getLessonStart());
			preStmt.setInt(8, t.getNumOfLesson());
			preStmt.setTime(9, t.getTimeStart());
			preStmt.setString(10, t.getTeacher());
			preStmt.setString(11, t.getClassName());
			preStmt.setString(12, t.getRoom());
			preStmt.setString(13, t.getWeek());
			preStmt.setString(14, t.getSemester());
			preStmt.executeUpdate();
			this.conn.commit();
			System.out.println("Inserted into schedules successfully!");
		} catch (SQLException e) {
			String errMess = e.getMessage();
			if (errMess.contains("Duplicate") && errMess.contains("'PRIMARY'")) {
				System.out.println("Schedule already exists!");
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
	public void update(Schedule t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Schedule t) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Schedule> getByUsernameQldt(String usernameQldt){
		List<Schedule> listSchedules = new ArrayList<Schedule>();
		try {
			String query = "SELECT schedules.* FROM `schedules`, `users_qldt_schedules`"
							+ " where username_qldt = ? and schedules.schedule_id = users_qldt_schedules.schedule_id";
			PreparedStatement preStmt = this.conn.prepareStatement(query);
			preStmt.setString(1, usernameQldt);
			ResultSet resultSet = preStmt.executeQuery();
			while(resultSet.next()) {
				Schedule schedule = new Schedule(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
						resultSet.getInt(4), resultSet.getString(5), resultSet.getDate(6), resultSet.getInt(7),
						resultSet.getInt(8), resultSet.getTime(9), resultSet.getString(10), resultSet.getString(11),
						resultSet.getString(12), resultSet.getString(13), resultSet.getString(14));
				listSchedules.add(schedule);
				System.out.println(schedule);
			}
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + " : " + e.getMessage() );
		}
		return listSchedules;
	}

}
