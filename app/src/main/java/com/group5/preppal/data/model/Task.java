package com.group5.preppal.data.model;

public class Task {
    private String id;
    private String description;
    private String imgUrl;
    private String taskType;
    private String title;

    public Task() {}

    public Task(String id, String description, String imgUrl, String taskType, String title) {
        this.id = id;
        this.description = description;
        this.imgUrl = imgUrl;
        this.taskType = taskType;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
