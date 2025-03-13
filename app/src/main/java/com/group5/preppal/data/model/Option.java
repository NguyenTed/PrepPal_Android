package com.group5.preppal.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

public class Option implements Parcelable {
    private String answer;

    @PropertyName("isCorrect")
    private boolean isCorrect;

    public Option() {}

    public Option(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    protected Option(Parcel in) {
        answer = in.readString();
        isCorrect = in.readByte() != 0; // Đọc `boolean` từ `byte`
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(answer);
        parcel.writeByte((byte) (isCorrect ? 1 : 0)); // Ghi `boolean` thành `byte`
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters & Setters
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    @PropertyName("isCorrect")
    public boolean isCorrect() { return isCorrect; }

    @PropertyName("isCorrect")
    public void setCorrect(boolean correct) { isCorrect = correct; }
}
