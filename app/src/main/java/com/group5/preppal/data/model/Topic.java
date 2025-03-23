package com.group5.preppal.data.model;

import java.util.List;

public class Topic {
    private String id;
    private String topic;
    private List<Vocabulary> vocabularies;

    public Topic() {}

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public List<Vocabulary> getVocabularies() {
        return vocabularies;
    }
}
