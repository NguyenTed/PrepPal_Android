package com.group5.preppal.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WritingTest {
    private String id;
    private String name;
    private List<Task> tasks;

    public WritingTest() {
        this.tasks = new ArrayList<>();
    }

    public WritingTest(String id, String name, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = (tasks != null) ? tasks : new ArrayList<>();
    }

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

    public List<Task> getTasks() {
        return (tasks != null) ? tasks : new ArrayList<>(); // Tránh null
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = (tasks != null) ? tasks : new ArrayList<>(); // Tránh null
    }
}
