package com.group5.preppal.data.model;

import java.util.Date;

public class User {
    private String uid;
    private String email;
    private String name;
    private Date dateOfBirth;

    public User() {}

    public User(String uid, String email, String name, Date dateOfBirth) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
