package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.group5.preppal.data.model.MultipleChoiceQuiz;
import com.group5.preppal.data.model.MultipleChoiceQuizResult;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MultipleChoiceQuizResultRepository {
    private final FirebaseFirestore firestore;
    private final CollectionReference resultsCollection;

    @Inject
    public MultipleChoiceQuizResultRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
        this.resultsCollection = firestore.collection("multiple_choice_quiz_result");
    }

    public void saveQuizResult(MultipleChoiceQuizResult quizResult, SaveResultCallback callback) {
        String documentId = quizResult.getStudentId() + "_" + quizResult.getQuizId();

        resultsCollection.document(documentId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Nếu student đã có điểm, chỉ cập nhật nếu điểm mới cao hơn
                MultipleChoiceQuizResult existingResult = documentSnapshot.toObject(MultipleChoiceQuizResult.class);
                if (existingResult != null && quizResult.getScore() >= existingResult.getScore()) {
                    resultsCollection.document(documentId).set(quizResult, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> callback.onSuccess())
                            .addOnFailureListener(callback::onFailure);
                } else {
                    callback.onFailure(new Exception("New score is not higher"));
                }
            } else {
                // Nếu chưa có điểm, lưu mới
                resultsCollection.document(documentId).set(quizResult)
                        .addOnSuccessListener(aVoid -> callback.onSuccess())
                        .addOnFailureListener(callback::onFailure);
            }
        }).addOnFailureListener(callback::onFailure);
    }

    public LiveData<MultipleChoiceQuizResult> getQuizResultById(String quizResultId) {
        MutableLiveData<MultipleChoiceQuizResult> quizResultLiveData = new MutableLiveData<>();
        resultsCollection.document(quizResultId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null || documentSnapshot == null || !documentSnapshot.exists()) {
                return;
            }

            MultipleChoiceQuizResult quizResult = documentSnapshot.toObject(MultipleChoiceQuizResult.class);
            quizResult.setQuizId(documentSnapshot.getId());
            quizResultLiveData.setValue(quizResult);
        });

        return quizResultLiveData;
    }


    public interface SaveResultCallback {
        void onSuccess();
        void onFailure(@NonNull Exception e);
    }
}
