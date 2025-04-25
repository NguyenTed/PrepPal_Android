package com.group5.preppal.data.model.test;

import com.google.firebase.firestore.PropertyName;

public class TestAttempt {
    private String testId;
    private String testSetId;
    private Float listeningBandScore;
    private Float readingBandScore;

    public TestAttempt() {}

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestSetId() {
        return testSetId;
    }

    public void setTestSetId(String testSetId) {
        this.testSetId = testSetId;
    }

    @PropertyName("listening_band_score")
    public Float getListeningBandScore() {
        return listeningBandScore;
    }

    @PropertyName("listening_band_score")
    public void setListeningBandScore(float listeningBandScore) {
        this.listeningBandScore = listeningBandScore;
    }

    @PropertyName("reading_band_score")
    public Float getReadingBandScore() {
        return readingBandScore;
    }

    @PropertyName("reading_band_score")
    public void setReadingBandScore(float readingBandScore) {
        this.readingBandScore = readingBandScore;
    }
}
