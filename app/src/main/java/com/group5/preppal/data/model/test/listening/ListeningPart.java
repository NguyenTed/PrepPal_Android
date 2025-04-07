package com.group5.preppal.data.model.test.listening;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ListeningPart implements Parcelable {
    private String audioUrl;
    private List<ListeningQuestionGroup> listeningQuestionGroups;

    public ListeningPart() {}

    protected ListeningPart(Parcel in) {
        audioUrl = in.readString();
        listeningQuestionGroups = in.createTypedArrayList(ListeningQuestionGroup.CREATOR);
    }

    @PropertyName("audio_url")
    public String getAudioUrl() {
        return audioUrl;
    }

    @PropertyName("audio_url")
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @PropertyName("question_groups")
    public List<ListeningQuestionGroup> getListeningQuestionGroups() {
        return listeningQuestionGroups;
    }

    @PropertyName("question_groups")
    public void setListeningQuestionGroups(List<ListeningQuestionGroup> listeningQuestionGroups) {
        this.listeningQuestionGroups = listeningQuestionGroups;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(audioUrl);
        dest.writeTypedList(listeningQuestionGroups);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListeningPart> CREATOR = new Creator<ListeningPart>() {
        @Override
        public ListeningPart createFromParcel(Parcel in) {
            return new ListeningPart(in);
        }

        @Override
        public ListeningPart[] newArray(int size) {
            return new ListeningPart[size];
        }
    };
}
