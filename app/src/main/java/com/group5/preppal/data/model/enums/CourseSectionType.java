package com.group5.preppal.data.model.enums;

public enum CourseSectionType {
    READING("Reading"),
    VIDEO("Video"),
    MULTIPLE_CHOICE("Multiple Choice"),
    WRITING("Writing"),
    SPEAKING("Speaking");

    private final String displayName;

    CourseSectionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
