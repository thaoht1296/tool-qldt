package com.ducnhan.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ducnhan.dbutils.Icommon;
import com.ducnhan.model.UserQldt;

public class UsersQldtDAO extends Icommon<UserQldt> {

    public UsersQldtDAO() {
        super();
    }

    @Override
    public List<UserQldt> getAll() {
        List<UserQldt> listUsers = new ArrayList<>();
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM users_qldt");
            while (resultSet.next()) {
                UserQldt user = new UserQldt(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getDate(4));
                listUsers.add(user);
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + " : " + e.getMessage());
        }
        return listUsers;
    }

    @Override
    public List<UserQldt> getById(String username_qldt) {
        List<UserQldt> listUsers = new ArrayList<>();
        try {
            String query = "SELECT * FROM `users_qldt` where username_qldt = ?";
            PreparedStatement preStmt = this.conn.prepareStatement(query);
            preStmt.setString(1, username_qldt);
            ResultSet resultSet = preStmt.executeQuery();
            while (resultSet.next()) {
                UserQldt user = new UserQldt(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getDate(4));
                System.out.println(user);
                listUsers.add(user);
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + " : " + e.getMessage());
        }
        return listUsers;
    }

    @Override
    public void insert(UserQldt t) {
        String query = "INSERT INTO users_qldt(`username_qldt`, `password_qldt`, `name`, `date`)"
                + " VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preStmt = this.conn.prepareStatement(query);
            preStmt.setString(1, t.getUsername());
            preStmt.setString(2, t.getPassword());
            preStmt.setString(3, t.getName());
            preStmt.setDate(4, t.getDate() == null ? null : new Date(t.getDate().getTime()));
            preStmt.executeUpdate();
            this.conn.commit();
            System.out.println("Inserted into users_qldt successfully!");
        } catch (SQLException e) {
            String errMess = e.getMessage();
            if (errMess.contains("Duplicate") && errMess.contains("'PRIMARY'")) {
                System.out.println("UserQLDT already exists!");
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
    public void update(UserQldt t) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(UserQldt t) {
        // TODO Auto-generated method stub

    }

}
