package com.group5.preppal.data.repository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.MultipleChoiceAnsweredQuestion;
import com.group5.preppal.data.model.MultipleChoiceQuestion;
import com.group5.preppal.data.model.MultipleChoiceQuiz;
import com.group5.preppal.data.model.MultipleChoiceQuizResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MultipleChoiceQuizRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference quizCollection;
    private final MultipleChoiceQuestionRepository questionRepository;

    @Inject
    public MultipleChoiceQuizRepository(FirebaseFirestore firestore, MultipleChoiceQuestionRepository questionRepository) {
        this.firestore = firestore;
        this.quizCollection = firestore.collection("multiple_choice_quizs");
        this.questionRepository = questionRepository;
    }

    public LiveData<MultipleChoiceQuiz> getQuizById(String quizId) {
        MutableLiveData<MultipleChoiceQuiz> quizLiveData = new MutableLiveData<>();

        quizCollection.document(quizId).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) return;

            MultipleChoiceQuiz quiz = documentSnapshot.toObject(MultipleChoiceQuiz.class);
            if (quiz == null) return;

            quiz.setId(documentSnapshot.getId());

            List<String> questionIds = quiz.getQuestionIds();
            int numberToPick = quiz.getQuestionNumber();

            if (questionIds == null || questionIds.isEmpty() || numberToPick <= 0) {
                quiz.setQuestions(new ArrayList<>());
                quizLiveData.setValue(quiz);
                return;
            }

            Collections.shuffle(questionIds);
            List<String> selectedIds = questionIds.subList(0, Math.min(numberToPick, questionIds.size()));

            questionRepository.getQuestionsByIds(selectedIds).observeForever(questions -> {
                quiz.setQuestions(questions);
                quizLiveData.setValue(quiz);
            });
        });

        return quizLiveData;
    }

    public LiveData<MultipleChoiceQuiz> getAnsweredQuizByResult(MultipleChoiceQuizResult multipleChoiceQuizResult) {
        MutableLiveData<MultipleChoiceQuiz> quizLiveData = new MutableLiveData<>();

        String quizId = multipleChoiceQuizResult.getQuizId();
        quizCollection.document(quizId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {    Log.d("CHECKABC", "doc is EMPTY"); return;}

                    MultipleChoiceQuiz quiz = documentSnapshot.toObject(MultipleChoiceQuiz.class);
                    quiz.setId(documentSnapshot.getId());

                    if (quiz == null) {    Log.d("CHECKABC", "quiz is EMPTY"); return;}
                    List <String> questionIds = new ArrayList<>();

                    for (MultipleChoiceAnsweredQuestion answer : multipleChoiceQuizResult.getAnsweredQuestions()) {
                        questionIds.add(answer.getQuestionId());
                    }

                    if (questionIds.isEmpty()) {
                        Log.d("question size", "Question size: 0");
                        quiz.setQuestions(new ArrayList<>());
                        quizLiveData.setValue(quiz);
                    }
                    Log.d("question size", "Question size: " + questionIds.size());
                    questionRepository.getQuestionsByIds(questionIds).observeForever(questions -> {
                        quiz.setQuestions(questions);
                        quizLiveData.setValue(quiz);
                    });
                });

        return quizLiveData;
    }
}
