package com.group5.preppal.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Teacher extends User {
    private List<String> courses;

    public Teacher() {}

    public Teacher(String uid, String email, String name, Date dateOfBirth, Gender gender, String role) {
        super(uid, email, name, dateOfBirth, gender, role);
        this.courses = new ArrayList<>();
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
