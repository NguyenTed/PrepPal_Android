package com.group5.preppal.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.group5.preppal.data.model.MultipleChoiceQuiz;
import com.group5.preppal.data.model.MultipleChoiceQuizResult;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MultipleChoiceQuizResultRepository {
    private final FirebaseFirestore firestore;
    private final FirebaseAuth firebaseAuth;
    private CollectionReference resultsCollection;

    @Inject
    public MultipleChoiceQuizResultRepository(FirebaseFirestore firestore, FirebaseAuth firebaseAuth) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
    }

    public void init() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) throw new IllegalStateException("User not logged in");
        this.resultsCollection = firestore.collection("students")
                .document(user.getUid())
                .collection("multiple_choice_quiz_result");
    }

    public void saveQuizResult(MultipleChoiceQuizResult quizResult, SaveResultCallback callback) {
        String quizId = quizResult.getQuizId();

        resultsCollection
                .whereEqualTo("quizId", quizId)
                .limit(1)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        // Nếu student đã có điểm, chỉ cập nhật nếu điểm mới cao hơn
                        MultipleChoiceQuizResult existingResult = doc.toObject(MultipleChoiceQuizResult.class);
                        if (existingResult != null && quizResult.getScore() >= existingResult.getScore()) {
                            doc.getReference().set(quizResult, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                                    .addOnFailureListener(callback::onFailure);
                        } else {
                            callback.onFailure(new Exception("New score is not higher"));
                        }
                    } else {
                        // Nếu chưa có điểm, lưu mới
                        resultsCollection.add(quizResult)
                                .addOnSuccessListener(aVoid -> callback.onSuccess())
                                .addOnFailureListener(callback::onFailure);
                    }
                }).addOnFailureListener(callback::onFailure);
    }

    public LiveData<MultipleChoiceQuizResult> getQuizResult(String quizId) {
        MutableLiveData<MultipleChoiceQuizResult> quizResultLiveData = new MutableLiveData<>();
        resultsCollection
                .whereEqualTo("quizId", quizId)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null || querySnapshot == null || querySnapshot.isEmpty()) {
                        quizResultLiveData.setValue(null);
                        return;
                    }
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
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
