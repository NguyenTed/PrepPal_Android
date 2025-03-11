package com.group5.preppal.data.model;

import java.util.List;

public class MultipleChoiceQuiz {
    private String id;
    private String name;
    private float maxPoints;
    private List<Question> questions;

    public MultipleChoiceQuiz() {}

    public MultipleChoiceQuiz(String id, String name, float maxPoints, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
        this.questions = questions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(float maxPoints) {
        this.maxPoints = maxPoints;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
