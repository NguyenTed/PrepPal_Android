package com.group5.preppal.data.model.test.reading;

import android.os.Parcel;
import android.os.Parcelable;

import com.group5.preppal.data.model.test.SkillAttempt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReadingAttempt implements Parcelable, SkillAttempt {
    private String testId;
    private String testSetId;
    private Map<String, String> answers;// questionNumber -> userAnswer
    private int rawScore;
    private float bandScore;
    private Date startedAt;
    private Date submittedAt;

    public ReadingAttempt() {
        answers = new HashMap<>();
    }

    protected ReadingAttempt(Parcel in) {
        testId = in.readString();
        testSetId = in.readString();

        int size = in.readInt();
        answers = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            answers.put(key, value);
        }

        rawScore = in.readInt();
        bandScore = in.readFloat();

        long startedAtMillis = in.readLong();
        startedAt = startedAtMillis == -1 ? null : new Date(startedAtMillis);

        long submittedAtMillis = in.readLong();
        submittedAt = submittedAtMillis == -1 ? null : new Date(submittedAtMillis);
    }

    public static final Creator<ReadingAttempt> CREATOR = new Creator<ReadingAttempt>() {
        @Override
        public ReadingAttempt createFromParcel(Parcel in) {
            return new ReadingAttempt(in);
        }

        @Override
        public ReadingAttempt[] newArray(int size) {
            return new ReadingAttempt[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testId);
        dest.writeString(testSetId);

        dest.writeInt(answers != null ? answers.size() : 0);
        if (answers != null) {
            for (Map.Entry<String, String> entry : answers.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }

        dest.writeInt(rawScore);
        dest.writeFloat(bandScore);
        dest.writeLong(startedAt != null ? startedAt.getTime() : -1);
        dest.writeLong(submittedAt != null ? submittedAt.getTime() : -1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters & setters
    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }

    public String getTestSetId() { return testSetId; }
    public void setTestSetId(String testSetId) { this.testSetId = testSetId; }

    public Map<String, String> getAnswers() { return answers; }
    public void setAnswers(Map<String, String> answers) { this.answers = answers; }

    public int getRawScore() { return rawScore; }
    public void setRawScore(int rawScore) { this.rawScore = rawScore; }

    @Override
    public float getBandScore() { return bandScore; }
    public void setBandScore(float bandScore) { this.bandScore = bandScore; }

    public Date getStartedAt() { return startedAt; }
    public void setStartedAt(Date startedAt) { this.startedAt = startedAt; }

    @Override
    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }
}

