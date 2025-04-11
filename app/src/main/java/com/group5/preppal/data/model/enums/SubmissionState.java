package com.group5.preppal.data.model.enums;

public enum SubmissionState {
    PENDING("pending"),
    UNPASSED("unpass"),
    PASSED("pass");

    private final String displayName;

    SubmissionState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
