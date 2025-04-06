package com.group5.preppal.data.model.test.listening;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ListeningPart {
    private String audioUrl;
    private List<QuestionGroup> questionGroups;

    @PropertyName("audio_url")
    public String getAudioUrl() {
        return audioUrl;
    }

    @PropertyName("audio_url")
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @PropertyName("question_groups")
    public List<QuestionGroup> getQuestionGroups() {
        return questionGroups;
    }

    @PropertyName("question_groups")
    public void setQuestionGroups(List<QuestionGroup> questionGroups) {
        this.questionGroups = questionGroups;
    }
}
