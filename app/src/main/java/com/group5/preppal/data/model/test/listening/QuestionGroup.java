package com.group5.preppal.data.model.test.listening;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class QuestionGroup implements Parcelable {
    private String type;
    private String imageUrl;
    private List<ListeningQuestion> questions;
    private List<String> options;         // Optional for MCQ/matching
    private List<String> correctAnswers;  // Only used for mcq_multiple

    public QuestionGroup() {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(imageUrl);
        dest.writeTypedList(questions);
        dest.writeStringList(options);
        dest.writeStringList(correctAnswers);
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

    public List<ListeningQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ListeningQuestion> questions) {
        this.questions = questions;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    @PropertyName("correct_answers")
    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    @PropertyName("correct_answers")
    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    protected QuestionGroup(Parcel in) {
        type = in.readString();
        imageUrl = in.readString();
        questions = in.createTypedArrayList(ListeningQuestion.CREATOR);
        options = in.createStringArrayList();
        correctAnswers = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuestionGroup> CREATOR = new Creator<QuestionGroup>() {
        @Override
        public QuestionGroup createFromParcel(Parcel in) {
            return new QuestionGroup(in);
        }

        @Override
        public QuestionGroup[] newArray(int size) {
            return new QuestionGroup[size];
        }
    };
}
