package com.group5.preppal.data.model.test.listening;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class QuestionGroup {
    private GroupQuestionType type;
    private String imageUrl;
    private List<ListeningQuestion> questions;
    private List<String> options;         // Optional for MCQ/matching
    private List<String> correctAnswers;  // Only used for mcq_multiple

    public QuestionGroup() {}

    public GroupQuestionType getType() {
        return type;
    }

    public void setType(GroupQuestionType type) {
        this.type = type;
    }

    @PropertyName("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @PropertyName("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ListeningQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ListeningQuestion> questions) {
        this.questions = questions;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
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
