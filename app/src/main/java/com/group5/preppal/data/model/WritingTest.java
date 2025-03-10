package com.group5.preppal.data.model;

public class WritingTest {
    private String id;
    private String title;
    private String description;

    public WritingTest() {
        // Constructor mặc định cho Firestore
    }

    public WritingTest(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
