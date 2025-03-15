package com.group5.preppal.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Question implements Parcelable {
    private String id;
    private String questionName;
    private float point;
    private List<Option> options;

    public Question() {}

    public Question(String id, String questionName, List<Option> options, Float point) {
        this.id = id;
        this.questionName = questionName;
        this.options = options;
        this.point = point;
    }

    protected Question(Parcel in) {
        id = in.readString();
        questionName = in.readString();
        point = in.readFloat();
        options = new ArrayList<>();
        in.readTypedList(options, Option.CREATOR); // Đọc danh sách `Option`
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(questionName);
        parcel.writeFloat(point);
        parcel.writeTypedList(options);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestionName() { return questionName; }
    public void setQuestionName(String questionName) { this.questionName = questionName; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }

    public float getPoint() { return point; }
    public void setPoint(float questionPoint) { this.point = questionPoint; }
}
