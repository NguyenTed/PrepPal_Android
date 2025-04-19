package com.group5.preppal.data.model.test.speaking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class SpeakingSection implements Parcelable {
    private List<String> part1;
    private SpeakingPartTwo part2;
    private List<String> part3;

    public SpeakingSection() {}

    protected SpeakingSection(Parcel in) {
        part1 = in.createStringArrayList();
        part2 = in.readParcelable(SpeakingPartTwo.class.getClassLoader());
        part3 = in.createStringArrayList();
    }

    public static final Creator<SpeakingSection> CREATOR = new Creator<SpeakingSection>() {
        @Override
        public SpeakingSection createFromParcel(Parcel in) {
            return new SpeakingSection(in);
        }

        @Override
        public SpeakingSection[] newArray(int size) {
            return new SpeakingSection[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(part1);
        dest.writeParcelable(part2, flags);
        dest.writeStringList(part3);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @PropertyName("part_1")
    public List<String> getPart1() {
        return part1;
    }

    @PropertyName("part_1")
    public void setPart1(List<String> part1) {
        this.part1 = part1;
    }

    @PropertyName("part_2")
    public SpeakingPartTwo getPart2() {
        return part2;
    }

    @PropertyName("part_2")
    public void setPart2(SpeakingPartTwo part2) {
        this.part2 = part2;
    }

    @PropertyName("part_3")
    public List<String> getPart3() {
        return part3;
    }

    @PropertyName("part_3")
    public void setPart3(List<String> part3) {
        this.part3 = part3;
    }
}

