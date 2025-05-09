package com.group5.preppal.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student extends User {
    protected float currentBand;
    protected float aimBand;
    private List<String> courses;
    private List<String> finishedLessons;
    private List<String> finishedSpeakingTests;
    private List<String> finishedCourses;
    private List<StudentBookedSpeaking> studentBookedSpeaking;

    public Student() {}

    public Student(String uid, String email, String name, Date dateOfBirth, Gender gender ) {
        super(uid, email, name, dateOfBirth, gender, "student");
        this.currentBand = 0;
        this.aimBand = 0;
        this.courses = new ArrayList<>();
        this.finishedLessons = new ArrayList<>();
    }


    public Student(String uid, String email, String name, Date dateOfBirth, Gender gender, String role, float currentBand, float aimBand, List<String> courses, List<String> finishedLessons, List<String> finishedSpeakingTests, List<String> finishedCourses ,List<StudentBookedSpeaking> studentBookedSpeaking) {
        super(uid, email, name, dateOfBirth, gender, role);
        this.currentBand = currentBand;
        this.aimBand = aimBand;
        this.courses = courses;
        this.finishedLessons = finishedLessons;
        this.finishedSpeakingTests = finishedSpeakingTests;
        this.finishedCourses = finishedCourses;
        this.studentBookedSpeaking = studentBookedSpeaking;
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

    public List<String> getFinishedLessons() {
        return finishedLessons;
    }

    public void setFinishedLessons(List<String> finishedLessons) {
        this.finishedLessons = finishedLessons;
    }

    public List<String> getFinishedSpeakingTests() {
        return finishedSpeakingTests;
    }

    public void setFinishedSpeakingTests(List<String> finishedSpeakingTests) {
        this.finishedSpeakingTests = finishedSpeakingTests;
    }

    public List<String> getFinishedCourses() {
        return finishedCourses;
    }

    public void setFinishedCourses(List<String> finishedCourses) {
        this.finishedCourses = finishedCourses;
    }

    public List<StudentBookedSpeaking> getStudentBookedSpeaking() {
        return studentBookedSpeaking;
    }

    public void setStudentBookedSpeaking(List<StudentBookedSpeaking> studentBookedSpeaking) {
        this.studentBookedSpeaking = studentBookedSpeaking;
    }

    public List<StudentBookedSpeaking> getBookedSpeaking() {
        return studentBookedSpeaking;
    }

    public void setBookedSpeaking(List<StudentBookedSpeaking> studentBookedSpeaking) {
        this.studentBookedSpeaking = studentBookedSpeaking;
    }
}
