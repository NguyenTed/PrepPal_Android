package com.group5.preppal.data.model.test.speaking;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SpeakingPartTwo implements Parcelable {

    private String task;
    private List<String> hints;
    private String followUp;

    public SpeakingPartTwo() {
        // Needed for Firestore
    }

    public SpeakingPartTwo(String task, List<String> hints, String followUp) {
        this.task = task;
        this.hints = hints;
        this.followUp = followUp;
    }

    protected SpeakingPartTwo(Parcel in) {
        task = in.readString();
        hints = new ArrayList<>();
        in.readStringList(hints);
        followUp = in.readString();
    }

    public static final Creator<SpeakingPartTwo> CREATOR = new Creator<SpeakingPartTwo>() {
        @Override
        public SpeakingPartTwo createFromParcel(Parcel in) {
            return new SpeakingPartTwo(in);
        }

        @Override
        public SpeakingPartTwo[] newArray(int size) {
            return new SpeakingPartTwo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task);
        dest.writeStringList(hints);
        dest.writeString(followUp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public List<String> getHints() {
        return hints;
    }

    public void setHints(List<String> hints) {
        this.hints = hints;
    }

    @PropertyName("follow_up")
    public String getFollowUp() {
        return followUp;
    }

    @PropertyName("follow_up")
    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }
}
