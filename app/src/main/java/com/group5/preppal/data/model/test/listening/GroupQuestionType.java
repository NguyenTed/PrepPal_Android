package com.group5.preppal.data.model.test.listening;

public enum GroupQuestionType {
    TEXT_FILL("text_fill"),
    MCQ("mcq_single"),
    MCQ_MULTIPLE("mcq_multiple"),
    MATCHING("matching"),
    LABELLING("labelling"),
    SHORT_ANSWER("short_answer");

    private final String type;

    GroupQuestionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static GroupQuestionType fromString(String type) {
        for (GroupQuestionType groupQuestionType : GroupQuestionType.values()) {
            if (groupQuestionType.type.equalsIgnoreCase(type)) {
                return groupQuestionType;
            }
        }
        throw new IllegalArgumentException("Unknown group question type: " + type);
    }
}
