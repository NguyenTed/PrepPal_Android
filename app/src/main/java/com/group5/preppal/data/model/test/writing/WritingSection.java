package com.group5.preppal.data.model.test.writing;

import com.google.firebase.firestore.PropertyName;

public class WritingSection {
    private WritingTask task1;
    private WritingTask task2;

    public WritingSection() {}

    @PropertyName("task_1")
    public WritingTask getTask1() {
        return task1;
    }

    @PropertyName("task_1")
    public void setTask1(WritingTask task1) {
        this.task1 = task1;
    }

    @PropertyName("task_2")
    public WritingTask getTask2() {
        return task2;
    }

    @PropertyName("task_2")
    public void setTask2(WritingTask task2) {
        this.task2 = task2;
    }
}

