package com.group5.preppal.data.model.test;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class TestSet {
    private String id;
    private String name;
    private List<String> testIds;

    public TestSet() {}

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

    @PropertyName("test_ids")
    public List<String> getTestIds() {
        return testIds;
    }

    @PropertyName("test_ids")
    public void setTestIds(List<String> testIds) {
        this.testIds = testIds;
    }
}

