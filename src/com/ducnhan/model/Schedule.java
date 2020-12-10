/**
 *
 */
package com.ducnhan.model;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.assertj.core.util.ToString;

/**
 * @author nhani
 *
 */
public class Schedule {

    /**
     * ====== Class dinh nghia thong tin mon hoc ======
     */
    private String id;
    private String subjectName;
    private String subjectCode;
    private int numOfCredits;
    private String dayOfWeek;
    private Date date;
    private int lessonStart;
    private int numOfLesson;
    private Time timeStart;
    private String teacher;
    private String className;
    private String room;
    private String week;
    private String semester;

    public Schedule() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Schedule(String id, String subjectName, String subjectCode, int numOfCredits, String dayOfWeek, Date date,
            int lessonStart, int numOfLesson, Time timeStart, String teacher, String className, String room,
            String week, String semester) {
        super();
        this.id = id;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.numOfCredits = numOfCredits;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.lessonStart = lessonStart;
        this.numOfLesson = numOfLesson;
        this.timeStart = timeStart;
        this.teacher = teacher;
        this.className = className;
        this.room = room;
        this.week = week;
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getNumOfCredits() {
        return numOfCredits;
    }

    public void setNumOfCredits(int numOfCredits) {
        this.numOfCredits = numOfCredits;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getLessonStart() {
        return lessonStart;
    }

    public void setLessonStart(int lessonStart) {
        this.lessonStart = lessonStart;
    }

    public int getNumOfLesson() {
        return numOfLesson;
    }

    public void setNumOfLesson(int numOfLesson) {
        this.numOfLesson = numOfLesson;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Schedule [id = " + id + ", subjectName = " + subjectName + ", subjectCode = " + subjectCode
                + ", numOfCredits = " + numOfCredits + ", dayOfWeek = " + dayOfWeek + ", date = " + date + ", lessonStart="
                + lessonStart + ", numOfLesson = " + numOfLesson + ", timeStart = " + timeStart + ", teacher = " + teacher
                + ", className = " + className + ", room = " + room + ", week = " + week + ", semester = " + semester + "]";
    }

    public String getDescription() {
        return "Schedule\nid: " + id
                + ",\nTên môn học: " + subjectName
                + ",\nMã môn học: " + subjectCode
                + ",\nPhòng: " + room
                + ",\nSố tín chỉ: " + numOfCredits
                + ",\nNgày: " + dayOfWeek + ", " + new SimpleDateFormat("dd-MM-yyyy").format(date)
                + ",\nGiờ bắt đầu: " + timeStart
                + ",\nTiết bắt đầu: " + lessonStart
                + ",\nSố tiết: " + numOfLesson
                + ",\nGiảng viên: " + teacher
                + ",\nLớp: " + className
                + ",\nTuần: " + week
                + ",\nHọc kỳ: " + semester
                + " \nBạn ơi đừng nghỉ nữa! :v";
    }
}
