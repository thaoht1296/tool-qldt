/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ducnhan.model;

import java.util.Comparator;
/**
 *
 * @author Hoa Nguyen
 */
public class Announcement {
    private String url;
    private String ann_id;
    private String dateAnn;
    private String title;
    private String content;

    public Announcement(){
        
    }

    public Announcement(String url, String dateAnn, String title, String content) {
        this.url = url;
        this.dateAnn = dateAnn;
        this.title = title;
        this.content = content;
//        ann_id = url;
    }

    public String getDateAnn() {
        return dateAnn;
    }
    
    public String getUrl() {
        return url;
    }

    public String getAnn_id() {
        return ann_id;
    }
    
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDateAnn(String dateAnn) {
        this.dateAnn = dateAnn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAnn_id(String ann_id) {
        this.ann_id = ann_id;
    }

    public String annId(String id) {
        int idx = 0;
        for(int i = 0; i < url.length(); i++){
            if(Character.isDigit(url.charAt(i))){
                idx = i;
                break;
            }
        }
        return url.substring(idx, url.length()-1);
    }
    
    public static Comparator<Announcement> compare = new Comparator<Announcement>() {
        @Override
        public int compare(Announcement a1, Announcement a2){
            if(a1.url.compareTo(a2.url) == 0){
                return a1.url.compareTo(a2.url);
            }
            else
                return a1.dateAnn.compareTo(a2.dateAnn);
        }
    };
    

}
