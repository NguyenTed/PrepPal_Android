package com.group5.preppal.data.model;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class SpeakingTest {
    private String id;
    private String name;
    private String availableMorningTime;
    private String availableAfternoonTime;
    private List<BookedTime>bookedTime;
    private String courseId;
    private String type;

    public SpeakingTest() {}

    public SpeakingTest(String id, String name, String availableMorningTime, String availableAfternoonTime, List<BookedTime> bookedTime, String courseId, String type) {
        this.id = id;
        this.name = name;
        this.availableMorningTime = availableMorningTime;
        this.availableAfternoonTime = availableAfternoonTime;
        this.bookedTime = bookedTime;
        this.courseId = courseId;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailableMorningTime() {
        return availableMorningTime;
    }

    public void setAvailableMorningTime(String availableMorningTime) {
        this.availableMorningTime = availableMorningTime;
    }

    public String getAvailableAfternoonTime() {
        return availableAfternoonTime;
    }

    public void setAvailableAfternoonTime(String availableAfternoonTime) {
        this.availableAfternoonTime = availableAfternoonTime;
    }

    public List<BookedTime> getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(List<BookedTime> bookedTime) {
        this.bookedTime = bookedTime;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
