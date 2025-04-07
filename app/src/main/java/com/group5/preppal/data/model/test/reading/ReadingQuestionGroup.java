package com.group5.preppal.data.model.test.reading;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ReadingQuestionGroup implements Parcelable {
    private String type;
    private String imageUrl;
    private List<ReadingQuestion> questions;
    private List<String> options;         // Optional for MCQ/matching
    private List<String> correctAnswers;  // Only used for mcq_multiple

    public ReadingQuestionGroup() {}

    protected ReadingQuestionGroup(Parcel in) {
        type = in.readString();
        imageUrl = in.readString();
        questions = in.createTypedArrayList(ReadingQuestion.CREATOR);
        options = in.createStringArrayList();
        correctAnswers = in.createStringArrayList();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PropertyName("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @PropertyName("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ReadingQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ReadingQuestion> questions) {
        this.questions = questions;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(imageUrl);
        dest.writeTypedList(questions);
        dest.writeStringList(options);
        dest.writeStringList(correctAnswers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReadingQuestionGroup> CREATOR = new Creator<ReadingQuestionGroup>() {
        @Override
        public ReadingQuestionGroup createFromParcel(Parcel in) {
            return new ReadingQuestionGroup(in);
        }

        @Override
        public ReadingQuestionGroup[] newArray(int size) {
            return new ReadingQuestionGroup[size];
        }
    };
}
