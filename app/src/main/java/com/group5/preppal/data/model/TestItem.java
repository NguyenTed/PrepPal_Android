package com.group5.preppal.data.model;

public class TestItem {
    private final String testType;
    private final String topic;

    public TestItem(String testType, String topic) {
        this.testType = testType;
        this.topic = topic;
    }

    public String getTestType() {
        return testType;
    }

    public String getTopic() {
        return topic;
    }
}
