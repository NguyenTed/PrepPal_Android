package com.group5.preppal.data.model;

import java.util.Date;

public class StudentBookedSpeaking {
    private Date bookedDate;
    private String speakingTestId;

    public StudentBookedSpeaking() {}

    public StudentBookedSpeaking(Date bookedDate, String speakingTestId) {
        this.bookedDate = bookedDate;
        this.speakingTestId = speakingTestId;
    }

    public Date getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(Date bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getSpeakingTestId() {
        return speakingTestId;
    }

    public void setSpeakingTestId(String speakingTestId) {
        this.speakingTestId = speakingTestId;
    }
}
