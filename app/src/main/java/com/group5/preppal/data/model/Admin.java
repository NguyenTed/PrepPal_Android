package com.group5.preppal.data.model;

import java.util.Date;

public class Admin extends User {

    public Admin() {}

    public Admin(String uid, String email, String name, Date dateOfBirth, Gender gender, String role) {
        super(uid, email, name, dateOfBirth, gender, role);
    }
}
