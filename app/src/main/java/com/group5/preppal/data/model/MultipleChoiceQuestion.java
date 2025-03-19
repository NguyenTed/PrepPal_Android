package com.group5.preppal.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

// 1 MultipleChoiceQuiz includes many MultipleChoiceQuestion
public class MultipleChoiceQuestion implements Parcelable {
    private String id;
    private String questionName;
    private float point;
    private List<Option> options;

    public MultipleChoiceQuestion() {}

    public MultipleChoiceQuestion(String id, String questionName, List<Option> options, Float point) {
        this.id = id;
        this.questionName = questionName;
        this.options = options;
        this.point = point;
    }

    protected MultipleChoiceQuestion(Parcel in) {
        id = in.readString();
        questionName = in.readString();
        point = in.readFloat();
        options = new ArrayList<>();
        in.readTypedList(options, Option.CREATOR); // Đọc danh sách `Option`
    }

    public static final Creator<MultipleChoiceQuestion> CREATOR = new Creator<MultipleChoiceQuestion>() {
        @Override
        public MultipleChoiceQuestion createFromParcel(Parcel in) {
            return new MultipleChoiceQuestion(in);
        }

        @Override
        public MultipleChoiceQuestion[] newArray(int size) {
            return new MultipleChoiceQuestion[size];
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
