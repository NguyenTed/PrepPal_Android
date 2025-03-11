package com.group5.preppal.data.model;

import java.util.List;

public class Question {
    private String questionName;
    List<Option> options;

    public Question() {}

    public Question(String questionName, List<Option> options) {
        this.questionName = questionName;
        this.options = options;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
