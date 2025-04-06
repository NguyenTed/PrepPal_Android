package com.group5.preppal.data.model.test.listening;

public enum QuestionInputType {
    MCQ("mcq"),
    MATCH("match"),
    TEXT("text");

    private final String type;

    QuestionInputType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static QuestionInputType fromString(String type) {
        for (QuestionInputType questionInputType : QuestionInputType.values()) {
            if (questionInputType.type.equalsIgnoreCase(type)) {
                return questionInputType;
            }
        }
        throw new IllegalArgumentException("Unknown question type: " + type);
    }
}
