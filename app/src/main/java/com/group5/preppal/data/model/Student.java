package com.group5.preppal.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student extends User {
    protected float currentBand;
    protected float aimBand;
    private List<String> courses;

    public Student() {}

    public Student(String uid, String email, String name, Date dateOfBirth, Gender gender ) {
        super(uid, email, name, dateOfBirth, gender, "student");
        this.currentBand = 0;
        this.aimBand = 0;
        this.courses = new ArrayList<>();
    }

    public float getCurrentBand() {
        return currentBand;
    }

    public void setCurrentBand(float currentBand) {
        this.currentBand = currentBand;
    }

    public float getAimBand() {
        return aimBand;
    }

    public void setAimBand(float aimBand) {
        this.aimBand = aimBand;
    }

    public List<String> getCourses() {
        return courses != null ? courses : new ArrayList<>();
    }

    public void setCourses(List<String> courses) {
        this.courses = courses != null ? courses : new ArrayList<>();
    }

}
