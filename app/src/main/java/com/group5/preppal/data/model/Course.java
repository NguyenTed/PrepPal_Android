package com.group5.preppal.data.model;

public class Course {
    private String courseId;
    private float entryLevel, targetLevel;

    public Course(String courseId, float entryLevel, float targetLevel) {
        this.courseId = courseId;
        this.entryLevel = entryLevel;
        this.targetLevel = targetLevel;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public float getEntryLevel() {
        return entryLevel;
    }

    public void setEntryLevel(float entryLevel) {
        this.entryLevel = entryLevel;
    }

    public float getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(float targetLevel) {
        this.targetLevel = targetLevel;
    }
}
