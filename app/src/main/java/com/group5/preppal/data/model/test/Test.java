package com.group5.preppal.data.model.test;

import com.google.firebase.firestore.PropertyName;
import com.group5.preppal.data.model.test.listening.ListeningSection;
import com.group5.preppal.data.model.test.speaking.SpeakingSection;
import com.group5.preppal.data.model.test.writing.WritingSection;

public class Test {
    private String id;
    private String testSetId;
    private String name;
    private ListeningSection listeningSection;
    private SpeakingSection speakingSection;
    private WritingSection writingSection;

    public Test() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("test_set_id")
    public String getTestSetId() {
        return testSetId;
    }

    @PropertyName("test_set_id")
    public void setTestSetId(String testSetId) {
        this.testSetId = testSetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("listening_section")
    public ListeningSection getListeningSection() {
        return listeningSection;
    }

    @PropertyName("listening_section")
    public void setListeningSection(ListeningSection listeningSection) {
        this.listeningSection = listeningSection;
    }

    @PropertyName("speaking_section")
    public SpeakingSection getSpeakingSection() {
        return speakingSection;
    }

    @PropertyName("speaking_section")
    public void setSpeakingSection(SpeakingSection speakingSection) {
        this.speakingSection = speakingSection;
    }

    @PropertyName("writing_section")
    public WritingSection getWritingSection() {
        return writingSection;
    }

    @PropertyName("writing_section")
    public void setWritingSection(WritingSection writingSection) {
        this.writingSection = writingSection;
    }
}

