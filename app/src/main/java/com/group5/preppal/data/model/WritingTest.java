package com.group5.preppal.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WritingTest {
    private String id;
    private String name;
    private List<Map<String, String>> tasks; // Danh sách các task (id, title, description)

    public WritingTest() {
        // Firebase yêu cầu constructor rỗng
        this.tasks = new ArrayList<>(); // Đảm bảo không bị null
    }

    public WritingTest(String id, String name, List<Map<String, String>> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = (tasks != null) ? tasks : new ArrayList<>(); // Tránh null
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

    public List<Map<String, String>> getTasks() {
        return (tasks != null) ? tasks : new ArrayList<>(); // Tránh null
    }

    public void setTasks(List<Map<String, String>> tasks) {
        this.tasks = (tasks != null) ? tasks : new ArrayList<>(); // Tránh null
    }
}
