package com.group5.preppal.data.model.test.reading;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ReadingPassage implements Parcelable {
    private List<String> passageImageUrls;
    private List<ReadingQuestionGroup> readingQuestionGroups;

    public ReadingPassage() {}

    protected ReadingPassage(Parcel in) {
        passageImageUrls = in.createStringArrayList();
        readingQuestionGroups = in.createTypedArrayList(ReadingQuestionGroup.CREATOR);
    }

    @PropertyName("passage_image_urls")
    public List<String> getPassageImageUrls() {
        return passageImageUrls;
    }

    @PropertyName("passage_image_urls")
    public void setPassageImageUrls(List<String> passageImageUrls) {
        this.passageImageUrls = passageImageUrls;
    }

    @PropertyName("question_groups")
    public List<ReadingQuestionGroup> getReadingQuestionGroups() {
        return readingQuestionGroups;
    }

    @PropertyName("question_groups")
    public void setReadingQuestionGroups(List<ReadingQuestionGroup> readingQuestionGroups) {
        this.readingQuestionGroups = readingQuestionGroups;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(passageImageUrls);
        dest.writeTypedList(readingQuestionGroups);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReadingPassage> CREATOR = new Creator<ReadingPassage>() {
        @Override
        public ReadingPassage createFromParcel(Parcel in) {
            return new ReadingPassage(in);
        }

        @Override
        public ReadingPassage[] newArray(int size) {
            return new ReadingPassage[size];
        }
    };
}
