package com.group5.preppal.data.model;

import java.util.Date;

public class User {
    protected String uid;
    protected String email;
    protected String name;
    protected Date dateOfBirth;
    protected Gender gender;
    protected String role;

    public enum Gender {
        MALE, FEMALE, OTHER
    }


    public User() {}

    public User(String uid, String email, String name, Date dateOfBirth, Gender gender, String role) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.role = role;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
