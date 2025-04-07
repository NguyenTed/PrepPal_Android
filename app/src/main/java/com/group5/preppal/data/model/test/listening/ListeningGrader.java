package com.group5.preppal.data.model.test.listening;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListeningGrader {

    public static int gradeListeningSection(
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
            if (part == null || part.getQuestionGroups() == null) continue;

            for (QuestionGroup group : part.getQuestionGroups()) {
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
}

