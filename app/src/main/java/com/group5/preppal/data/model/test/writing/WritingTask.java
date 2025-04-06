package com.group5.preppal.data.model.test.writing;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public class WritingTask implements Parcelable {
    private String task;
    private String description;
    private String imgUrl;

    public WritingTask() {} // Required for Firestore

    public WritingTask(String task, String description, String img_url) {
        this.task = task;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    protected WritingTask(Parcel in) {
        task = in.readString();
        description = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<WritingTask> CREATOR = new Creator<WritingTask>() {
        @Override
        public WritingTask createFromParcel(Parcel in) {
            return new WritingTask(in);
        }

        @Override
        public WritingTask[] newArray(int size) {
            return new WritingTask[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task);
        dest.writeString(description);
        dest.writeString(imgUrl);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("img_url")
    public String getImg_url() {
        return imgUrl;
    }

    @PropertyName("img_url")
    public void setImg_url(String img_url) {
        this.imgUrl = img_url;
    }
}
