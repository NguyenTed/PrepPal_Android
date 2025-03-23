package com.group5.preppal.data.model;

public class TopicWithProgress {
    private String topicId;
    private String name;
    private int learnedCount;
    private int totalCount;

    public TopicWithProgress () {}

    public TopicWithProgress(String topicId, String name, int learnedCount, int totalCount) {
        this.topicId = topicId;
        this.name = name;
        this.learnedCount = learnedCount;
        this.totalCount = totalCount;
    }

    public String getTopicId() { return topicId; }
    public String getName() { return name; }
    public int getLearnedCount() { return learnedCount; }
    public int getTotalCount() { return totalCount; }
}

