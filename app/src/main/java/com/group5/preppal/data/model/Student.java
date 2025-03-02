package com.group5.preppal.data.model;

import java.util.Date;

public class Student extends User {
    private float level = 0;

    public Student() {}

    public Student(String uid, String email, String name, Date dateOfBirth, Gender gender) {
        super(uid, email, name, dateOfBirth, gender, "student");
    }
}
