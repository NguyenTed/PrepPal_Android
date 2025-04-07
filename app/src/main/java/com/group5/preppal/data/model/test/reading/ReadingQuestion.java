package com.group5.preppal.data.model.test.reading;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ReadingQuestion implements Parcelable {
    private int number;
    private String inputType;
    private List<String> options;
    private List<String> correctAnswers;

    public ReadingQuestion() {}

    protected ReadingQuestion(Parcel in) {
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

    public static final Parcelable.Creator<ReadingQuestion> CREATOR = new Parcelable.Creator<ReadingQuestion>() {
        @Override
        public ReadingQuestion createFromParcel(Parcel in) {
            return new ReadingQuestion(in);
        }

        @Override
        public ReadingQuestion[] newArray(int size) {
            return new ReadingQuestion[size];
        }
    };
}
