package com.group5.preppal.data.model;

import java.util.List;

public class MultipleChoiceQuizResult {
    private String studentId;
    private String quizId;
    private float score;
    private boolean pass;
    private List<MultipleChoiceAnsweredQuestion> answeredQuestions;

    public MultipleChoiceQuizResult() {}

    public MultipleChoiceQuizResult(String studentId, String quizId, float score, boolean pass, List<MultipleChoiceAnsweredQuestion> answeredQuestions) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.score = score;
        this.pass = pass;
        this.answeredQuestions = answeredQuestions;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getQuizId() {
        return quizId;
    }

    public float getScore() {
        return score;
    }

    public boolean isPass() {
        return pass;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public void setAnsweredQuestions(List<MultipleChoiceAnsweredQuestion> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public List<MultipleChoiceAnsweredQuestion> getAnsweredQuestions() {
        return answeredQuestions;
    }
}
