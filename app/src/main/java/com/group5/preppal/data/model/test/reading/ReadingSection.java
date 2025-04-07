package com.group5.preppal.data.model.test.reading;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public class ReadingSection implements Parcelable {
    private ReadingPassage passage1;
    private ReadingPassage passage2;
    private ReadingPassage passage3;

    public ReadingSection() {}

    protected ReadingSection(Parcel in) {
        passage1 = in.readParcelable(ReadingPassage.class.getClassLoader());
        passage2 = in.readParcelable(ReadingPassage.class.getClassLoader());
        passage3 = in.readParcelable(ReadingPassage.class.getClassLoader());
    }

    @PropertyName("passage_1")
    public ReadingPassage getPassage1() { return passage1; }

    @PropertyName("passage_1")
    public void setPassage1(ReadingPassage passage1) { this.passage1 = passage1; }

    @PropertyName("passage_2")
    public ReadingPassage getPassage2() { return passage2; }

    @PropertyName("passage_2")
    public void setPassage2(ReadingPassage passage2) { this.passage2 = passage2; }

    @PropertyName("passage_3")
    public ReadingPassage getPassage3() { return passage3; }

    @PropertyName("passage_3")
    public void setPassage3(ReadingPassage passage3) { this.passage3 = passage3; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(passage1, flags);
        dest.writeParcelable(passage2, flags);
        dest.writeParcelable(passage3, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReadingSection> CREATOR = new Creator<ReadingSection>() {
        @Override
        public ReadingSection createFromParcel(Parcel in) {
            return new ReadingSection(in);
        }

        @Override
        public ReadingSection[] newArray(int size) {
            return new ReadingSection[size];
        }
    };
}
