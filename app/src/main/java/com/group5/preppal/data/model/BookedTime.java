package com.group5.preppal.data.model;

import java.util.Date;

public class BookedTime {
    private Date date;
    private String studentId;

    public BookedTime() {}

    public BookedTime(Date date, String studentId) {
        this.date = date;
        this.studentId = studentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
