package com.group5.preppal.data.model.test.listening;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public class ListeningSection implements Parcelable {
    private ListeningPart part1;
    private ListeningPart part2;
    private ListeningPart part3;
    private ListeningPart part4;

    public ListeningSection() {}

    protected ListeningSection(Parcel in) {
        part1 = in.readParcelable(ListeningPart.class.getClassLoader());
        part2 = in.readParcelable(ListeningPart.class.getClassLoader());
        part3 = in.readParcelable(ListeningPart.class.getClassLoader());
        part4 = in.readParcelable(ListeningPart.class.getClassLoader());
    }

    @PropertyName("part_1")
    public ListeningPart getPart1() {
        return part1;
    }

    @PropertyName("part_1")
    public void setPart1(ListeningPart part1) {
        this.part1 = part1;
    }

    @PropertyName("part_2")
    public ListeningPart getPart2() {
        return part2;
    }

    @PropertyName("part_2")
    public void setPart2(ListeningPart part2) {
        this.part2 = part2;
    }

    @PropertyName("part_3")
    public ListeningPart getPart3() {
        return part3;
    }

    @PropertyName("part_3")
    public void setPart3(ListeningPart part3) {
        this.part3 = part3;
    }

    @PropertyName("part_4")
    public ListeningPart getPart4() {
        return part4;
    }

    @PropertyName("part_4")
    public void setPart4(ListeningPart part4) {
        this.part4 = part4;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(part1, flags);
        dest.writeParcelable(part2, flags);
        dest.writeParcelable(part3, flags);
        dest.writeParcelable(part4, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListeningSection> CREATOR = new Creator<ListeningSection>() {
        @Override
        public ListeningSection createFromParcel(Parcel in) {
            return new ListeningSection(in);
        }

        @Override
        public ListeningSection[] newArray(int size) {
            return new ListeningSection[size];
        }
    };
}
