package com.group5.preppal.data.model;

public class MultipleChoiceAnsweredQuestion {
    private String questionId;
    private String selectedAnswer;
    private boolean isCorrect;

    public MultipleChoiceAnsweredQuestion() {}

    public MultipleChoiceAnsweredQuestion(String questionId, String selectedAnswer, boolean isCorrect) {
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
