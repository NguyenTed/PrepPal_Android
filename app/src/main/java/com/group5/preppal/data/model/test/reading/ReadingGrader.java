package com.group5.preppal.data.model.test.reading;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ReadingGrader {

    public static int grade(ReadingSection section, Map<Integer, String> userAnswers) {
        if (section == null || userAnswers == null) return 0;

        int score = 0;

        List<ReadingPassage> passages = Arrays.asList(
                section.getPassage1(),
                section.getPassage2(),
                section.getPassage3()
        );

        for (ReadingPassage passage : passages) {
            if (passage == null || passage.getReadingQuestionGroups() == null) continue;

            for (ReadingQuestionGroup group : passage.getReadingQuestionGroups()) {
                List<ReadingQuestion> questions = group.getQuestions();
                List<String> groupCorrectAnswers = group.getCorrectAnswers(); // optional (used in MCQ_MULTIPLE)

                for (int i = 0; i < questions.size(); i++) {
                    ReadingQuestion question = questions.get(i);
                    int number = question.getNumber();

                    // Fetch user answer
                    String userAnswer = userAnswers.get(number);
                    if (userAnswer == null || userAnswer.trim().isEmpty()) continue;

                    List<String> correctAnswers;

                    if (group.getType().equalsIgnoreCase("MCQ_MULTIPLE")) {
                        // Each question gets one correct answer from group-level
                        correctAnswers = Collections.singletonList(
                                groupCorrectAnswers.get(i)
                        );
                    } else {
                        correctAnswers = question.getCorrectAnswers();
                    }

                    // Compare: case-insensitive, trim
                    for (String correct : correctAnswers) {
                        if (userAnswer.trim().equalsIgnoreCase(correct.trim())) {
                            score++;
                            break;
                        }
                    }
                }
            }
        }

        return score;
    }

    public static float convertRawScoreToBand(int rawScore) {
        if (rawScore >= 39) return 9.0f;
        if (rawScore >= 37) return 8.5f;
        if (rawScore >= 35) return 8.0f;
        if (rawScore >= 33) return 7.5f;
        if (rawScore >= 30) return 7.0f;
        if (rawScore >= 27) return 6.5f;
        if (rawScore >= 23) return 6.0f;
        if (rawScore >= 19) return 5.5f;
        if (rawScore >= 15) return 5.0f;
        if (rawScore >= 13) return 4.5f;
        if (rawScore >= 10) return 4.0f;
        if (rawScore >= 8) return 3.5f;
        if (rawScore >= 6) return 3.0f;
        if (rawScore >= 3) return 2.5f;
        if (rawScore == 2) return 2.0f;
        if (rawScore == 1) return 1.0f;
        return 0.0f;
    }
}

