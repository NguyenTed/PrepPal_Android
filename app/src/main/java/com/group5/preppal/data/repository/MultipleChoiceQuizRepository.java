package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group5.preppal.data.model.MultipleChoiceQuiz;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MultipleChoiceQuizRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference quizCollection;

    @Inject
    public MultipleChoiceQuizRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.quizCollection = firestore.collection("multiple_choice_quizs");
    }


    public LiveData<MultipleChoiceQuiz> getQuizById(String quizId) {
        MutableLiveData<MultipleChoiceQuiz> quizLiveData = new MutableLiveData<>();
        quizCollection.document(quizId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                return;
            }

            MultipleChoiceQuiz quiz = documentSnapshot.toObject(MultipleChoiceQuiz.class);
            quiz.setId(documentSnapshot.getId());
            quizLiveData.setValue(quiz);
            Log.d("isCorrect", "isCorrect: " + quiz.getQuestions().get(0).getOptions().get(1).isCorrect());
        });

        return quizLiveData;
    }
}
