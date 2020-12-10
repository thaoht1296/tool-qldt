/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ducnhan.dao;

import com.ducnhan.dbutils.Icommon;
import com.ducnhan.model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hoa Nguyen
 */
public class AnnouncementDAO extends Icommon<Announcement>{

    public AnnouncementDAO() throws ClassNotFoundException {
    }
    

    @Override
    public ArrayList<Announcement> getAll() {
        ArrayList<Announcement> lstAnn = new ArrayList<Announcement>();
        try {
            String sql = "SELECT * FROM tbl_ann";
            Statement statement = this.conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                Announcement ann = new Announcement(resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5));
                lstAnn.add(ann);
                System.out.println(ann);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AnnouncementDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lstAnn;
    }

    @Override
    public ArrayList<Announcement> getById(String id) {
        return null;
    }

    @Override
    public void insert(Announcement t){
//        int row = 0;
        try {
            String sql = "INSERT INTO tbl_ann (url, date, title, content) VALUE (?, ?, ?, ?);";
            PreparedStatement statement = this.conn.prepareStatement(sql);
            //dat gtri cho tham so da cho
            statement.setString(1, t.getUrl());
            statement.setString(2, t.getDateAnn());
            statement.setString(3, t.getTitle());
            statement.setString(4, t.getContent());
            //thuc hien truy van
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AnnouncementDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
//        dBUtil.closeConnection();
//        return row;
    }

    @Override
    public void update(Announcement t) {
//        return 0;
    }

    @Override
    public void delete(Announcement id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}