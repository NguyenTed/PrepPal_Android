package com.group5.preppal.data.model.test.listening;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ListeningQuestion implements Parcelable {
    private int number;
    private String inputType;
    private List<String> options;
    private List<String> correctAnswers;

    public ListeningQuestion() {}

    protected ListeningQuestion(Parcel in) {
        number = in.readInt();
        inputType = in.readString();
        options = in.createStringArrayList();
        correctAnswers = in.createStringArrayList();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @PropertyName("input_type")
    public String getInputType() {
        return inputType;
    }

    @PropertyName("input_type")
    public void setInputType(String inputType) {
        this.inputType = inputType;
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
        dest.writeInt(number);
        dest.writeString(inputType);
        dest.writeStringList(options);
        dest.writeStringList(correctAnswers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListeningQuestion> CREATOR = new Creator<ListeningQuestion>() {
        @Override
        public ListeningQuestion createFromParcel(Parcel in) {
            return new ListeningQuestion(in);
        }

        @Override
        public ListeningQuestion[] newArray(int size) {
            return new ListeningQuestion[size];
        }
    };
}
