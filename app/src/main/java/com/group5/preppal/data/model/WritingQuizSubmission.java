package com.group5.preppal.data.model;

public class WritingQuizSubmission {
    private String answer;
    private float points;
    private String taskId;
    private String userId;
    private String state;

    public WritingQuizSubmission() {}

    public WritingQuizSubmission(String answer, float points, String taskId, String userId, String state) {
        this.answer = answer;
        this.points = points;
        this.taskId = taskId;
        this.userId = userId;
        this.state = state;
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
