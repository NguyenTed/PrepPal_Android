package com.group5.preppal.data.model.test.listening;

import java.util.Date;
import java.util.Map;

public class ListeningAttempt {
    private String testId;
    private String testSetId;
    private Map<String, String> answers;// questionNumber -> userAnswer
    private int rawScore;
    private float bandScore;
    private Date startedAt;
    private Date submittedAt;

    public ListeningAttempt() {}

    // Getters & setters
    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }

    public String getTestSetId() { return testSetId; }
    public void setTestSetId(String testSetId) { this.testSetId = testSetId; }

    public Map<String, String> getAnswers() { return answers; }
    public void setAnswers(Map<String, String> answers) { this.answers = answers; }

    public int getRawScore() { return rawScore; }
    public void setRawScore(int rawScore) { this.rawScore = rawScore; }

    public float getBandScore() { return bandScore; }
    public void setBandScore(float bandScore) { this.bandScore = bandScore; }

    public Date getStartedAt() { return startedAt; }
    public void setStartedAt(Date startedAt) { this.startedAt = startedAt; }

    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }
}