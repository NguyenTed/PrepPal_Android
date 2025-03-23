package com.group5.preppal.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Course {
    private String courseId;
    private String name;
    private String introduction;
    private float entryLevel, targetLevel;
    private List<Map<String, Object>> sections;

    public Course() {}

    public Course(String courseId, String name, String introduction, float entryLevel, float targetLevel, List<Map<String, Object>> sections) {
        this.courseId = courseId;
        this.name = name;
        this.introduction = introduction;
        this.entryLevel = entryLevel;
        this.targetLevel = targetLevel;
        this.sections = sections;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public float getEntryLevel() {
        return entryLevel;
    }

    public void setEntryLevel(float entryLevel) {
        this.entryLevel = entryLevel;
    }

    public float getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(float targetLevel) {
        this.targetLevel = targetLevel;
    }

    public List<Map<String, Object>> getSections() {
        return sections;
    }

    public void setSections(List<Map<String, Object>> sections) {
        this.sections = sections;
    }

    public List<Map<String, Object>> extractWritingQuizzes() {
        List<Map<String, Object>> writingQuizzes = new ArrayList<>();

        for (Map<String, Object> section : sections) {
            if (section.containsKey("quiz")) {
                Map<String, Object> quiz = (Map<String, Object>) section.get("quiz");

                String type = quiz.get("type") != null ? quiz.get("type").toString() : "";
                if (type.toLowerCase().contains("writing")) {
                    writingQuizzes.add(quiz);
                }
            }
        }
        return writingQuizzes;
    }
}
