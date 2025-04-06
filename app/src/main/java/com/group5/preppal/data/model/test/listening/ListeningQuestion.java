package com.group5.preppal.data.model.test.listening;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ListeningQuestion {
    private int number;
    private QuestionInputType inputType;
    private List<String> correctAnswers;

    public ListeningQuestion() {}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @PropertyName("input_type")
    public QuestionInputType getInputType() {
        return inputType;
    }

    @PropertyName("input_type")
    public void setInputType(QuestionInputType inputType) {
        this.inputType = inputType;
    }

    @PropertyName("correct_answers")
    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    @PropertyName("correct_answers")
    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
