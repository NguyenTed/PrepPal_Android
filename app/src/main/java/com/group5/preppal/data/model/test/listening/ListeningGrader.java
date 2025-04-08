package com.group5.preppal.data.model.test.listening;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListeningGrader {

    public static int grade(
            ListeningSection section,
            Map<Integer, String> userAnswers
    ) {
        int score = 0;

        List<ListeningPart> parts = Arrays.asList(
                section.getPart1(),
                section.getPart2(),
                section.getPart3(),
                section.getPart4()
        );

        for (ListeningPart part : parts) {
            if (part == null || part.getListeningQuestionGroups() == null) continue;

            for (ListeningQuestionGroup group : part.getListeningQuestionGroups()) {
                List<ListeningQuestion> questions = group.getQuestions();
                List<String> groupCorrectAnswers = group.getCorrectAnswers(); // optional (used in MCQ_MULTIPLE)

                for (int i = 0; i < questions.size(); i++) {
                    ListeningQuestion question = questions.get(i);
                    int number = question.getNumber();

                    // Fetch user answer
                    String userAnswer = userAnswers.get(number);
                    if (userAnswer == null || userAnswer.trim().isEmpty()) continue;

                    List<String> correctAnswers;

                    if (group.getType().equalsIgnoreCase("MCQ_MULTIPLE")) {
                        // Each question gets one correct answer from group-level
                        correctAnswers = groupCorrectAnswers;
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

