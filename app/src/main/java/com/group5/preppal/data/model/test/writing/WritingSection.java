package com.group5.preppal.data.model.test.writing;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public class WritingSection implements Parcelable {
    private WritingTask task1;
    private WritingTask task2;

    public WritingSection() {}

    protected WritingSection(Parcel in) {
        task1 = in.readParcelable(WritingTask.class.getClassLoader());
        task2 = in.readParcelable(WritingTask.class.getClassLoader());
    }

    @PropertyName("task_1")
    public WritingTask getTask1() {
        return task1;
    }

    @PropertyName("task_1")
    public void setTask1(WritingTask task1) {
        this.task1 = task1;
    }

    @PropertyName("task_2")
    public WritingTask getTask2() {
        return task2;
    }

    @PropertyName("task_2")
    public void setTask2(WritingTask task2) {
        this.task2 = task2;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(task1, flags);
        dest.writeParcelable(task2, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WritingSection> CREATOR = new Creator<WritingSection>() {
        @Override
        public WritingSection createFromParcel(Parcel in) {
            return new WritingSection(in);
        }

        @Override
        public WritingSection[] newArray(int size) {
            return new WritingSection[size];
        }
    };
}

