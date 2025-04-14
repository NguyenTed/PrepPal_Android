package com.group5.preppal.data.model;

import java.util.List;

public class MultipleChoiceQuiz {
    private String id;
    private String name;
    private float maxPoints;
    private float passPoint;
    private List<String> questionIds;
    private int questionNumber;
    private List<MultipleChoiceQuestion> questions;

    public MultipleChoiceQuiz() {}

    public MultipleChoiceQuiz(String id, String name, float maxPoints, float passPoint, List<String> questionsId) {
        this.id = id;
        this.name = name;
        this.maxPoints = maxPoints;
        this.passPoint = passPoint;
        this.questionIds = questionIds;
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

    public float getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(float passPoint) {
        this.passPoint = passPoint;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public List<MultipleChoiceQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<MultipleChoiceQuestion> questions) {
        this.questions = questions;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}
