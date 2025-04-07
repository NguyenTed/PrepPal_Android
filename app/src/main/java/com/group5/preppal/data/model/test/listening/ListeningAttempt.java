package com.group5.preppal.data.model.test.listening;

import java.util.Date;
import java.util.Map;

public class ListeningAttempt {
    private Map<String, String> answers; // questionNumber -> answer
    private int score;
    private Date startedAt;
    private Date submittedAt;

    public ListeningAttempt() {}

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }
}
