package com.group5.preppal.data.model.test;

import com.group5.preppal.data.model.test.listening.ListeningAttempt;

public class TestAttempt {
    private String testId;
    private String testSetId;
    private ListeningAttempt listeningAttempt;

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

    public ListeningAttempt getListeningAttempt() {
        return listeningAttempt;
    }

    public void setListeningAttempt(ListeningAttempt listeningAttempt) {
        this.listeningAttempt = listeningAttempt;
    }
}
