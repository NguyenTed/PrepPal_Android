package com.group5.preppal.data.model.test.listening;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ListeningQuestionGroup implements Parcelable {
    private String type;
    private String imageUrl;
    private List<ListeningQuestion> questions;
    private List<String> options;         // Optional for MCQ/matching
    private List<String> correctAnswers;  // Only used for mcq_multiple

    public ListeningQuestionGroup() {}

    protected ListeningQuestionGroup(Parcel in) {
        type = in.readString();
        imageUrl = in.readString();
        questions = in.createTypedArrayList(ListeningQuestion.CREATOR);
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

    public static final Creator<ListeningQuestionGroup> CREATOR = new Creator<ListeningQuestionGroup>() {
        @Override
        public ListeningQuestionGroup createFromParcel(Parcel in) {
            return new ListeningQuestionGroup(in);
        }

        @Override
        public ListeningQuestionGroup[] newArray(int size) {
            return new ListeningQuestionGroup[size];
        }
    };
}
