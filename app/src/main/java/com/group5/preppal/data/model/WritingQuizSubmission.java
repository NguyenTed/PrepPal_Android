package com.group5.preppal.data.model;

public class WritingQuizSubmission {
    private String id;
    private String answer;
    private float points;
    private String taskId;
    private String userId;
    private String state;
    private String comment;

    public WritingQuizSubmission() {}

    public WritingQuizSubmission(String id, String answer, float points, String taskId, String userId, String state, String comment) {
        this.id = id;
        this.answer = answer;
        this.points = points;
        this.taskId = taskId;
        this.userId = userId;
        this.state = state;
        this.comment = comment;
    }

    public WritingQuizSubmission(String answer, float points, String taskId, String userId, String state, String comment) {
        this.answer = answer;
        this.points = points;
        this.taskId = taskId;
        this.userId = userId;
        this.state = state;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
